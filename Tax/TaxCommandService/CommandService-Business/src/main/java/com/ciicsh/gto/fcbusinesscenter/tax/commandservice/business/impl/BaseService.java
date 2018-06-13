package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BaseService {

    /**
     * 模板文件目录
     */
    public static final String TEMPLATE_FILE_VOUCHER_PATH = "template/file/";
    public static final String TEMPLATE_FILE_GOLDEN_PATH_SH = "template/file/golden/sh/";
    public static final String TEMPLATE_FILE_GOLDEN_PATH_JS = "template/file/golden/js/";
    public static final String TEMPLATE_FILE_GOLDEN_PATH_SZ = "template/file/golden/sz/";
    public static final String TEMPLATE_FILE_GOLDEN_PATH_GD = "template/file/golden/gd/";
//            Thread.currentThread().getContextClassLoader()
//                    .getResource("template/file/")
//                    .getPath().substring(1);

    /**
     * 通过文件名获取POIFSFileSystem对象
     *
     * @param fileName
     * @return
     */
    protected POIFSFileSystem getFSFileSystem(String fileName,String type) {
        POIFSFileSystem fs = null;
        //excel全路径
        if("sh".equals(type)){
            try {
                InputStream stream = getClass().getClassLoader().getResourceAsStream(TEMPLATE_FILE_GOLDEN_PATH_SH + fileName);
                fs = new POIFSFileSystem(stream);
            } catch (Exception e) {
                LogTaskFactory.getLogger().error(e, "BaseController.downloadFile", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.APP, null);
            }
        }else if("js".equals(type)){
            try {
                InputStream stream = getClass().getClassLoader().getResourceAsStream(TEMPLATE_FILE_GOLDEN_PATH_JS + fileName);
                fs = new POIFSFileSystem(stream);
            } catch (Exception e) {
                LogTaskFactory.getLogger().error(e, "BaseController.downloadFile", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.APP, null);
            }
        }else if("sz".equals(type)){
            try {
                InputStream stream = getClass().getClassLoader().getResourceAsStream(TEMPLATE_FILE_GOLDEN_PATH_SZ + fileName);
                fs = new POIFSFileSystem(stream);
            } catch (Exception e) {
                LogTaskFactory.getLogger().error(e, "BaseController.downloadFile", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.APP, null);
            }
        }else if("gd".equals(type)){
            try {
                InputStream stream = getClass().getClassLoader().getResourceAsStream(TEMPLATE_FILE_GOLDEN_PATH_GD + fileName);
                fs = new POIFSFileSystem(stream);
            } catch (Exception e) {
                LogTaskFactory.getLogger().error(e, "BaseController.downloadFile", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.APP, null);
            }
        }else{
            try {
                InputStream stream = getClass().getClassLoader().getResourceAsStream(TEMPLATE_FILE_VOUCHER_PATH + fileName);
                fs = new POIFSFileSystem(stream);
            } catch (Exception e) {
                LogTaskFactory.getLogger().error(e, "BaseController.downloadFile", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.APP, null);
            }
        }
        return fs;
    }

    /**
     * 通过POIFSFileSystem对象获取wb
     *
     * @param fs
     * @return
     */
    protected HSSFWorkbook getHSSFWorkbook(POIFSFileSystem fs) {
        HSSFWorkbook wb = null;
        //excel全路径
        try {
            // 读取excel模板
            wb = new HSSFWorkbook(fs);
        } catch (Exception e) {
            LogTaskFactory.getLogger().error(e, "BaseController.downloadFile", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.APP, null);
        }
        return wb;
    }

    /**
     * 插入行
     *
     * @param sheet
     * @param startRow 从第几行插入
     * @param rows     插入行数
     */
    protected void insertRow(HSSFSheet sheet, int startRow, int rows) {
        sheet.shiftRows(startRow, sheet.getLastRowNum(), rows, true, false);
        //复制行中所有的合并单元格
        List<CellRangeAddress> startMerged = new ArrayList<>();
        //模板中所有的合并单元格
        List<CellRangeAddress> originMerged = sheet.getMergedRegions();
        for (CellRangeAddress cellRangeAddress : originMerged) {
            //这里的startRow是插入行的index，表示原合并单元格在这之间的，需要重新合并
            if (cellRangeAddress.getFirstRow() < startRow && cellRangeAddress.getLastRow() > startRow) {
                //你插入了几行就加几
                int firstRow = cellRangeAddress.getFirstRow() + rows;
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(firstRow, (firstRow + (cellRangeAddress
                        .getLastRow() - cellRangeAddress.getFirstRow())), cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                sheet.addMergedRegionUnsafe(newCellRangeAddress);
            }
            if (cellRangeAddress.getFirstRow() == cellRangeAddress.getLastRow() && cellRangeAddress.getFirstRow() == (startRow - 1)) {
                startMerged.add(cellRangeAddress);
            }
        }

        for (int i = 0; i < rows; i++) {
            //模板样式
            HSSFRow sourceRow = null;
            //新插入行样式
            HSSFRow targetRow = null;
            HSSFCell sourceCell = null;
            HSSFCell targetCell = null;
            sourceRow = sheet.getRow(startRow - 1);
            targetRow = sheet.createRow(startRow);
            targetRow.setHeight(sourceRow.getHeight());
            for (int m = sourceRow.getFirstCellNum(); m < sourceRow.getPhysicalNumberOfCells(); m++) {
                sourceCell = sourceRow.getCell(m);
                targetCell = targetRow.createCell(m);
                targetCell.setCellStyle(sourceCell.getCellStyle());
            }
            //循环新增当前行的合并单元格
            for (CellRangeAddress cellRangeAddress : startMerged) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(startRow, startRow, cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                sheet.addMergedRegionUnsafe(newCellRangeAddress);
            }
            startRow++;
        }

    }

    /**
     *
     * @return
     */
    protected String getResourcePath(){
        String filePath = System.getProperty("java.class.path");
        //得到当前操作系统的分隔符，windows下是";",linux下是":"
        String pathSplit = System.getProperty("path.separator");
        /**
         * 若没有其他依赖，则filePath的结果应当是该可运行jar包的绝对路径，
         * 此时我们只需要经过字符串解析，便可得到jar所在目录
         */
        if(filePath.contains(pathSplit)){
            filePath = filePath.substring(0,filePath.indexOf(pathSplit));
        }else if (filePath.endsWith(".jar")) {//截取路径中的jar包名,可执行jar包运行的结果里包含".jar"
            filePath = filePath.substring(0, filePath.lastIndexOf(File.separator) + 1);
        }
        return filePath;
    }




}
