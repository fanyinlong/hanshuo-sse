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
import com.shenmei.data.sse.domain.SseParam;
import com.shenmei.data.sse.service.ISseParamService;

/**
 * 参数定义Controller
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
@RestController
@RequestMapping("/sse/param")
public class SseParamController extends BaseController
{
    @Autowired
    private ISseParamService sseParamService;

    /**
     * 查询参数定义列表
     */
    @PreAuthorize("@ss.hasPermi('sse:param:list')")
    @GetMapping("/list")
    public TableDataInfo list(SseParam sseParam)
    {
        startPage();
        List<SseParam> list = sseParamService.selectSseParamList(sseParam);
        return getDataTable(list);
    }

    /**
     * 导出参数定义列表
     */
    @PreAuthorize("@ss.hasPermi('sse:param:export')")
    @Log(title = "参数定义", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SseParam sseParam)
    {
        List<SseParam> list = sseParamService.selectSseParamList(sseParam);
        ExcelUtil<SseParam> util = new ExcelUtil<SseParam>(SseParam.class);
        util.exportExcel(response, list, "参数定义数据");
    }

    /**
     * 获取参数定义详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:param:query')")
    @GetMapping(value = "/{paramId}")
    public AjaxResult getInfo(@PathVariable("paramId") String paramId)
    {
        return success(sseParamService.selectSseParamByParamId(paramId));
    }

    /**
     * 新增参数定义
     */
    @PreAuthorize("@ss.hasPermi('sse:param:add')")
    @Log(title = "参数定义", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SseParam sseParam)
    {
        return toAjax(sseParamService.insertSseParam(sseParam));
    }

    /**
     * 修改参数定义
     */
    @PreAuthorize("@ss.hasPermi('sse:param:edit')")
    @Log(title = "参数定义", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SseParam sseParam)
    {
        return toAjax(sseParamService.updateSseParam(sseParam));
    }

    /**
     * 删除参数定义
     */
    @PreAuthorize("@ss.hasPermi('sse:param:remove')")
    @Log(title = "参数定义", businessType = BusinessType.DELETE)
	@DeleteMapping("/{paramIds}")
    public AjaxResult remove(@PathVariable String[] paramIds)
    {
        return toAjax(sseParamService.deleteSseParamByParamIds(paramIds));
    }
}
