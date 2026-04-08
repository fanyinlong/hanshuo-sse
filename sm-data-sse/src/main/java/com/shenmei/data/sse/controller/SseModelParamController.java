package com.shenmei.data.sse.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.shenmei.data.common.annotation.Log;
import com.shenmei.data.common.core.controller.BaseController;
import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.common.core.page.TableDataInfo;
import com.shenmei.data.common.enums.BusinessType;
import com.shenmei.data.common.utils.poi.ExcelUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.shenmei.data.sse.domain.SseModelParam;
import com.shenmei.data.sse.service.ISseModelParamService;


/**
 * 模型参数关联Controller
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
@RestController
@RequestMapping("/sse/modelParam")
public class SseModelParamController extends BaseController
{
    @Autowired
    private ISseModelParamService sseModelParamService;

    /**
     * 查询模型参数关联列表
     */
    @PreAuthorize("@ss.hasPermi('sse:param:list')")
    @GetMapping("/list")
    public TableDataInfo list(SseModelParam sseModelParam)
    {
        startPage();
        List<SseModelParam> list = sseModelParamService.selectSseModelParamList(sseModelParam);
        return getDataTable(list);
    }

    /**
     * 导出模型参数关联列表
     */
    @PreAuthorize("@ss.hasPermi('sse:param:export')")
    @Log(title = "模型参数关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SseModelParam sseModelParam)
    {
        List<SseModelParam> list = sseModelParamService.selectSseModelParamList(sseModelParam);
        ExcelUtil<SseModelParam> util = new ExcelUtil<SseModelParam>(SseModelParam.class);
        util.exportExcel(response, list, "模型参数关联数据");
    }

    /**
     * 获取模型参数关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:param:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sseModelParamService.selectSseModelParamById(id));
    }

    /**
     * 新增模型参数关联
     */
    @PreAuthorize("@ss.hasPermi('sse:param:add')")
    @Log(title = "模型参数关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SseModelParam sseModelParam)
    {
        return toAjax(sseModelParamService.insertSseModelParam(sseModelParam));
    }

    /**
     * 修改模型参数关联
     */
    @PreAuthorize("@ss.hasPermi('sse:param:edit')")
    @Log(title = "模型参数关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SseModelParam sseModelParam)
    {
        return toAjax(sseModelParamService.updateSseModelParam(sseModelParam));
    }

    /**
     * 删除模型参数关联
     */
    @PreAuthorize("@ss.hasPermi('sse:param:remove')")
    @Log(title = "模型参数关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sseModelParamService.deleteSseModelParamByIds(ids));
    }
}
