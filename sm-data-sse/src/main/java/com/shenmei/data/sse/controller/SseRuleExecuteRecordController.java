package com.shenmei.data.sse.controller;

import com.shenmei.data.common.annotation.Log;
import com.shenmei.data.common.core.controller.BaseController;
import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.common.core.page.TableDataInfo;
import com.shenmei.data.common.enums.BusinessType;
import com.shenmei.data.common.utils.poi.ExcelUtil;
import com.shenmei.data.sse.domain.SseRuleExecuteRecord;
import com.shenmei.data.sse.service.ISseRuleExecuteRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 规则执行记录Controller
 *
 * @author ruoyi
 * @date 2025-08-05
 */
@RestController
@RequestMapping("/sse/ruleExecute")
public class SseRuleExecuteRecordController extends BaseController {
    @Autowired
    private ISseRuleExecuteRecordService sseRuleExecuteRecordService;

    /**
     * 查询规则执行记录列表
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleExecute:list')")
    @GetMapping("/list")
    public TableDataInfo list(SseRuleExecuteRecord sseRuleExecuteRecord) {
        startPage();
        List<SseRuleExecuteRecord> list = sseRuleExecuteRecordService.selectSseRuleExecuteRecordList(sseRuleExecuteRecord);
        return getDataTable(list);
    }

    /**
     * 导出规则执行记录列表
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleExecute:export')")
    @Log(title = "规则执行记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SseRuleExecuteRecord sseRuleExecuteRecord) {
        List<SseRuleExecuteRecord> list = sseRuleExecuteRecordService.selectSseRuleExecuteRecordList(sseRuleExecuteRecord);
        ExcelUtil<SseRuleExecuteRecord> util = new ExcelUtil<SseRuleExecuteRecord>(SseRuleExecuteRecord.class);
        util.exportExcel(response, list, "规则执行记录数据");
    }

    /**
     * 获取规则执行记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleExecute:query')")
    @GetMapping(value = "/{pkId}")
    public AjaxResult getInfo(@PathVariable("pkId") Long pkId) {
        return success(sseRuleExecuteRecordService.selectSseRuleExecuteRecordByPkId(pkId));
    }

    /**
     * 新增规则执行记录
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleExecute:add')")
    @Log(title = "规则执行记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SseRuleExecuteRecord sseRuleExecuteRecord) throws Exception {
        return toAjax(sseRuleExecuteRecordService.insertSseRuleExecuteRecord(sseRuleExecuteRecord));
    }

    /**
     * 修改规则执行记录
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleExecute:edit')")
    @Log(title = "规则执行记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SseRuleExecuteRecord sseRuleExecuteRecord) {
        return toAjax(sseRuleExecuteRecordService.updateSseRuleExecuteRecord(sseRuleExecuteRecord));
    }

    /**
     * 删除规则执行记录
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleExecute:remove')")
    @Log(title = "规则执行记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{pkIds}")
    public AjaxResult remove(@PathVariable Long[] pkIds) {
        return toAjax(sseRuleExecuteRecordService.deleteSseRuleExecuteRecordByPkIds(pkIds));
    }

    /**
     * 获取规则执行过程记录
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleExecute:query')")
    @GetMapping(value = "/progress/{pkId}")
    public AjaxResult getProgressInfo(@PathVariable("pkId") Long pkId) {
        return sseRuleExecuteRecordService.getProgressInfo(pkId);
    }


    /**
     * 根据规则 ID 获取订单总数
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleExecute:query')")
    @GetMapping(value = "/totalOrderCount/{ruleId}")
    public AjaxResult getTotalOrderCountByRuleId(@PathVariable("ruleId") Long ruleId) {
        return sseRuleExecuteRecordService.getTotalOrderCountByRuleId(ruleId);
    }


}
