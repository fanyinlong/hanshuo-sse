package com.shenmei.data.sse.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.shenmei.data.sse.dataservice.orderdata.OrderStatsChartsService;
import com.shenmei.data.sse.dataservice.ruledata.OrderStatsCalService;
import com.shenmei.data.sse.service.ISseOrderInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.shenmei.data.sse.domain.SseOrderStats;
import com.shenmei.data.sse.service.ISseOrderStatsService;
import com.shenmei.data.common.annotation.Log;
import com.shenmei.data.common.core.controller.BaseController;
import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.common.core.page.TableDataInfo;
import com.shenmei.data.common.enums.BusinessType;
import com.shenmei.data.common.utils.poi.ExcelUtil;

/**
 * 订单统计Controller
 *
 * @author Song
 * @date 2025-08-11
 */
@RestController
@RequestMapping("/sse/orderStats")
public class SseOrderStatsController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(SseOrderStatsController.class);

    @Autowired
    private ISseOrderStatsService sseOrderStatsService;

    @Autowired
    private ISseOrderInfoService sseOrderInfoService;

    @Resource
    private OrderStatsCalService orderStatsCalService;

    @Resource
    private OrderStatsChartsService orderStatsChartsService;

    /**
     * 查询订单统计列表
     */
    @PreAuthorize("@ss.hasPermi('sse:orderStats:list')")
    @GetMapping("/list")
    public TableDataInfo list(SseOrderStats sseOrderStats)
    {
        startPage();
        List<SseOrderStats> list = sseOrderStatsService.selectSseOrderStatsList(sseOrderStats);
        return getDataTable(list);
    }

    /**
     * 导出订单统计列表
     */
    @PreAuthorize("@ss.hasPermi('sse:orderStats:export')")
    @Log(title = "订单统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SseOrderStats sseOrderStats)
    {
        List<SseOrderStats> list = sseOrderStatsService.selectSseOrderStatsList(sseOrderStats);
        ExcelUtil<SseOrderStats> util = new ExcelUtil<SseOrderStats>(SseOrderStats.class);
        util.exportExcel(response, list, "订单统计数据");
    }

    /**
     * 获取订单统计详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:orderStats:query')")
    @GetMapping(value = "/{pkId}")
    public AjaxResult getInfo(@PathVariable("pkId") Long pkId)
    {
        return success(sseOrderStatsService.selectSseOrderStatsByPkId(pkId));
    }

    /**
     * 新增订单统计
     */
    @PreAuthorize("@ss.hasPermi('sse:orderStats:add')")
    @Log(title = "订单统计", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SseOrderStats sseOrderStats)
    {
        SseOrderStats orderStats = new SseOrderStats();
        orderStats=orderStatsCalService.calOrderStats(sseOrderStats.getRuleId());
        return toAjax(sseOrderStatsService.insertSseOrderStats(orderStats));
    }

    /**
     * 修改订单统计
     */
    @PreAuthorize("@ss.hasPermi('sse:orderStats:edit')")
    @Log(title = "订单统计", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SseOrderStats sseOrderStats)
    {
        return toAjax(sseOrderStatsService.updateSseOrderStats(sseOrderStats));
    }

    /**
     * 刷新订单统计
     */
//    @PreAuthorize("@ss.hasPermi('sse:orderStats:edit')")
    @Log(title = "订单统计", businessType = BusinessType.REFRESH)
    @PostMapping("/refresh/{pkId}")
    public AjaxResult refresh(@PathVariable("pkId") Long pkId)
    {
        SseOrderStats orderStats = new SseOrderStats();
        orderStats= sseOrderStatsService.selectSseOrderStatsByPkId(pkId);
        orderStats=orderStatsCalService.calOrderStats(orderStats.getRuleId());
        orderStats.setPkId(pkId);
        log.info("orderStats:{}",orderStats);
        return toAjax(sseOrderStatsService.updateSseOrderStats(orderStats));
    }

    /**
     * 删除订单统计
     */
    @PreAuthorize("@ss.hasPermi('sse:orderStats:remove')")
    @Log(title = "订单统计", businessType = BusinessType.DELETE)
    @DeleteMapping("/{pkIds}")
    public AjaxResult remove(@PathVariable Long[] pkIds)
    {
        return toAjax(sseOrderStatsService.deleteSseOrderStatsByPkIds(pkIds));
    }

    /**
     * 查询订单统计详情
     */
//    @PreAuthorize("@ss.hasPermi('sse:orderStats:list')")
    @Log(title = "订单统计单项", businessType = BusinessType.OTHER)
    @PostMapping("/listDetail")
    public String listDetail(
            @RequestParam("chartType") String chartType,
            @RequestParam("ruleId") Long ruleId  // 直接接收表单参数
            ) {
        try {
            // 获取数据
            String data = orderStatsChartsService.getOrderStatsChartsData(chartType, ruleId);
            log.info("data:{}",data);
            return data;
        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("message", e.getMessage());
            return errorMap.toString();
        }
    }

}
