package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.online.js;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.BaseService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.TaskSubDeclareDetailServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.EmployeeInfoBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclarePO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.IncomeSubject;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 外籍人员正常工资薪金(江苏)
 * @author yuantongqing
 */
@Service
public class ExportAboutForeignNormalSalaryJs extends BaseService{

    @Autowired
    public TaskSubDeclareDetailServiceImpl taskSubDeclareDetailService;

    /**
     * 导出外籍人员正常工资薪金
     *
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     * @param fileName
     * @param type
     * @return
     */
    public HSSFWorkbook getForeignNormalSalaryWB(TaskSubDeclarePO taskSubDeclarePO, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList, String fileName, String type) {
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //获取POIFSFileSystem对象
            fs = getFSFileSystem(fileName, type);
            //通过POIFSFileSystem对象获取WB对象
            wb = getHSSFWorkbook(fs);
            if(taskSubDeclarePO.getCombined()){
                taskSubDeclareDetailService.addMergeSubDeclareDetailList(taskSubDeclarePO,taskSubDeclareDetailPOList,employeeInfoBatchPOList);
            }
            //根据不同的业务需要处理wb
            this.handleForeignNormalSalaryWB(wb, taskSubDeclareDetailPOList, employeeInfoBatchPOList);
        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    Map<String, String> tags = new HashMap<>(16);
                    //日志工具类返回
                    LogTaskFactory.getLogger().error(ee, "ExportAboutForeignNormalSalaryJs.getForeignNormalSalaryWB", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.OTHER, tags);
                }
            }
            throw e;
        }
        return wb;
    }

    /**
     * 导出外籍人员正常工资薪金
     *
     * @param wb
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     */
    public void handleForeignNormalSalaryWB(HSSFWorkbook wb, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList) {
        //筛选出外籍人员正常工资薪金详细信息
        List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOS = taskSubDeclareDetailPOList.stream().filter(item -> IncomeSubject.FOREIGNNORMALSALARY.getCode().equals(item.getIncomeSubject())).collect(Collectors.toList());
        // 读取了模板内所有sheet内容
        HSSFSheet sheet = wb.getSheetAt(0);
        //在相应的单元格进行赋值
        int sheetRowIndex = 1;
        for (TaskSubDeclareDetailPO po : taskSubDeclareDetailPOS) {
            List<EmployeeInfoBatchPO> employeeInfoBatchPOS = new ArrayList<>();
            //判断是否是合并明细
            if(po.getCombined()){
                //获取合并前批次明细ID
                List<TaskSubDeclareDetailPO>  taskSubDeclareDetailPOList1 = taskSubDeclareDetailService.querySubDeclareDetailListBeforeMergeByMergeId(po.getId());
                employeeInfoBatchPOS = taskSubDeclareDetailPOList1.size() > 0 ? employeeInfoBatchPOList.stream().filter(item -> taskSubDeclareDetailPOList1.get(0).getId().equals(item.getCalBatchDetailId())).collect(Collectors.toList()) : null;
            }else{
                employeeInfoBatchPOS = employeeInfoBatchPOList.stream().filter(item -> po.getCalculationBatchDetailId().equals(item.getCalBatchDetailId())).collect(Collectors.toList());
            }
            EmployeeInfoBatchPO employeeInfoBatchPO = new EmployeeInfoBatchPO();
            if (employeeInfoBatchPOS.size() > 0) {
                employeeInfoBatchPO = employeeInfoBatchPOS.get(0);
            }
            HSSFRow row = sheet.getRow(sheetRowIndex);
            if (null == row) {
                row = sheet.createRow(sheetRowIndex);
            }
            //工号-A列
            HSSFCell cellA = row.getCell(0);
            if (null == cellA) {
                cellA = row.createCell(0);
            }
            cellA.setCellValue(employeeInfoBatchPO.getWorkNumber());
            //姓名-B列
            HSSFCell cellB = row.getCell(1);
            if (null == cellB) {
                cellB = row.createCell(1);
            }
            cellB.setCellValue(employeeInfoBatchPO.getTaxName());
            //*证照类型-C列
            HSSFCell cellC = row.getCell(2);
            if (null == cellC) {
                cellC = row.createCell(2);
            }
            cellC.setCellValue(EnumUtil.getMessage(EnumUtil.ID_TYPE_ONLINE_COMPLEX_COMMON, po.getIdType()));
            //*证照号码-D列
            HSSFCell cellD = row.getCell(3);
            if (null == cellD) {
                cellD = row.createCell(3);
            }
            cellD.setCellValue(po.getIdNo());
            //税款负担方式-E列burden
            HSSFCell cellE = row.getCell(4);
            if (null == cellE) {
                cellE = row.createCell(4);
            }
            cellE.setCellValue(EnumUtil.getMessage(EnumUtil.TAX_BURDENS_ONLINE_COMMON, employeeInfoBatchPO.getBurden()));
            //雇主负担税额-F列
            //雇主负担比例-G列
            //*适用公式-H列applicableFormula
            HSSFCell cellH = row.getCell(7);
            if (null == cellH) {
                cellH = row.createCell(7);
            }
            cellH.setCellValue(EnumUtil.getMessage(EnumUtil.APPLICABLE_FORMULA_ONLINE_COMMON, employeeInfoBatchPO.getApplicableFormula()));
            //境内天数-I列domestic_days
            HSSFCell cellI = row.getCell(8);
            if (null == cellI) {
                cellI = row.createCell(8);
            }
            cellI.setCellValue(po.getDomesticDays());
            //境外天数-J列overseas_days
            HSSFCell cellJ = row.getCell(9);
            if (null == cellJ) {
                cellJ = row.createCell(9);
            }
            cellJ.setCellValue(po.getOverseasDays());
            //境内所得境内支付-K列domestic_income_domestic_payment
            HSSFCell cellK = row.getCell(10);
            if (null == cellK) {
                cellK = row.createCell(10);
            }
            cellK.setCellValue(po.getDomesticIncomeDomesticPayment() == null ? "" : po.getDomesticIncomeDomesticPayment().toString());
            //境内所得境外支付-L列domestic_income_overseas_payment
            HSSFCell cellL = row.getCell(11);
            if (null == cellL) {
                cellL = row.createCell(11);
            }
            cellL.setCellValue(po.getDomesticIncomeOverseasPayment() == null ? "" : po.getDomesticIncomeOverseasPayment().toString());
            //境外所得境内支付-M列overseas_income_domestic_payment
            HSSFCell cellM = row.getCell(12);
            if (null == cellM) {
                cellM = row.createCell(12);
            }
            cellM.setCellValue(po.getOverseasIncomeDomesticPayment() == null ? "" : po.getOverseasIncomeDomesticPayment().toString());
            //境外所得境外支付-N列overseas_income_overseas_payment
            HSSFCell cellN = row.getCell(13);
            if (null == cellN) {
                cellN = row.createCell(13);
            }
            cellN.setCellValue(po.getOverseasIncomeOverseasPayment() == null ? "" : po.getOverseasIncomeOverseasPayment().toString());
            //基本养老保险费-O列deduct_retirement_insurance
            HSSFCell cellO = row.getCell(14);
            if (null == cellO) {
                cellO = row.createCell(14);
            }
            cellO.setCellValue(po.getDeductRetirementInsurance() == null ? "" : po.getDeductRetirementInsurance().toString());
            //基本医疗保险费-P列deduct_medical_insurance
            HSSFCell cellP = row.getCell(15);
            if (null == cellP) {
                cellP = row.createCell(15);
            }
            cellP.setCellValue(po.getDeductMedicalInsurance() == null ? "" : po.getDeductMedicalInsurance().toString());
            //失业保险费-Q列deduct_dleness_insurance
            HSSFCell cellQ = row.getCell(16);
            if (null == cellQ) {
                cellQ = row.createCell(16);
            }
            cellQ.setCellValue(po.getDeductDlenessInsurance() == null ? "" : po.getDeductDlenessInsurance().toString());
            //住房公积金-R列deduct_house_fund
            HSSFCell cellR = row.getCell(17);
            if (null == cellR) {
                cellR = row.createCell(17);
            }
            cellR.setCellValue(po.getDeductHouseFund() == null ? "" : po.getDeductHouseFund().toString());
            //允许扣除的税费-S列deduct_takeoff
            HSSFCell cellS = row.getCell(18);
            if (null == cellS) {
                cellS = row.createCell(18);
            }
            cellS.setCellValue(po.getDeductTakeoff() == null ? "" : po.getDeductTakeoff().toString());
            //年金-T列annuity
            HSSFCell cellT = row.getCell(19);
            if (null == cellT) {
                cellT = row.createCell(19);
            }
            cellT.setCellValue(po.getAnnuity() == null ? "" : po.getAnnuity().toString());
            //商业健康保险费-U列
            //税延养老保险费-V列
            //其他>其他扣除-W列deduct_other
            HSSFCell cellW = row.getCell(22);
            if (null == cellW) {
                cellW = row.createCell(22);
            }
            cellW.setCellValue(po.getOthers() == null ? "" : po.getOthers().toString());
            //住房补贴-X列housing_subsidy
            HSSFCell cellX = row.getCell(23);
            if (null == cellX) {
                cellX = row.createCell(23);
            }
            cellX.setCellValue(po.getHousingSubsidy() == null ? "" : po.getHousingSubsidy().toString());
            //伙食补贴-Y列meal_allowance
            HSSFCell cellY = row.getCell(24);
            if (null == cellY) {
                cellY = row.createCell(24);
            }
            cellY.setCellValue(po.getMealAllowance() == null ? "" : po.getMealAllowance().toString());
            //洗衣费-Z列laundry_fee
            HSSFCell cellZ = row.getCell(25);
            if (null == cellZ) {
                cellZ = row.createCell(25);
            }
            cellZ.setCellValue(po.getLaundryFee() == null ? "" : po.getLaundryFee().toString());
            //搬迁费-AA列removing_indemnity_fee
            HSSFCell cellAA = row.getCell(26);
            if (null == cellAA) {
                cellAA = row.createCell(26);
            }
            cellAA.setCellValue(po.getRemovingIndemnityFee() == null ? "" : po.getRemovingIndemnityFee().toString());
            //出差补贴-AB列missionallowance
            HSSFCell cellAB = row.getCell(27);
            if (null == cellAB) {
                cellAB = row.createCell(27);
            }
            cellAB.setCellValue(po.getMissionallowance() == null ? "" : po.getMissionallowance().toString());
            //探亲费-AC列visiting_relatives_fee
            HSSFCell cellAC = row.getCell(28);
            if (null == cellAC) {
                cellAC = row.createCell(28);
            }
            cellAC.setCellValue(po.getVisitingRelativesFee() == null ? "" : po.getVisitingRelativesFee().toString());
            //语言培训费-AD列language_training_fee
            HSSFCell cellAD = row.getCell(29);
            if (null == cellAD) {
                cellAD = row.createCell(29);
            }
            cellAD.setCellValue(po.getLanguageTrainingFee() == null ? "" : po.getLanguageTrainingFee().toString());
            //子女教育经费-AE列education_funds
            HSSFCell cellAE = row.getCell(30);
            if (null == cellAE) {
                cellAE = row.createCell(30);
            }
            cellAE.setCellValue(po.getEducationFunds() == null ? "" : po.getEducationFunds().toString());
            //免税所得>其他费用-AF列
            //实际捐赠额-AG列
            //允许列支的捐赠比例-AH列
            //准予扣除的捐赠额-AI列donation
            HSSFCell cellAI = row.getCell(34);
            if (null == cellAI) {
                cellAI = row.createCell(34);
            }
            cellAI.setCellValue(po.getDonation() == null ? "" : po.getDonation().toString());
            //减免税额-AJ列tax_deduction
            HSSFCell cellAJ = row.getCell(35);
            if (null == cellAJ) {
                cellAJ = row.createCell(35);
            }
            cellAJ.setCellValue(po.getTaxDeduction() == null ? "" : po.getTaxDeduction().toString());
            //备注-AK列
            sheetRowIndex++;
        }
    }
}
