package com.shenmei.data.sse.controller;

import com.shenmei.data.common.annotation.Log;
import com.shenmei.data.common.core.controller.BaseController;
import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.common.core.page.TableDataInfo;
import com.shenmei.data.common.enums.BusinessType;
import com.shenmei.data.common.utils.poi.ExcelUtil;
import com.shenmei.data.sse.domain.SseDataRuleInit;
import com.shenmei.data.sse.service.ISseDataRuleInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 规则初始化Controller
 * 
 * @author ruoyi
 * @date 2025-08-02
 */
@RestController
@RequestMapping("/sse/ruleInit")
public class SseDataRuleInitController extends BaseController
{
    @Autowired
    private ISseDataRuleInitService sseDataRuleInitService;

    /**
     * 查询规则初始化列表
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleinfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(SseDataRuleInit sseDataRuleInit)
    {
        startPage();
        List<SseDataRuleInit> list = sseDataRuleInitService.selectSseDataRuleInitList(sseDataRuleInit);
        return getDataTable(list);
    }

    /**
     * 导出规则初始化列表
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleinfo:export')")
    @Log(title = "规则初始化", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SseDataRuleInit sseDataRuleInit)
    {
        List<SseDataRuleInit> list = sseDataRuleInitService.selectSseDataRuleInitList(sseDataRuleInit);
        ExcelUtil<SseDataRuleInit> util = new ExcelUtil<SseDataRuleInit>(SseDataRuleInit.class);
        util.exportExcel(response, list, "规则初始化数据");
    }

    /**
     * 获取规则初始化详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleinfo:query')")
    @GetMapping(value = "/{pkId}")
    public AjaxResult getInfo(@PathVariable("pkId") Long pkId)
    {
        return success(sseDataRuleInitService.selectSseDataRuleInitByPkId(pkId));
    }

    /**
     * 新增规则初始化
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleinfo:add')")
    @Log(title = "规则初始化", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SseDataRuleInit sseDataRuleInit)
    {
       return toAjax(sseDataRuleInitService.insertSseDataRuleInit(sseDataRuleInit));
    }

    /**
     * 修改规则初始化
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleinfo:edit')")
    @Log(title = "规则初始化", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SseDataRuleInit sseDataRuleInit)
    {
        return toAjax(sseDataRuleInitService.updateSseDataRuleInit(sseDataRuleInit));
    }

    /**
     * 删除规则初始化
     */
    @PreAuthorize("@ss.hasPermi('sse:ruleinfo:remove')")
    @Log(title = "规则初始化", businessType = BusinessType.DELETE)
	@DeleteMapping("/{pkIds}")
    public AjaxResult remove(@PathVariable Long[] pkIds)
    {
        return toAjax(sseDataRuleInitService.deleteSseDataRuleInitByPkIds(pkIds));
    }
}
