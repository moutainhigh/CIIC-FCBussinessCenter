package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.ExportFileService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author wuhua
 */
@Service
public class ExportFileServiceImpl implements ExportFileService {

    /**
     * 完税凭证徐汇模板列表初始大小20
     */
    private static final int INITIAL_XH_SIZE = 0;

    /**
     * 完税凭证三分局模板列表初始大小19
     */
    private static final int INITIAL_SFJ_SIZE = 0;

    /**
     * 完税凭证浦东模板列表初始大小18*2
     */
    private static final int INITIAL_PD_SIZE = 0;

    /**
     * 完税凭证处理(徐汇)
     * @param wb
     * @param taskSubProofDetailPOList
     */
    @Override
    public void exportAboutXH(HSSFWorkbook wb,List<TaskSubProofDetailPO> taskSubProofDetailPOList) {
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

        //如果结果集大于模板列表初始大小
        if (taskSubProofDetailPOList.size() > INITIAL_XH_SIZE) {
            int rows = taskSubProofDetailPOList.size() - INITIAL_XH_SIZE;
            //插入行数据
            insertRow(sheet, 7, rows);
        }

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
    }

    /**
     * 完税凭证模板处理(三分局)
     * @param wb
     * @param taskSubProofDetailPOList
     */
    @Override
    public void exportAboutSFJ(HSSFWorkbook wb, List<TaskSubProofDetailPO> taskSubProofDetailPOList) {
        // 读取了模板内所有sheet内容
        HSSFSheet sheet = wb.getSheetAt(0);
        //第3行
        HSSFRow row3 = sheet.getRow(2);
        if (null == row3) {
            row3 = sheet.createRow(2);
        }
        //填表日期相关信息-A3
        HSSFCell cellA3 = row3.getCell(0);
        String dateStr = "";
        if (null == cellA3) {
            cellA3 = row3.createCell(1);
        }
        String oldDateStr = cellA3.getStringCellValue();
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        int day = LocalDate.now().getDayOfMonth();
        String[] arrs = oldDateStr.split("\\s+");
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < arrs.length; i++) {
            stringBuilder.append(arrs[i]);
            if (i == 0) {
                stringBuilder.append(year);
            } else if (i == 1) {
                stringBuilder.append(month);
            } else if (i == 2) {
                stringBuilder.append(day);
            }
        }
        cellA3.setCellValue(stringBuilder.toString());
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
        //如果结果集大于模板列表初始大小
        if (taskSubProofDetailPOList.size() > INITIAL_SFJ_SIZE) {
            int rows = taskSubProofDetailPOList.size() - INITIAL_SFJ_SIZE;
            //插入行数据
            insertRow(sheet, 17, rows);
        }
        // 在相应的单元格进行赋值
        int rowIndex = 12;
        for (TaskSubProofDetailPO taskSubProofDetailPO : taskSubProofDetailPOList) {
            HSSFRow row = sheet.getRow(rowIndex);
            if (null == row) {
                row = sheet.createRow(rowIndex);
            }
            //B列-税种
            HSSFCell cellB = row.getCell(1);
            if (null == cellB) {
                cellB = row.createCell(1);
            }
            cellB.setCellValue(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT, taskSubProofDetailPO.getIncomeSubject()));

            //D列-姓名
            HSSFCell cellD = row.getCell(3);
            if (null == cellD) {
                cellD = row.createCell(3);
            }
            cellD.setCellValue(taskSubProofDetailPO.getEmployeeName());

            //F列-证件号
            HSSFCell cellF = row.getCell(5);
            if (null == cellF) {
                cellF = row.createCell(5);
            }
            cellF.setCellValue(taskSubProofDetailPO.getIdNo());

            //H列-税款期间
            HSSFCell cellH = row.getCell(7);
            if (null == cellH) {
                cellH = row.createCell(7);
            }
            String period = "";
            String incomeStart = taskSubProofDetailPO.getIncomeStart() == null ? "" : taskSubProofDetailPO.getIncomeStart().toString();
            String incomeEnd = taskSubProofDetailPO.getIncomeEnd() == null ? "" : taskSubProofDetailPO.getIncomeEnd().toString();
            if (incomeStart.equals(incomeEnd)) {
                period = incomeStart;
            } else {
                period = incomeStart + "~" + incomeEnd;
            }
            cellH.setCellValue(period);
            rowIndex++;
        }
    }

    /**
     * 完税凭证模板处理(浦东)
     * @param wb
     * @param taskSubProofDetailPOList
     */
    @Override
    public void exportAboutPD(HSSFWorkbook wb, List<TaskSubProofDetailPO> taskSubProofDetailPOList) {
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
        //如果结果集大于模板列表初始大小
        if (taskSubProofDetailPOList.size() > INITIAL_PD_SIZE) {
            //由于每行显示2条数据,所以大于36条数据部分需要没2条数据添加插入一行,
            int rows = (taskSubProofDetailPOList.size() - INITIAL_PD_SIZE) / 2;
            //插入行数据
            insertRow(sheet, 7, rows);
        }
        //在相应的单元格进行赋值
        int rowIndex = 6;
        //序号
        int num = 1;
        for (TaskSubProofDetailPO taskSubProofDetailPO : taskSubProofDetailPOList) {
            //如果是基数行
            if (num % 2 == 1) {
                HSSFRow row = sheet.getRow(rowIndex);
                if (null == row) {
                    row = sheet.createRow(rowIndex);
                }
                //序号-B列
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
                cellC.setCellValue(taskSubProofDetailPO.getEmployeeName());
                //证件号-D列
                HSSFCell cellD = row.getCell(3);
                if (null == cellD) {
                    cellD = row.createCell(3);
                }
                cellD.setCellValue(taskSubProofDetailPO.getIdNo());

                //证件类型-E列
                HSSFCell cellE = row.getCell(4);
                if (null == cellE) {
                    cellE = row.createCell(4);
                }
                cellE.setCellValue(EnumUtil.getMessage(EnumUtil.IT_TYPE, taskSubProofDetailPO.getIdType()));

                //所属期-F列
                HSSFCell cellF = row.getCell(5);
                if (null == cellF) {
                    cellF = row.createCell(5);
                }
                String period = "";
                String incomeStart = taskSubProofDetailPO.getIncomeStart() == null ? "" : taskSubProofDetailPO.getIncomeStart().toString();
                String incomeEnd = taskSubProofDetailPO.getIncomeEnd() == null ? "" : taskSubProofDetailPO.getIncomeEnd().toString();
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
                cellH.setCellValue(taskSubProofDetailPO.getEmployeeName());
                //证件号-I列
                HSSFCell cellI = row.getCell(8);
                if (null == cellI) {
                    cellI = row.createCell(8);
                }
                cellI.setCellValue(taskSubProofDetailPO.getIdNo());

                //证件类型-J列
                HSSFCell cellJ = row.getCell(9);
                if (null == cellJ) {
                    cellJ = row.createCell(9);
                }
                cellJ.setCellValue(EnumUtil.getMessage(EnumUtil.IT_TYPE, taskSubProofDetailPO.getIdType()));

                //所属期-K列
                HSSFCell cellK = row.getCell(10);
                if (null == cellK) {
                    cellK = row.createCell(10);
                }
                String period = "";
                String incomeStart = taskSubProofDetailPO.getIncomeStart() == null ? "" : taskSubProofDetailPO.getIncomeStart().toString();
                String incomeEnd = taskSubProofDetailPO.getIncomeEnd() == null ? "" : taskSubProofDetailPO.getIncomeEnd().toString();
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
    }


    /**
     * 插入行
     *
     * @param sheet
     * @param startRow 从第几行插入
     * @param rows     插入行数
     */
    private void insertRow(HSSFSheet sheet, int startRow, int rows) {
        sheet.shiftRows(startRow, sheet.getLastRowNum(), rows, true, false);
        List<CellRangeAddress> originMerged = sheet.getMergedRegions();
        for(CellRangeAddress cellRangeAddress : originMerged) {
            //这里的startRow是插入行的index，表示原合并单元格在这之间的，需要重新合并
            if(cellRangeAddress.getFirstRow() < startRow && cellRangeAddress.getLastRow() > startRow) {
                //你插入了几行就加几
                int firstRow = cellRangeAddress.getFirstRow() + rows;
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(firstRow, (firstRow + (cellRangeAddress
                        .getLastRow() - cellRangeAddress.getFirstRow())), cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                sheet.addMergedRegionUnsafe(newCellRangeAddress);
            }
        }
        for (int i = 0; i < rows; i++) {
            //原始位置
            HSSFRow sourceRow = null;
            //移动后位置
            HSSFRow targetRow = null;
            HSSFCell sourceCell = null;
            HSSFCell targetCell = null;
            sourceRow = sheet.createRow(startRow);
            targetRow = sheet.getRow(startRow + rows);
            sourceRow.setHeight(targetRow.getHeight());
            for (int m = targetRow.getFirstCellNum(); m < targetRow.getPhysicalNumberOfCells(); m++) {
                sourceCell = sourceRow.createCell(m);
                targetCell = targetRow.getCell(m);
                sourceCell.setCellStyle(targetCell.getCellStyle());
            }
            startRow++;
        }
    }
}
