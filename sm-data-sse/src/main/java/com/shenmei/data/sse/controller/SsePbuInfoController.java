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

import com.shenmei.data.sse.domain.SsePbuInfo;
import com.shenmei.data.sse.service.ISsePbuInfoService;

/**
 * PBU信息Controllerimport poi.utils.common.com.shenmei.data.ExcelUtil;
import page.core.common.com.shenmei.data.TableDataInfo;

 * 
 * @author ruoyi
 * @date 2025-07-27
 */
@RestController
@RequestMapping("/sse/pbuInfo")
public class SsePbuInfoController extends BaseController
{
    @Autowired
    private ISsePbuInfoService ssePbuInfoService;

    /**
     * 查询PBU信息列表
     */
    @PreAuthorize("@ss.hasPermi('sse:pbuInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(SsePbuInfo ssePbuInfo)
    {
        startPage();
        List<SsePbuInfo> list = ssePbuInfoService.selectSsePbuInfoList(ssePbuInfo);
        return getDataTable(list);
    }

    /**
     * 导出PBU信息列表
     */
    @PreAuthorize("@ss.hasPermi('sse:pbuInfo:export')")
    @Log(title = "PBU信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SsePbuInfo ssePbuInfo)
    {
        List<SsePbuInfo> list = ssePbuInfoService.selectSsePbuInfoList(ssePbuInfo);
        ExcelUtil<SsePbuInfo> util = new ExcelUtil<SsePbuInfo>(SsePbuInfo.class);
        util.exportExcel(response, list, "PBU信息数据");
    }

    /**
     * 获取PBU信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:pbuInfo:query')")
    @GetMapping(value = "/{pbuId}")
    public AjaxResult getInfo(@PathVariable("pbuId") String pbuId)
    {
        return success(ssePbuInfoService.selectSsePbuInfoByPbuId(pbuId));
    }

    /**
     * 新增PBU信息
     */
    @PreAuthorize("@ss.hasPermi('sse:pbuInfo:add')")
    @Log(title = "PBU信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SsePbuInfo ssePbuInfo)
    {
        return toAjax(ssePbuInfoService.insertSsePbuInfo(ssePbuInfo));
    }

    /**
     * 修改PBU信息
     */
    @PreAuthorize("@ss.hasPermi('sse:pbuInfo:edit')")
    @Log(title = "PBU信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SsePbuInfo ssePbuInfo)
    {
        return toAjax(ssePbuInfoService.updateSsePbuInfo(ssePbuInfo));
    }

    /**
     * 删除PBU信息
     */
    @PreAuthorize("@ss.hasPermi('sse:pbuInfo:remove')")
    @Log(title = "PBU信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{pbuIds}")
    public AjaxResult remove(@PathVariable String[] pbuIds)
    {
        return toAjax(ssePbuInfoService.deleteSsePbuInfoByPbuIds(pbuIds));
    }

    /**
     * 初始化PBU信息
     */
    @PreAuthorize("@ss.hasPermi('sse:pbuInfo:add')")
    @Log(title = "PBU信息", businessType = BusinessType.INSERT)
    @PostMapping("/init")
    public AjaxResult init()
    {
        ssePbuInfoService.init();
        return AjaxResult.success();
    }
}
