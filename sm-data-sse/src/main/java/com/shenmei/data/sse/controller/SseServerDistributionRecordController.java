package com.shenmei.data.sse.controller;

import com.shenmei.data.common.annotation.Log;
import com.shenmei.data.common.core.controller.BaseController;
import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.common.core.page.TableDataInfo;
import com.shenmei.data.common.enums.BusinessType;
import com.shenmei.data.common.utils.poi.ExcelUtil;
import com.shenmei.data.sse.domain.SseServerDistributionRecord;
import com.shenmei.data.sse.service.ISseServerDistributionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 服务器分发记录Controller
 *
 * @author ruoyi
 * @date 2025-08-05
 */
@RestController
@RequestMapping("/sse/serverDistribution")
public class SseServerDistributionRecordController extends BaseController {
    @Autowired
    private ISseServerDistributionRecordService sseServerDistributionRecordService;

    /**
     * 查询服务器分发记录列表
     */
    @PreAuthorize("@ss.hasPermi('sse:serverDistribution:list')")
    @GetMapping("/list")
    public TableDataInfo list(SseServerDistributionRecord sseServerDistributionRecord) {
        startPage();
        List<SseServerDistributionRecord> list = sseServerDistributionRecordService.selectSseServerDistributionRecordList(sseServerDistributionRecord);
        return getDataTable(list);
    }

    /**
     * 导出服务器分发记录列表
     */
    @PreAuthorize("@ss.hasPermi('sse:serverDistribution:export')")
    @Log(title = "服务器分发记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SseServerDistributionRecord sseServerDistributionRecord) {
        List<SseServerDistributionRecord> list = sseServerDistributionRecordService.selectSseServerDistributionRecordList(sseServerDistributionRecord);
        ExcelUtil<SseServerDistributionRecord> util = new ExcelUtil<SseServerDistributionRecord>(SseServerDistributionRecord.class);
        util.exportExcel(response, list, "服务器分发记录数据");
    }

    /**
     * 获取服务器分发记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:serverDistribution:query')")
    @GetMapping(value = "/{pkId}")
    public AjaxResult getInfo(@PathVariable("pkId") Long pkId) {
        return success(sseServerDistributionRecordService.selectSseServerDistributionRecordByPkId(pkId));
    }

    /**
     * 新增服务器分发记录
     */
    @PreAuthorize("@ss.hasPermi('sse:serverDistribution:add')")
    @Log(title = "服务器分发记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SseServerDistributionRecord sseServerDistributionRecord) throws Exception {
        int result = sseServerDistributionRecordService.insertSseServerDistributionRecord(sseServerDistributionRecord);
        if (result > 0) {
            // 插入成功后，实体对象中已经包含了数据库生成的自增ID
            return AjaxResult.success("创建成功", sseServerDistributionRecord);
        } else {
            return AjaxResult.error("创建失败");
        }
    }

    /**
     * 修改服务器分发记录
     */
    @PreAuthorize("@ss.hasPermi('sse:serverDistribution:edit')")
    @Log(title = "服务器分发记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SseServerDistributionRecord sseServerDistributionRecord) {
        return toAjax(sseServerDistributionRecordService.updateSseServerDistributionRecord(sseServerDistributionRecord));
    }

    /**
     * 删除服务器分发记录
     */
    @PreAuthorize("@ss.hasPermi('sse:serverDistribution:remove')")
    @Log(title = "服务器分发记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{pkIds}")
    public AjaxResult remove(@PathVariable Long[] pkIds) {
        return toAjax(sseServerDistributionRecordService.deleteSseServerDistributionRecordByPkIds(pkIds));
    }

    /**
     * 获取分发过程记录
     */
    @PreAuthorize("@ss.hasPermi('sse:serverDistribution:query')")
    @GetMapping(value = "/progress/{pkId}")
    public AjaxResult getProgressInfo(@PathVariable("pkId") Long pkId) {
        return sseServerDistributionRecordService.getProgressInfo(pkId);
    }
}

