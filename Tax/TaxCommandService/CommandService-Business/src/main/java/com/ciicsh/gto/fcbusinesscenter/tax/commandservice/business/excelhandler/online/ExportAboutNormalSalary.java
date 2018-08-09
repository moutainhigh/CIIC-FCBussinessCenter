package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.online;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.BaseService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.TaskSubDeclareDetailServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.EmployeeInfoBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclarePO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
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
 * 正常工资薪金
 *
 * @author yuantongqing 2018-05-31
 */
@Service
public class ExportAboutNormalSalary extends BaseService {

    @Autowired
    public TaskSubDeclareDetailServiceImpl taskSubDeclareDetailService;

    /**
     * 导出正常工资薪金
     *
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     * @param fileName
     * @param type
     * @return
     */
    public HSSFWorkbook getNormalSalaryWB(TaskSubDeclarePO taskSubDeclarePO,List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList, String fileName, String type) {
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //获取POIFSFileSystem对象
            fs = getFSFileSystem(fileName, type);
            //通过POIFSFileSystem对象获取WB对象
            wb = getHSSFWorkbook(fs);
            //根据不同的业务需要处理wb
            this.handleNormalSalaryWB(wb, taskSubDeclareDetailPOList, employeeInfoBatchPOList);
        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    Map<String, String> tags = new HashMap<>(16);
                    //日志工具类返回
                    LogTaskFactory.getLogger().error(ee, "ExportAboutNormalSalary.getNormalSalaryWB", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.OTHER, tags);
                }
            }
            throw e;
        }
        return wb;
    }

    /**
     * 导出正常工资薪金
     *
     * @param wb
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     */
    public void handleNormalSalaryWB(HSSFWorkbook wb, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList) {
        // 读取了模板内所有sheet内容
        HSSFSheet sheet = wb.getSheetAt(0);
        //在相应的单元格进行赋值
        int sheetRowIndex = 1;
        for (TaskSubDeclareDetailPO po : taskSubDeclareDetailPOList) {
            List<EmployeeInfoBatchPO> employeeInfoBatchPOS = new ArrayList<>();
            //判断是否是合并明细
            if(po.getCombined()){
                //获取合并前批次明细ID
                List<TaskSubDeclareDetailPO>  taskSubDeclareDetailPOList1 = taskSubDeclareDetailService.querySubDeclareDetailListBeforeMergeByMergeId(po.getId());
                employeeInfoBatchPOS = taskSubDeclareDetailPOList1.size() > 0 ? employeeInfoBatchPOList.stream().filter(item -> taskSubDeclareDetailPOList1.get(0).getCalculationBatchDetailId().equals(item.getCalBatchDetailId())).collect(Collectors.toList()) : null;
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
            cellC.setCellValue(EnumUtil.getMessage(EnumUtil.ID_TYPE_ONLINE_SIMPLE_COMMON, po.getIdType()));
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
            //*收入额-F列income_total
            HSSFCell cellF = row.getCell(5);
            if (null == cellF) {
                cellF = row.createCell(5);
            }
            cellF.setCellValue(po.getIncomeTotal() == null ? "" : po.getIncomeTotal().toString());
            //免税所得-G列income_dutyfree
            HSSFCell cellG = row.getCell(6);
            if (null == cellG) {
                cellG = row.createCell(6);
            }
            cellG.setCellValue(po.getIncomeDutyfree() == null ? "" : po.getIncomeDutyfree().toString());
            //基本养老保险费-H列deduct_retirement_insurance
            HSSFCell cellH = row.getCell(7);
            if (null == cellH) {
                cellH = row.createCell(7);
            }
            cellH.setCellValue(po.getDeductRetirementInsurance() == null ? "" : po.getDeductRetirementInsurance().toString());
            //基本医疗保险费-I列deduct_medical_insurance
            HSSFCell cellI = row.getCell(8);
            if (null == cellI) {
                cellI = row.createCell(8);
            }
            cellI.setCellValue(po.getDeductMedicalInsurance() == null ? "" : po.getDeductMedicalInsurance().toString());
            //失业保险费-J列deduct_dleness_insurance
            HSSFCell cellJ = row.getCell(9);
            if (null == cellJ) {
                cellJ = row.createCell(9);
            }
            cellJ.setCellValue(po.getDeductDlenessInsurance() == null ? "" : po.getDeductDlenessInsurance().toString());
            //住房公积金-K列deduct_house_fund
            HSSFCell cellK = row.getCell(10);
            if (null == cellK) {
                cellK = row.createCell(10);
            }
            cellK.setCellValue(po.getDeductHouseFund() == null ? "" : po.getDeductHouseFund().toString());
            //允许扣除的税费-L列deduct_takeoff
            HSSFCell cellL = row.getCell(11);
            if (null == cellL) {
                cellL = row.createCell(11);
            }
            cellL.setCellValue(po.getDeductTakeoff() == null ? "" : po.getDeductTakeoff().toString());
            //年金-M列annuity
            HSSFCell cellM = row.getCell(12);
            if (null == cellM) {
                cellM = row.createCell(12);
            }
            cellM.setCellValue(po.getAnnuity() == null ? "" : po.getAnnuity().toString());
            //商业健康保险费-N列business_health_insurance
            HSSFCell cellN = row.getCell(13);
            if (null == cellN) {
                cellN = row.createCell(13);
            }
            cellN.setCellValue(po.getBusinessHealthInsurance() == null ? "" : po.getBusinessHealthInsurance().toString());
            //税延养老保险费-O列endowment_insurance
            HSSFCell cellO = row.getCell(14);
            if (null == cellO) {
                cellO = row.createCell(14);
            }
            cellO.setCellValue(po.getEndowmentInsurance() == null ? "" : po.getEndowmentInsurance().toString());
            //其他扣除-P列
            //减除费用-Q列deduction
            HSSFCell cellQ = row.getCell(16);
            if (null == cellQ) {
                cellQ = row.createCell(16);
            }
            cellQ.setCellValue(po.getDeduction() == null ? "" : po.getDeduction().abs().toString());
            //实际捐赠额-R列
            //允许列支的捐赠比例-S列
            //准予扣除的捐赠额-T列donation
            HSSFCell cellT = row.getCell(19);
            if (null == cellT) {
                cellT = row.createCell(19);
            }
            cellT.setCellValue(po.getDonation() == null ? "" : po.getDonation().toString());
            //减免税额-U列tax_deduction
            HSSFCell cellU = row.getCell(20);
            if (null == cellU) {
                cellU = row.createCell(20);
            }
            cellU.setCellValue(po.getTaxDeduction() == null ? "" : po.getTaxDeduction().toString());
            //已扣缴税额-V列
            //备注-W列
            sheetRowIndex++;
        }
    }
}
