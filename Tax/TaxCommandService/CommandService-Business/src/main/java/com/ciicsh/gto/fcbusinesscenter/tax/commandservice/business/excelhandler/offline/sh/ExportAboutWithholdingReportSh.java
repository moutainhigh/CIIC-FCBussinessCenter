package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.offline.sh;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.BaseService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchAccountPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclarePO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扣缴个人所得税报告表(上海)
 *
 * @author yuantongqing on 2018-06-04
 */
@Service
public class ExportAboutWithholdingReportSh extends BaseService {

    /**
     * 扣缴个人所得税报告表初始大小3
     */
    private static final int INITIAL_TAX_SIZE = 2;
    /**
     * 获取扣缴个人所得税报告表(上海)WB
     * @param taskSubDeclarePO
     * @param taskSubDeclareDetailPOList
     * @param fileName
     * @param type
     * @return
     */
    public HSSFWorkbook getWithholdingReportWB(TaskSubDeclarePO taskSubDeclarePO, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, CalculationBatchAccountPO calculationBatchAccountPO, String fileName, String type) {
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //获取POIFSFileSystem对象
            fs = getFSFileSystem(fileName, type);
            //通过POIFSFileSystem对象获取WB对象
            wb = getHSSFWorkbook(fs);
            //用于存放模板列表头部
            Map<String, String> map = new HashMap<>(16);
            //税款所属期
            map.put("taxPeriod", DateTimeFormatter.ofPattern("yyyy-MM").format(taskSubDeclarePO.getPeriod()));
            //扣缴义务人名称
            map.put("withholdingAgent", calculationBatchAccountPO.getAccountName());
            //扣缴义务人编码
            map.put("withholdingAgentCode", calculationBatchAccountPO.getAccountNumber());
            //根据不同的业务需要处理wb
            this.handleWithholdingReportWB(wb, map, taskSubDeclareDetailPOList);
        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    Map<String, String> tags = new HashMap<>(16);
                    //日志工具类返回
                    LogTaskFactory.getLogger().error(ee, "ExportAboutWithholdingReport.getWithholdingReportWB", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.OTHER, tags);
                }
            }
            throw e;
        }
        return wb;
    }

    /**
     * 处理扣缴个人所得税报告表WB
     * @param wb
     * @param map
     * @param taskSubDeclareDetailPOList
     */
    public void handleWithholdingReportWB(HSSFWorkbook wb, Map<String, String> map, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList){
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
            super.insertRow(sheet, 8, rows);
        }
        //在相应的单元格进行赋值
        int rowIndex = 7;
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
            cellF.setCellValue(DateTimeFormatter.ofPattern("yyyy-MM").format(taskSubDeclareDetailPO.getPeriod()));
            //收入额-G列
            HSSFCell cellG = row.getCell(6);
            if (null == cellG) {
                cellG = row.createCell(6);
            }
            cellG.setCellValue(taskSubDeclareDetailPO.getIncomeTotal() == null ? "" : taskSubDeclareDetailPO.getIncomeTotal().toString());
            //免税所得-H列
            HSSFCell cellH = row.getCell(7);
            if (null == cellH) {
                cellH = row.createCell(7);
            }
            cellH.setCellValue(taskSubDeclareDetailPO.getIncomeDutyfree() == null ? "" : taskSubDeclareDetailPO.getIncomeDutyfree().toString());
            //基本养老保险费-I列
            HSSFCell cellI = row.getCell(8);
            if (null == cellI) {
                cellI = row.createCell(8);
            }
            cellI.setCellValue(taskSubDeclareDetailPO.getDeductRetirementInsurance() == null ? "" : taskSubDeclareDetailPO.getDeductRetirementInsurance().toString());
            //基本医疗保险费-J列
            HSSFCell cellJ = row.getCell(9);
            if (null == cellJ) {
                cellJ = row.createCell(9);
            }
            cellJ.setCellValue(taskSubDeclareDetailPO.getDeductMedicalInsurance() == null ? "" : taskSubDeclareDetailPO.getDeductMedicalInsurance().toString());
            //失业保险费-K列
            HSSFCell cellK = row.getCell(10);
            if (null == cellK) {
                cellK = row.createCell(10);
            }
            cellK.setCellValue(taskSubDeclareDetailPO.getDeductDlenessInsurance() == null ? "" : taskSubDeclareDetailPO.getDeductDlenessInsurance().toString());
            //住房公积金-L列
            HSSFCell cellL = row.getCell(11);
            if (null == cellL) {
                cellL = row.createCell(11);
            }
            cellL.setCellValue(taskSubDeclareDetailPO.getDeductHouseFund() == null ? "" : taskSubDeclareDetailPO.getDeductHouseFund().toString());
            //财产原值-M列
            HSSFCell cellM = row.getCell(12);
            if (null == cellM) {
                cellM = row.createCell(12);
            }
            cellM.setCellValue(taskSubDeclareDetailPO.getDeductProperty() == null ? "" : taskSubDeclareDetailPO.getDeductProperty().toString());
            //允许扣除的税费-N列
            HSSFCell cellN = row.getCell(13);
            if (null == cellN) {
                cellN = row.createCell(13);
            }
            cellN.setCellValue(taskSubDeclareDetailPO.getDeductTakeoff() == null ? "" : taskSubDeclareDetailPO.getDeductTakeoff().toString());
            //其他-O列
            HSSFCell cellO = row.getCell(14);
            if (null == cellO) {
                cellO = row.createCell(14);
            }
            cellO.setCellValue(taskSubDeclareDetailPO.getDeductOther() == null ? "" : taskSubDeclareDetailPO.getDeductOther().toString());
            //合计-P列
            HSSFCell cellP = row.getCell(15);
            if (null == cellP) {
                cellP = row.createCell(15);
            }
            cellP.setCellValue(taskSubDeclareDetailPO.getDeductTotal() == null ? "" : taskSubDeclareDetailPO.getDeductTotal().toString());
            //减除费用-Q列
            HSSFCell cellQ = row.getCell(16);
            if (null == cellQ) {
                cellQ = row.createCell(16);
            }
            cellQ.setCellValue(taskSubDeclareDetailPO.getDeduction() == null ? "" : taskSubDeclareDetailPO.getDeduction().toString());
            //准予扣除的捐赠额-R列
            HSSFCell cellR = row.getCell(17);
            if (null == cellR) {
                cellR = row.createCell(17);
            }
            cellR.setCellValue(taskSubDeclareDetailPO.getDonation() == null ? "" : taskSubDeclareDetailPO.getDonation().toString());
            //应纳税所得额-S列
            HSSFCell cellS = row.getCell(18);
            if (null == cellS) {
                cellS = row.createCell(18);
            }
            cellS.setCellValue(taskSubDeclareDetailPO.getIncomeForTax() == null ? "" : taskSubDeclareDetailPO.getIncomeForTax().toString());
            //税率%-T列
            HSSFCell cellT = row.getCell(19);
            if (null == cellT) {
                cellT = row.createCell(19);
            }
            cellT.setCellValue(taskSubDeclareDetailPO.getTaxRate() == null ? "" : taskSubDeclareDetailPO.getTaxRate().toString());
            //速算扣除数-U列quick_cal_deduct
            HSSFCell cellU = row.getCell(20);
            if (null == cellU) {
                cellU = row.createCell(20);
            }
            cellU.setCellValue(taskSubDeclareDetailPO.getQuickCalDeduct() == null ? "" : taskSubDeclareDetailPO.getQuickCalDeduct().toString());
            //应纳税额-V列
            HSSFCell cellV = row.getCell(21);
            if (null == cellV) {
                cellV = row.createCell(21);
            }
            cellV.setCellValue(taskSubDeclareDetailPO.getTaxAmount() == null ? "" : taskSubDeclareDetailPO.getTaxAmount().toString());
            //减免税额-W列tax_deduction
            HSSFCell cellW = row.getCell(22);
            if (null == cellW) {
                cellW = row.createCell(22);
            }
            cellW.setCellValue(taskSubDeclareDetailPO.getTaxDeduction() == null ? "" : taskSubDeclareDetailPO.getTaxDeduction().toString());
            //应扣缴税额-X列tax_withhold_amount
            HSSFCell cellX = row.getCell(23);
            if (null == cellX) {
                cellX = row.createCell(23);
            }
            cellX.setCellValue(taskSubDeclareDetailPO.getTaxWithholdAmount() == null ? "" : taskSubDeclareDetailPO.getTaxWithholdAmount().toString());
            //已扣缴税额-Y列
            HSSFCell cellY = row.getCell(24);
            if (null == cellY) {
                cellY = row.createCell(24);
            }
            cellY.setCellValue(taskSubDeclareDetailPO.getTaxWithholdedAmount() == null ? "" : taskSubDeclareDetailPO.getTaxWithholdedAmount().toString());
            //应补（退）税额-Z列tax_remedy_or_return
            HSSFCell cellZ = row.getCell(25);
            if (null == cellZ) {
                cellZ = row.createCell(25);
            }
            cellZ.setCellValue(taskSubDeclareDetailPO.getTaxRemedyOrReturn() == null ? "" : taskSubDeclareDetailPO.getTaxRemedyOrReturn().toString());
            //备注-AA列
//            HSSFCell cellAA = row.getCell(26);
//            if (null == cellAA) {
//                cellAA = row.createCell(26);
//            }
//            cellAA.setCellValue("test");

            //合计金额计算
            incomeTotal = incomeTotal.add(taskSubDeclareDetailPO.getIncomeTotal() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getIncomeTotal());
            incomeDutyfreeTotal = incomeDutyfreeTotal.add(taskSubDeclareDetailPO.getIncomeDutyfree() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getIncomeDutyfree());
            deductRetirementInsuranceTotal = deductRetirementInsuranceTotal.add(taskSubDeclareDetailPO.getDeductRetirementInsurance() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getDeductRetirementInsurance());
            deductMedicalInsuranceTotal = deductMedicalInsuranceTotal.add(taskSubDeclareDetailPO.getDeductMedicalInsurance() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getDeductMedicalInsurance());
            deductDlenessInsurance = deductDlenessInsurance.add(taskSubDeclareDetailPO.getDeductDlenessInsurance() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getDeductDlenessInsurance());
            deductHouseFundTotal = deductHouseFundTotal.add(taskSubDeclareDetailPO.getDeductHouseFund() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getDeductHouseFund());
            deductPropertyTotal = deductPropertyTotal.add(taskSubDeclareDetailPO.getDeductProperty() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getDeductProperty());
            deductTakeoffTotal = deductTakeoffTotal.add(taskSubDeclareDetailPO.getDeductTakeoff() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getDeductTakeoff());
            deductOtherTotal = deductOtherTotal.add(taskSubDeclareDetailPO.getDeductOther() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getDeductOther());
            deductTotal = deductTotal.add(taskSubDeclareDetailPO.getDeductTotal() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getDeductTotal());
            deductionTotal = deductionTotal.add(taskSubDeclareDetailPO.getDeduction() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getDeduction());
            donationTotal = donationTotal.add(taskSubDeclareDetailPO.getDonation() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getDonation());
            incomeForTaxTotal = incomeForTaxTotal.add(taskSubDeclareDetailPO.getIncomeForTax() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getIncomeForTax());
            taxAmountTotal = taxAmountTotal.add(taskSubDeclareDetailPO.getTaxAmount() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getTaxAmount());
            taxDeductionTotal = taxDeductionTotal.add(taskSubDeclareDetailPO.getTaxDeduction() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getTaxDeduction());
            taxWithholdAmountTotal = taxWithholdAmountTotal.add(taskSubDeclareDetailPO.getTaxWithholdAmount() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getTaxWithholdAmount());
            taxWithholdedAmountTotal = taxWithholdedAmountTotal.add(taskSubDeclareDetailPO.getTaxWithholdedAmount() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getTaxWithholdedAmount());
            taxRemedyOrReturnTotal = taxRemedyOrReturnTotal.add(taskSubDeclareDetailPO.getTaxRemedyOrReturn() == null ? new BigDecimal(0) : taskSubDeclareDetailPO.getTaxRemedyOrReturn());
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
}
