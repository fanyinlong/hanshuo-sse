package com.shenmei.data.sse.controller;

import com.shenmei.data.common.annotation.Log;
import com.shenmei.data.common.core.controller.BaseController;
import com.shenmei.data.common.core.domain.AjaxResult;
import com.shenmei.data.common.core.domain.entity.SysUser;
import com.shenmei.data.common.core.page.TableDataInfo;
import com.shenmei.data.common.enums.BusinessType;
import com.shenmei.data.common.utils.StringUtils;
import com.shenmei.data.common.utils.poi.ExcelUtil;
import com.shenmei.data.sse.domain.SseSecurityInfo;
import com.shenmei.data.sse.service.ISseSecurityInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * 产品信息Controller
 *
 * @author Song
 * @date 2025-07-26
 */
@RestController
@RequestMapping("/sse/securityInfo")
public class SseSecurityInfoController extends BaseController {
    @Autowired
    private ISseSecurityInfoService sseSecurityInfoService;

    /**
     * 查询产品信息列表
     */
    @PreAuthorize("@ss.hasPermi('sse:securityInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(SseSecurityInfo sseSecurityInfo) {
        startPage();
        List<SseSecurityInfo> list = sseSecurityInfoService.selectSseSecurityInfoList(sseSecurityInfo);
        return getDataTable(list);
    }

    /**
     * 导出产品信息列表
     */
    @PreAuthorize("@ss.hasPermi('sse:securityInfo:export')")
    @Log(title = "产品信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SseSecurityInfo sseSecurityInfo) {
        List<SseSecurityInfo> list = sseSecurityInfoService.selectSseSecurityInfoList(sseSecurityInfo);
        ExcelUtil<SseSecurityInfo> util = new ExcelUtil<SseSecurityInfo>(SseSecurityInfo.class);
        util.exportExcel(response, list, "产品信息数据");
    }

    /**
     * 获取产品信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:securityInfo:query')")
    @GetMapping(value = "/{pkId}")
    public AjaxResult getInfo(@PathVariable("pkId") Long pkId) {
        return success(sseSecurityInfoService.selectSseSecurityInfoByPkId(pkId));
    }

    /**
     * 新增产品信息
     */
    @PreAuthorize("@ss.hasPermi('sse:securityInfo:add')")
    @Log(title = "产品信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SseSecurityInfo sseSecurityInfo) {
        return toAjax(sseSecurityInfoService.insertSseSecurityInfo(sseSecurityInfo));
    }

    /**
     * 修改产品信息
     */
    @PreAuthorize("@ss.hasPermi('sse:securityInfo:edit')")
    @Log(title = "产品信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SseSecurityInfo sseSecurityInfo) {
        return toAjax(sseSecurityInfoService.updateSseSecurityInfo(sseSecurityInfo));
    }

    /**
     * 删除产品信息
     */
    @PreAuthorize("@ss.hasPermi('sse:securityInfo:remove')")
    @Log(title = "产品信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{pkIds}")
    public AjaxResult remove(@PathVariable Long[] pkIds) {
        return toAjax(sseSecurityInfoService.deleteSseSecurityInfoByPkIds(pkIds));
    }

    /**
     * 初始化产品信息
     */
    @PreAuthorize("@ss.hasPermi('sse:securityInfo:add')")
    @Log(title = "产品信息", businessType = BusinessType.INSERT)
    @PostMapping("/init")
    public AjaxResult init() {
        sseSecurityInfoService.init();
        return AjaxResult.success();
    }

    /**
     * 导入产品信息文件
     */
    @PreAuthorize("@ss.hasPermi('sse:securityInfo:add')")
    @Log(title = "产品信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importFile")
    public AjaxResult importFile(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return AjaxResult.error("上传的文件不能为空");
        }

        // 验证文件扩展名
        String fileName = file.getOriginalFilename();
        if (!fileName.toLowerCase().endsWith(".txt")) {
            return AjaxResult.error("只支持导入.txt 格式的文件");
        }

        // 调用 Service 进行文件导入
        int count = sseSecurityInfoService.importSecurityInfoFromFile(file);

        return AjaxResult.success("导入成功，共导入 " + count + " 条数据");
    }

    /**
     * 通过本地文件路径初始化数据
     */
    @PostMapping("/initData")
    @ResponseBody
    public AjaxResult initData(@RequestBody Map<String, String> params) throws Exception
    {
        try
        {
            String filePath = params.get("filePath");
            
            if (StringUtils.isEmpty(filePath))
            {
                return AjaxResult.error("文件路径不能为空");
            }
            
            if (!filePath.toLowerCase().endsWith(".txt"))
            {
                return AjaxResult.error("仅支持导入 .txt 格式文件");
            }
            
            //int count = sseSecurityInfoService.initData(filePath);
            return AjaxResult.success("初始化成功，共导入 "  + " 条产品数据");
        }

        catch (Exception e)
        {
            return AjaxResult.error("初始化失败：" + e.getMessage());
        }
    }
}
