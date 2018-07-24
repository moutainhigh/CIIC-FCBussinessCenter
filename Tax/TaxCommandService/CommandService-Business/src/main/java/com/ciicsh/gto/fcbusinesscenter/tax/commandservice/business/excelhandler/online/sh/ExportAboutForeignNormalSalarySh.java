package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.online.sh;

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
 * 外籍人员正常工资薪金(上海)
 *
 * @author yuantongqing 2018-05-31
 */
@Service
public class ExportAboutForeignNormalSalarySh extends BaseService {
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
                    LogTaskFactory.getLogger().error(ee, "ExportAboutForeignNormalSalarySh.getForeignNormalSalaryWB", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.OTHER, tags);
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
            //*适用公式-E列applicableFormula
            HSSFCell cellE = row.getCell(4);
            if (null == cellE) {
                cellE = row.createCell(4);
            }
            cellE.setCellValue(EnumUtil.getMessage(EnumUtil.APPLICABLE_FORMULA_ONLINE_COMMON, employeeInfoBatchPO.getApplicableFormula()));
            //境内天数-F列domestic_days
            HSSFCell cellF = row.getCell(5);
            if (null == cellF) {
                cellF = row.createCell(5);
            }
            cellF.setCellValue(po.getDomesticDays());
            //境外天数-G列overseas_days
            HSSFCell cellG = row.getCell(6);
            if (null == cellG) {
                cellG = row.createCell(6);
            }
            cellG.setCellValue(po.getOverseasDays());
            //境内所得境内支付-H列domestic_income_domestic_payment
            HSSFCell cellH = row.getCell(7);
            if (null == cellH) {
                cellH = row.createCell(7);
            }
            cellH.setCellValue(po.getDomesticIncomeDomesticPayment() == null ? "" : po.getDomesticIncomeDomesticPayment().toString());
            //境内所得境外支付-domestic_income_overseas_payment
            HSSFCell cellI = row.getCell(8);
            if (null == cellI) {
                cellI = row.createCell(8);
            }
            cellI.setCellValue(po.getDomesticIncomeOverseasPayment() == null ? "" : po.getDomesticIncomeOverseasPayment().toString());
            //境外所得境内支付-J列overseas_income_domestic_payment
            HSSFCell cellJ = row.getCell(9);
            if (null == cellJ) {
                cellJ = row.createCell(9);
            }
            cellJ.setCellValue(po.getOverseasIncomeDomesticPayment() == null ? "" : po.getOverseasIncomeDomesticPayment().toString());
            //境外所得境外支付-K列
            HSSFCell cellK = row.getCell(10);
            if (null == cellK) {
                cellK = row.createCell(10);
            }
            cellK.setCellValue(po.getOverseasIncomeOverseasPayment() == null ? "" : po.getOverseasIncomeOverseasPayment().toString());
            //基本养老保险费-L列deduct_retirement_insurance
            HSSFCell cellL = row.getCell(11);
            if (null == cellL) {
                cellL = row.createCell(11);
            }
            cellL.setCellValue(po.getDeductRetirementInsurance() == null ? "" : po.getDeductRetirementInsurance().toString());
            //基本医疗保险费-M列deduct_medical_insurance
            HSSFCell cellM = row.getCell(12);
            if (null == cellM) {
                cellM = row.createCell(12);
            }
            cellM.setCellValue(po.getDeductMedicalInsurance() == null ? "" : po.getDeductMedicalInsurance().toString());
            //失业保险费-N列deduct_dleness_insurance
            HSSFCell cellN = row.getCell(13);
            if (null == cellN) {
                cellN = row.createCell(13);
            }
            cellN.setCellValue(po.getDeductDlenessInsurance() == null ? "" : po.getDeductDlenessInsurance().toString());
            //住房公积金-O列deduct_house_fund
            HSSFCell cellO = row.getCell(14);
            if (null == cellO) {
                cellO = row.createCell(14);
            }
            cellO.setCellValue(po.getDeductHouseFund() == null ? "" : po.getDeductHouseFund().toString());
            //允许扣除的税费-P列deduct_takeoff
            HSSFCell cellP = row.getCell(15);
            if (null == cellP) {
                cellP = row.createCell(15);
            }
            cellP.setCellValue(po.getDeductTakeoff() == null ? "" : po.getDeductTakeoff().toString());
            //年金-Q列annuity
            HSSFCell cellQ = row.getCell(16);
            if (null == cellQ) {
                cellQ = row.createCell(16);
            }
            cellQ.setCellValue(po.getAnnuity() == null ? "" : po.getAnnuity().toString());
            //商业健康保险费-R列
            //税延养老保险费-S列
            //其他>其他扣除-T列deduct_other
            HSSFCell cellT = row.getCell(19);
            if (null == cellT) {
                cellT = row.createCell(19);
            }
            cellT.setCellValue(po.getOthers() == null ? "" : po.getOthers().toString());
            //住房补贴-U列housing_subsidy
            HSSFCell cellU = row.getCell(20);
            if (null == cellU) {
                cellU = row.createCell(20);
            }
            cellU.setCellValue(po.getHousingSubsidy() == null ? "" : po.getHousingSubsidy().toString());
            //伙食补贴-V列meal_allowance
            HSSFCell cellV = row.getCell(21);
            if (null == cellV) {
                cellV = row.createCell(21);
            }
            cellV.setCellValue(po.getMealAllowance() == null ? "" : po.getMealAllowance().toString());
            //洗衣费-W列laundry_fee
            HSSFCell cellW = row.getCell(22);
            if (null == cellW) {
                cellW = row.createCell(22);
            }
            cellW.setCellValue(po.getLaundryFee() == null ? "" : po.getLaundryFee().toString());
            //搬迁费-X列removing_indemnity_fee
            HSSFCell cellX = row.getCell(23);
            if (null == cellX) {
                cellX = row.createCell(23);
            }
            cellX.setCellValue(po.getRemovingIndemnityFee() == null ? "" : po.getRemovingIndemnityFee().toString());
            //出差补贴-Y列missionallowance
            HSSFCell cellY = row.getCell(24);
            if (null == cellY) {
                cellY = row.createCell(24);
            }
            cellY.setCellValue(po.getMissionallowance() == null ? "" : po.getMissionallowance().toString());
            //探亲费-Z列visiting_relatives_fee
            HSSFCell cellZ = row.getCell(25);
            if (null == cellZ) {
                cellZ = row.createCell(25);
            }
            cellZ.setCellValue(po.getVisitingRelativesFee() == null ? "" : po.getVisitingRelativesFee().toString());
            //语言培训费-AA列language_training_fee
            HSSFCell cellAA = row.getCell(26);
            if (null == cellAA) {
                cellAA = row.createCell(26);
            }
            cellAA.setCellValue(po.getLanguageTrainingFee() == null ? "" : po.getLanguageTrainingFee().toString());
            //子女教育经费-AB列education_funds
            HSSFCell cellAB = row.getCell(27);
            if (null == cellAB) {
                cellAB = row.createCell(27);
            }
            cellAB.setCellValue(po.getEducationFunds() == null ? "" : po.getEducationFunds().toString());
            //免税所得>其他费用-AC列
            //实际捐赠额-AD列
            //允许列支的捐赠比例-AE列
            //准予扣除的捐赠额-AF列donation
            HSSFCell cellAF = row.getCell(31);
            if (null == cellAF) {
                cellAF = row.createCell(31);
            }
            cellAF.setCellValue(po.getDonation() == null ? "" : po.getDonation().toString());
            //减免税额-AG列tax_deduction
            HSSFCell cellAG = row.getCell(32);
            if (null == cellAG) {
                cellAG = row.createCell(32);
            }
            cellAG.setCellValue(po.getTaxDeduction() == null ? "" : po.getTaxDeduction().toString());
            //备注-AH列
            sheetRowIndex++;
        }
    }
}
