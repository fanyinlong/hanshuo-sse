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
import com.shenmei.data.sse.domain.SseSceneParamInfo;
import com.shenmei.data.sse.service.ISseSceneParamInfoService;

/**
 * 场景参数信息Controller
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
@RestController
@RequestMapping("/sse/info")
public class SseSceneParamInfoController extends BaseController
{
    @Autowired
    private ISseSceneParamInfoService sseSceneParamInfoService;

    /**
     * 查询场景参数信息列表
     */
    @PreAuthorize("@ss.hasPermi('sse:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(SseSceneParamInfo sseSceneParamInfo)
    {
        startPage();
        List<SseSceneParamInfo> list = sseSceneParamInfoService.selectSseSceneParamInfoList(sseSceneParamInfo);
        return getDataTable(list);
    }

    /**
     * 导出场景参数信息列表
     */
    @PreAuthorize("@ss.hasPermi('sse:info:export')")
    @Log(title = "场景参数信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SseSceneParamInfo sseSceneParamInfo)
    {
        List<SseSceneParamInfo> list = sseSceneParamInfoService.selectSseSceneParamInfoList(sseSceneParamInfo);
        ExcelUtil<SseSceneParamInfo> util = new ExcelUtil<SseSceneParamInfo>(SseSceneParamInfo.class);
        util.exportExcel(response, list, "场景参数信息数据");
    }

    /**
     * 获取场景参数信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:info:query')")
    @GetMapping(value = "/{paramInfoId}")
    public AjaxResult getInfo(@PathVariable("paramInfoId") Long paramInfoId)
    {
        return success(sseSceneParamInfoService.selectSseSceneParamInfoByParamInfoId(paramInfoId));
    }

    /**
     * 新增场景参数信息
     */
    @PreAuthorize("@ss.hasPermi('sse:info:add')")
    @Log(title = "场景参数信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SseSceneParamInfo sseSceneParamInfo)
    {
        return toAjax(sseSceneParamInfoService.insertSseSceneParamInfo(sseSceneParamInfo));
    }

    /**
     * 修改场景参数信息
     */
    @PreAuthorize("@ss.hasPermi('sse:info:edit')")
    @Log(title = "场景参数信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SseSceneParamInfo sseSceneParamInfo)
    {
        return toAjax(sseSceneParamInfoService.updateSseSceneParamInfo(sseSceneParamInfo));
    }

    /**
     * 删除场景参数信息
     */
    @PreAuthorize("@ss.hasPermi('sse:info:remove')")
    @Log(title = "场景参数信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{paramInfoIds}")
    public AjaxResult remove(@PathVariable Long[] paramInfoIds)
    {
        return toAjax(sseSceneParamInfoService.deleteSseSceneParamInfoByParamInfoIds(paramInfoIds));
    }
}
