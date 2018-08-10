package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.taxdata;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.BaseService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.TaskMainDetailServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskMainDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出个税数据明细
 *
 * @author yuantongqing on 2018-07-03
 */
@Service
public class ExportAboutTaskMainDetail extends BaseService {

    @Autowired
    private TaskMainDetailServiceImpl taskMainDetailService;

    /**
     * 获取个税数据明细WB
     * @param taskMainDetailBOList
     * @param fileName
     * @param type
     * @return
     */
    public HSSFWorkbook getTaskMainDetailWB(List<TaskMainDetailBO> taskMainDetailBOList,String fileName, String type){
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //获取POIFSFileSystem对象
            fs = getFSFileSystem(fileName, type);
            //通过POIFSFileSystem对象获取WB对象
            wb = getHSSFWorkbook(fs);
            //根据不同的业务需要处理wb
            this.handleTaskMainDetailWB(wb, taskMainDetailBOList);
        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    Map<String, String> tags = new HashMap<>(16);
                    //日志工具类返回
                    LogTaskFactory.getLogger().error(ee, "ExportAboutTaskMainDetail.getTaskMainDetailWB", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.OTHER, tags);
                }
            }
            throw e;
        }
        return wb;
    }

    /**
     * 处理个税数据WB
     * @param wb
     * @param taskMainDetailBOList
     */
    public void handleTaskMainDetailWB(HSSFWorkbook wb,List<TaskMainDetailBO> taskMainDetailBOList){
        // 读取了模板内所有sheet内容
        HSSFSheet sheet = wb.getSheetAt(0);
        //在相应的单元格进行赋值
        int sheetRowIndex = 1;
        for (TaskMainDetailBO taskMainDetailBO : taskMainDetailBOList){
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.andNew("task_main_id={0}",taskMainDetailBO.getTaskMainId());
            wrapper.andNew("task_main_detail_id={0}",taskMainDetailBO.getId());
            wrapper.orderBy("id",true);
            List<TaskMainDetailPO> taskMainDetailPOList = taskMainDetailService.selectList(wrapper);
            for(TaskMainDetailPO taskMainDetailPO:taskMainDetailPOList){
                HSSFRow row = sheet.getRow(sheetRowIndex);
                if (null == row) {
                    row = sheet.createRow(sheetRowIndex);
                }
                //-A列
                HSSFCell cellA = row.getCell(0);
                if (null == cellA) {
                    cellA = row.createCell(0);
                }
                cellA.setCellValue("");
                //批次号-B列
                HSSFCell cellB = row.getCell(1);
                if (null == cellB) {
                    cellB = row.createCell(1);
                }
                cellB.setCellValue(taskMainDetailPO.getBatchNo());
                //雇员编号-C列
                HSSFCell cellC = row.getCell(2);
                if (null == cellC) {
                    cellC = row.createCell(2);
                }
                cellC.setCellValue(taskMainDetailPO.getEmployeeNo());
                //雇员姓名-D列
                HSSFCell cellD = row.getCell(3);
                if (null == cellD) {
                    cellD = row.createCell(3);
                }
                cellD.setCellValue(taskMainDetailPO.getEmployeeName());
                //证件类型-E列
                HSSFCell cellE = row.getCell(4);
                if (null == cellE) {
                    cellE = row.createCell(4);
                }
                cellE.setCellValue(taskMainDetailPO.getIdTypeName());
                //证件号码-F列
                HSSFCell cellF = row.getCell(5);
                if (null == cellF) {
                    cellF = row.createCell(5);
                }
                cellF.setCellValue(taskMainDetailPO.getIdNo());
                //申报账户-G列
                HSSFCell cellG = row.getCell(6);
                if (null == cellG) {
                    cellG = row.createCell(6);
                }
                cellG.setCellValue(taskMainDetailPO.getDeclareAccountName());
                //缴纳账户-H列
                HSSFCell cellH = row.getCell(7);
                if (null == cellH) {
                    cellH = row.createCell(7);
                }
                cellH.setCellValue(taskMainDetailPO.getPayAccountName());
                //所得项目-I列
                HSSFCell cellI = row.getCell(8);
                if (null == cellI) {
                    cellI = row.createCell(8);
                }
                cellI.setCellValue(taskMainDetailPO.getIncomeSubjectName());
                //所得期间-J列
                HSSFCell cellJ = row.getCell(9);
                if (null == cellJ) {
                    cellJ = row.createCell(9);
                }
                cellJ.setCellValue(taskMainDetailPO.getPeriod() == null ? "" : DateTimeFormatter.ofPattern("yyyy-MM").format(taskMainDetailPO.getPeriod()));
                //税金-K列
                HSSFCell cellK = row.getCell(10);
                if (null == cellK) {
                    cellK = row.createCell(10);
                }
                cellK.setCellValue(taskMainDetailPO.getTaxReal() == null ? "" : taskMainDetailPO.getTaxReal().toString());
                //收入额-L列
                HSSFCell cellL = row.getCell(11);
                if (null == cellL) {
                    cellL = row.createCell(11);
                }
                cellL.setCellValue(taskMainDetailPO.getIncomeTotal() == null ? "" : taskMainDetailPO.getIncomeTotal().toString());
                //免税所得-M列incomeDutyfree
                HSSFCell cellM = row.getCell(12);
                if (null == cellM) {
                    cellM = row.createCell(12);
                }
                cellM.setCellValue(taskMainDetailPO.getIncomeDutyfree() == null ? "" : taskMainDetailPO.getIncomeDutyfree().toString());
                //基本养老保险费-N列deductRetirementInsurance
                HSSFCell cellN = row.getCell(13);
                if (null == cellN) {
                    cellN = row.createCell(13);
                }
                cellN.setCellValue(taskMainDetailPO.getDeductRetirementInsurance() == null ? "" : taskMainDetailPO.getDeductRetirementInsurance().toString());
                //基本医疗保险费-O列deductMedicalInsurance
                HSSFCell cellO = row.getCell(14);
                if (null == cellO) {
                    cellO = row.createCell(14);
                }
                cellO.setCellValue(taskMainDetailPO.getDeductMedicalInsurance() == null ? "" : taskMainDetailPO.getDeductMedicalInsurance().toString());
                //失业保险费-P列deductDlenessInsurance
                HSSFCell cellP = row.getCell(15);
                if (null == cellP) {
                    cellP = row.createCell(15);
                }
                cellP.setCellValue(taskMainDetailPO.getDeductDlenessInsurance() == null ? "" : taskMainDetailPO.getDeductDlenessInsurance().toString());
                //住房公积金-Q列deductHouseFund
                HSSFCell cellQ = row.getCell(16);
                if (null == cellQ) {
                    cellQ = row.createCell(16);
                }
                cellQ.setCellValue(taskMainDetailPO.getDeductHouseFund() == null ? "" : taskMainDetailPO.getDeductHouseFund().toString());
                //允许扣除的税费-R列deductTakeoff
                HSSFCell cellR = row.getCell(17);
                if (null == cellR) {
                    cellR = row.createCell(17);
                }
                cellR.setCellValue(taskMainDetailPO.getDeductTakeoff() == null ? "" : taskMainDetailPO.getDeductTakeoff().toString());
                //其他-S列deductOther
                HSSFCell cellS = row.getCell(18);
                if (null == cellS) {
                    cellS = row.createCell(18);
                }
                cellS.setCellValue(taskMainDetailPO.getDeductOther() == null ? "" : taskMainDetailPO.getDeductOther().toString());
                //合计（税前扣除项目）-T列deductTotal
                HSSFCell cellT = row.getCell(19);
                if (null == cellT) {
                    cellT = row.createCell(19);
                }
                cellT.setCellValue(taskMainDetailPO.getDeductTotal() == null ? "" : taskMainDetailPO.getDeductTotal().toString());
                //减除费用-U列deduction
                HSSFCell cellU = row.getCell(20);
                if (null == cellU) {
                    cellU = row.createCell(20);
                }
                cellU.setCellValue(taskMainDetailPO.getDeduction() == null ? "" : taskMainDetailPO.getDeduction().toString());
                //准予扣除的捐赠额-V列donation
                HSSFCell cellV = row.getCell(21);
                if (null == cellV) {
                    cellV = row.createCell(21);
                }
                cellV.setCellValue(taskMainDetailPO.getDonation() == null ? "" : taskMainDetailPO.getDonation().toString());
                //应纳税所得额-W列incomeForTax
                HSSFCell cellW = row.getCell(22);
                if (null == cellW) {
                    cellW = row.createCell(22);
                }
                cellW.setCellValue(taskMainDetailPO.getIncomeForTax() == null ? "" : taskMainDetailPO.getIncomeForTax().toString());
                //税率-X列taxRate
                HSSFCell cellX = row.getCell(23);
                if (null == cellX) {
                    cellX = row.createCell(23);
                }
                cellX.setCellValue(taskMainDetailPO.getTaxRate() == null ? "" : taskMainDetailPO.getTaxRate().toString());
                //速算扣除数-Y列quickCalDeduct
                HSSFCell cellY = row.getCell(24);
                if (null == cellY) {
                    cellY = row.createCell(24);
                }
                cellY.setCellValue(taskMainDetailPO.getQuickCalDeduct() == null ? "" : taskMainDetailPO.getQuickCalDeduct().toString());
                //应纳税额-Z列taxAmount
                HSSFCell cellZ = row.getCell(25);
                if (null == cellZ) {
                    cellZ = row.createCell(25);
                }
                cellZ.setCellValue(taskMainDetailPO.getTaxAmount() == null ? "" : taskMainDetailPO.getTaxAmount().toString());
                //减免税额-AA列taxDeduction
                HSSFCell cellAA = row.getCell(26);
                if (null == cellAA) {
                    cellAA = row.createCell(26);
                }
                cellAA.setCellValue(taskMainDetailPO.getTaxDeduction() == null ? "" : taskMainDetailPO.getTaxDeduction().toString());
                //应扣缴税额-AB列taxWithholdAmount
                HSSFCell cellAB = row.getCell(27);
                if (null == cellAB) {
                    cellAB = row.createCell(27);
                }
                cellAB.setCellValue(taskMainDetailPO.getTaxWithholdAmount()== null ? "" : taskMainDetailPO.getTaxWithholdAmount().toString());
                //已扣缴税额-AC列taxWithholdedAmount
                HSSFCell cellAC = row.getCell(28);
                if (null == cellAC) {
                    cellAC = row.createCell(28);
                }
                cellAC.setCellValue(taskMainDetailPO.getTaxWithholdedAmount()== null ? "" : taskMainDetailPO.getTaxWithholdedAmount().toString());
                //应补（退）税额-AD列taxRemedyOrReturn
                HSSFCell cellAD = row.getCell(29);
                if (null == cellAD) {
                    cellAD = row.createCell(29);
                }
                cellAD.setCellValue(taskMainDetailPO.getTaxRemedyOrReturn()== null ? "" : taskMainDetailPO.getTaxRemedyOrReturn().toString());
                sheetRowIndex++;
            }
            HSSFRow row = sheet.getRow(sheetRowIndex);
            if (null == row) {
                row = sheet.createRow(sheetRowIndex);
            }
            //-A列
            HSSFCell cellA = row.getCell(0);
            if (null == cellA) {
                cellA = row.createCell(0);
            }
            cellA.setCellValue("合并后:");
            //批次号-B列
            HSSFCell cellB = row.getCell(1);
            if (null == cellB) {
                cellB = row.createCell(1);
            }
            cellB.setCellValue("");
            //雇员编号-C列
            HSSFCell cellC = row.getCell(2);
            if (null == cellC) {
                cellC = row.createCell(2);
            }
            cellC.setCellValue(taskMainDetailBO.getEmployeeNo());
            //雇员姓名-D列
            HSSFCell cellD = row.getCell(3);
            if (null == cellD) {
                cellD = row.createCell(3);
            }
            cellD.setCellValue(taskMainDetailBO.getEmployeeName());
            //证件类型-E列
            HSSFCell cellE = row.getCell(4);
            if (null == cellE) {
                cellE = row.createCell(4);
            }
            cellE.setCellValue(taskMainDetailBO.getIdTypeName());
            //证件号码-F列
            HSSFCell cellF = row.getCell(5);
            if (null == cellF) {
                cellF = row.createCell(5);
            }
            cellF.setCellValue(taskMainDetailBO.getIdNo());
            //申报账户-G列
            HSSFCell cellG = row.getCell(6);
            if (null == cellG) {
                cellG = row.createCell(6);
            }
            cellG.setCellValue(taskMainDetailBO.getDeclareAccountName());
            //缴纳账户-H列
            HSSFCell cellH = row.getCell(7);
            if (null == cellH) {
                cellH = row.createCell(7);
            }
            cellH.setCellValue(taskMainDetailBO.getPayAccountName());
            //所得项目-I列
            HSSFCell cellI = row.getCell(8);
            if (null == cellI) {
                cellI = row.createCell(8);
            }
            cellI.setCellValue(taskMainDetailBO.getIncomeSubjectName());
            //所得期间-J列
            HSSFCell cellJ = row.getCell(9);
            if (null == cellJ) {
                cellJ = row.createCell(9);
            }
            cellJ.setCellValue(taskMainDetailBO.getPeriod() == null ? "" : DateTimeFormatter.ofPattern("yyyy-MM").format(taskMainDetailBO.getPeriod()));
            //税金-K列
            HSSFCell cellK = row.getCell(10);
            if (null == cellK) {
                cellK = row.createCell(10);
            }
            cellK.setCellValue(taskMainDetailBO.getTaxReal() == null ? "" : taskMainDetailBO.getTaxReal().toString());
            //收入额-L列
            HSSFCell cellL = row.getCell(11);
            if (null == cellL) {
                cellL = row.createCell(11);
            }
            cellL.setCellValue(taskMainDetailBO.getIncomeTotal() == null ? "" : taskMainDetailBO.getIncomeTotal().toString());
            //免税所得-M列incomeDutyfree
            HSSFCell cellM = row.getCell(12);
            if (null == cellM) {
                cellM = row.createCell(12);
            }
            cellM.setCellValue(taskMainDetailBO.getIncomeDutyfree() == null ? "" : taskMainDetailBO.getIncomeDutyfree().toString());
            //基本养老保险费-N列deductRetirementInsurance
            HSSFCell cellN = row.getCell(13);
            if (null == cellN) {
                cellN = row.createCell(13);
            }
            cellN.setCellValue(taskMainDetailBO.getDeductRetirementInsurance() == null ? "" : taskMainDetailBO.getDeductRetirementInsurance().toString());
            //基本医疗保险费-O列deductMedicalInsurance
            HSSFCell cellO = row.getCell(14);
            if (null == cellO) {
                cellO = row.createCell(14);
            }
            cellO.setCellValue(taskMainDetailBO.getDeductMedicalInsurance() == null ? "" : taskMainDetailBO.getDeductMedicalInsurance().toString());
            //失业保险费-P列deductDlenessInsurance
            HSSFCell cellP = row.getCell(15);
            if (null == cellP) {
                cellP = row.createCell(15);
            }
            cellP.setCellValue(taskMainDetailBO.getDeductDlenessInsurance() == null ? "" : taskMainDetailBO.getDeductDlenessInsurance().toString());
            //住房公积金-Q列deductHouseFund
            HSSFCell cellQ = row.getCell(16);
            if (null == cellQ) {
                cellQ = row.createCell(16);
            }
            cellQ.setCellValue(taskMainDetailBO.getDeductHouseFund() == null ? "" : taskMainDetailBO.getDeductHouseFund().toString());
            //允许扣除的税费-R列deductTakeoff
            HSSFCell cellR = row.getCell(17);
            if (null == cellR) {
                cellR = row.createCell(17);
            }
            cellR.setCellValue(taskMainDetailBO.getDeductTakeoff() == null ? "" : taskMainDetailBO.getDeductTakeoff().toString());
            //其他-S列deductOther
            HSSFCell cellS = row.getCell(18);
            if (null == cellS) {
                cellS = row.createCell(18);
            }
            cellS.setCellValue(taskMainDetailBO.getDeductOther() == null ? "" : taskMainDetailBO.getDeductOther().toString());
            //合计（税前扣除项目）-T列deductTotal
            HSSFCell cellT = row.getCell(19);
            if (null == cellT) {
                cellT = row.createCell(19);
            }
            cellT.setCellValue(taskMainDetailBO.getDeductTotal() == null ? "" : taskMainDetailBO.getDeductTotal().toString());
            //减除费用-U列deduction
            HSSFCell cellU = row.getCell(20);
            if (null == cellU) {
                cellU = row.createCell(20);
            }
            cellU.setCellValue(taskMainDetailBO.getDeduction() == null ? "" : taskMainDetailBO.getDeduction().toString());
            //准予扣除的捐赠额-V列donation
            HSSFCell cellV = row.getCell(21);
            if (null == cellV) {
                cellV = row.createCell(21);
            }
            cellV.setCellValue(taskMainDetailBO.getDonation() == null ? "" : taskMainDetailBO.getDonation().toString());
            //应纳税所得额-W列incomeForTax
            HSSFCell cellW = row.getCell(22);
            if (null == cellW) {
                cellW = row.createCell(22);
            }
            cellW.setCellValue(taskMainDetailBO.getIncomeForTax() == null ? "" : taskMainDetailBO.getIncomeForTax().toString());
            //税率-X列taxRate
            HSSFCell cellX = row.getCell(23);
            if (null == cellX) {
                cellX = row.createCell(23);
            }
            cellX.setCellValue(taskMainDetailBO.getTaxRate() == null ? "" : taskMainDetailBO.getTaxRate().toString());
            //速算扣除数-Y列quickCalDeduct
            HSSFCell cellY = row.getCell(24);
            if (null == cellY) {
                cellY = row.createCell(24);
            }
            cellY.setCellValue(taskMainDetailBO.getQuickCalDeduct() == null ? "" : taskMainDetailBO.getQuickCalDeduct().toString());
            //应纳税额-Z列taxAmount
            HSSFCell cellZ = row.getCell(25);
            if (null == cellZ) {
                cellZ = row.createCell(25);
            }
            cellZ.setCellValue(taskMainDetailBO.getTaxAmount() == null ? "" : taskMainDetailBO.getTaxAmount().toString());
            //减免税额-AA列taxDeduction
            HSSFCell cellAA = row.getCell(26);
            if (null == cellAA) {
                cellAA = row.createCell(26);
            }
            cellAA.setCellValue(taskMainDetailBO.getTaxDeduction() == null ? "" : taskMainDetailBO.getTaxDeduction().toString());
            //应扣缴税额-AB列taxWithholdAmount
            HSSFCell cellAB = row.getCell(27);
            if (null == cellAB) {
                cellAB = row.createCell(27);
            }
            cellAB.setCellValue(taskMainDetailBO.getTaxWithholdAmount()== null ? "" : taskMainDetailBO.getTaxWithholdAmount().toString());
            //已扣缴税额-AC列taxWithholdedAmount
            HSSFCell cellAC = row.getCell(28);
            if (null == cellAC) {
                cellAC = row.createCell(28);
            }
            cellAC.setCellValue(taskMainDetailBO.getTaxWithholdedAmount()== null ? "" : taskMainDetailBO.getTaxWithholdedAmount().toString());
            //应补（退）税额-AD列taxRemedyOrReturn
            HSSFCell cellAD = row.getCell(29);
            if (null == cellAD) {
                cellAD = row.createCell(29);
            }
            cellAD.setCellValue(taskMainDetailBO.getTaxRemedyOrReturn()== null ? "" : taskMainDetailBO.getTaxRemedyOrReturn().toString());
            sheetRowIndex++;
        }
    }

}
