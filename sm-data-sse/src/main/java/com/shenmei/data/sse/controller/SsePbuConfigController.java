package com.shenmei.data.sse.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.shenmei.data.common.annotation.Log;
import com.shenmei.data.common.core.controller.BaseController;
import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.common.core.page.TableDataInfo;
import com.shenmei.data.common.enums.BusinessType;
import com.shenmei.data.common.utils.poi.ExcelUtil;
import com.shenmei.data.sse.domain.SsePbuConfig;
import com.shenmei.data.sse.service.ISsePbuConfigService;

/**
 * PBU配置Controller
 *
 * @author Song
 * @date 2025-08-01
 */
@RestController
@RequestMapping("/sse/pbuConfig")
public class SsePbuConfigController extends BaseController
{
    @Autowired
    private ISsePbuConfigService ssePbuConfigService;

    /**
     * 查询PBU配置列表
     */
    @PreAuthorize("@ss.hasPermi('sse:pbuConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(SsePbuConfig ssePbuConfig)
    {
        startPage();
        List<SsePbuConfig> list = ssePbuConfigService.selectSsePbuConfigList(ssePbuConfig);
        return getDataTable(list);
    }

    /**
     * 导出PBU配置列表
     */
    @PreAuthorize("@ss.hasPermi('sse:pbuConfig:export')")
    @Log(title = "PBU配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SsePbuConfig ssePbuConfig)
    {
        List<SsePbuConfig> list = ssePbuConfigService.selectSsePbuConfigList(ssePbuConfig);
        ExcelUtil<SsePbuConfig> util = new ExcelUtil<SsePbuConfig>(SsePbuConfig.class);
        util.exportExcel(response, list, "PBU配置数据");
    }

    /**
     * 获取PBU配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:pbuConfig:query')")
    @GetMapping(value = "/{pkId}")
    public AjaxResult getInfo(@PathVariable("pkId") Long pkId)
    {
        return success(ssePbuConfigService.selectSsePbuConfigByPkId(pkId));
    }

    /**
     * 新增PBU配置
     */
    @PreAuthorize("@ss.hasPermi('sse:pbuConfig:add')")
    @Log(title = "PBU配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SsePbuConfig ssePbuConfig)
    {
        return toAjax(ssePbuConfigService.insertSsePbuConfig(ssePbuConfig));
    }

    /**
     * 修改PBU配置
     */
    @PreAuthorize("@ss.hasPermi('sse:pbuConfig:edit')")
    @Log(title = "PBU配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SsePbuConfig ssePbuConfig)
    {
        return toAjax(ssePbuConfigService.updateSsePbuConfig(ssePbuConfig));
    }

    /**
     * 删除PBU配置
     */
    @PreAuthorize("@ss.hasPermi('sse:pbuConfig:remove')")
    @Log(title = "PBU配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{pkIds}")
    public AjaxResult remove(@PathVariable Long[] pkIds)
    {
        return toAjax(ssePbuConfigService.deleteSsePbuConfigByPkIds(pkIds));
    }

/**
     * 初始化PBU配置
     */
    @PreAuthorize("@ss.hasPermi('sse:pbuInfo:add')")
    @Log(title = "PBU配置", businessType = BusinessType.INSERT)
    @PostMapping("/init")
    public AjaxResult init()
    {
        ssePbuConfigService.init();
        return AjaxResult.success();
    }
}

