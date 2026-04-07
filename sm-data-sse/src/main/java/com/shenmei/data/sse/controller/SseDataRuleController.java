package com.shenmei.data.sse.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shenmei.data.common.annotation.Log;
import com.shenmei.data.common.core.controller.BaseController;
import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.common.core.page.TableDataInfo;
import com.shenmei.data.common.enums.BusinessType;
import com.shenmei.data.common.utils.poi.ExcelUtil;
import com.shenmei.data.sse.dataservice.ruledata.RuleDataInitSetupService;
import com.shenmei.data.sse.domain.SseDataRule;
import com.shenmei.data.sse.service.ISseDataRuleInitService;
import com.shenmei.data.sse.service.ISseDataRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 规则配置Controller
 * 
 * @author teabot
 * @date 2025-07-19
 */
@RestController
@RequestMapping("/sse/ruleinfo")
public class SseDataRuleController extends BaseController
{
    @Autowired
    private ISseDataRuleService sseDataRuleService;

    @Resource
    private ISseDataRuleInitService sseDataRuleInitService;

    @Resource
    private RuleDataInitSetupService ruleDataInitSetupService;

    /**
     * 查询规则配置列表
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleinfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(SseDataRule sseDataRule)
    {
        startPage();
        List<SseDataRule> list = sseDataRuleService.selectSseDataRuleList(sseDataRule);
        return getDataTable(list);
    }

    /**
     * 导出规则配置列表
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleinfo:export')")
    @Log(title = "SseDataRuleService", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SseDataRule sseDataRule)
    {
        List<SseDataRule> list = sseDataRuleService.selectSseDataRuleList(sseDataRule);
        ExcelUtil<SseDataRule> util = new ExcelUtil<SseDataRule>(SseDataRule.class);
        util.exportExcel(response, list, "规则配置数据");
    }

    /**
     * 获取规则配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleinfo:query')")
    @GetMapping(value = "/{pkId}")
    public AjaxResult getInfo(@PathVariable("pkId") Long pkId)
    {
        return success(sseDataRuleService.selectSseDataRuleByPkId(pkId));
    }

    /**
     * 新增规则配置
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleinfo:add')")
    @Log(title = "SseDataRuleService", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SseDataRule sseDataRule)
    {
        return toAjax(sseDataRuleService.insertSseDataRule(sseDataRule));
    }

    /**
     * 修改规则配置
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleinfo:edit')")
    @Log(title = "SseDataRuleService", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SseDataRule sseDataRule)
    {
        return toAjax(sseDataRuleService.updateSseDataRule(sseDataRule));
    }

    /**
     * 删除规则配置
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleinfo:remove')")
    @Log(title = "SseDataRuleService", businessType = BusinessType.DELETE)
	@DeleteMapping("/{pkIds}")
    public AjaxResult remove(@PathVariable Long[] pkIds)
    {
        return toAjax(sseDataRuleService.deleteSseDataRuleByPkIds(pkIds));
    }

    //初始化规则
    @PreAuthorize("@ss.hasPermi('sse:ruleinfo:edit')")
    @Log(title = "SseDataRuleService", businessType = BusinessType.INSERT)
    @PostMapping("/ruleInit/{pkId}")
    public AjaxResult initData(@PathVariable("pkId") Long pkId) throws JsonProcessingException {

        return  ruleDataInitSetupService.setUp(pkId);


    }
}
