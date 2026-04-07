package com.shenmei.data.sse.controller;

import com.shenmei.data.common.core.page.TableDataInfo;
import com.shenmei.data.sse.domain.SseServerInfo;
import com.shenmei.data.sse.domain.SseServerRule;
import com.shenmei.data.sse.service.ISseServerRuleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.shenmei.data.common.annotation.Log;
import com.shenmei.data.common.core.controller.BaseController;
import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.common.enums.BusinessType;

import java.io.IOException;
import java.util.List;

/**
 * 主机规则信息Controller
 *
 * @author Song
 * @date 2025-07-29
 */
@RestController
@RequestMapping("/sse/serverRule")
public class SseServerRuleController extends BaseController
{


    @Autowired
    private ISseServerRuleService sseServerRuleService;

    /**
     * 查询主机规则列表
     */
    @PreAuthorize("@ss.hasPermi('sse:host:list')")
    @GetMapping("/list")
    public TableDataInfo list(SseServerRule sseServiceRule) throws Exception {
        startPage();
        List<SseServerRule> list = sseServerRuleService.selectSseServerInfoList(sseServiceRule);
        return getDataTable(list);
    }
    /**
     * 新增主机规则信息
     */
    @PreAuthorize("@ss.hasPermi('sse:host:add')")
    @Log(title = "主机规则信息", businessType = BusinessType.INSERT)
    @PostMapping("/addServerRule")
    public AjaxResult addServerInfo(@RequestBody SseServerRule sseServiceRule) throws Exception {
        return toAjax(sseServerRuleService.insertSseServerRule(sseServiceRule));
    }

    @PreAuthorize("@ss.hasPermi('sse:host:remove')")
    @Log(title = "主机规则信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(sseServerRuleService.deleteSseServerRuleByServerIds(ids));
    }

    /**
     * 获取主机信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:host:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) throws Exception {
        return success(sseServerRuleService.selectSseServerRuleById(id));
    }

    /**
     * 修改主机规则
     */
    @PreAuthorize("@ss.hasPermi('sse:host:edit')")
    @Log(title = "主机规则信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SseServerRule sseServerRule) throws Exception {
        return toAjax(sseServerRuleService.updateSseServerRule(sseServerRule));
    }

//    @PreAuthorize("@ss.hasPermi('sse:orderInfo:export')")
//    @Log(title = "订单导出sql文件", businessType = BusinessType.EXPORT)
//    @GetMapping("/export/{ruleId}")
//    public AjaxResult export(@PathVariable("ruleId") Long ruleId) throws IOException {
//        return AjaxResult.success(sseServerRuleService.orderDataDistribution(ruleId));
//    }



}

