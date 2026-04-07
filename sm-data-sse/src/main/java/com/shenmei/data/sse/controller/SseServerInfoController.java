package com.shenmei.data.sse.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpServletResponse;

import com.shenmei.data.sse.service.ISseServerRuleService;
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
import com.shenmei.data.sse.domain.SseServerInfo;
import com.shenmei.data.sse.service.ISseServerInfoService;
import com.shenmei.data.common.annotation.Log;
import com.shenmei.data.common.core.controller.BaseController;
import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.common.core.page.TableDataInfo;
import com.shenmei.data.common.enums.BusinessType;
import com.shenmei.data.common.utils.poi.ExcelUtil;
/**
 * 主机信息Controller
 *
 * @author Song
 * @date 2025-07-29
 */
@RestController
@RequestMapping("/sse/serverInfo")
public class SseServerInfoController extends BaseController
{
    @Autowired
    private ISseServerInfoService sseServerInfoService;
    @Autowired
    private ISseServerRuleService sseServerConfigService;
    /**
     * 查询主机信息列表
     */
    @PreAuthorize("@ss.hasPermi('sse:serverInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(SseServerInfo sseServerInfo) throws Exception {
        startPage();
        List<SseServerInfo> list = sseServerInfoService.selectSseServerInfoList(sseServerInfo);
        return getDataTable(list);
    }

    /**
     * 导出主机信息列表
     */
    @PreAuthorize("@ss.hasPermi('sse:serverInfo:export')")
    @Log(title = "主机信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SseServerInfo sseServerInfo) throws Exception {

        List<SseServerInfo> list = sseServerInfoService.selectSseServerInfoList(sseServerInfo);
        ExcelUtil<SseServerInfo> util = new ExcelUtil<SseServerInfo>(SseServerInfo.class);
        util.exportExcel(response, list, "主机信息数据");
//            sseServerConfigService.orderDataDistribution(101L);

    }

    /**
     * 获取主机信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:serverInfo:query')")
    @GetMapping(value = "/{serverId}")
    public AjaxResult getInfo(@PathVariable("serverId") String serverId) throws Exception {
        return success(sseServerInfoService.selectSseServerInfoByServerId(serverId));
    }

    /**
     * 新增主机信息
     */
    @PreAuthorize("@ss.hasPermi('sse:serverInfo:add')")
    @Log(title = "主机信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SseServerInfo sseServerInfo) throws Exception {
        return toAjax(sseServerInfoService.insertSseServerInfo(sseServerInfo));
    }



    /**
     * 修改主机信息
     */
    @PreAuthorize("@ss.hasPermi('sse:serverInfo:edit')")
    @Log(title = "主机信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SseServerInfo sseServerInfo) throws Exception {
        return toAjax(sseServerInfoService.updateSseServerInfo(sseServerInfo));
    }

    /**
     * 删除主机信息
     */
    @PreAuthorize("@ss.hasPermi('sse:serverInfo:remove')")
    @Log(title = "主机信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{serverIds}")
    public AjaxResult remove(@PathVariable String[] serverIds)
    {
        return toAjax(sseServerInfoService.deleteSseServerInfoByServerIds(serverIds));
    }
}
