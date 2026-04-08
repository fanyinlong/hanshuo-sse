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
import com.shenmei.data.sse.domain.SseScene;
import com.shenmei.data.sse.service.ISseSceneService;


/**
 * 测试场景Controller
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
@RestController
@RequestMapping("/sse/scene")
public class SseSceneController extends BaseController
{
    @Autowired
    private ISseSceneService sseSceneService;

    /**
     * 查询测试场景列表
     */
    @PreAuthorize("@ss.hasPermi('sse:scene:list')")
    @GetMapping("/list")
    public TableDataInfo list(SseScene sseScene)
    {
        startPage();
        List<SseScene> list = sseSceneService.selectSseSceneList(sseScene);
        return getDataTable(list);
    }

    /**
     * 导出测试场景列表
     */
    @PreAuthorize("@ss.hasPermi('sse:scene:export')")
    @Log(title = "测试场景", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SseScene sseScene)
    {
        List<SseScene> list = sseSceneService.selectSseSceneList(sseScene);
        ExcelUtil<SseScene> util = new ExcelUtil<SseScene>(SseScene.class);
        util.exportExcel(response, list, "测试场景数据");
    }

    /**
     * 获取测试场景详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:scene:query')")
    @GetMapping(value = "/{sceneId}")
    public AjaxResult getInfo(@PathVariable("sceneId") Long sceneId)
    {
        return success(sseSceneService.selectSseSceneBySceneId(sceneId));
    }

    /**
     * 新增测试场景
     */
    @PreAuthorize("@ss.hasPermi('sse:scene:add')")
    @Log(title = "测试场景", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SseScene sseScene)
    {
        return toAjax(sseSceneService.insertSseScene(sseScene));
    }

    /**
     * 修改测试场景
     */
    @PreAuthorize("@ss.hasPermi('sse:scene:edit')")
    @Log(title = "测试场景", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SseScene sseScene)
    {
        return toAjax(sseSceneService.updateSseScene(sseScene));
    }

    /**
     * 删除测试场景
     */
    @PreAuthorize("@ss.hasPermi('sse:scene:remove')")
    @Log(title = "测试场景", businessType = BusinessType.DELETE)
	@DeleteMapping("/{sceneIds}")
    public AjaxResult remove(@PathVariable Long[] sceneIds)
    {
        return toAjax(sseSceneService.deleteSseSceneBySceneIds(sceneIds));
    }
}
