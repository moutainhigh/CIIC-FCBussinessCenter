package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.offline.sz;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.BaseService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.EmployeeInfoBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
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
import java.util.stream.Collectors;

/**
 * 申报信息(深圳网页版)
 *
 * @author yuantongqing on 2018-06-04
 */
@Service
public class ExportAboutDeclarationInformationSz extends BaseService {
    /**
     * 获取申报模板信息(深圳网页版)WB
     *
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     * @param fileName
     * @param type
     * @return
     */
    public HSSFWorkbook getDeclarationInformationWB(List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList, String fileName, String type) {
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //获取POIFSFileSystem对象
            fs = getFSFileSystem(fileName, type);
            //通过POIFSFileSystem对象获取WB对象
            wb = getHSSFWorkbook(fs);
            //根据不同的业务需要处理wb
            this.handleDeclarationInformationWB(wb, taskSubDeclareDetailPOList, employeeInfoBatchPOList);
        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    Map<String, String> tags = new HashMap<>(16);
                    //日志工具类返回
                    LogTaskFactory.getLogger().error(ee, "ExportAboutDeclarationInformationSz.getDeclarationInformationWB", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.OTHER, tags);
                }
            }
            throw e;
        }
        return wb;
    }

    /**
     * 处理申报模板信息(深圳网页版)WB
     *
     * @param wb
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     */
    public void handleDeclarationInformationWB(HSSFWorkbook wb, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList) {
        //申报信息页签
        HSSFSheet declareSheet = wb.getSheetAt(0);
        int declareSheetRowIndex = 3;
        for (TaskSubDeclareDetailPO taskSubDeclareDetailPO : taskSubDeclareDetailPOList) {
            List<EmployeeInfoBatchPO> employeeInfoBatchPOS = employeeInfoBatchPOList.stream().filter(item -> taskSubDeclareDetailPO.getCalculationBatchDetailId().equals(item.getCalBatchDetailId())).collect(Collectors.toList());
            EmployeeInfoBatchPO employeeInfoBatchPO = new EmployeeInfoBatchPO();
            if (employeeInfoBatchPOS.size() > 0) {
                employeeInfoBatchPO = employeeInfoBatchPOS.get(0);
            }
            HSSFRow row = declareSheet.getRow(declareSheetRowIndex);
            if (null == row) {
                row = declareSheet.createRow(declareSheetRowIndex);
            }
            //纳税人名称*-A列
            HSSFCell cellA = row.getCell(0);
            if (null == cellA) {
                cellA = row.createCell(0);
            }
            cellA.setCellValue(taskSubDeclareDetailPO.getEmployeeName());
            //身份证照类型*-B列
            HSSFCell cellB = row.getCell(1);
            if (null == cellB) {
                cellB = row.createCell(1);
            }
            cellB.setCellValue(EnumUtil.getMessage(EnumUtil.ID_TYPE_OFFLINE_SZ, taskSubDeclareDetailPO.getIdType()));
            //身份证照号码*-C列
            HSSFCell cellC = row.getCell(2);
            if (null == cellC) {
                cellC = row.createCell(2);
            }
            cellC.setCellValue(taskSubDeclareDetailPO.getIdNo());
            //国家（地区）*-D列
            HSSFCell cellD = row.getCell(3);
            if (null == cellD) {
                cellD = row.createCell(3);
            }
            cellD.setCellValue(EnumUtil.getMessage(EnumUtil.COUNTRY_OFFLINE_SZ, employeeInfoBatchPO.getNationality()));
            //所得项目*-E列
            HSSFCell cellE = row.getCell(4);
            if (null == cellE) {
                cellE = row.createCell(4);
            }
            cellE.setCellValue(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT_OFFLINE_SZ, taskSubDeclareDetailPO.getIncomeSubject()));
            //所得期间起*-F列
            HSSFCell cellF = row.getCell(5);
            if (null == cellF) {
                cellF = row.createCell(5);
            }
            cellF.setCellValue(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(taskSubDeclareDetailPO.getPeriod()));
            //所得期间止*-G列(不需要)
            //收入额*-H列income_total
            HSSFCell cellH = row.getCell(7);
            if (null == cellH) {
                cellH = row.createCell(7);
            }
            cellH.setCellValue(taskSubDeclareDetailPO.getIncomeTotal() == null ? "" : taskSubDeclareDetailPO.getIncomeTotal().toString());
            //免税所得-I列income_dutyfree
            HSSFCell cellI = row.getCell(8);
            if (null == cellI) {
                cellI = row.createCell(8);
            }
            cellI.setCellValue(taskSubDeclareDetailPO.getIncomeDutyfree() == null ? "" : taskSubDeclareDetailPO.getIncomeDutyfree().toString());
            //基本养老保险费-J列deduct_retirement_insurance
            HSSFCell cellJ = row.getCell(9);
            if (null == cellJ) {
                cellJ = row.createCell(9);
            }
            cellJ.setCellValue(taskSubDeclareDetailPO.getDeductRetirementInsurance() == null ? "" : taskSubDeclareDetailPO.getDeductRetirementInsurance().toString());
            //基本医疗保险费-K列deduct_medical_insurance
            HSSFCell cellK = row.getCell(10);
            if (null == cellK) {
                cellK = row.createCell(10);
            }
            cellK.setCellValue(taskSubDeclareDetailPO.getDeductMedicalInsurance() == null ? "" : taskSubDeclareDetailPO.getDeductMedicalInsurance().toString());
            //失业保险费-L列deduct_dleness_insurance
            HSSFCell cellL = row.getCell(11);
            if (null == cellL) {
                cellL = row.createCell(11);
            }
            cellL.setCellValue(taskSubDeclareDetailPO.getDeductDlenessInsurance() == null ? "" : taskSubDeclareDetailPO.getDeductDlenessInsurance().toString());
            //住房公积金-M列deduct_house_fund
            HSSFCell cellM = row.getCell(12);
            if (null == cellM) {
                cellM = row.createCell(12);
            }
            cellM.setCellValue(taskSubDeclareDetailPO.getDeductHouseFund() == null ? "" : taskSubDeclareDetailPO.getDeductHouseFund().toString());
            //财产原值-N列deduct_property
            HSSFCell cellN = row.getCell(13);
            if (null == cellN) {
                cellN = row.createCell(13);
            }
            cellN.setCellValue(taskSubDeclareDetailPO.getDeductProperty() == null ? "" : taskSubDeclareDetailPO.getDeductProperty().toString());
            //允许扣除的税费-O列deduct_takeoff
            HSSFCell cellO = row.getCell(14);
            if (null == cellO) {
                cellO = row.createCell(14);
            }
            cellO.setCellValue(taskSubDeclareDetailPO.getDeductTakeoff() == null ? "" : taskSubDeclareDetailPO.getDeductTakeoff().toString());
            //年金-P列annuity
            HSSFCell cellP = row.getCell(15);
            if (null == cellP) {
                cellP = row.createCell(15);
            }
            cellP.setCellValue(taskSubDeclareDetailPO.getAnnuity() == null ? "" : taskSubDeclareDetailPO.getAnnuity().toString());
            //商业健康险-Q列business_health_insurance
            HSSFCell cellQ = row.getCell(16);
            if (null == cellQ) {
                cellQ = row.createCell(16);
            }
            cellQ.setCellValue(taskSubDeclareDetailPO.getBusinessHealthInsurance() == null ? "" : taskSubDeclareDetailPO.getBusinessHealthInsurance().toString());
            //投资抵扣-R列(不需要)
            //其他扣除-S列deduct_other
            HSSFCell cellS = row.getCell(18);
            if (null == cellS) {
                cellS = row.createCell(18);
            }
            cellS.setCellValue(taskSubDeclareDetailPO.getOthers() == null ? "" : taskSubDeclareDetailPO.getOthers().toString());
            //合计-T列(不需要)
            //减除费用-U列(不需要)
            //准予扣除的捐赠额-V列donation
            HSSFCell cellV = row.getCell(21);
            if (null == cellV) {
                cellV = row.createCell(21);
            }
            cellV.setCellValue(taskSubDeclareDetailPO.getDonation() == null ? "" : taskSubDeclareDetailPO.getDonation().toString());
            //应纳税所得额*-W列(不需要)
            //税率-X列(不需要)
            //速算扣除数-Y列(不需要)
            //应纳税额-Z列(不需要)
            //减免税额-AA列tax_deduction
            HSSFCell cellAA = row.getCell(26);
            if (null == cellAA) {
                cellAA = row.createCell(26);
            }
            cellAA.setCellValue(taskSubDeclareDetailPO.getTaxDeduction() == null ? "" : taskSubDeclareDetailPO.getTaxDeduction().toString());
            //应扣缴税额-AB列(不需要)
            //已扣缴税额-AC列tax_withholded_amount
            HSSFCell cellAC = row.getCell(28);
            if (null == cellAC) {
                cellAC = row.createCell(28);
            }
            cellAC.setCellValue(taskSubDeclareDetailPO.getTaxWithholdedAmount() == null ? "" : taskSubDeclareDetailPO.getTaxWithholdedAmount().toString());
            //应补（退）税额-AD列(不需要)
            //是否本单位雇员*-AE列is_employee
            HSSFCell cellAE = row.getCell(30);
            if (null == cellAE) {
                cellAE = row.createCell(30);
            }
            cellAE.setCellValue(employeeInfoBatchPO.getEmployee() == null ? "Y": employeeInfoBatchPO.getEmployee()  ? "Y" : "N");
            //导入失败原因-AF列(不需要)
            declareSheetRowIndex++;
        }
        //减免事项报告表（减免事项部分）页签
        HSSFSheet reductionSheet = wb.getSheetAt(1);
        //刷选出减免金额大于0的项
        List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOS = taskSubDeclareDetailPOList.stream().filter(item -> item.getTaxDeduction() != null && item.getTaxDeduction().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        int reductionSheetRowIndex = 3;
        for(TaskSubDeclareDetailPO taskSubDeclareDetailPO : taskSubDeclareDetailPOS){
            HSSFRow row = reductionSheet.getRow(reductionSheetRowIndex);
            if (null == row) {
                row = reductionSheet.createRow(reductionSheetRowIndex);
            }
            //序号-A列(默认有)
            //姓名-B列
            HSSFCell cellB = row.getCell(1);
            if (null == cellB) {
                cellB = row.createCell(1);
            }
            cellB.setCellValue(taskSubDeclareDetailPO.getEmployeeName());
            //身份证件类型-C列
            HSSFCell cellC = row.getCell(2);
            if (null == cellC) {
                cellC = row.createCell(2);
            }
            cellC.setCellValue(EnumUtil.getMessage(EnumUtil.ID_TYPE_OFFLINE_SZ_REDUCTION, taskSubDeclareDetailPO.getIdType()));
            //身份证件号码-D列
            HSSFCell cellD = row.getCell(3);
            if (null == cellD) {
                cellD = row.createCell(3);
            }
            cellD.setCellValue(taskSubDeclareDetailPO.getIdNo());
            //减免事项-E列(如有默认： __SXA031900042__残疾、孤老、烈属减征个人所得税__)
            HSSFCell cellE = row.getCell(4);
            if (null == cellE) {
                cellE = row.createCell(4);
            }
            cellE.setCellValue("__SXA031900042__残疾、孤老、烈属减征个人所得税__");
            //减免性质-F列(默认和减免事项对应的：__0005012710__《中华人民共和国个人所得税法》 中华人民共和国主席令第48号第五条第一项__)
            HSSFCell cellF = row.getCell(5);
            if (null == cellD) {
                cellF = row.createCell(5);
            }
            cellF.setCellValue("__0005012710__《中华人民共和国个人所得税法》 中华人民共和国主席令第48号第五条第一项__");
            //减免税额-G列tax_deduction
            HSSFCell cellG = row.getCell(6);
            if (null == cellG) {
                cellG = row.createCell(6);
            }
            cellG.setCellValue(taskSubDeclareDetailPO.getTaxDeduction() == null ? "" : taskSubDeclareDetailPO.getTaxDeduction().toString());
            reductionSheetRowIndex++;
        }
        //减免事项报告表（税收协定部分）页签---不需要
        //商业健康保险税前扣除表页签
        HSSFSheet bussinessSheet = wb.getSheetAt(3);
        //筛选出商业健康保险费大于0的详细信息business_health_insurance
        List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOSBusiness = taskSubDeclareDetailPOList.stream().filter(item -> item.getBusinessHealthInsurance() != null && item.getBusinessHealthInsurance().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        int businessSheetRowIndex = 3;
        for(TaskSubDeclareDetailPO taskSubDeclareDetailPO : taskSubDeclareDetailPOSBusiness){
            List<EmployeeInfoBatchPO> employeeInfoBatchPOS = employeeInfoBatchPOList.stream().filter(item -> taskSubDeclareDetailPO.getCalculationBatchDetailId().equals(item.getCalBatchDetailId())).collect(Collectors.toList());
            EmployeeInfoBatchPO employeeInfoBatchPO = new EmployeeInfoBatchPO();
            if (employeeInfoBatchPOS.size() > 0) {
                employeeInfoBatchPO = employeeInfoBatchPOS.get(0);
            }
            HSSFRow row = bussinessSheet.getRow(businessSheetRowIndex);
            if (null == row) {
                row = bussinessSheet.createRow(businessSheetRowIndex);
            }
            //序号-A列
            //姓名-B列
            HSSFCell cellB = row.getCell(1);
            if (null == cellB) {
                cellB = row.createCell(1);
            }
            cellB.setCellValue(taskSubDeclareDetailPO.getEmployeeName());
            //身份证件类型-C列
            HSSFCell cellC = row.getCell(2);
            if (null == cellC) {
                cellC = row.createCell(2);
            }
            cellC.setCellValue(EnumUtil.getMessage(EnumUtil.ID_TYPE_OFFLINE_SZ_REDUCTION, taskSubDeclareDetailPO.getIdType()));
            //身份证件号码-D列
            HSSFCell cellD = row.getCell(3);
            if (null == cellD) {
                cellD = row.createCell(3);
            }
            cellD.setCellValue(taskSubDeclareDetailPO.getIdNo());
            //税优识别号-E列
            HSSFCell cellE = row.getCell(4);
            if (null == cellE) {
                cellE = row.createCell(4);
            }
            cellE.setCellValue(employeeInfoBatchPO.getRecognitionCode());
            //年度保费-F列annual_premium
            HSSFCell cellF = row.getCell(5);
            if (null == cellF) {
                cellF = row.createCell(5);
            }
            cellF.setCellValue(employeeInfoBatchPO.getAnnualPremium() == null ? "":employeeInfoBatchPO.getAnnualPremium().toString());
            //月度保费-G列monthly_premium
            HSSFCell cellG = row.getCell(6);
            if (null == cellG) {
                cellG = row.createCell(6);
            }
            cellG.setCellValue(employeeInfoBatchPO.getMonthlyPremium() == null ? "" : employeeInfoBatchPO.getMonthlyPremium().toString());
            //表单生效日期-H列
            //本期扣除金额-I列
            HSSFCell cellI = row.getCell(8);
            if (null == cellI) {
                cellI = row.createCell(8);
            }
            cellI.setCellValue(taskSubDeclareDetailPO.getBusinessHealthInsurance()== null ? "" : taskSubDeclareDetailPO.getBusinessHealthInsurance().toString());
            businessSheetRowIndex++;
        }
    }
}
