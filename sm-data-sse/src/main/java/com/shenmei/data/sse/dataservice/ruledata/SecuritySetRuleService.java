package com.shenmei.data.sse.dataservice.ruledata;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.shenmei.data.sse.domain.SseDataRule;
import com.shenmei.data.sse.domain.SseDataRuleInit;
import com.shenmei.data.sse.domain.SseSecurityInfo;
import com.shenmei.data.sse.dto.CountCommonDo;
import com.shenmei.data.sse.service.impl.SseDataRuleServiceImpl;
import com.shenmei.data.sse.service.impl.SseSecurityInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.shenmei.data.sse.constant.RuleConstant.TOTAL_ORDER_NUMBER;

/**
 * description 根据股票数量配置，查找set集合并分配股票，set集合笔数和比例分布
 *
 * @author: teabot
 * @date: 2025-08-01
 */
@Service
@Slf4j
public class SecuritySetRuleService {

    @Resource
    private SseDataRuleServiceImpl sseDataRuleService;

    @Resource
    private SseSecurityInfoServiceImpl sseSecurityInfoService;


    /**
     *  1.按主板和科创板股票数量正序
     *  2.按setgup数量倒叙
     *  3.对股票按照set顺序进行均分
     *  4.落数：
     *  `security_ash_list` varchar(200) DEFAULT NULL COMMENT '主板股票',
     *   `security_ksh_list` varchar(200) DEFAULT NULL COMMENT '科创板股票',
     *   `set_ash_security` varchar(200) DEFAULT NULL COMMENT '主板SET股票分配',
     *   `set_ksh_security` varchar(200) DEFAULT NULL COMMENT '科创板SET股票分配',
     */

    /**
     * 设置set股票和比例
     *
     * @param sseDataRuleInit
     * @return
     */
    public SseDataRuleInit setSecuritySetup(SseDataRuleInit sseDataRuleInit, List<String> errorList) {
        sseDataRuleInit = setSecuritySetQuantity(sseDataRuleInit, errorList);
        sseDataRuleInit = allocateSecuritySet(sseDataRuleInit);
        return sseDataRuleInit;

    }

    /**
     * 获取股票数量
     * 数据结构“”{"ASH":{"ratio":91.2,"quantity":14},"KSH":{"ratio":8.8,"quantity":6}}
     */
    private Map<String, Integer> getSecurityQuantityMap(SseDataRule sseDataRule) {

        //主板股票数量
        int ash_security_quantity = 0;
        //科创板股票数量
        int ksh_security_quantity = 0;

        Map<String, Integer> securityMap = new LinkedHashMap<>();
        if (StringUtils.isNotBlank(sseDataRule.getSecuritySubCategory())) {
            // 解析JSON字符串
            JSONObject jsonObject = JSON.parseObject(sseDataRule.getSecuritySubCategory());

            // 提取主板(ASH)股票数量
            if (jsonObject.containsKey("ASH")) {
                JSONObject ashObject = jsonObject.getJSONObject("ASH");
                ash_security_quantity = ashObject.getIntValue("quantity");
            }

            // 提取科创板(KSH)股票数量
            if (jsonObject.containsKey("KSH")) {
                JSONObject kshObject = jsonObject.getJSONObject("KSH");
                ksh_security_quantity = kshObject.getIntValue("quantity");
            }
        }
        // 按数量排序
        if (ash_security_quantity < ksh_security_quantity) {
            securityMap.put("ASH", ash_security_quantity);
            securityMap.put("KSH", ksh_security_quantity);
        } else {
            securityMap.put("KSH", ksh_security_quantity);
            securityMap.put("ASH", ash_security_quantity);
        }

        return securityMap;

    }

    /**
     * 提取set集合
     * set数据结构：{"1":{"ratio":15.84,"quantity":3},"2":{"ratio":18.34,"quantity":3},"3":{"ratio":13.97,"quantity":4},"4":{"ratio":9.83,"quantity":4},"5":{"ratio":23.39,"quantity":3},"6":{"ratio":18.63,"quantity":3}}
     */
    private Map<String, Integer> getSecuritySetQuantityMap(SseDataRule sseDataRule) {

        if (StringUtils.isNotBlank(sseDataRule.getProductSet())) {
            // 解析JSON字符串
            JSONObject setJsonObject = JSON.parseObject(sseDataRule.getProductSet());

            // 创建Map存储setId和对应的quantity
            Map<String, Integer> setQuantityMap = new HashMap<>();

            // 遍历所有set
            for (String setId : setJsonObject.keySet()) {
                JSONObject setInfo = setJsonObject.getJSONObject(setId);
                int quantity = setInfo.getIntValue("quantity");
                setQuantityMap.put(setId, quantity);

            }

            // 按quantity进行倒序排序
            List<Map.Entry<String, Integer>> entryList = new ArrayList<>(setQuantityMap.entrySet());
            entryList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

            // 重建LinkedHashMap以保持排序顺序
            Map<String, Integer> sortedMap = new LinkedHashMap<>();
            for (Map.Entry<String, Integer> entry : entryList) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }
            log.info("setQuantityMap:" + sortedMap);
            return sortedMap;

        }
        return new LinkedHashMap<>();
    }

    /**
     * 按照科创版或主板的股票数量均分至set集合上股票数量
     * 落数
     * `set_ash_security`  '主板SET股票分配',
     * `set_ksh_security` '科创板SET股票分配',
     *
     * @param sseDataRuleInit
     */
    public SseDataRuleInit setSecuritySetQuantity(SseDataRuleInit sseDataRuleInit, List<String> errorList) {
        SseDataRule sseDataRule = sseDataRuleService.selectSseDataRuleByPkId(sseDataRuleInit.getRuleId());
        Map<String, Integer> securityQuantityMap = getSecurityQuantityMap(sseDataRule);
        Map<String, Integer> securitySetQuantityMap = getSecuritySetQuantityMap(sseDataRule);


        // 校验：securityQuantityMap的quantity数量和等于securitySetQuantityMap上的quantity数量和
        int totalSecurityQuantity = securityQuantityMap.values().stream().mapToInt(Integer::intValue).sum();
        int totalSetQuantity = securitySetQuantityMap.values().stream().mapToInt(Integer::intValue).sum();

        if (securityQuantityMap.isEmpty()) {
            errorList.add("股票数量分配错误：子类别股票总数量为！");
            return sseDataRuleInit;
        }
        if (totalSecurityQuantity != totalSetQuantity) {
            errorList.add("股票数量分配错误：总数量不匹配。详细信息：子类别股票总数量：" + totalSecurityQuantity + "，set股票总数量：" + totalSetQuantity);
            return sseDataRuleInit;
        }


        // 获取第一个股票类型（主板或科创板）
        Iterator<String> keyIterator = securityQuantityMap.keySet().iterator();
        if (!keyIterator.hasNext()) {
            errorList.add("股票数量分配错误：股票数量分配错误！详细信息：子类别股票数量未设置！");
            return sseDataRuleInit;
        }
        String firstSecurityType = keyIterator.next();
        int firstSecurityQuantity = securityQuantityMap.get(firstSecurityType);

        // 获取set集合大小
        int setCount = securitySetQuantityMap.size();

        // 初始化分配结果
        Map<String, Integer> firstTypeAllocation = new HashMap<>();

        // 为第一个股票类型分配set
        int remainingQuantity = firstSecurityQuantity;

        // 获取set集合列表，按quantity倒序排列
        List<String> sortedSetIds = new ArrayList<>(securitySetQuantityMap.keySet());

        // 根据股票类型获取各set集合对应的股票数量，用于分配时，不能超过该集合对应的股票数量
        List<CountCommonDo> securitySetCountList = getSecuritySetCountByType(firstSecurityType);
        if (securitySetCountList == null) {
            securitySetCountList = Collections.emptyList();
        }

        // 根据实际可分配股票数量进行分配
        Map<String, Integer> actualSetCountMap = new HashMap<>();
        for (CountCommonDo countCommonDo : securitySetCountList) {
            actualSetCountMap.put(countCommonDo.getValue(), countCommonDo.getCount());
        }

        // 逐一循环分配，确保不超过实际可分配数量，确保每个set都能分配到（一笔一笔分配）
        int[] allocatedPerSet = new int[sortedSetIds.size()];
        boolean hasAllocation = true;
        int round = 0;

        while (remainingQuantity > 0 && hasAllocation) {
            hasAllocation = false;
            for (int i = 0; i < sortedSetIds.size() && remainingQuantity > 0; i++) {

                String setId = sortedSetIds.get(i);
                int setCapacity = securitySetQuantityMap.get(setId);
                int actualCount = actualSetCountMap.getOrDefault(setId, 0);

                // 当前已分配数量
                int currentAllocated = allocatedPerSet[i];

                // 检查是否还能继续分配（未达到设定容量且未达到实际数量且还有剩余需分配数量）
                if (currentAllocated < setCapacity &&
                        currentAllocated < actualCount &&
                        remainingQuantity > 0) {

                    // 分配一个单位
                    allocatedPerSet[i] += 1;
                    remainingQuantity -= 1;
                    hasAllocation = true;
                }
            }
            round++;
        }

        log.info("分配次数：{}", round);
        // 将分配结果存入firstTypeAllocation
        for (int i = 0; i < sortedSetIds.size(); i++) {
            firstTypeAllocation.put(sortedSetIds.get(i), allocatedPerSet[i]);
        }

        // 校验是否分配完毕
        if (remainingQuantity > 0) {
            throw new RuntimeException("第一个股票类型分配失败，剩余数量：" + remainingQuantity);
        }

        // 为第二个股票类型分配（通过从set总容量中减去第一个类型的分配量）
        String secondSecurityType = null;
        for (String type : securityQuantityMap.keySet()) {
            if (!type.equals(firstSecurityType)) {
                secondSecurityType = type;
                break;
            }
        }

        Map<String, Integer> secondTypeAllocation = new HashMap<>();
        if (secondSecurityType != null) {
            int secondSecurityQuantity = securityQuantityMap.get(secondSecurityType);
            int secondRemainingQuantity = secondSecurityQuantity;

            // 获取第二个类型的可分配股票数量
            List<CountCommonDo> secondSecuritySetCountList = getSecuritySetCountByType(secondSecurityType);
            if (secondSecuritySetCountList == null) {
                secondSecuritySetCountList = Collections.emptyList();
            }

            Map<String, Integer> secondActualSetCountMap = new HashMap<>();
            for (CountCommonDo countCommonDo : secondSecuritySetCountList) {
                secondActualSetCountMap.put(countCommonDo.getValue(), countCommonDo.getCount());
            }

            for (String setId : sortedSetIds) {
                int setCapacity = securitySetQuantityMap.get(setId);
                int firstAllocated = firstTypeAllocation.get(setId);
                // 第二个类型可分配的数量是set总容量减去第一个类型已分配的数量
                int availableForSecond = setCapacity - firstAllocated;
                int secondAllocated = Math.min(secondActualSetCountMap.getOrDefault(setId, 0), availableForSecond);

                secondTypeAllocation.put(setId, secondAllocated);
                secondRemainingQuantity -= secondAllocated;
            }
            log.info("股票类型二分配结果: {}", secondTypeAllocation);

            // 校验第二个类型是否分配完毕
            if (secondRemainingQuantity > 0) {
                throw new RuntimeException("第二个股票类型分配失败，剩余数量：" + secondRemainingQuantity);
            }
        }

        /**
         * 股票比例
         * 计算方式：
         * 循环每个集合，
         * 先提取set集合为0的数据，如果securitySetRatioMap中的set集合为0，则其对应的另外一类型的比例计算方式为：为当前set比例
         * 第二步，计算扣除第一步已计算好的值，计算方式为当前股票类型的比例减去为上面第一步已分配的比例，然后乘以当前set的类型比例
         */
        Map<String, BigDecimal> securityRatioMap = getSecurityRatioMap(sseDataRule);
        Map<String, BigDecimal> securitySetRatioMap = getSecuritySetRatioMap(sseDataRule);

        // 存储每个set的股票类型比例分配结果
        Map<String, Map<String, BigDecimal>> setRatioAllocation = new HashMap<>();

        // 初始化两个股票类型的分配结果
        Map<String, BigDecimal> firstTypeRatioAllocation = new HashMap<>();
        Map<String, BigDecimal> secondTypeRatioAllocation = new HashMap<>();


        Map<String, BigDecimal> firstZeroSetAllocations = new HashMap<>();


        // 第一步：处理set的quantity集合为0的数据，
        // 其对应的另外一类型的比例计算方式为：当前set比例
        for (String firstSet : firstTypeAllocation.keySet()) {
            int quantity = firstTypeAllocation.get(firstSet);
            if (quantity == 0) {
                BigDecimal setRatio = securitySetRatioMap.getOrDefault(firstSet, BigDecimal.ZERO);
                BigDecimal secondTypeRatio = securityRatioMap.getOrDefault(secondSecurityType, BigDecimal.ZERO);

                if (setRatio.compareTo(BigDecimal.ZERO) > 0 && secondTypeRatio.compareTo(BigDecimal.ZERO) > 0) {
                    firstTypeRatioAllocation.put(firstSet, BigDecimal.ZERO);
                    firstZeroSetAllocations.put(firstSet, setRatio);
                } else {
                    firstTypeRatioAllocation.put(firstSet, BigDecimal.ZERO);
                }
            }
        }


        //计算各类型为0的比例总和
        // 计算第一步已分配的比例总和,待扣除,计算分配比例时，需要扣除该占比
        BigDecimal deduct_firstZeroTotalRatio = BigDecimal.ZERO;
        BigDecimal deduct_secondZeroTotalRatio = BigDecimal.ZERO;
        BigDecimal deduct_totalRatio = BigDecimal.ZERO;
        for (BigDecimal ratio : firstZeroSetAllocations.values()) {
            if (ratio != null) {
                deduct_firstZeroTotalRatio = deduct_firstZeroTotalRatio.add(ratio);
            }
        }

        // 计算第一步已分配的比例总和
        BigDecimal assigned_firstRatio = BigDecimal.ZERO;

        for (BigDecimal ratio : firstTypeRatioAllocation.values()) {
            if (ratio != null) {
                assigned_firstRatio = assigned_firstRatio.add(ratio);
            }
        }

        // 计算剩余比例
        BigDecimal remainingFirstTypeRatio = securityRatioMap.getOrDefault(firstSecurityType, BigDecimal.ZERO)
                .subtract(assigned_firstRatio);

        // 按照set比例分配剩余比例
        BigDecimal totalNonZeroSetRatio = securitySetRatioMap.values().stream()
                .filter(ratio -> ratio.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalNonZeroSetRatio.compareTo(BigDecimal.ZERO) > 0) {
            for (String setId : sortedSetIds) {
                BigDecimal setRatio = securitySetRatioMap.getOrDefault(setId, BigDecimal.ZERO);

                // 检查当前set的quantity是否非0
                boolean isFirstTypeNonZero = firstTypeAllocation.getOrDefault(setId, 0) > 0;
                boolean isSecondTypeNonZero = secondTypeAllocation.getOrDefault(setId, 0) > 0;

                if (setRatio.compareTo(BigDecimal.ZERO) > 0) {
                    // 只有当股票类型在该set中有分配(quantity>0)时才进行计算
                    if (isFirstTypeNonZero && !firstTypeRatioAllocation.containsKey(setId)) {
                        BigDecimal firstTypeAllocated = remainingFirstTypeRatio
                                .multiply(setRatio)
                                .divide(totalNonZeroSetRatio.subtract(deduct_firstZeroTotalRatio), 8, RoundingMode.HALF_UP);
                        firstTypeRatioAllocation.put(setId, firstTypeAllocated);
                    }

                }
            }
        }

        for (String secondSet : secondTypeAllocation.keySet()) {
            //set比例总和减去firstTypeRatioAllocation对应分配的set比例
            BigDecimal setRatio = securitySetRatioMap.getOrDefault(secondSet, BigDecimal.ZERO);
            secondTypeRatioAllocation.put(secondSet, setRatio.subtract(firstTypeRatioAllocation.getOrDefault(secondSet, BigDecimal.ZERO)));

        }


        // 确保所有setId都有对应的值
        for (String setId : sortedSetIds) {
            if (!firstTypeRatioAllocation.containsKey(setId)) {
                firstTypeRatioAllocation.put(setId, BigDecimal.ZERO);
            }
            if (!secondTypeRatioAllocation.containsKey(setId)) {
                secondTypeRatioAllocation.put(setId, BigDecimal.ZERO);
            }
        }

// 合并结果到setRatioAllocation
        setRatioAllocation.put(firstSecurityType, firstTypeRatioAllocation);
        setRatioAllocation.put(secondSecurityType, secondTypeRatioAllocation);

        log.info("股票类型一比例分配结果: {}", firstTypeRatioAllocation);
        log.info("股票类型二比例分配结果: {}", secondTypeRatioAllocation);

// 验证比例总和是否正确
        BigDecimal firstTypeTotalRatio = firstTypeRatioAllocation.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal secondTypeTotalRatio = secondTypeRatioAllocation.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("股票类型一比例总和: {}", firstTypeTotalRatio);
        log.info("股票类型二比例总和: {}", secondTypeTotalRatio);
        log.info("股票类型一原始比例: {}", securityRatioMap.getOrDefault(firstSecurityType, BigDecimal.ZERO));
        log.info("股票类型二原始比例: {}", securityRatioMap.getOrDefault(secondSecurityType, BigDecimal.ZERO));

        // 合并结果到setRatioAllocation
        setRatioAllocation.put(firstSecurityType, firstTypeRatioAllocation);
        setRatioAllocation.put(secondSecurityType, secondTypeRatioAllocation);

        // 判断股票类型是否主板还是科创板
        String firstTypeJson = "{}";
        String secondTypeJson = "{}";


        Map<String, Integer> first_orderSetQty = calcOrderSetQty(sseDataRule, firstTypeRatioAllocation, firstSecurityType);
        Map<String, Integer> second_orderSetQty = calcOrderSetQty(sseDataRule, secondTypeRatioAllocation, secondSecurityType);

        // 组合firstTypeAllocation和firstTypeRatioAllocation为包含quantity和ratio的结构
        Map<String, Map<String, Object>> firstTypeCombined = new HashMap<>();
        Map<String, Map<String, Object>> secondTypeCombined = new HashMap<>();

        // 处理firstType的数据组合
        for (String setId : firstTypeAllocation.keySet()) {
            Map<String, Object> combinedData = new HashMap<>();
            combinedData.put("quantity", firstTypeAllocation.get(setId));
            combinedData.put("ratio", firstTypeRatioAllocation.getOrDefault(setId, BigDecimal.ZERO));
            combinedData.put("orderNumber", first_orderSetQty.get(setId));
            firstTypeCombined.put(setId, combinedData);
        }

        // 处理secondType的数据组合
        for (String setId : secondTypeAllocation.keySet()) {
            Map<String, Object> combinedData = new HashMap<>();
            combinedData.put("quantity", secondTypeAllocation.get(setId));
            combinedData.put("ratio", secondTypeRatioAllocation.getOrDefault(setId, BigDecimal.ZERO));
            combinedData.put("orderNumber", second_orderSetQty.get(setId));
            secondTypeCombined.put(setId, combinedData);
        }

        // 处理空值情况，确保不会出现null
        if (!firstTypeCombined.isEmpty()) {
            firstTypeJson = JSON.toJSONString(firstTypeCombined);
        }

        if (!secondTypeCombined.isEmpty()) {
            secondTypeJson = JSON.toJSONString(secondTypeCombined);
        }

        final String KSH = "KSH";
        final String ASHR = "ASHR";

        if (KSH.equals(firstSecurityType)) {
            sseDataRuleInit.setSetKshSecurity(firstTypeJson);
            sseDataRuleInit.setSetAshSecurity(secondTypeJson);
        } else if (ASHR.equals(firstSecurityType)) {
            sseDataRuleInit.setSetKshSecurity(secondTypeJson);
            sseDataRuleInit.setSetAshSecurity(firstTypeJson);
        }
        return sseDataRuleInit;
    }


    /**
     * 获取股票比例
     * 数据结构“”{"ASH":{"ratio":91.2,"quantity":14},"KSH":{"ratio":8.8,"quantity":6}}
     */
    private Map<String, BigDecimal> getSecurityRatioMap(SseDataRule sseDataRule) {

        //主板股票比例
        BigDecimal ash_security_ratio = BigDecimal.ZERO;
        //科创板股票比例
        BigDecimal ksh_security_ratio = BigDecimal.ZERO;

        Map<String, BigDecimal> securityRatioMap = new LinkedHashMap<>();
        if (StringUtils.isNotBlank(sseDataRule.getSecuritySubCategory())) {
            // 解析JSON字符串
            JSONObject jsonObject = JSON.parseObject(sseDataRule.getSecuritySubCategory());

            // 提取主板(ASH)股票数量
            if (jsonObject.containsKey("ASH")) {
                JSONObject ashObject = jsonObject.getJSONObject("ASH");
                ash_security_ratio = ashObject.getBigDecimal("ratio");
            }

            // 提取科创板(KSH)股票数量
            if (jsonObject.containsKey("KSH")) {
                JSONObject kshObject = jsonObject.getJSONObject("KSH");
                ksh_security_ratio = kshObject.getBigDecimal("ratio");
            }
        }
        securityRatioMap.put("KSH", ksh_security_ratio);
        securityRatioMap.put("ASH", ash_security_ratio);

        return securityRatioMap;

    }

    /**
     * 提取set比例
     * set数据结构：{"1":{"ratio":15.84,"quantity":3},"2":{"ratio":18.34,"quantity":3},"3":{"ratio":13.97,"quantity":4},"4":{"ratio":9.83,"quantity":4},"5":{"ratio":23.39,"quantity":3},"6":{"ratio":18.63,"quantity":3}}
     */
    private Map<String, BigDecimal> getSecuritySetRatioMap(SseDataRule sseDataRule) {
        // 创建Map存储setId和对应的ratio
        Map<String, BigDecimal> setRatioMap = new LinkedHashMap<>();
        if (StringUtils.isNotBlank(sseDataRule.getProductSet())) {
            // 解析JSON字符串
            JSONObject setJsonObject = JSON.parseObject(sseDataRule.getProductSet());

            // 遍历所有set
            for (String setId : setJsonObject.keySet()) {
                JSONObject setInfo = setJsonObject.getJSONObject(setId);
                BigDecimal ratio = setInfo.getBigDecimal("ratio");
                setRatioMap.put(setId, ratio);
            }

        }
        return setRatioMap;
    }


    //获取set集合对应的股票数量方法：
    private List<CountCommonDo> getSecuritySetCountByType(String securityType) {
        SseSecurityInfo sseSecurityInfo = new SseSecurityInfo();
        sseSecurityInfo.setSecurityMarket("ASHR");
        sseSecurityInfo.setSecurityType("ES");
        sseSecurityInfo.setSecuritySubType(securityType);
        return sseSecurityInfoService.SecuritySetCount(sseSecurityInfo);
    }


    /**i
     * 分配具体股票
     * 1.获取各股票类型总数
     * 2.获取各个set的股票数量
     * 3.分配具体股票
     */
    private SseDataRuleInit allocateSecuritySet(SseDataRuleInit sseDataRuleInit) {
        /**
         * 主板股票
         */
        String setAshSecurity = sseDataRuleInit.getSetAshSecurity();
        if (StringUtils.isNotBlank(setAshSecurity)) {
            JSONObject ashSecurityJson = JSON.parseObject(setAshSecurity);
            JSONObject jsonObject = new JSONObject();
            for (String setId : ashSecurityJson.keySet()) {
                JSONObject setInfo = ashSecurityJson.getJSONObject(setId);
                int quantity = setInfo.getIntValue("quantity");
                //随机获取主板股票
                if (quantity > 0) {
                    JSONArray jsonArray = getRandomSecurityList("ASH", Long.valueOf(setId), quantity);
                    jsonObject.put(setId, jsonArray);
                }
            }
            sseDataRuleInit.setSecurityAshList(jsonObject.toJSONString());
        }

        /**
         * 科创股票
         */
        String setKshSecurity = sseDataRuleInit.getSetKshSecurity();
        if (StringUtils.isNotBlank(setKshSecurity)) {
            JSONObject kshSecurityJson = JSON.parseObject(setKshSecurity);
            JSONObject jsonObject = new JSONObject();
            for (String setId : kshSecurityJson.keySet()) {
                JSONObject setInfo = kshSecurityJson.getJSONObject(setId);
                int quantity = setInfo.getIntValue("quantity");
                if (quantity > 0) {
                    //随机获取科创股票
                    JSONArray jsonArray = getRandomSecurityList("KSH", Long.valueOf(setId), quantity);
                    jsonObject.put(setId, jsonArray);
                }

            }
            sseDataRuleInit.setSecurityKshList(jsonObject.toJSONString());
        }

        return sseDataRuleInit;

    }

    /**
     * 获取随机股票组合
     *
     * @param securitySubType 股票子类别
     * @param setId
     * @param quantity
     * @return
     */
    private JSONArray getRandomSecurityList(String securitySubType, long setId, int quantity) {
        SseSecurityInfo sseSecurityInfo = new SseSecurityInfo();
        sseSecurityInfo.setSecuritySubType(securitySubType);
        sseSecurityInfo.setSecurityType("ES");
        sseSecurityInfo.setSecuritySet(setId);
        sseSecurityInfo.setSecurityMarket("ASHR");
        sseSecurityInfo.setUsageStatus("0");
        List<SseSecurityInfo> securityInfoList = sseSecurityInfoService.selectRandomSecurityIds(sseSecurityInfo, quantity);
        JSONArray securityArray = new JSONArray();
        securityInfoList.stream().forEach(securityInfo -> {
            JSONObject securityObj = new JSONObject();
            securityObj.put("securityId", securityInfo.getSecurityId());
            securityObj.put("priorClosingPrice", securityInfo.getPriorClosingPrice());
            securityArray.add(securityObj);
        });
        return securityArray;

    }


    /**
     * 按子类别计算set的订单数量
     */
    public HashMap<String, Integer> calcOrderSetQty(SseDataRule sseDataRule, Map<String, BigDecimal> setRatioAllocation, String subCategory) {

        BigDecimal security_ratio = BigDecimal.ZERO;
        String securitySubCategory = sseDataRule.getSecuritySubCategory();
        JSONObject securitySubCategoryJson = JSON.parseObject(securitySubCategory);
        if (securitySubCategoryJson.containsKey(subCategory)) {
            security_ratio = securitySubCategoryJson.getJSONObject(subCategory).getBigDecimal("ratio");
        }

        //计算当前set订单总数
        int setTotalOrderNumber = TOTAL_ORDER_NUMBER.multiply(security_ratio).divide(BigDecimal.valueOf(100), 0, RoundingMode.UP).intValue();

        //获取各子类别的订单数量
        int preSetTotalOrderNumber = 0;

        String maxSetId = null;
        int maxSetOrderNumber = -1;

        HashMap<String, Integer> setQtyMap = new HashMap<>();
        // 处理firstType的数据组合
        for (String setId : setRatioAllocation.keySet()) {
            BigDecimal ratio = setRatioAllocation.getOrDefault(setId, BigDecimal.ZERO);
            //计算SET订单数 set订单笔数 = 订单总数* set比例
            int setOrderNumber = TOTAL_ORDER_NUMBER.multiply(ratio).divide(BigDecimal.valueOf(100), 0, RoundingMode.UP).intValue();
            preSetTotalOrderNumber = preSetTotalOrderNumber + setOrderNumber;
            setQtyMap.put(setId, setOrderNumber);
            // 记录订单数最大的SET
            if (setOrderNumber > maxSetOrderNumber) {
                maxSetOrderNumber = setOrderNumber;
                maxSetId = setId;
            }
        }

        // 比较preSetTotalOrderNumber和setTotalOrderNumber并进行调整
        int difference = setTotalOrderNumber - preSetTotalOrderNumber;
        if (difference > 0) {
            // 如果setTotalOrderNumber比preSetTotalOrderNumber大，则将多余的数分配给setOrderNumber最大的值
            setQtyMap.put(maxSetId, setQtyMap.get(maxSetId) + difference);
        } else if (difference < 0) {
            // 如果setTotalOrderNumber比preSetTotalOrderNumber小，则从setOrderNumber最大的值中减少多余的数
            int adjustment = Math.min(Math.abs(difference), setQtyMap.get(maxSetId));
            setQtyMap.put(maxSetId, setQtyMap.get(maxSetId) - adjustment);
        }

        log.info("子类别：{},set订单数量:{}", subCategory, setQtyMap);
        return setQtyMap;
    }

}
