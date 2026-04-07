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
import com.shenmei.data.sse.domain.SseAccountInfo;
import com.shenmei.data.sse.service.ISseAccountInfoService;
import com.shenmei.data.common.annotation.Log;
import com.shenmei.data.common.core.controller.BaseController;
import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.common.core.page.TableDataInfo;
import com.shenmei.data.common.enums.BusinessType;
import com.shenmei.data.common.utils.poi.ExcelUtil;

/**
 * 账户信息Controller
 *
 * @author song
 * @date 2025-07-28
 */
@RestController
@RequestMapping("/sse/accountInfo")
public class SseAccountInfoController extends BaseController
{
    @Autowired
    private ISseAccountInfoService sseAccountInfoService;

    /**
     * 查询账户信息列表
     */
    @PreAuthorize("@ss.hasPermi('sse:accountInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(SseAccountInfo sseAccountInfo)
    {
        startPage();
        List<SseAccountInfo> list = sseAccountInfoService.selectSseAccountInfoList(sseAccountInfo);
        return getDataTable(list);
    }

    /**
     * 导出账户信息列表
     */
    @PreAuthorize("@ss.hasPermi('sse:accountInfo:export')")
    @Log(title = "账户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SseAccountInfo sseAccountInfo)
    {
        List<SseAccountInfo> list = sseAccountInfoService.selectSseAccountInfoList(sseAccountInfo);
        ExcelUtil<SseAccountInfo> util = new ExcelUtil<SseAccountInfo>(SseAccountInfo.class);
        util.exportExcel(response, list, "账户信息数据");
    }

    /**
     * 获取账户信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:accountInfo:query')")
    @GetMapping(value = "/{pkId}")
    public AjaxResult getInfo(@PathVariable("pkId") Long pkId)
    {
        return success(sseAccountInfoService.selectSseAccountInfoByPkId(pkId));
    }

    /**
     * 新增账户信息
     */
    @PreAuthorize("@ss.hasPermi('sse:accountInfo:add')")
    @Log(title = "账户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SseAccountInfo sseAccountInfo)
    {
        return toAjax(sseAccountInfoService.insertSseAccountInfo(sseAccountInfo));
    }

    /**
     * 修改账户信息
     */
    @PreAuthorize("@ss.hasPermi('sse:accountInfo:edit')")
    @Log(title = "账户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SseAccountInfo sseAccountInfo)
    {
        return toAjax(sseAccountInfoService.updateSseAccountInfo(sseAccountInfo));
    }

    /**
     * 删除账户信息
     */
    @PreAuthorize("@ss.hasPermi('sse:accountInfo:remove')")
    @Log(title = "账户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{pkIds}")
    public AjaxResult remove(@PathVariable Long[] pkIds)
    {
        return toAjax(sseAccountInfoService.deleteSseAccountInfoByPkIds(pkIds));
    }

    /**
     * 初始化账户信息
     */
    @PreAuthorize("@ss.hasPermi('sse:accountInfo:add')")
    @Log(title = "账户信息", businessType = BusinessType.INSERT)
    @PostMapping("/init")
    public AjaxResult init()
    {
        sseAccountInfoService.init();
        return AjaxResult.success();
    }
}
