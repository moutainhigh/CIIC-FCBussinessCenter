package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.offline.gd;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.BaseService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.EmployeeInfoBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.IncomeSubject;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.DateTimeKit;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
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
 * 申报信息(广东)
 * @author yuantongqing on 2018-06-04
 */
@Service
public class ExportAboutDeclarationInformationGd extends BaseService{

    /**
     * 获取申报模板信息(广东)WB
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     * @param fileName
     * @param type
     * @return
     */
    public HSSFWorkbook getDeclarationInformationWB(List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList, String fileName, String type){
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
                    LogTaskFactory.getLogger().error(ee, "ExportAboutDeclarationInformationGd.getDeclarationInformationWB", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.OTHER, tags);
                }
            }
            throw e;
        }
        return wb;
    }

    /**
     * 处理申报模板信息(广东)WB
     * @param wb
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     */
    public void handleDeclarationInformationWB(HSSFWorkbook wb, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList){
        //国内人员集合
        List<EmployeeInfoBatchPO> chineseEmployeeInfoList = employeeInfoBatchPOList.stream().filter(employeeInfo -> "CN".equals(employeeInfo.getNationality())).collect(Collectors.toList());
        //扣缴个人所得税报告表（国内）sheet页
        HSSFSheet guoneiSheet = wb.getSheetAt(0);
        int guoneiSheetRowIndex = 10;
        for(EmployeeInfoBatchPO employeeInfoBatchPO : chineseEmployeeInfoList){
            List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOLists = taskSubDeclareDetailPOList.stream().filter(item -> item.getCalculationBatchDetailId().equals(employeeInfoBatchPO.getCalBatchDetailId())).collect(Collectors.toList());
            TaskSubDeclareDetailPO taskSubDeclareDetailPO = new TaskSubDeclareDetailPO();
            if(taskSubDeclareDetailPOLists.size() > 0){
                taskSubDeclareDetailPO = taskSubDeclareDetailPOLists.get(0);
            }
            HSSFRow row = guoneiSheet.getRow(guoneiSheetRowIndex);
            if (null == row) {
                row = guoneiSheet.createRow(guoneiSheetRowIndex);
            }
            //序号-A列
            //*是否明细申报-B列(默认"是")
            HSSFCell cellB = row.getCell(1);
            if (null == cellB) {
                cellB = row.createCell(1);
            }
            cellB.setCellValue("是");
            //纳税人姓名-C列
            HSSFCell cellC = row.getCell(2);
            if (null == cellC) {
                cellC = row.createCell(2);
            }
            cellC.setCellValue(employeeInfoBatchPO.getTaxName());
            //身份证件类型-D列
            HSSFCell cellD = row.getCell(3);
            if (null == cellD) {
                cellD = row.createCell(3);
            }
            cellD.setCellValue(EnumUtil.getMessage(EnumUtil.ID_TYPE_OFFLINE_GD, taskSubDeclareDetailPO.getIdType()));
            //身份证件号码-E列
            HSSFCell cellE = row.getCell(4);
            if (null == cellE) {
                cellE = row.createCell(4);
            }
            cellE.setCellValue(taskSubDeclareDetailPO.getIdNo());
            //*所得项目-F列
            HSSFCell cellF = row.getCell(5);
            if (null == cellF) {
                cellF = row.createCell(5);
            }
            String incomeSubjectName = EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT_OFFLINE_GD, taskSubDeclareDetailPO.getIncomeSubject());
            cellF.setCellValue(incomeSubjectName);
            //所得子目-G列(当所得项目为“_0101_正常工资薪金”时，默认值“_010199_正常工资薪金”，其余为空)
            HSSFCell cellG = row.getCell(6);
            if (null == cellG) {
                cellG = row.createCell(6);
            }
            cellG.setCellValue("_0101_正常工资薪金".equals(incomeSubjectName) ? "_010199_正常工资薪金":"");
            //*所得期间起-H列(个税期间起始日,如:20160701)
            HSSFCell cellH = row.getCell(7);
            if (null == cellH) {
                cellH = row.createCell(7);
            }
            String periodStartStr = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(taskSubDeclareDetailPO.getPeriod());
            cellH.setCellValue(periodStartStr);
            //*所得期间止-I列(个税期间结束日,如:20160731)
            HSSFCell cellI = row.getCell(8);
            if (null == cellI) {
                cellI = row.createCell(8);
            }
            cellI.setCellValue(DateTimeKit.getLastDayForMonth(periodStartStr));
            //*收入所属期起-J列(正常薪金：个税期间起始日（如20160701）,年度奖金：年终奖年度起始日,离职补偿金：入职日期,个人股票期权行权收入：个税期间结束日倒推规定月份数（如20160101）)
            String incomeStartStr = "";
            String incomeEndStr = "";
            if(IncomeSubject.NORMALSALARY.getCode().equals(taskSubDeclareDetailPO.getIncomeSubject()) || IncomeSubject.FOREIGNNORMALSALARY.getCode().equals(taskSubDeclareDetailPO.getIncomeSubject())){
                incomeStartStr = periodStartStr;
                incomeEndStr = DateTimeKit.getLastDayFromStr(incomeStartStr);
            }else if(IncomeSubject.BONUSINCOMEYEAR.getCode().equals(taskSubDeclareDetailPO.getIncomeSubject())){
                incomeStartStr = DateTimeKit.getFirstDayForYear(periodStartStr);
                incomeEndStr = DateTimeKit.getLastDayForYear(periodStartStr);
            }else if(IncomeSubject.LABORCONTRACT.getCode().equals(taskSubDeclareDetailPO.getIncomeSubject())){
                incomeStartStr = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(employeeInfoBatchPO.getEntryDate());
                incomeEndStr = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(employeeInfoBatchPO.getLeaveDate());
            }else if(IncomeSubject.STOCKOPTIONINCOME.getCode().equals(taskSubDeclareDetailPO.getIncomeSubject())){
                //个人股票期权行权收入：个税期间结束日倒推规定月份数，如2018-06-30 倒推2个月就是2018-05-01
                incomeStartStr = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(taskSubDeclareDetailPO.getPeriod().plusMonths(StrKit.strToInt(taskSubDeclareDetailPO.getNumberOfMonths()) - 1));
                incomeEndStr = DateTimeKit.getLastDayForMonth(periodStartStr);
            }
            HSSFCell cellJ = row.getCell(9);
            if (null == cellJ) {
                cellJ = row.createCell(9);
            }
            cellJ.setCellValue(incomeStartStr);
            //*收入所属期止-K列(正常薪金：个税期间结束日（如20160731）,年度奖金：年终奖年度结束日,离职补偿金：离职日期,个人股票期权行权收入：个税期间结束日（如20161231）)
            HSSFCell cellK = row.getCell(10);
            if (null == cellK) {
                cellK = row.createCell(10);
            }
            cellK.setCellValue(incomeEndStr);
            //*含税收入额-L列(不需要)
            //*收入额-M列(正常薪金：收入额,年度奖金：年度奖金,离职补偿金：离职金)
            HSSFCell cellM = row.getCell(12);
            if (null == cellM) {
                cellM = row.createCell(12);
            }
            cellM.setCellValue(taskSubDeclareDetailPO.getIncomeTotal() == null ? "" : taskSubDeclareDetailPO.getIncomeTotal().toString());
            //免税所得-N列income_dutyfree
            HSSFCell cellN = row.getCell(13);
            if (null == cellN) {
                cellN = row.createCell(13);
            }
            cellN.setCellValue(taskSubDeclareDetailPO.getIncomeDutyfree()== null ? "" : taskSubDeclareDetailPO.getIncomeDutyfree().toString());
            //基本养老保险费-O列deduct_retirement_insurance
            HSSFCell cellO = row.getCell(14);
            if (null == cellO) {
                cellO = row.createCell(14);
            }
            cellO.setCellValue(taskSubDeclareDetailPO.getDeductRetirementInsurance()== null ? "" : taskSubDeclareDetailPO.getDeductRetirementInsurance().toString());
            //基本医疗保险费-P列deduct_medical_insurance
            HSSFCell cellP = row.getCell(15);
            if (null == cellP) {
                cellP = row.createCell(15);
            }
            cellP.setCellValue(taskSubDeclareDetailPO.getDeductMedicalInsurance()== null ? "" : taskSubDeclareDetailPO.getDeductMedicalInsurance().toString());
            //失业保险费-Q列deduct_dleness_insurance
            HSSFCell cellQ = row.getCell(16);
            if (null == cellQ) {
                cellQ = row.createCell(16);
            }
            cellQ.setCellValue(taskSubDeclareDetailPO.getDeductDlenessInsurance()== null ? "" : taskSubDeclareDetailPO.getDeductDlenessInsurance().toString());
            //住房公积金-R列deduct_house_fund
            HSSFCell cellR = row.getCell(17);
            if (null == cellR) {
                cellR = row.createCell(17);
            }
            cellR.setCellValue(taskSubDeclareDetailPO.getDeductHouseFund() == null ? "" : taskSubDeclareDetailPO.getDeductHouseFund().toString());
            //财产原值-S列deduct_property
            HSSFCell cellS = row.getCell(18);
            if (null == cellS) {
                cellS = row.createCell(18);
            }
            cellS.setCellValue(taskSubDeclareDetailPO.getDeductProperty() == null ? "" : taskSubDeclareDetailPO.getDeductProperty().toString());
            //允许扣除的税费-T列deduct_takeoff
            HSSFCell cellT = row.getCell(19);
            if (null == cellT) {
                cellT = row.createCell(19);
            }
            cellT.setCellValue(taskSubDeclareDetailPO.getDeductTakeoff() == null ? "" : taskSubDeclareDetailPO.getDeductTakeoff().toString());
            //年金-U列annuity
            HSSFCell cellU = row.getCell(20);
            if (null == cellU) {
                cellU = row.createCell(20);
            }
            cellU.setCellValue(taskSubDeclareDetailPO.getAnnuity() == null ? "" : taskSubDeclareDetailPO.getAnnuity().toString());
            //商业健康险-V列business_health_insurance
            HSSFCell cellV = row.getCell(21);
            if (null == cellV) {
                cellV = row.createCell(21);
            }
            cellV.setCellValue(taskSubDeclareDetailPO.getBusinessHealthInsurance() == null ? "" : taskSubDeclareDetailPO.getBusinessHealthInsurance().toString());
            //投资抵扣-W列(不需要)
            //其他扣除额-X列(不需要)
            //合计-Y列(不需要)
            //减除费用额-Z列(不需要)
            //准予扣除的捐赠额-AA列donation
            HSSFCell cellAA = row.getCell(26);
            if (null == cellAA) {
                cellAA = row.createCell(26);
            }
            cellAA.setCellValue(taskSubDeclareDetailPO.getDonation() == null ? "" : taskSubDeclareDetailPO.getDonation().toString());
            //*税款负担方式-AB列(默认值"_01_个人负担")burden
            HSSFCell cellAB = row.getCell(27);
            if (null == cellAB) {
                cellAB = row.createCell(27);
            }
            cellAB.setCellValue(EnumUtil.getMessage(EnumUtil.BURDEN_OFFLINE_GD, employeeInfoBatchPO.getBurden()));
            //雇主负担比例-AC列(不需要)
            //雇主负担税额-AD列(不需要)
            //*应纳税所得额-AE列(不需要)
            //*税率-AF列(不需要)
            //*速算扣除数-AG列(不需要)
            //*应纳税额-AH列(不需要)
            //减免税额-AI列tax_deduction
            HSSFCell cellAI = row.getCell(34);
            if (null == cellAI) {
                cellAI = row.createCell(34);
            }
            cellAI.setCellValue(taskSubDeclareDetailPO.getTaxDeduction()== null ? "" : taskSubDeclareDetailPO.getTaxDeduction().toString());
            //*应扣缴税额-AJ列(不需要)
            //已扣缴税额-AK列tax_withholded_amount
            HSSFCell cellAK = row.getCell(36);
            if (null == cellAK) {
                cellAK = row.createCell(36);
            }
            cellAK.setCellValue(taskSubDeclareDetailPO.getTaxWithholdedAmount() == null ? "" : taskSubDeclareDetailPO.getTaxWithholdedAmount().toString());
            //*应补退税额-AL列(不需要)
            //计税结果-AM列(不需要)
            //本年累计行权收入-AN列
            //本年累计已纳税额-AO列
            //备注-AP列
            //已申报收入额-AQ列(不需要)
            guoneiSheetRowIndex++;
        }
        //外籍人员集合
        List<EmployeeInfoBatchPO> foreignEmployeeInfoList = employeeInfoBatchPOList.stream().filter(employeeInfo -> !"CN".equals(employeeInfo.getNationality())).collect(Collectors.toList());
        //扣缴个人所得税报告表（外籍）sheet页
        HSSFSheet waijiSheet = wb.getSheetAt(1);
        int waijiSheetRowIndex = 10;
        for(EmployeeInfoBatchPO employeeInfoBatchPO : foreignEmployeeInfoList){
            List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOLists = taskSubDeclareDetailPOList.stream().filter(item -> item.getCalculationBatchDetailId().equals(employeeInfoBatchPO.getCalBatchDetailId())).collect(Collectors.toList());
            TaskSubDeclareDetailPO taskSubDeclareDetailPO = new TaskSubDeclareDetailPO();
            if(taskSubDeclareDetailPOLists.size() > 0){
                taskSubDeclareDetailPO = taskSubDeclareDetailPOLists.get(0);
            }
            HSSFRow row = waijiSheet.getRow(waijiSheetRowIndex);
            if (null == row) {
                row = waijiSheet.createRow(waijiSheetRowIndex);
            }
            //序号-A列
            //*是否明细申报-B列(默认值“是”)
            HSSFCell cellB = row.getCell(1);
            if (null == cellB) {
                cellB = row.createCell(1);
            }
            cellB.setCellValue("是");
            //姓名(中文)-C列
            HSSFCell cellC = row.getCell(2);
            if (null == cellC) {
                cellC = row.createCell(2);
            }
            cellC.setCellValue(taskSubDeclareDetailPO.getEmployeeName());
            //姓名(英文)-D列
            HSSFCell cellD = row.getCell(3);
            if (null == cellD) {
                cellD = row.createCell(3);
            }
            cellD.setCellValue(employeeInfoBatchPO.getChineseName());
            //身份证件类型-E列
            HSSFCell cellE = row.getCell(4);
            if (null == cellE) {
                cellE = row.createCell(4);
            }
            cellE.setCellValue(EnumUtil.getMessage(EnumUtil.ID_TYPE_OFFLINE_GD, taskSubDeclareDetailPO.getIdType()));
            //身份证件号码-F列
            HSSFCell cellF = row.getCell(5);
            if (null == cellF) {
                cellF = row.createCell(5);
            }
            cellF.setCellValue(taskSubDeclareDetailPO.getIdNo());
            //国籍和地区-G列(根据“报税国籍”匹配代码)
            HSSFCell cellG = row.getCell(6);
            if (null == cellG) {
                cellG = row.createCell(6);
            }
            cellG.setCellValue(EnumUtil.getMessage(EnumUtil.COUNTRY_OFFLINE_GD, employeeInfoBatchPO.getNationality()));
            //*是否税收协定国或地区-H列(不需要)
            //*境内是否有固定居住场所-I列(默认值“否”)
            HSSFCell cellI = row.getCell(8);
            if (null == cellI) {
                cellI = row.createCell(8);
            }
            cellI.setCellValue("否");
            //*职务-J列(根据“境内职务”匹配代码)domestic_duty
            HSSFCell cellJ = row.getCell(9);
            if (null == cellJ) {
                cellJ = row.createCell(9);
            }
            cellJ.setCellValue(EnumUtil.getMessage(EnumUtil.POST_OFFLINE_GD, employeeInfoBatchPO.getDomesticDuty()));
            //*在华居住情况-K列(默认值“_03_1年（满一年）＜＝居住天数＜＝5年（不超过5年）”)
            HSSFCell cellK = row.getCell(10);
            if (null == cellK) {
                cellK = row.createCell(10);
            }
            cellK.setCellValue("_03_1年（满一年）＜＝居住天数＜＝5年（不超过5年）");
            //本月在华工作天数-L列(不需要)
            //本月境外工作天数-M列(不需要)
            //一次离境最长天数-N列(不需要)
            //*所得项目-O列
            HSSFCell cellO = row.getCell(14);
            if (null == cellO) {
                cellO = row.createCell(14);
            }
            String incomeSubjectName = EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT_OFFLINE_GD, taskSubDeclareDetailPO.getIncomeSubject());
            cellO.setCellValue(incomeSubjectName);
            //所得子目-P列(当所得项目为“_0101_正常工资薪金”时，默认值“_010199_正常工资薪金”，其余为空)
            HSSFCell cellP = row.getCell(15);
            if (null == cellP) {
                cellP = row.createCell(15);
            }
            cellP.setCellValue("_0101_正常工资薪金".equals(incomeSubjectName) ? "_010199_正常工资薪金":"");
            //*所得期间起-Q列(个税期间起始日（如20160701）)
            HSSFCell cellQ = row.getCell(16);
            if (null == cellQ) {
                cellQ = row.createCell(16);
            }
            String periodStartStr = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(taskSubDeclareDetailPO.getPeriod());
            cellQ.setCellValue(periodStartStr);
            //*所得期间止-R列(个税期间结束日（如20160731）)
            HSSFCell cellR = row.getCell(17);
            if (null == cellR) {
                cellR = row.createCell(17);
            }
            cellR.setCellValue(DateTimeKit.getLastDayForMonth(periodStartStr));
            //*收入所属期起-S列(正常薪金：个税期间起始日（如20160701）,年度奖金：年终奖年度起始日,离职补偿金：入职日期,个人股票期权行权收入：个税期间结束日倒推规定月份数（如20160101）)
            String incomeStartStr = "";
            String incomeEndStr = "";
            if(IncomeSubject.NORMALSALARY.getCode().equals(taskSubDeclareDetailPO.getIncomeSubject()) || IncomeSubject.FOREIGNNORMALSALARY.getCode().equals(taskSubDeclareDetailPO.getIncomeSubject())){
                incomeStartStr = periodStartStr;
                incomeEndStr = DateTimeKit.getLastDayFromStr(incomeStartStr);
            }else if(IncomeSubject.BONUSINCOMEYEAR.getCode().equals(taskSubDeclareDetailPO.getIncomeSubject())){
                incomeStartStr = DateTimeKit.getFirstDayForYear(periodStartStr);
                incomeEndStr = DateTimeKit.getLastDayForYear(periodStartStr);
            }else if(IncomeSubject.LABORCONTRACT.getCode().equals(taskSubDeclareDetailPO.getIncomeSubject())){
                incomeStartStr = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(employeeInfoBatchPO.getEntryDate());
                incomeEndStr = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(employeeInfoBatchPO.getLeaveDate());
            }else if(IncomeSubject.STOCKOPTIONINCOME.getCode().equals(taskSubDeclareDetailPO.getIncomeSubject())){
                //个人股票期权行权收入：个税期间结束日倒推规定月份数，如2018-06-30 倒推2个月就是2018-05-01
                incomeStartStr = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(taskSubDeclareDetailPO.getPeriod().plusMonths(StrKit.strToInt(taskSubDeclareDetailPO.getNumberOfMonths()) - 1));
                incomeEndStr = DateTimeKit.getLastDayForMonth(periodStartStr);
            }
            HSSFCell cellS = row.getCell(18);
            if (null == cellS) {
                cellS = row.createCell(18);
            }
            cellS.setCellValue(incomeStartStr);
            //*收入所属期止-T列(正常薪金：个税期间结束日（如20160731）,年度奖金：年终奖年度结束日,离职补偿金：离职日期,个人股票期权行权收入：个税期间结束日（如20161231）)
            HSSFCell cellT = row.getCell(19);
            if (null == cellT) {
                cellT = row.createCell(19);
            }
            cellT.setCellValue(incomeEndStr);
            //*境内企业支付-U列(不需要)
            //*境外企业支付-V列(不需要)
            //*含税收入总额-W列(不需要)
            //*境内企业支付-X列(收入额)income_total
            HSSFCell cellX = row.getCell(23);
            if (null == cellX) {
                cellX = row.createCell(23);
            }
            cellX.setCellValue(taskSubDeclareDetailPO.getIncomeTotal() == null ? "" : taskSubDeclareDetailPO.getIncomeTotal().toString());
            //*境外企业支付-Y列(默认值0)
            HSSFCell cellY = row.getCell(24);
            if (null == cellY) {
                cellY = row.createCell(24);
            }
            cellY.setCellValue(0);
            //*收入总额-Z列(不需要)
            //免税所得-AA列(当所得项目为“_0108_解除劳动合同一次性补偿金”时进行判断：,当离职金>离职金免税额时，=离职金免税额；,当离职金<离职金免税额时，=离职金)income_dutyfree
            HSSFCell cellAA = row.getCell(26);
            if (null == cellAA) {
                cellAA = row.createCell(26);
            }
            cellAA.setCellValue(taskSubDeclareDetailPO.getIncomeDutyfree() == null ? "" : taskSubDeclareDetailPO.getIncomeDutyfree().toString());
            //基本养老保险费-AB列deduct_retirement_insurance
            HSSFCell cellAB = row.getCell(27);
            if (null == cellAB) {
                cellAB = row.createCell(27);
            }
            cellAB.setCellValue(taskSubDeclareDetailPO.getDeductRetirementInsurance() == null ? "" : taskSubDeclareDetailPO.getDeductRetirementInsurance().toString());
            //基本医疗保险费-AC列deduct_medical_insurance
            HSSFCell cellAC = row.getCell(28);
            if (null == cellAC) {
                cellAC = row.createCell(28);
            }
            cellAC.setCellValue(taskSubDeclareDetailPO.getDeductMedicalInsurance() == null ? "" : taskSubDeclareDetailPO.getDeductMedicalInsurance().toString());
            //失业保险费-AD列deduct_dleness_insurance
            HSSFCell cellAD = row.getCell(29);
            if (null == cellAD) {
                cellAD = row.createCell(29);
            }
            cellAD.setCellValue(taskSubDeclareDetailPO.getDeductDlenessInsurance() == null ? "" : taskSubDeclareDetailPO.getDeductDlenessInsurance().toString());
            //住房公积金-AE列deduct_house_fund
            HSSFCell cellAE = row.getCell(30);
            if (null == cellAE) {
                cellAE = row.createCell(30);
            }
            cellAE.setCellValue(taskSubDeclareDetailPO.getDeductHouseFund() == null ? "" : taskSubDeclareDetailPO.getDeductHouseFund().toString());
            //财产原值-AF列(不需要)
            //允许扣除的税费-AG列(允许扣除的税费)deduct_takeoff
            HSSFCell cellAG = row.getCell(32);
            if (null == cellAG) {
                cellAG = row.createCell(32);
            }
            cellAG.setCellValue(taskSubDeclareDetailPO.getDeductTakeoff() == null ? "" : taskSubDeclareDetailPO.getDeductTakeoff().toString());
            //年金-AH列(if【所得项目】=“0101 正常工资薪金”then 企业年金个人部分 else 为空)annuity
            HSSFCell cellAH = row.getCell(33);
            if (null == cellAH) {
                cellAH = row.createCell(33);
            }
            cellAH.setCellValue(taskSubDeclareDetailPO.getAnnuity() == null ? "" : taskSubDeclareDetailPO.getAnnuity().toString());
            //商业健康险-AI列(if【所得项目】=“0101 正常工资薪金”then 商业保险 else 为空)business_health_insurance
            HSSFCell cellAI = row.getCell(34);
            if (null == cellAI) {
                cellAI = row.createCell(34);
            }
            cellAI.setCellValue(taskSubDeclareDetailPO.getBusinessHealthInsurance() == null ? "" : taskSubDeclareDetailPO.getBusinessHealthInsurance().toString());
            //投资抵扣额-AJ列(不需要)
            //其他扣除额-AK列(不需要)
            //合计-AL列(不需要)
            //法定减除费用额-AM列(不需要)
            //准予扣除的捐赠额-AN列(准予扣除的捐赠额)donation
            HSSFCell cellAN = row.getCell(39);
            if (null == cellAN) {
                cellAN = row.createCell(39);
            }
            cellAN.setCellValue(taskSubDeclareDetailPO.getDonation() == null ? "" : taskSubDeclareDetailPO.getDonation().toString());
            //*税款负担方式-AO列(默认值“_01_个人负担”)burden
            HSSFCell cellAO = row.getCell(40);
            if (null == cellAO) {
                cellAO = row.createCell(40);
            }
            cellAO.setCellValue(EnumUtil.getMessage(EnumUtil.BURDEN_OFFLINE_GD, employeeInfoBatchPO.getBurden()));
            //雇主负担比例-AP列(不需要)
            //雇主负担税额-AQ列(不需要)
            //原国应纳税额-AR列(不需要)
            //*应纳税所得额-AS列(不需要)
            //*税率-AT列(不需要)
            //*速算扣除数-AU列(不需要)
            //*应纳税额-AV列(不需要)
            //减免税额-AW列(减免税额（绝对值))tax_deduction
            HSSFCell cellAW = row.getCell(48);
            if (null == cellAW) {
                cellAW = row.createCell(48);
            }
            cellAW.setCellValue(taskSubDeclareDetailPO.getTaxDeduction() == null ? "" : taskSubDeclareDetailPO.getTaxDeduction().toString());
            //*应扣缴税额-AX列(不需要)
            //已扣缴税额-AY列tax_withholded_amount
            HSSFCell cellAY = row.getCell(50);
            if (null == cellAY) {
                cellAY = row.createCell(50);
            }
            cellAY.setCellValue(taskSubDeclareDetailPO.getTaxWithholdedAmount()== null ? "" : taskSubDeclareDetailPO.getTaxWithholdedAmount().toString());
            //*应补退税额-AZ列(不需要)
            //计税结果-BA列(不需要)
            //本年累计行权收入-BB列(个人股票期权行权收入：本月度累计行权收入(不含本月))exercise_income_year
            HSSFCell cellBB = row.getCell(53);
            if (null == cellBB) {
                cellBB = row.createCell(53);
            }
            cellBB.setCellValue(taskSubDeclareDetailPO.getExerciseIncomeYear()== null ? "" : taskSubDeclareDetailPO.getExerciseIncomeYear().toString());
            //本年累计已纳税额-BC列(个人股票期权行权收入：本月累计已纳税额)exercise_tax_amount
            HSSFCell cellBC = row.getCell(54);
            if (null == cellBC) {
                cellBC = row.createCell(54);
            }
            cellBC.setCellValue(taskSubDeclareDetailPO.getExerciseTaxAmount()== null ? "" : taskSubDeclareDetailPO.getExerciseTaxAmount().toString());
            //备注-BD列(不需要)
            //境内企业支付-BE列(不需要)
            //境外企业支付-BF列(不需要)
            //已申报收入额总额-BG列(不需要)
            waijiSheetRowIndex++;
        }
        //减免事项报告表（减免事项部分）sheet页
        HSSFSheet jianmianSheet = wb.getSheetAt(2);
        //刷选出减免金额大于0的项
        List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOS = taskSubDeclareDetailPOList.stream().filter(item -> item.getTaxDeduction() != null && item.getTaxDeduction().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        int jianmianSheetRowIndex = 3;
        for(TaskSubDeclareDetailPO taskSubDeclareDetailPO : taskSubDeclareDetailPOS){
            HSSFRow row = jianmianSheet.getRow(jianmianSheetRowIndex);
            if (null == row) {
                row = jianmianSheet.createRow(jianmianSheetRowIndex);
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
            cellC.setCellValue(EnumUtil.getMessage(EnumUtil.ID_TYPE_OFFLINE_GD_REDUCTION, taskSubDeclareDetailPO.getIdType()));
            //身份证件号码-D列
            HSSFCell cellD = row.getCell(3);
            if (null == cellD) {
                cellD = row.createCell(3);
            }
            cellD.setCellValue(taskSubDeclareDetailPO.getIdNo());
            //减免事项-E列 如有默认： __SXA031900042__残疾、孤老、烈属减征个人所得税__
            HSSFCell cellE = row.getCell(4);
            if (null == cellE) {
                cellE = row.createCell(4);
            }
            cellE.setCellValue("__SXA031900042__残疾、孤老、烈属减征个人所得税__");
            //减免性质-F列 默认和减免事项对应的：__0005012710__《中华人民共和国个人所得税法》 中华人民共和国主席令第48号第五条第一项__
            HSSFCell cellF = row.getCell(5);
            if (null == cellD) {
                cellF = row.createCell(5);
            }
            cellF.setCellValue("__0005012710__《中华人民共和国个人所得税法》 中华人民共和国主席令第48号第五条第一项__");
            //减免金额-G列tax_deduction
            HSSFCell cellG = row.getCell(6);
            if (null == cellG) {
                cellG = row.createCell(6);
            }
            cellG.setCellValue(taskSubDeclareDetailPO.getTaxDeduction() == null ? "" : taskSubDeclareDetailPO.getTaxDeduction().toString());
            jianmianSheetRowIndex++;
        }
        //减免事项报告表（税收协定部分）sheet页(不需要)
        //商业保险明细表 sheet页
        HSSFSheet shangyeSheet = wb.getSheetAt(4);
        //筛选出商业健康保险费大于0的详细信息business_health_insurance
        List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOSBusiness = taskSubDeclareDetailPOList.stream().filter(item -> item.getBusinessHealthInsurance() != null && item.getBusinessHealthInsurance().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        int shangyeSheetRowIndex = 3;
        for(TaskSubDeclareDetailPO taskSubDeclareDetailPO : taskSubDeclareDetailPOSBusiness){
            List<EmployeeInfoBatchPO> employeeInfoBatchPOS = employeeInfoBatchPOList.stream().filter(item -> taskSubDeclareDetailPO.getCalculationBatchDetailId().equals(item.getCalBatchDetailId())).collect(Collectors.toList());
            EmployeeInfoBatchPO employeeInfoBatchPO = new EmployeeInfoBatchPO();
            if (employeeInfoBatchPOS.size() > 0) {
                employeeInfoBatchPO = employeeInfoBatchPOS.get(0);
            }
            HSSFRow row = shangyeSheet.getRow(shangyeSheetRowIndex);
            if (null == row) {
                row = shangyeSheet.createRow(shangyeSheetRowIndex);
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
            cellC.setCellValue(EnumUtil.getMessage(EnumUtil.ID_TYPE_OFFLINE_GD_REDUCTION, taskSubDeclareDetailPO.getIdType()));
            //身份证件号码-D列
            HSSFCell cellD = row.getCell(3);
            if (null == cellD) {
                cellD = row.createCell(3);
            }
            cellD.setCellValue(taskSubDeclareDetailPO.getIdNo());
            //税优识别号-E列recognition_code
            HSSFCell cellE = row.getCell(4);
            if (null == cellE) {
                cellE = row.createCell(4);
            }
            cellE.setCellValue(employeeInfoBatchPO.getRecognitionCode());
            //保单生效日期-F列
            //年度保费-G列annual_premium
            HSSFCell cellG = row.getCell(6);
            if (null == cellG) {
                cellG = row.createCell(6);
            }
            cellG.setCellValue(employeeInfoBatchPO.getAnnualPremium() == null ? "":employeeInfoBatchPO.getAnnualPremium().toString());
            //月度保费-H列monthly_premium
            HSSFCell cellH = row.getCell(7);
            if (null == cellH) {
                cellH = row.createCell(7);
            }
            cellH.setCellValue(employeeInfoBatchPO.getMonthlyPremium() == null ? "" : employeeInfoBatchPO.getMonthlyPremium().toString());
            //本期扣除金额-I列business_health_insurance
            HSSFCell cellI = row.getCell(8);
            if (null == cellI) {
                cellI = row.createCell(8);
            }
            cellI.setCellValue(taskSubDeclareDetailPO.getBusinessHealthInsurance()== null ? "" : taskSubDeclareDetailPO.getBusinessHealthInsurance().toString());
            shangyeSheetRowIndex++;
        }
    }
}
