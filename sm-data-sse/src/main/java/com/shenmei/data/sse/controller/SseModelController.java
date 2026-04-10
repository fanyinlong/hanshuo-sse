package com.shenmei.data.sse.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.shenmei.data.common.annotation.Log;
import com.shenmei.data.common.core.controller.BaseController;
import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.common.core.page.TableDataInfo;
import com.shenmei.data.common.enums.BusinessType;
import com.shenmei.data.common.utils.poi.ExcelUtil;
import com.shenmei.data.sse.dto.ModelDo;
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
import com.shenmei.data.sse.domain.SseModel;
import com.shenmei.data.sse.service.ISseModelService;

/**
 * 数据模型Controller
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
@RestController
@RequestMapping("/sse/model")
public class SseModelController extends BaseController {
    @Autowired
    private ISseModelService sseModelService;

    /**
     * 查询数据模型列表
     */
    @PreAuthorize("@ss.hasPermi('sse:model:list')")
    @GetMapping("/list")
    public TableDataInfo list(SseModel sseModel) {
        startPage();
        List<SseModel> list = sseModelService.selectSseModelList(sseModel);
        return getDataTable(list);
    }

    /**
     * 导出数据模型列表
     */
    @PreAuthorize("@ss.hasPermi('sse:model:export')")
    @Log(title = "数据模型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SseModel sseModel) {
        List<SseModel> list = sseModelService.selectSseModelList(sseModel);
        ExcelUtil<SseModel> util = new ExcelUtil<SseModel>(SseModel.class);
        util.exportExcel(response, list, "数据模型数据");
    }

    /**
     * 获取数据模型详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:model:query')")
    @GetMapping(value = "/{modelId}")
    public AjaxResult getInfo(@PathVariable("modelId") String modelId) {
        return success(sseModelService.selectSseModelByModelId(modelId));
    }

    /**
     * 新增数据模型
     */
    @PreAuthorize("@ss.hasPermi('sse:model:add')")
    @Log(title = "数据模型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ModelDo sseModel) {
        return toAjax(sseModelService.insertSseModel(sseModel));
    }

    /**
     * 修改数据模型
     */
    @PreAuthorize("@ss.hasPermi('sse:model:edit')")
    @Log(title = "数据模型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SseModel sseModel) {
        return toAjax(sseModelService.updateSseModel(sseModel));
    }

    /**
     * 删除数据模型
     */
    @PreAuthorize("@ss.hasPermi('sse:model:remove')")
    @Log(title = "数据模型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{modelIds}")
    public AjaxResult remove(@PathVariable String[] modelIds) {
        return toAjax(sseModelService.deleteSseModelByModelIds(modelIds));
    }

    /**
     * 新增显示页面
     * @return
     */
    @PreAuthorize("@ss.hasPermi('sse:model:addShow')")
    @GetMapping("addShow")
    public AjaxResult addShow() {
        return success(sseModelService.getParamList());
    }

    /**
     * 更新显示页面
     * @param modelId
     * @return
     */
    @PreAuthorize("@ss.hasPermi('sse:model:edit')")
    @GetMapping("updateShow/{modelId}")
    public AjaxResult updateShow(@PathVariable String modelId) {
        return success(sseModelService.getModel(modelId));
    }


    /**
     * 启用(status-传0)/停用(status-传1)
     * @param modelId
     * @param status
     * @return
     */
    @PreAuthorize("@ss.hasPermi('sse:model:setStatus')")
    @GetMapping("setStatus/{modelId}/{status}")
    public AjaxResult setStatus(@PathVariable String modelId,
                                @PathVariable String status) {
        return toAjax(sseModelService.setStatus(modelId,status));
    }


}
