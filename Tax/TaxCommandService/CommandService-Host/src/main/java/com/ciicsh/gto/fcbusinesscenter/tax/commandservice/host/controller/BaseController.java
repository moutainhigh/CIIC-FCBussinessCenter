package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * base控制器
 * </p>
 *
 * @author linhaihai
 * @since 2017-12-12
 */
@RequestMapping("/tax")
public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    public static final String TEMPLATE_FILE_PATH =
            Thread.currentThread().getContextClassLoader()
                    .getResource("template/file/")
                    .getPath().substring(1);

    /**
     * 下载文件
     *
     * @param response
     * @param fileName
     * @param content
     */
    protected void downloadFile(HttpServletResponse response, String fileName, byte[] content) {
        BufferedOutputStream bos = null;
        ServletOutputStream outputStream = null;
        BufferedInputStream bis = null;
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.reset();
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentLength(content.length);
            outputStream = response.getOutputStream();
            InputStream is = new ByteArrayInputStream(content);
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(outputStream);

            byte[] buff = new byte[8192];
            int bytesRead;

            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {

                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            logger.error("baseController downloadFile error " + e.toString());
        } finally {
            try {
                bis.close();
            } catch (Exception e) {
                logger.error("close bis error " + e.toString());
            }
            try {
                bos.close();
            } catch (Exception e) {
                logger.error("close bos error " + e.toString());
            }
            try {
                outputStream.flush();
            } catch (Exception e) {
                logger.error("flush outputStream error " + e.toString());
            }
            try {
                outputStream.close();
            } catch (Exception e) {
                logger.error("close outputStream error " + e.toString());
            }
        }

    }

    /**
     * 导出EXCEL2003
     *
     * @param fileName
     * @param taskSubProofDetailPOList
     * @param response
     */
    protected void exportExcel(String fileName, List<TaskSubProofDetailPO> taskSubProofDetailPOList, HttpServletResponse response, String type) {
        if ("XH".equals(type)) {
            exportAboutXH(fileName, taskSubProofDetailPOList, response);
        } else if ("SFJ".equals(type)) {
            exportAboutSFJ(fileName, taskSubProofDetailPOList, response);
        } else if ("PD".equals(type)) {
            exportAboutPD(fileName, taskSubProofDetailPOList, response);
        }
    }

    /**
     * 导出完税凭证(徐汇)
     *
     * @param fileName
     * @param taskSubProofDetailPOList
     * @param response
     */
    protected void exportAboutXH(String fileName, List<TaskSubProofDetailPO> taskSubProofDetailPOList, HttpServletResponse response) {
        HSSFWorkbook wb = null;
        POIFSFileSystem fs = null;
        ByteArrayOutputStream os = null;
        //excel全路径
        try {
            String excel = TEMPLATE_FILE_PATH + fileName;
            File file = new File(excel);
            fs = new POIFSFileSystem(new FileInputStream(file));
            // 读取excel模板
            wb = new HSSFWorkbook(fs);
            // 读取了模板内所有sheet内容
            HSSFSheet sheet = wb.getSheetAt(0);
            //单位税号A3
            HSSFRow rowA3 = sheet.getRow(2);
            if (null == rowA3) {
                rowA3 = sheet.createRow(2);
            }
            HSSFCell cellA3 = rowA3.getCell(0);
            if (null == cellA3) {
                cellA3 = rowA3.createCell(0);
            }
            cellA3.setCellValue(cellA3.getStringCellValue() + "TEST123456");
            //单位名称A4
            HSSFRow rowA4 = sheet.getRow(3);
            if (null == rowA4) {
                rowA4 = sheet.createRow(3);
            }
            HSSFCell cellA4 = rowA4.getCell(0);
            if (null == cellA4) {
                cellA4 = rowA4.createCell(0);
            }
            cellA4.setCellValue(cellA4.getStringCellValue() + "上海中智");
            // 在相应的单元格进行赋值
            int rowIndex = 6;
            for (TaskSubProofDetailPO taskSubProofDetailPO : taskSubProofDetailPOList) {
                HSSFRow row = sheet.getRow(rowIndex);
                if (null == row) {
                    row = sheet.createRow(rowIndex);
                }
                //A列-证件类型
                HSSFCell cellA = row.getCell(0);
                if (null == cellA) {
                    cellA = row.createCell(0);
                }
                cellA.setCellValue(EnumUtil.getMessage(EnumUtil.IT_TYPE, taskSubProofDetailPO.getIdType()));

                //B列-证件号
                HSSFCell cellB = row.getCell(1);
                if (null == cellB) {
                    cellB = row.createCell(1);
                }
                cellB.setCellValue(taskSubProofDetailPO.getIdNo());

                //C列-姓名
                HSSFCell cellC = row.getCell(2);
                if (null == cellC) {
                    cellC = row.createCell(2);
                }
                cellC.setCellValue(taskSubProofDetailPO.getEmployeeName());

                //D列-税款期间
                HSSFCell cellD = row.getCell(3);
                if (null == cellD) {
                    cellD = row.createCell(3);
                }
                String period = "";
                String incomeStart = taskSubProofDetailPO.getIncomeStart() == null ? "" : taskSubProofDetailPO.getIncomeStart().toString();
                String incomeEnd = taskSubProofDetailPO.getIncomeEnd() == null ? "" : taskSubProofDetailPO.getIncomeEnd().toString();
                if (incomeStart.equals(incomeEnd)) {
                    period = incomeStart;
                } else {
                    period = incomeStart + "~" + incomeEnd;
                }
                cellD.setCellValue(period);
                rowIndex++;
            }
            os = new ByteArrayOutputStream();
            wb.write(os);
            exportNewExcel(response, fileName, os);
        } catch (Exception e) {
            logger.error("exportAboutXH error " + e.toString());
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception e) {
                    logger.error("exportAboutXH wb close error" + e.toString());
                }
            }
            if (fs != null) {
                try {
                    fs.close();
                } catch (Exception e) {
                    logger.error("exportAboutXH fs close error" + e.toString());
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    logger.error("exportAboutXH os close error" + e.toString());
                }
            }
        }
    }

    /**
     * 导出完税凭证(三分局)
     *
     * @param fileName
     * @param taskSubProofDetailPOList
     * @param response
     */
    protected void exportAboutSFJ(String fileName, List<TaskSubProofDetailPO> taskSubProofDetailPOList, HttpServletResponse response) {
        HSSFWorkbook wb = null;
        POIFSFileSystem fs = null;
        ByteArrayOutputStream os = null;
        //excel全路径
        try {
            String excel = TEMPLATE_FILE_PATH + fileName;
            File file = new File(excel);
            fs = new POIFSFileSystem(new FileInputStream(file));
            // 读取excel模板
            wb = new HSSFWorkbook(fs);
            // 读取了模板内所有sheet内容
            HSSFSheet sheet = wb.getSheetAt(0);
            //第5行
            HSSFRow row5 = sheet.getRow(4);
            if (null == row5) {
                row5 = sheet.createRow(4);
            }
            //扣缴义务人名称B5
            HSSFCell cellB5 = row5.getCell(1);
            if (null == cellB5) {
                cellB5 = row5.createCell(1);
            }
            cellB5.setCellValue("上海中智");
            //扣缴义务人代码(税务电脑编码)I5
            HSSFCell cellI5 = row5.getCell(8);
            if (null == cellI5) {
                cellI5 = row5.createCell(8);
            }
            cellI5.setCellValue("BM123456789");
            //第6行
            HSSFRow row6 = sheet.getRow(5);
            if (null == row6) {
                row6 = sheet.createRow(5);
            }
            //扣缴义务人电话B6
            HSSFCell cellB6 = row6.getCell(1);
            if (null == cellB6) {
                cellB6 = row6.createCell(1);
            }
            cellB6.setCellValue("18201886666");
            //换开人姓名E6
            HSSFCell cellE6 = row6.getCell(4);
            if (null == cellE6) {
                cellE6 = row6.createCell(4);
            }
            cellE6.setCellValue("admin");
            //换开人身份证号码H6
            HSSFCell cellH6 = row6.getCell(7);
            if (null == cellH6) {
                cellH6 = row6.createCell(7);
            }
            cellH6.setCellValue("321281199001011234");
            // 在相应的单元格进行赋值
            int rowIndex = 12;
            for (int i=0;i<2 ; i++) {
                HSSFRow row = sheet.getRow(rowIndex);
                if (null == row) {
                    row = sheet.createRow(rowIndex);
                }
                //B列-税种
                HSSFCell cellB = row.getCell(1);
                if (null == cellB) {
                    cellB = row.createCell(1);
                }
                cellB.setCellValue(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT, "100"/*taskSubProofDetailPO.getIncomeSubject()*/));

                //D列-姓名
                HSSFCell cellD = row.getCell(3);
                if (null == cellD) {
                    cellD = row.createCell(3);
                }
                cellD.setCellValue("100");

                //F列-证件号
                HSSFCell cellF = row.getCell(5);
                if (null == cellF) {
                    cellF = row.createCell(5);
                }
                cellF.setCellValue("100");

                //H列-税款期间
                HSSFCell cellH = row.getCell(7);
                if (null == cellH) {
                    cellH = row.createCell(7);
                }
                String period = "";
                String incomeStart = "100" == null ? "" : "100".toString();
                String incomeEnd = "100" == null ? "" : "100".toString();
                if (incomeStart.equals(incomeEnd)) {
                    period = incomeStart;
                } else {
                    period = incomeStart + "~" + incomeEnd;
                }
                cellH.setCellValue(period);
                rowIndex++;
            }
            os = new ByteArrayOutputStream();
            wb.write(os);
            exportNewExcel(response, fileName, os);
        } catch (Exception e) {
            logger.error("exportAboutSFJ error " + e.toString());
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception e) {
                    logger.error("exportAboutSFJ wb close error" + e.toString());
                }
            }
            if (fs != null) {
                try {
                    fs.close();
                } catch (Exception e) {
                    logger.error("exportAboutSFJ fs close error" + e.toString());
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    logger.error("exportAboutSFJ os close error" + e.toString());
                }
            }
        }
    }

    /**
     * 导出完税凭证(浦东)
     *
     * @param fileName
     * @param taskSubProofDetailPOList
     * @param response
     */
    protected void exportAboutPD(String fileName, List<TaskSubProofDetailPO> taskSubProofDetailPOList, HttpServletResponse response) {
        HSSFWorkbook wb = null;
        POIFSFileSystem fs = null;
        ByteArrayOutputStream os = null;
        //excel全路径
        try {
            String excel = TEMPLATE_FILE_PATH + fileName;
            File file = new File(excel);
            fs = new POIFSFileSystem(new FileInputStream(file));
            // 读取excel模板
            wb = new HSSFWorkbook(fs);
            // 读取了模板内所有sheet内容
            HSSFSheet sheet = wb.getSheetAt(0);
            //第4行
            HSSFRow row4 = sheet.getRow(3);
            if (null == row4) {
                row4 = sheet.createRow(3);
            }
            //扣缴单位-C4
            HSSFCell cellC4 = row4.getCell(2);
            if (null == cellC4) {
                cellC4 = row4.createCell(2);
            }
            cellC4.setCellValue("上海中智");
            //电脑编码-E4
            HSSFCell cellE4 = row4.getCell(4);
            if (null == cellE4) {
                cellE4 = row4.createCell(4);
            }
            cellE4.setCellValue("123456789");
            //通用缴款书流水号-I4
            HSSFCell cellI4 = row4.getCell(8);
            if (null == cellI4) {
                cellI4 = row4.createCell(8);
            }
            cellI4.setCellValue("147258369");
            //办税人员-C5
            HSSFRow row5 = sheet.getRow(4);
            if (null == row5) {
                row5 = sheet.createRow(4);
            }
            HSSFCell cellC5 = row5.getCell(2);
            if (null == cellC5) {
                cellC5 = row5.createCell(2);
            }
            cellC5.setCellValue("admin");
            //联系电话-E5
            HSSFCell cellE5 = row5.getCell(4);
            if (null == cellE5) {
                cellE5 = row5.createCell(4);
            }
            cellE5.setCellValue("18201886666");
            //换开份数-G5
            HSSFCell cellG5 = row5.getCell(6);
            if (null == cellG5) {
                cellG5 = row5.createCell(6);
            }
            cellG5.setCellValue(2);
            //换开原因-I5
            HSSFCell cellI5 = row5.getCell(8);
            if (null == cellI5) {
                cellI5 = row5.createCell(8);
            }
            cellI5.setCellValue("重新申报");
            //从第7行插入3行数据
            int test =7;
            //for (int j = 1; j < 3; j++) {
                sheet.shiftRows(test, sheet.getLastRowNum(), 5, true, false);

            HSSFRow sourceRow = null;
            //移动后位置
            HSSFRow targetRow = null;
            HSSFCell sourceCell = null;
            HSSFCell targetCell = null;
            sourceRow = sheet.createRow(26);
            targetRow = sheet.getRow(7);
            sourceRow.setHeight(targetRow.getHeight());
            for (int m = targetRow.getFirstCellNum(); m < targetRow.getPhysicalNumberOfCells(); m++) {
                int k = m;
                /*sourceCell = sourceRow.createCell(m);
                targetCell = targetRow.getCell(m);
                sourceCell.setCellStyle(targetCell.getCellStyle());*/
            }

                /*for (int i = 0; i < 1; i++) {
                    //原始位置
                    HSSFRow sourceRow = null;
                    //移动后位置
                    HSSFRow targetRow = null;
                    HSSFCell sourceCell = null;
                    HSSFCell targetCell = null;
                    sourceRow = sheet.createRow(test);
                    targetRow = sheet.getRow(test + 1);
                    sourceRow.setHeight(targetRow.getHeight());
                    for (int m = targetRow.getFirstCellNum(); m < targetRow.getPhysicalNumberOfCells(); m++) {
                        sourceCell = sourceRow.createCell(m);
                        targetCell = targetRow.getCell(m);
                        sourceCell.setCellStyle(targetCell.getCellStyle());
                    }


                    *//*List<CellRangeAddress> originMerged = sheet.getMergedRegions();

                    for(CellRangeAddress cellRangeAddress : originMerged) {
                        //这里的8是插入行的index，表示这行之后才重新合并
                        if(cellRangeAddress.getFirstRow() > test) {
                            //你插入了几行就加几，我这里插入了一行，加1
                            int firstRow = cellRangeAddress.getFirstRow() + 1;
                            CellRangeAddress newCellRangeAddress = new CellRangeAddress(firstRow, (firstRow + (cellRangeAddress
                                    .getLastRow() - cellRangeAddress.getFirstRow())), cellRangeAddress.getFirstColumn(),
                                    cellRangeAddress.getLastColumn());
                            sheet.addMergedRegion(newCellRangeAddress);
                        }
                    }*//*
                    test++;
                }*/
            //}
            //在相应的单元格进行赋值
            int rowIndex = 10;
            //序号
            int num = 1;
            for (int i=0;i<0 ; i++) {
                //如果是基数行
                if (num % 2 == 1) {
                    HSSFRow row = sheet.getRow(rowIndex);
                    if (null == row) {
                        row = sheet.createRow(rowIndex);
                    }
                    //姓名-B列
                    HSSFCell cellB = row.getCell(1);
                    if (null == cellB) {
                        cellB = row.createCell(1);
                    }
                    cellB.setCellValue(num);
                    //姓名-C列
                    HSSFCell cellC = row.getCell(2);
                    if (null == cellC) {
                        cellC = row.createCell(2);
                    }
                    cellC.setCellValue("100");
                    //证件号-D列
                    HSSFCell cellD = row.getCell(3);
                    if (null == cellD) {
                        cellD = row.createCell(3);
                    }
                    cellD.setCellValue("100");

                    //证件类型-E列
                    HSSFCell cellE = row.getCell(4);
                    if (null == cellE) {
                        cellE = row.createCell(4);
                    }
                    cellE.setCellValue("100");

                    //所属期-F列
                    HSSFCell cellF = row.getCell(5);
                    if (null == cellF) {
                        cellF = row.createCell(5);
                    }
                    String period = "";
                    String incomeStart = "100" == null ? "" : "100".toString();
                    String incomeEnd = "100" == null ? "" : "100".toString();
                    if (incomeStart.equals(incomeEnd)) {
                        period = incomeStart;
                    } else {
                        period = incomeStart + "~" + incomeEnd;
                    }
                    cellF.setCellValue(period);
                } else {
                    HSSFRow row = sheet.getRow(rowIndex);
                    if (null == row) {
                        row = sheet.createRow(rowIndex);
                    }
                    //序号-G列
                    HSSFCell cellG = row.getCell(6);
                    if (null == cellG) {
                        cellG = row.createCell(6);
                    }
                    cellG.setCellValue(num);
                    //姓名-H列
                    HSSFCell cellH = row.getCell(7);
                    if (null == cellH) {
                        cellH = row.createCell(7);
                    }
                    cellH.setCellValue("100");
                    //证件号-I列
                    HSSFCell cellI = row.getCell(8);
                    if (null == cellI) {
                        cellI = row.createCell(8);
                    }
                    cellI.setCellValue("100");

                    //证件类型-J列
                    HSSFCell cellJ = row.getCell(9);
                    if (null == cellJ) {
                        cellJ = row.createCell(9);
                    }
                    cellJ.setCellValue("100");

                    //所属期-K列
                    HSSFCell cellK = row.getCell(10);
                    if (null == cellK) {
                        cellK = row.createCell(10);
                    }
                    String period = "";
                    String incomeStart = "100" == null ? "" : "100".toString();
                    String incomeEnd = "100" == null ? "" : "100".toString();
                    if (incomeStart.equals(incomeEnd)) {
                        period = incomeStart;
                    } else {
                        period = incomeStart + "~" + incomeEnd;
                    }
                    cellK.setCellValue(period);
                    rowIndex++;
                }
                num++;
            }
            os = new ByteArrayOutputStream();
            wb.write(os);
            exportNewExcel(response, fileName, os);
        } catch (Exception e) {
//            logger.error("exportAboutPD error " + e.toString());
            e.printStackTrace();
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception e) {
                    logger.error("exportAboutPD wb close error" + e.toString());
                }
            }
            if (fs != null) {
                try {
                    fs.close();
                } catch (Exception e) {
                    logger.error("exportAboutPD fs close error" + e.toString());
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    logger.error("exportAboutPD os close error" + e.toString());
                }
            }
        }
    }


    /**
     * 写入新的EXCEL
     *
     * @param response
     * @param fileName
     * @param os
     * @throws IOException
     */
    protected void exportNewExcel(HttpServletResponse response, String fileName, ByteArrayOutputStream os) throws IOException {
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName).getBytes(), "iso-8859-1"));
        ServletOutputStream sout = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(sout);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            logger.error("exportNewExcel error " + e.toString());
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
    }
}

