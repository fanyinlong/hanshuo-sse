package com.shenmei.data.sse.controller;

import com.shenmei.data.common.annotation.Log;
import com.shenmei.data.common.core.controller.BaseController;
import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.common.core.page.TableDataInfo;
import com.shenmei.data.common.enums.BusinessType;
import com.shenmei.data.sse.dataservice.orderdata.SseOrderExportService;
import com.shenmei.data.sse.domain.SseOrderInfo;
import com.shenmei.data.sse.service.ISseOrderInfoService;
import com.shenmei.data.sse.util.MultiSheetExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * 订单汇总Controller
 *
 * @author song
 * @date 2025-08-02
 */
@RestController
@RequestMapping("/sse/orderInfo")
public class SseOrderInfoController extends BaseController
{
    @Autowired
    private ISseOrderInfoService sseOrderInfoService;

    @Resource
    private SseOrderExportService orderExportService;

    /**
     * 查询订单汇总列表
     */
    @PreAuthorize("@ss.hasPermi('sse:orderInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(SseOrderInfo sseOrderInfo)
    {
        startPage();
        List<SseOrderInfo> list = sseOrderInfoService.selectSseOrderInfoList(sseOrderInfo);
        return getDataTable(list);
    }

    /**
     * 导出订单汇总列表
     */
    @PreAuthorize("@ss.hasPermi('sse:orderInfo:export')")
    @Log(title = "订单汇总", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SseOrderInfo sseOrderInfo) throws IOException {
        long ruleId = sseOrderInfo.getRuleId();
        List<String> tables = sseOrderInfoService.selectOrderTableListByRuleId(ruleId);

        // 初始化多sheet导出工具
        MultiSheetExcelUtil<SseOrderInfo> util = new MultiSheetExcelUtil<>(SseOrderInfo.class);
        util.initMultiSheet();

        // 添加每个sheet页
        for (String table : tables) {
            sseOrderInfo.setOrderTable(table);
            List<SseOrderInfo> list = sseOrderInfoService.selectSseOrderInfoList(sseOrderInfo);
            util.addSheet(list, table);
        }

        // 一次性写入响应
        util.writeToResponse(response, "订单汇总数据.xlsx");
//        util.exportExcel(response, list, "订单汇总数据");
    }

    /**
     * 获取订单汇总详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:orderInfo:query')")
    @GetMapping(value = "/{pkId}")
    public AjaxResult getInfo(@PathVariable("pkId") Long pkId)
    {
        return success(sseOrderInfoService.selectSseOrderInfoByPkId(pkId));
    }

    /**
     * 新增订单汇总
     */
    @PreAuthorize("@ss.hasPermi('sse:orderInfo:add')")
    @Log(title = "订单汇总", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SseOrderInfo sseOrderInfo)
    {
        return toAjax(sseOrderInfoService.insertSseOrderInfo(sseOrderInfo));
    }

    /**
     * 修改订单汇总
     */
    @PreAuthorize("@ss.hasPermi('sse:orderInfo:edit')")
    @Log(title = "订单汇总", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SseOrderInfo sseOrderInfo)
    {
        return toAjax(sseOrderInfoService.updateSseOrderInfo(sseOrderInfo));
    }

    /**
     * 删除订单汇总
     */
    @PreAuthorize("@ss.hasPermi('sse:orderInfo:remove')")
    @Log(title = "订单汇总", businessType = BusinessType.DELETE)
    @DeleteMapping("/{pkIds}")
    public AjaxResult remove(@PathVariable Long[] pkIds)
    {
        return toAjax(sseOrderInfoService.deleteSseOrderInfoByPkIds(pkIds));
    }

    /**
     * 导出订单汇总sql文件
     * @param ruleId
     * @return
     * @throws IOException
     */
    @PreAuthorize("@ss.hasPermi('sse:orderInfo:export')")
    @Log(title = "订单导出sql文件", businessType = BusinessType.EXPORT)
    @GetMapping("/exportSqlFile/{ruleId}")
    public AjaxResult exportSqlZip(@PathVariable("ruleId") Long ruleId) throws IOException {
        byte[] zipData = orderExportService.exportOrderSqlToZip(ruleId);
        return AjaxResult.success(zipData);
    }
}
