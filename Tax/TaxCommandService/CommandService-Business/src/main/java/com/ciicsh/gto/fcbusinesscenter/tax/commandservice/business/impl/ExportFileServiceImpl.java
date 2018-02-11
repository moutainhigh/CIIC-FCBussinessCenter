package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.ExportFileService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
     * 扣缴个人所得税报告表初始大小3
     */
    private static final int INITIAL_TAX_SIZE = 0;

    /**
     * 完税凭证处理(徐汇)
     *
     * @param wb
     * @param map
     * @param taskSubProofDetailPOList
     */
    @Override
    public void exportAboutXH(HSSFWorkbook wb, Map<String, String> map, List<TaskSubProofDetailPO> taskSubProofDetailPOList) {
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
        cellA3.setCellValue(cellA3.getStringCellValue() + map.get("unitNumber"));
        //单位名称A4
        HSSFRow rowA4 = sheet.getRow(3);
        if (null == rowA4) {
            rowA4 = sheet.createRow(3);
        }
        HSSFCell cellA4 = rowA4.getCell(0);
        if (null == cellA4) {
            cellA4 = rowA4.createCell(0);
        }
        cellA4.setCellValue(cellA4.getStringCellValue() + map.get("unitName"));

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
     *
     * @param wb
     * @param map
     * @param taskSubProofDetailPOList
     */
    @Override
    public void exportAboutSFJ(HSSFWorkbook wb, Map<String, String> map, List<TaskSubProofDetailPO> taskSubProofDetailPOList) {
        // 读取了模板内所有sheet内容
        HSSFSheet sheet = wb.getSheetAt(0);
        //第2行
        HSSFRow row3 = sheet.getRow(2);
        if (null == row3) {
            row3 = sheet.createRow(2);
        }
        //填表日期相关信息-A3
        HSSFCell cellA3 = row3.getCell(0);
        if (null == cellA3) {
            cellA3 = row3.createCell(1);
        }
        //获取原文本框的值
        String oldDateStr = cellA3.getStringCellValue();
        //年
        int year = LocalDate.now().getYear();
        //月
        int month = LocalDate.now().getMonthValue();
        //日
        int day = LocalDate.now().getDayOfMonth();
        //匹配空白字符,包括空格、制表符、换页符
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
        cellB5.setCellValue(map.get("withholdingAgent"));
        //扣缴义务人代码(税务电脑编码)I5
        HSSFCell cellI5 = row5.getCell(8);
        if (null == cellI5) {
            cellI5 = row5.createCell(8);
        }
        cellI5.setCellValue(map.get("withholdingAgentCode"));
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
        cellB6.setCellValue(map.get("withholdingAgentPhone"));
        //换开人姓名E6
        HSSFCell cellE6 = row6.getCell(4);
        if (null == cellE6) {
            cellE6 = row6.createCell(4);
        }
        cellE6.setCellValue(map.get("changePersonName"));
        //换开人身份证号码H6
        HSSFCell cellH6 = row6.getCell(7);
        if (null == cellH6) {
            cellH6 = row6.createCell(7);
        }
        cellH6.setCellValue(map.get("changePersonIdNo"));
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
     *
     * @param wb
     * @param map
     * @param taskSubProofDetailPOList
     */
    @Override
    public void exportAboutPD(HSSFWorkbook wb, Map<String, String> map, List<TaskSubProofDetailPO> taskSubProofDetailPOList) {
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
        cellC4.setCellValue(map.get("withholdingUnit"));
        //电脑编码-E4
        HSSFCell cellE4 = row4.getCell(4);
        if (null == cellE4) {
            cellE4 = row4.createCell(4);
        }
        cellE4.setCellValue(map.get("withholdingCode"));
        //通用缴款书流水号-I4
        HSSFCell cellI4 = row4.getCell(8);
        if (null == cellI4) {
            cellI4 = row4.createCell(8);
        }
        cellI4.setCellValue(map.get("generalPaymentBook"));
        //办税人员-C5
        HSSFRow row5 = sheet.getRow(4);
        if (null == row5) {
            row5 = sheet.createRow(4);
        }
        HSSFCell cellC5 = row5.getCell(2);
        if (null == cellC5) {
            cellC5 = row5.createCell(2);
        }
        cellC5.setCellValue(map.get("taxationPersonnel"));
        //联系电话-E5
        HSSFCell cellE5 = row5.getCell(4);
        if (null == cellE5) {
            cellE5 = row5.createCell(4);
        }
        cellE5.setCellValue(map.get("phone"));
        //换开份数-G5
        HSSFCell cellG5 = row5.getCell(6);
        if (null == cellG5) {
            cellG5 = row5.createCell(6);
        }
        cellG5.setCellValue(map.get("changeNum"));
        //换开原因-I5
        HSSFCell cellI5 = row5.getCell(8);
        if (null == cellI5) {
            cellI5 = row5.createCell(8);
        }
        cellI5.setCellValue(map.get("changeReason"));
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
     * 申报导出报税数据(扣缴个人所得税报告表)
     *
     * @param wb
     * @param taskSubDeclareDetailPOList
     */
    @Override
    public void exportAboutTax(HSSFWorkbook wb, Map<String, String> map, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList) {
        // 读取了模板内所有sheet内容
        HSSFSheet sheet = wb.getSheetAt(0);
        //第2行
        HSSFRow row2 = sheet.getRow(1);
        if (null == row2) {
            row2 = sheet.createRow(1);
        }
        //税款所属期-D2
        HSSFCell cellD2 = row2.getCell(3);
        if (null == cellD2) {
            cellD2 = row2.createCell(3);
        }
        cellD2.setCellValue(map.get("taxPeriod"));
        //第3行
        HSSFRow row3 = sheet.getRow(2);
        if (null == row3) {
            row3 = sheet.createRow(2);
        }
        //扣缴义务人名称-D3
        HSSFCell cellD3 = row3.getCell(3);
        if (null == cellD3) {
            cellD3 = row3.createCell(3);
        }
        cellD3.setCellValue(map.get("withholdingAgent"));
        //第4行
        HSSFRow row4 = sheet.getRow(3);
        if (null == row4) {
            row4 = sheet.createRow(3);
        }
        //扣缴义务人编码-D4
        HSSFCell cellD4 = row4.getCell(3);
        if (null == cellD4) {
            cellD4 = row4.createCell(3);
        }
        cellD4.setCellValue(map.get("withholdingAgentCode"));
        //插入的行数
        int rows = 0;
        //如果结果集大于模板列表初始大小
        if (taskSubDeclareDetailPOList.size() > INITIAL_TAX_SIZE) {
            rows = taskSubDeclareDetailPOList.size() - INITIAL_TAX_SIZE;
            //插入行数据
            insertRow(sheet, 7, rows);
        }
        //在相应的单元格进行赋值
        int rowIndex = 6;
        //序号
        int num = 1;
        //合计收入额
        BigDecimal incomeTotal = new BigDecimal(0);
        //合计免税所得
        BigDecimal incomeDutyfreeTotal = new BigDecimal(0);
        //合计基本养老保险费
        BigDecimal deductRetirementInsuranceTotal = new BigDecimal(0);
        //合计基本医疗保险费
        BigDecimal deductMedicalInsuranceTotal = new BigDecimal(0);
        //合计失业保险费
        BigDecimal deductDlenessInsurance = new BigDecimal(0);
        //合计住房公积金
        BigDecimal deductHouseFundTotal = new BigDecimal(0);
        //合计财产原值
        BigDecimal deductPropertyTotal = new BigDecimal(0);
        //合计允许扣除的税费
        BigDecimal deductTakeoffTotal = new BigDecimal(0);
        //合计其他
        BigDecimal deductOtherTotal = new BigDecimal(0);
        //合计合计
        BigDecimal deductTotal = new BigDecimal(0);
        //合计减除费用deduction
        BigDecimal deductionTotal = new BigDecimal(0);
        //合计准予扣除的捐赠额
        BigDecimal donationTotal = new BigDecimal(0);
        //合计应纳税所得额
        BigDecimal incomeForTaxTotal = new BigDecimal(0);
        //合计应纳税额
        BigDecimal taxAmountTotal = new BigDecimal(0);
        //合计减免税额
        BigDecimal taxDeductionTotal = new BigDecimal(0);
        //合计应扣缴税额
        BigDecimal taxWithholdAmountTotal = new BigDecimal(0);
        //合计已扣缴税额
        BigDecimal taxWithholdedAmountTotal = new BigDecimal(0);
        //合计应补（退）税额
        BigDecimal taxRemedyOrReturnTotal = new BigDecimal(0);
        for (TaskSubDeclareDetailPO taskSubDeclareDetailPO : taskSubDeclareDetailPOList) {
            HSSFRow row = sheet.getRow(rowIndex);
            if (null == row) {
                row = sheet.createRow(rowIndex);
            }
            //序号-A列
            HSSFCell cellA = row.getCell(0);
            if (null == cellA) {
                cellA = row.createCell(0);
            }
            cellA.setCellValue(num);
            //姓名-B列
            HSSFCell cellB = row.getCell(1);
            if (null == cellB) {
                cellB = row.createCell(1);
            }
            cellB.setCellValue(taskSubDeclareDetailPO.getEmployeeName());
            //证件类型-C列
            HSSFCell cellC = row.getCell(2);
            if (null == cellC) {
                cellC = row.createCell(2);
            }
            cellC.setCellValue(EnumUtil.getMessage(EnumUtil.IT_TYPE, taskSubDeclareDetailPO.getIdType()));
            //证件号-D列
            HSSFCell cellD = row.getCell(3);
            if (null == cellD) {
                cellD = row.createCell(3);
            }
            cellD.setCellValue(taskSubDeclareDetailPO.getIdNo());
            //所属项目-E列
            HSSFCell cellE = row.getCell(4);
            if (null == cellE) {
                cellE = row.createCell(4);
            }
            cellE.setCellValue(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT, taskSubDeclareDetailPO.getIncomeSubject()));
            //所得期间-F列
            HSSFCell cellF = row.getCell(5);
            if (null == cellF) {
                cellF = row.createCell(5);
            }
            cellF.setCellValue(taskSubDeclareDetailPO.getPeriod());
            //收入额-G列
            HSSFCell cellG = row.getCell(6);
            if (null == cellG) {
                cellG = row.createCell(6);
            }
            cellG.setCellValue(taskSubDeclareDetailPO.getIncomeTotal().toString());
            //免税所得-H列
            HSSFCell cellH = row.getCell(7);
            if (null == cellH) {
                cellH = row.createCell(7);
            }
            cellH.setCellValue(taskSubDeclareDetailPO.getIncomeDutyfree().toString());
            //基本养老保险费-I列
            HSSFCell cellI = row.getCell(8);
            if (null == cellI) {
                cellI = row.createCell(8);
            }
            cellI.setCellValue(taskSubDeclareDetailPO.getDeductRetirementInsurance().toString());
            //基本医疗保险费-J列
            HSSFCell cellJ = row.getCell(9);
            if (null == cellJ) {
                cellJ = row.createCell(9);
            }
            cellJ.setCellValue(taskSubDeclareDetailPO.getDeductMedicalInsurance().toString());
            //失业保险费-K列
            HSSFCell cellK = row.getCell(10);
            if (null == cellK) {
                cellK = row.createCell(10);
            }
            cellK.setCellValue(taskSubDeclareDetailPO.getDeductDlenessInsurance().toString());
            //住房公积金-L列
            HSSFCell cellL = row.getCell(11);
            if (null == cellL) {
                cellL = row.createCell(11);
            }
            cellL.setCellValue(taskSubDeclareDetailPO.getDeductHouseFund().toString());
            //财产原值-M列
            HSSFCell cellM = row.getCell(12);
            if (null == cellM) {
                cellM = row.createCell(12);
            }
            cellM.setCellValue(taskSubDeclareDetailPO.getDeductProperty().toString());
            //允许扣除的税费-N列
            HSSFCell cellN = row.getCell(13);
            if (null == cellN) {
                cellN = row.createCell(13);
            }
            cellN.setCellValue(taskSubDeclareDetailPO.getDeductTakeoff().toString());
            //其他-O列
            HSSFCell cellO = row.getCell(14);
            if (null == cellO) {
                cellO = row.createCell(14);
            }
            cellO.setCellValue(taskSubDeclareDetailPO.getDeductOther().toString());
            //合计-P列
            HSSFCell cellP = row.getCell(15);
            if (null == cellP) {
                cellP = row.createCell(15);
            }
            cellP.setCellValue(taskSubDeclareDetailPO.getDeductTotal().toString());
            //减除费用-Q列
            HSSFCell cellQ = row.getCell(16);
            if (null == cellQ) {
                cellQ = row.createCell(16);
            }
            cellQ.setCellValue(taskSubDeclareDetailPO.getDeduction().toString());
            //准予扣除的捐赠额-R列
            HSSFCell cellR = row.getCell(17);
            if (null == cellR) {
                cellR = row.createCell(17);
            }
            cellR.setCellValue(taskSubDeclareDetailPO.getDonation().toString());
            //应纳税所得额-S列
            HSSFCell cellS = row.getCell(18);
            if (null == cellS) {
                cellS = row.createCell(18);
            }
            cellS.setCellValue(taskSubDeclareDetailPO.getIncomeForTax().toString());
            //税率%-T列
            HSSFCell cellT = row.getCell(19);
            if (null == cellT) {
                cellT = row.createCell(19);
            }
            cellT.setCellValue(taskSubDeclareDetailPO.getTaxRate());
            //速算扣除数-U列quick_cal_deduct
            HSSFCell cellU = row.getCell(20);
            if (null == cellU) {
                cellU = row.createCell(20);
            }
            cellU.setCellValue(taskSubDeclareDetailPO.getQuickCalDeduct());
            //应纳税额-V列
            HSSFCell cellV = row.getCell(21);
            if (null == cellV) {
                cellV = row.createCell(21);
            }
            cellV.setCellValue(taskSubDeclareDetailPO.getTaxAmount().toString());
            //减免税额-W列tax_deduction
            HSSFCell cellW = row.getCell(22);
            if (null == cellW) {
                cellW = row.createCell(22);
            }
            cellW.setCellValue(taskSubDeclareDetailPO.getTaxDeduction().toString());
            //应扣缴税额-X列tax_withhold_amount
            HSSFCell cellX = row.getCell(23);
            if (null == cellX) {
                cellX = row.createCell(23);
            }
            cellX.setCellValue(taskSubDeclareDetailPO.getTaxWithholdAmount().toString());
            //已扣缴税额-Y列
            HSSFCell cellY = row.getCell(24);
            if (null == cellY) {
                cellY = row.createCell(24);
            }
            cellY.setCellValue(taskSubDeclareDetailPO.getTaxWithholdedAmount().toString());
            //应补（退）税额-Z列tax_remedy_or_return
            HSSFCell cellZ = row.getCell(25);
            if (null == cellZ) {
                cellZ = row.createCell(25);
            }
            cellZ.setCellValue(taskSubDeclareDetailPO.getTaxRemedyOrReturn().toString());
            //备注-AA列
            HSSFCell cellAA = row.getCell(26);
            if (null == cellAA) {
                cellAA = row.createCell(26);
            }
            cellAA.setCellValue("test");

            //合计金额计算
            incomeTotal = incomeTotal.add(taskSubDeclareDetailPO.getIncomeTotal());
            incomeDutyfreeTotal = incomeDutyfreeTotal.add(taskSubDeclareDetailPO.getIncomeDutyfree());
            deductRetirementInsuranceTotal = deductRetirementInsuranceTotal.add(taskSubDeclareDetailPO.getDeductRetirementInsurance());
            deductMedicalInsuranceTotal = deductMedicalInsuranceTotal.add(taskSubDeclareDetailPO.getDeductMedicalInsurance());
            deductDlenessInsurance = deductDlenessInsurance.add(taskSubDeclareDetailPO.getDeductDlenessInsurance());
            deductHouseFundTotal = deductHouseFundTotal.add(taskSubDeclareDetailPO.getDeductHouseFund());
            deductPropertyTotal = deductPropertyTotal.add(taskSubDeclareDetailPO.getDeductProperty());
            deductTakeoffTotal = deductTakeoffTotal.add(taskSubDeclareDetailPO.getDeductTakeoff());
            deductOtherTotal = deductOtherTotal.add(taskSubDeclareDetailPO.getDeductOther());
            deductTotal = deductTotal.add(taskSubDeclareDetailPO.getDeductTotal());
            deductionTotal = deductionTotal.add(taskSubDeclareDetailPO.getDeduction());
            donationTotal = donationTotal.add(taskSubDeclareDetailPO.getDonation());
            incomeForTaxTotal = incomeForTaxTotal.add(taskSubDeclareDetailPO.getIncomeForTax());
            taxAmountTotal = taxAmountTotal.add(taskSubDeclareDetailPO.getTaxAmount());
            taxDeductionTotal = taxDeductionTotal.add(taskSubDeclareDetailPO.getTaxDeduction());
            taxWithholdAmountTotal = taxWithholdAmountTotal.add(taskSubDeclareDetailPO.getTaxWithholdAmount());
            taxWithholdedAmountTotal = taxWithholdedAmountTotal.add(taskSubDeclareDetailPO.getTaxWithholdedAmount());
            taxRemedyOrReturnTotal = taxRemedyOrReturnTotal.add(taskSubDeclareDetailPO.getTaxRemedyOrReturn());
            rowIndex++;
            num++;
        }
        //如何列表大小不为空，则将计算的合并部分，写入到模板中
        if (taskSubDeclareDetailPOList.size() > 0) {
            HSSFRow row = sheet.getRow(9 + rows);
            if (null == row) {
                row = sheet.createRow(9 + rows);
            }
            //合计收入额-G列
            HSSFCell cellG = row.getCell(6);
            if (null == cellG) {
                cellG = row.createCell(6);
            }
            cellG.setCellValue(incomeTotal.toString());
            //合计免税所得-H列
            HSSFCell cellH = row.getCell(7);
            if (null == cellH) {
                cellH = row.createCell(7);
            }
            cellH.setCellValue(incomeDutyfreeTotal.toString());
            //合计基本养老保险费-I列
            HSSFCell cellI = row.getCell(8);
            if (null == cellI) {
                cellI = row.createCell(8);
            }
            cellI.setCellValue(deductRetirementInsuranceTotal.toString());
            //合计基本医疗保险费-J列
            HSSFCell cellJ = row.getCell(9);
            if (null == cellJ) {
                cellJ = row.createCell(9);
            }
            cellJ.setCellValue(deductMedicalInsuranceTotal.toString());
            //合计失业保险费-K列
            HSSFCell cellK = row.getCell(10);
            if (null == cellK) {
                cellK = row.createCell(10);
            }
            cellK.setCellValue(deductDlenessInsurance.toString());
            //合计住房公积金-L列
            HSSFCell cellL = row.getCell(11);
            if (null == cellL) {
                cellL = row.createCell(11);
            }
            cellL.setCellValue(deductHouseFundTotal.toString());
            //合计财产原值-M列
            HSSFCell cellM = row.getCell(12);
            if (null == cellM) {
                cellM = row.createCell(12);
            }
            cellM.setCellValue(deductPropertyTotal.toString());
            //合计允许扣除的税费-N列
            HSSFCell cellN = row.getCell(13);
            if (null == cellN) {
                cellN = row.createCell(13);
            }
            cellN.setCellValue(deductTakeoffTotal.toString());
            //合计其他-O列
            HSSFCell cellO = row.getCell(14);
            if (null == cellO) {
                cellO = row.createCell(14);
            }
            cellO.setCellValue(deductOtherTotal.toString());
            //合计合计-P列
            HSSFCell cellP = row.getCell(15);
            if (null == cellP) {
                cellP = row.createCell(15);
            }
            cellP.setCellValue(deductTotal.toString());
            //合计减除费用-Q列
            HSSFCell cellQ = row.getCell(16);
            if (null == cellQ) {
                cellQ = row.createCell(16);
            }
            cellQ.setCellValue(deductionTotal.toString());
            //合计准予扣除的捐赠额-R列
            HSSFCell cellR = row.getCell(17);
            if (null == cellR) {
                cellR = row.createCell(17);
            }
            cellR.setCellValue(donationTotal.toString());
            //合计应纳税所得额-S列
            HSSFCell cellS = row.getCell(18);
            if (null == cellS) {
                cellS = row.createCell(18);
            }
            cellS.setCellValue(incomeForTaxTotal.toString());
            //合计应纳税额-V列
            HSSFCell cellV = row.getCell(21);
            if (null == cellV) {
                cellV = row.createCell(21);
            }
            cellV.setCellValue(taxAmountTotal.toString());
            //合计减免税额-W列
            HSSFCell cellW = row.getCell(22);
            if (null == cellW) {
                cellW = row.createCell(22);
            }
            cellW.setCellValue(taxDeductionTotal.toString());
            //合计应扣缴税额-X列
            HSSFCell cellX = row.getCell(23);
            if (null == cellX) {
                cellX = row.createCell(23);
            }
            cellX.setCellValue(taxWithholdAmountTotal.toString());
            //合计已扣缴税额-Y列
            HSSFCell cellY = row.getCell(24);
            if (null == cellY) {
                cellY = row.createCell(24);
            }
            cellY.setCellValue(taxWithholdedAmountTotal.toString());
            //合计应补（退）税额-Z列
            HSSFCell cellZ = row.getCell(25);
            if (null == cellZ) {
                cellZ = row.createCell(25);
            }
            cellZ.setCellValue(taxRemedyOrReturnTotal.toString());
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
