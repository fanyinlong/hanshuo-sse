package com.shenmei.data.sse.dataservice.orderdata;

import com.shenmei.data.sse.domain.SseOrderInfo;
import com.shenmei.data.sse.service.ISseOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * 用于处理订单导出相关的业务逻辑
 *
 */
@Service
public class SseOrderExportService {

    @Autowired
    private ISseOrderInfoService orderInfoService;

    /**
     * 导出订单数据为SQL批量插入语句并压缩为ZIP
     */
    public byte[] exportOrderSqlToZip(Long ruleId) throws IOException {
        // 1. 查询所有OrderTable列表
        List<String> orderTableList = orderInfoService.selectOrderTableListByRuleId(ruleId);


        // 2. 创建线程池（根据CPU核心数优化）
        int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        // 2. 生成ZIP文件
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(byteOut)) {

            // 3. 为每个orderTable查询数据并生成SQL文件
            // 3. 为每个orderTable提交异步任务
            List<Future<Void>> futures = new ArrayList<>();
            for (String orderTable : orderTableList) {
                if (orderTable == null || orderTable.trim().isEmpty()) {
                    continue;
                }

                Callable<Void> task = () -> processOrderTable(orderTable, ruleId, zipOut);
                futures.add(executorService.submit(task));
            }

            // 4. 等待所有任务完成
            for (Future<Void> future : futures) {
                future.get(); // 抛出异常时会在此处阻塞
            }

            zipOut.finish();
            return byteOut.toByteArray();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }


    /**
     * 处理单个orderTable的私有方法（线程安全）
     */
    private Void processOrderTable(String orderTable, Long ruleId, ZipOutputStream zipOut) throws IOException {
        // 查询该orderTable下的所有订单数据
        SseOrderInfo queryInfo = new SseOrderInfo();
        queryInfo.setRuleId(ruleId);
        queryInfo.setOrderTable(orderTable);

        List<SseOrderInfo> orderList = new ArrayList<>();
        int pageSize = 10000; // 增大页大小减少网络请求次数
        int offset = 0;

        while (true) {
            List<SseOrderInfo> batchList = orderInfoService.selectSseOrderInfoListByPage(queryInfo, offset, pageSize);
            if (batchList.isEmpty()) {
                break;
            }
            orderList.addAll(batchList);
            if (batchList.size() < pageSize) {
                break;
            }
            offset += pageSize;
        }

        if (!orderList.isEmpty()) {
            // 同步块确保写入zip时的线程安全
            synchronized (zipOut) {
                ZipEntry zipEntry = new ZipEntry(orderTable + "_OrderRequest.sql");
                zipOut.putNextEntry(zipEntry);
                String sqlContent = generateBatchInsertSql(orderTable, orderList);
                zipOut.write(sqlContent.getBytes(StandardCharsets.UTF_8));
                zipOut.closeEntry();
            }
        }
        return null;
    }

    /**
     * 生成SQL Server批量插入语句
     */
    public String generateBatchInsertSql(String tableName, List<SseOrderInfo> orders) {
        if (orders.isEmpty()) {
            return "";
        }

        // 获取数据库字段名（与数据库表结构一致）
        String[] columns = {"MsgType", "BizID", "BizPbu", "ClOrdID",
                "SecurityID", "Account", "OwnerType", "Side", "Price",
                "OrderQty", "OrdType", "TimeInForce", "TransactTime",
                "CreditTag", "ClearingFirm", "BranchID", "OrigClOrdID",
                "UserInfo", "ExtendFields","ErrorCode","Status"};

        StringBuilder sql = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        // 每2000条记录作为一个批次
        int batchSize = 500;
        int totalSize = orders.size();
        int batchCount = (totalSize + batchSize - 1) / batchSize; // 计算总批次数

        for (int batchIndex = 0; batchIndex < batchCount; batchIndex++) {
            // 计算当前批次的起始和结束索引
            int startIndex = batchIndex * batchSize;
            int endIndex = Math.min((batchIndex + 1) * batchSize, totalSize);
            List<SseOrderInfo> batchOrders = orders.subList(startIndex, endIndex);

            // 生成批次INSERT语句
            sql.append("INSERT INTO ").append(tableName).append("_OrderRequest").append(" (")
                    .append(String.join(", ", columns))
                    .append(") VALUES ");

            /**
             * 字段特殊处理：
             * OwnerType默认为0;
             * MsgType对应MsgHead,转为数字类型
             */

            // 使用并行流替代普通for循环实现多线程处理
            List<String> valueList = batchOrders.parallelStream()
                    .map(order -> {
                        List<String> values = new ArrayList<>();
                        values.add(formatValue(Integer.valueOf(order.getMsgHead())));
                        values.add(formatValue(order.getBizId()));
                        values.add(formatValue(order.getBizPbu()));
                        values.add(formatValue(order.getClOrdId()));
                        values.add(formatValue(order.getSecurityId()));
                        values.add(formatValue(order.getAccount()));
                        values.add(formatValue(0));
                        values.add(formatValue(order.getSide()));
                        values.add(formatValue(order.getPrice()));
                        values.add(formatValue(order.getOrderQty()));
                        values.add(formatValue(order.getOrdType()));
                        values.add(formatValue(order.getTimeInForce()));
                        values.add(formatValue(order.getTransactTime()));
                        values.add(formatValue(order.getCreditTag()));
                        values.add(formatValue(order.getClearingFirm()));
                        values.add(formatValue(order.getBranchId()));
                        values.add(formatValue(order.getOrigClOrdId()));
                        values.add(formatValue(order.getUserInfo()));
                        values.add(formatValue(order.getExtendFields()));
                        values.add(formatValue(0));
                        values.add(formatValue(0));
                        return "(" + String.join(", ", values) + ")";
                    })
                    .collect(Collectors.toList());

            sql.append(String.join(",\n", valueList)).append(";\n\n"); // 每个批次SQL以分号和换行结束
        }

        return sql.toString();
    }

    // 格式化SQL值
    private String formatValue(Object value) {
        if (value == null) {
            return "''";
        }
        if (value instanceof String) {
            return "'" + ((String) value).replace("'", "''") + "'"; // 转义单引号
        }
        if (value instanceof Integer || value instanceof Long || value instanceof Double || value instanceof Float) {
            return value.toString();
        }
        if (value instanceof java.math.BigDecimal) {
            return ((BigDecimal) value).toBigInteger().toString();
        }
        return "'" + value.toString() + "'";
    }

    // 格式化日期值
    private String formatValue(Date date) {
        return date == null ? "" : "'" + date.getTime() + "'";
    }

}
