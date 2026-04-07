package com.shenmei.data.sse.util;

import com.shenmei.data.common.annotation.Excel;
import com.shenmei.data.common.utils.poi.ExcelUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MultiSheetExcelUtil<T> extends ExcelUtil<T> {
    private Workbook workbook;
    private int sheetIndex = 0;

    public MultiSheetExcelUtil(Class<T> clazz) {
        super(clazz);
    }

    /**
     * 初始化多sheet导出
     */
    public void initMultiSheet() {
        this.workbook = new SXSSFWorkbook();
    }

    /**
     * 添加一个sheet页
     */
    public void addSheet(List<T> list, String sheetName) {
        Sheet sheet = workbook.createSheet(sheetName);
        this.addData(sheet, list);
    }
    private void addData(Sheet sheet, List<T> list) {
        if (list == null || list.isEmpty()) return;

        // 创建表头
        Row headerRow = sheet.createRow(0);
        List<Field> fields = getAnnotatedFields();

        // 写入表头
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            Excel anno = field.getAnnotation(Excel.class);
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(anno.name());
        }

        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyyMMddHHmmss"));

        // 写入数据行
        for (int rowIndex = 0; rowIndex < list.size(); rowIndex++) {
            T item = list.get(rowIndex);
            Row row = sheet.createRow(rowIndex + 1);

            for (int colIndex = 0; colIndex < fields.size(); colIndex++) {
                Field field = fields.get(colIndex);
                try {
                    field.setAccessible(true);
                    Object value = field.get(item);
                    Cell cell = row.createCell(colIndex);
                    setCellValue(cell, value,dateStyle);
                } catch (Exception e) {
                    throw new RuntimeException("导出字段值失败", e);
                }
            }
        }
    }

    /**
     * 获取带Excel注解的字段（按sort排序）
     */
    private List<Field> getAnnotatedFields() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Excel.class))
                .sorted(Comparator.comparingInt(f -> f.getAnnotation(Excel.class).sort()))
                .collect(Collectors.toList());
    }

    /**
     * 设置单元格值
     */
    private void setCellValue(Cell cell, Object value, CellStyle style) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
//             设置日期格式
//            CellStyle style = cell.getSheet().getWorkbook().createCellStyle();
//            CreationHelper createHelper = cell.getSheet().getWorkbook().getCreationHelper();
//            style.setDataFormat(createHelper.createDataFormat().getFormat("yyyyMMddHHmmss"));
            cell.setCellStyle(style);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }
    /**
     * 写入响应流
     */
    public void writeToResponse(HttpServletResponse response, String fileName) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        // 关键修复：对中文文件名进行URL编码（编码为UTF-8，兼容大多数浏览器）
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
        // 设置响应头，注意文件名需放在双引号中，避免空格等特殊字符问题
        response.setHeader("Content-Disposition", "attachment;filename=\"" + encodedFileName + "\"");
//        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        try (OutputStream out = response.getOutputStream()) {
            workbook.write(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (workbook instanceof SXSSFWorkbook) {
                ((SXSSFWorkbook) workbook).dispose();
            }
        }
    }
}