package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.word.WordExportUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by houwanhua on 2018/1/22.
 */
public class ExcelUtil {

    /********************导出相关(开始)***************************/
    public static void exportExcel(List<?> list,String title,String sheetName,Class<?> pojoClass,String fileName,boolean isCreateHeader, HttpServletResponse response){
        ExportParams exportParams = new ExportParams();
        if(!StringUtils.isBlank(title)){
            exportParams.setTitle(title);
        }
        if(!StringUtils.isBlank(sheetName)){
            exportParams.setSheetName(sheetName);
        }
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, exportParams, response);
    }

    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass,String fileName, HttpServletResponse response){
        ExportParams exportParams = new ExportParams();
        if(!StringUtils.isBlank(title)){
            exportParams.setTitle(title);
        }
        if(!StringUtils.isBlank(sheetName)){
            exportParams.setSheetName(sheetName);
        }
        defaultExport(list, pojoClass, fileName, exportParams,response);
    }

    public static void exportExcel(List<?> list,Class<?> pojoClass,String fileName, HttpServletResponse response){
        defaultExport(list, pojoClass, fileName, new ExportParams(),response);
    }

    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response){
        defaultExport(list, fileName, response);
    }


    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, ExportParams exportParams,HttpServletResponse response) {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams,pojoClass,list);
        if (workbook != null);
        downLoadExcel(fileName, response, workbook);
    }


    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        if (workbook != null);
        downLoadExcel(fileName, response, workbook);
    }



    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /********************导出相关(结束)***************************/

    /********************导入相关(开始)***************************/
    public static <T> List<T> importExcel(String filePath,Integer titleRows,Integer headerRows, Class<T> pojoClass,boolean needVerify) throws Exception {
        if (StringUtils.isBlank(filePath)){
            return null;
        }
        ImportParams params = new ImportParams();
        if(needVerify){
            params.setNeedVerfiy(true);
        }
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        }catch (NoSuchElementException e){
//            e.printStackTrace();
            throw new NoSuchElementException("模板不能为空");
        } catch (Exception e) {
//            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return list;
    }

    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass, boolean needVerify) throws Exception {
        if (file == null){
            return null;
        }
        ImportParams params = new ImportParams();
        if(needVerify){
            params.setNeedVerfiy(needVerify);
        }
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        }catch (NoSuchElementException e){
            e.printStackTrace();
            throw new NoSuchElementException("excel文件不能为空");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return list;
    }

    /********************导入相关(结束)***************************/


    /********************Excel模板导出相关(开始)***********************/
    public static void exportExcelTemplate(String inputFilePath, Map<String, Object> map, boolean scanAllSheet, String outputFilePath, String... sheetName) throws IOException {
        TemplateExportParams params = new TemplateExportParams(inputFilePath,scanAllSheet,sheetName);
        Workbook book = ExcelExportUtil.exportExcel(params, map);
        FileOutputStream outputStream = new FileOutputStream(outputFilePath);
        book.write(outputStream);
        outputStream.close();
    }

    public static void exportExcelTemplate(String inputFilePath, Map<String, Object> map, String outputFilePath, Integer... sheetNum) throws IOException {
        TemplateExportParams params = new TemplateExportParams(inputFilePath,sheetNum);
        Workbook book = ExcelExportUtil.exportExcel(params, map);
        FileOutputStream outputStream = new FileOutputStream(outputFilePath);
        book.write(outputStream);
        outputStream.close();
    }

    public static void exportExcelTemplate(String inputFilePath, Map<String, Object> map, String outputFilePath,String sheetName, Integer... sheetNum) throws IOException {
        TemplateExportParams params = new TemplateExportParams(inputFilePath,sheetName,sheetNum);
        Workbook book = ExcelExportUtil.exportExcel(params, map);
        FileOutputStream outputStream = new FileOutputStream(outputFilePath);
        book.write(outputStream);
        outputStream.close();
    }

    /********************Excel模板导出相关(开始)***********************/

    /********************Word模板导出相关(开始)***********************/
    /**
     * Word 模板导出（07版本的word）
     * @param inputFilePath  模板文件路径
     * @param map map数据
     * @param outputFilePath 文件输入路径
     * @throws Exception
     */
    public static void exportWordTemplate(String inputFilePath,Map<String,Object> map,String outputFilePath) throws Exception {
        XWPFDocument document = WordExportUtil.exportWord07(inputFilePath,map);
        FileOutputStream outputStream = new FileOutputStream(outputFilePath);
        document.write(outputStream);
        outputStream.close();
    }

    /********************Word模板导出相关(开始)***********************/
}
