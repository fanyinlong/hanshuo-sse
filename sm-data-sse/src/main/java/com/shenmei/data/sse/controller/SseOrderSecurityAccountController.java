package com.shenmei.data.sse.controller;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.shenmei.data.sse.domain.SseOrderSecurityAccount;
import com.shenmei.data.sse.service.ISseOrderSecurityAccountService;

/**
 * 加持仓统计Controller
 *
 * @author Song
 * @date 2025-08-13
 */
@RestController
@RequestMapping("/sse/securityAndAccount")
public class SseOrderSecurityAccountController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(SseOrderSecurityAccountController.class);

    @Autowired
    private ISseOrderSecurityAccountService sseOrderSecurityAccountService;

    /**
     * 查询加持仓统计列表
     */
    @PreAuthorize("@ss.hasPermi('sse:securityAndAccount:list')")
    @GetMapping("/list")
    public TableDataInfo list(SseOrderSecurityAccount sseOrderSecurityAccount)
    {
        startPage();
        List<SseOrderSecurityAccount> list = sseOrderSecurityAccountService.selectSseOrderSecurityAccountList(sseOrderSecurityAccount);
        return getDataTable(list);
    }

    /**
     * 导出加持仓统计列表
     */
    @PreAuthorize("@ss.hasPermi('sse:securityAndAccount:export')")
    @Log(title = "加持仓统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SseOrderSecurityAccount sseOrderSecurityAccount)
    {
        List<SseOrderSecurityAccount> list = sseOrderSecurityAccountService.selectSseOrderSecurityAccountList(sseOrderSecurityAccount);
        ExcelUtil<SseOrderSecurityAccount> util = new ExcelUtil<SseOrderSecurityAccount>(SseOrderSecurityAccount.class);
        util.exportExcel(response, list, "加持仓统计数据");
    }

    /**
     * 获取加持仓统计详细信息
     */
    @PreAuthorize("@ss.hasPermi('sse:securityAndAccount:query')")
    @GetMapping(value = "/{pkId}")
    public AjaxResult getInfo(@PathVariable("pkId") Long pkId)
    {
        return success(sseOrderSecurityAccountService.selectSseOrderSecurityAccountByPkId(pkId));
    }

    /**
     * 新增加持仓统计
     */
    @PreAuthorize("@ss.hasPermi('sse:securityAndAccount:add')")
    @Log(title = "加持仓统计", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SseOrderSecurityAccount sseOrderSecurityAccount)
    {
        return toAjax(sseOrderSecurityAccountService.insertSseOrderSecurityAccount(sseOrderSecurityAccount));
    }

    /**
     * 修改加持仓统计
     */
    @PreAuthorize("@ss.hasPermi('sse:securityAndAccount:edit')")
    @Log(title = "加持仓统计", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SseOrderSecurityAccount sseOrderSecurityAccount)
    {
        return toAjax(sseOrderSecurityAccountService.updateSseOrderSecurityAccount(sseOrderSecurityAccount));
    }

    /**
     * 删除加持仓统计
     */
    @PreAuthorize("@ss.hasPermi('sse:securityAndAccount:remove')")
    @Log(title = "加持仓统计", businessType = BusinessType.DELETE)
    @DeleteMapping("/{pkIds}")
    public AjaxResult remove(@PathVariable Long[] pkIds)
    {
        return toAjax(sseOrderSecurityAccountService.deleteSseOrderSecurityAccountByPkIds(pkIds));
    }

    /**
     * 导出为TXT文件（无表头，|分隔）
     */
    @PreAuthorize("@ss.hasPermi('sse:securityAndAccount:export')")
    @Log(title = "加持仓统计", businessType = BusinessType.EXPORT)
    @PostMapping("/exportTxt")
    public void exportTxt(HttpServletResponse response, SseOrderSecurityAccount sseOrderSecurityAccount) {
        try {
            // 1. 查询符合条件的数据列表（复用Excel导出的查询逻辑）
            List<SseOrderSecurityAccount> list = sseOrderSecurityAccountService.selectSseOrderSecurityAccountList(sseOrderSecurityAccount);

            // 2. 设置响应头信息
            String fileName = "加持仓统计数据.txt";
            // 处理中文文件名编码
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
            response.setContentType("text/plain;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + encodedFileName + "\"");
            response.setHeader("Cache-Control", "no-store"); // 禁止缓存

            // 3. 写入TXT内容（无表头，|分隔）
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8))) {

                for (SseOrderSecurityAccount entity : list) {
                    // 拼接字段：按需求调整字段顺序和字段名
                    String line = String.join("|",
                            // 处理null值，避免出现"null"字符串
                            entity.getSecurityId() == null ? "" : entity.getSecurityId(),
                            entity.getAccountId() == null ? "" : entity.getAccountId(),
                            entity.getField1() == null ? "" : entity.getField1(),
                            entity.getField2() == null ? "" : entity.getField2()
                            // 可根据需要添加其他字段
                    );
                    writer.write(line);
                    writer.newLine(); // 换行
                }
                writer.flush(); // 确保所有数据写入响应流
                log.info("TXT文件导出成功，记录数：{}", list.size());
            }
        } catch (Exception e) {
            log.error("TXT文件导出失败", e);
            try {
                // 向前端返回错误信息
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write("导出失败：" + e.getMessage());
            } catch (Exception ex) {
                log.error("响应错误信息失败", ex);
            }
        }
    }
}
