package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.ExportFileService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubDeclareService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubProofService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.offline.gd.ExportAboutDeclarationInformationGd;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.offline.sh.ExportAboutWithholdingReportSh;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.offline.sz.ExportAboutDeclarationInformationSz;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.online.*;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.online.js.ExportAboutForeignNormalSalaryJs;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.online.js.ExportAboutPersonInfoJs;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.online.sh.ExportAboutCommercialInsuranceSh;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.online.sh.ExportAboutForeignNormalSalarySh;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.online.sh.ExportAboutReductionExemptionSh;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.online.sz.*;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TemplateFileBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.EmployeeInfoBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclarePO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.Deflater;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author wuhua
 */
@Service
public class ExportFileServiceImpl extends BaseService implements ExportFileService {

    private static final Logger logger = LoggerFactory.getLogger(ExportFileServiceImpl.class);

    @Autowired
    public TaskSubDeclareService taskSubDeclareService;

    @Autowired
    public TaskSubDeclareDetailServiceImpl taskSubDeclareDetailService;

    @Autowired
    private TaskSubProofService taskSubProofService;

    @Autowired
    private EmployeeInfoBatchImpl employeeInfoBatchImpl;

    @Autowired
    private ExportAboutReductionExemptionSh exportAboutReductionExemptionSh;

    @Autowired
    private ExportAboutLaborRescission exportAboutLaborRescission;

    @Autowired
    private ExportAboutLaborIncome exportAboutLaborIncome;

    @Autowired
    private ExportAboutIDDIncome exportAboutIDDIncome;

    @Autowired
    private ExportAboutFortuitousIncome exportAboutFortuitousIncome;

    @Autowired
    private ExportAboutBonusIncomeYear exportAboutBonusIncomeYear;

    @Autowired
    private ExportAboutPersonInfo exportAboutPersonInfo;

    @Autowired
    private ExportAboutCommercialInsuranceSh exportAboutCommercialInsuranceSh;

    @Autowired
    private ExportAboutForeignNormalSalarySh exportAboutForeignNormalSalarySh;

    @Autowired
    private ExportAboutNormalSalary exportAboutNormalSalary;

    @Autowired
    private ExportAboutStockOption exportAboutStockOption;

    @Autowired
    private ExportAboutStockAppreciation exportAboutStockAppreciation;

    @Autowired
    private ExportAboutRestrictiveStock exportAboutRestrictiveStock;

    @Autowired
    private ExportAboutForeignNormalSalaryJs exportAboutForeignNormalSalaryJs;

    @Autowired
    private ExportAboutLaborRescissionSz exportAboutLaborRescissionSz;

    @Autowired
    private ExportAboutLaborIncomeSz exportAboutLaborIncomeSz;

    @Autowired
    private ExportAboutBonusIncomeYearSz exportAboutBonusIncomeYearSz;

    @Autowired
    private ExportAboutForeignNormalSalarySz exportAboutForeignNormalSalarySz;

    @Autowired
    private ExportAboutNormalSalarySz exportAboutNormalSalarySz;

    @Autowired
    private ExportAboutStockOptionSz exportAboutStockOptionSz;

    @Autowired
    private ExportAboutStockAppreciationSz exportAboutStockAppreciationSz;

    @Autowired
    private ExportAboutRestrictiveStockSz exportAboutRestrictiveStockSz;

    @Autowired
    private ExportAboutPersonInfoJs exportAboutPersonInfoJs;

    @Autowired
    private ExportAboutWithholdingReportSh exportAboutWithholdingReport;

    @Autowired
    private ExportAboutDeclarationInformationGd exportAboutDeclarationInformationGd;

    @Autowired
    private ExportAboutDeclarationInformationSz exportAboutDeclarationInformationSz;

    @Autowired
    private ExportAboutIDDIncomeSz exportAboutIDDIncomeSz;

    @Autowired
    private ExportAboutFortuitousIncomeSz exportAboutFortuitousIncomeSz;

    /**
     * 完税凭证徐汇模板列表初始大小20
     */
    private static final int INITIAL_XH_SIZE = 20;

    /**
     * 完税凭证三分局模板列表初始大小19
     */
    private static final int INITIAL_SFJ_SIZE = 19;

    /**
     * 完税凭证浦东模板列表初始大小18*2
     */
    private static final int INITIAL_PD_SIZE = 36;

    /**
     * 所得项目_正常工资薪金收入
     */
    private static final String INCOME_SUBJECT_01 = "01";

    /**
     * 所得项目_外籍人员正常工资薪金
     */
    private static final String INCOME_SUBJECT_02 = "02";

    /**
     * 所得项目_劳务报酬所得
     */
    private static final String INCOME_SUBJECT_03 = "03";


    @Override
    public Map<String,Object> exportForDeclareOffline(Long subDeclareId) {
        Map<String,Object> map = new HashMap<>();
        HSSFWorkbook wb = null;
        try {
            //根据申报子任务ID查询申报信息
            TaskSubDeclarePO taskSubDeclarePO = taskSubDeclareService.queryTaskSubDeclaresById(subDeclareId);
            //根据申报子任务ID查询申报明细
            List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList = taskSubDeclareDetailService.querySubDeclareDetailList(subDeclareId);
            //文件名称
            String fileName = "";
            //00-本地,01-异地
            if ("00".equals(taskSubDeclarePO.getAreaType())) {
                fileName = "扣缴个人所得税报告表.xls";
                wb = exportAboutWithholdingReport.getWithholdingReportWB(taskSubDeclarePO,taskSubDeclareDetailPOList,fileName,"voucher");
            } else {
                // TODO 异地分地区 深圳网页版,广东申报信息 判断城市还没确定
                //申报明细计算批次明细ID
                List<Long> taskSubDeclareDetailIdList = taskSubDeclareDetailPOList.stream().map(TaskSubDeclareDetailPO::getCalculationBatchDetailId).collect(Collectors.toList());
                //获取雇员个税信息
                EntityWrapper wrapper = new EntityWrapper();
                wrapper.setEntity(new EmployeeInfoBatchPO());
                wrapper.in("cal_batch_detail_id", taskSubDeclareDetailIdList);
                List<EmployeeInfoBatchPO> employeeInfoBatchPOList = employeeInfoBatchImpl.selectList(wrapper);
                // TODO 临时定义城市编号
                String cityCode = "110";
                if("110".equals(cityCode)){
                    fileName = "深圳网页版.xls";
                    wb = exportAboutDeclarationInformationSz.getDeclarationInformationWB(taskSubDeclareDetailPOList,employeeInfoBatchPOList,fileName,"sz");
                }else if("111".equals(cityCode)){
                    fileName = "申报信息模板.xls";
                    wb = exportAboutDeclarationInformationGd.getDeclarationInformationWB(taskSubDeclareDetailPOList,employeeInfoBatchPOList,fileName,"gd");
                }
            }
            map.put("fileName",fileName);
        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    Map<String, String> tags = new HashMap<>(16);
                    //日志工具类返回
                    LogTaskFactory.getLogger().error(ee, "exportSubDeclare fs close error", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.OTHER, tags);
                }
            }
            throw e;
        }
        map.put("wb",wb);
        return map;
    }

    @Override
    public HSSFWorkbook exportForDeclareOnline(Long subDeclareId) {

        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;

        try {
            //根据申报子任务ID查询申报信息
            TaskSubDeclarePO taskSubDeclarePO = taskSubDeclareService.queryTaskSubDeclaresById(subDeclareId);
            //根据申报子任务ID查询申报明细
            List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList = taskSubDeclareDetailService.querySubDeclareDetailList(subDeclareId);
            //文件名称
            String fileName = "";
            //TODO 异地模板还没有
            //00-本地,01-异地
            if ("00".equals(taskSubDeclarePO.getAreaType())) {
                fileName = "上海地区个税.xls";
                //获取POIFSFileSystem对象
                fs = getFSFileSystem(fileName, "voucher");
                //通过POIFSFileSystem对象获取WB对象
                wb = getHSSFWorkbook(fs);
                //根据不同的业务需要处理wb
                this.exportAboutSubject(wb, taskSubDeclareDetailPOList);
            } else {
                fileName = "上海地区个税.xls";
                //获取POIFSFileSystem对象
                fs = getFSFileSystem(fileName, "voucher");
                //通过POIFSFileSystem对象获取WB对象
                wb = getHSSFWorkbook(fs);
                //根据不同的业务需要处理wb
                this.exportAboutSubject(wb, taskSubDeclareDetailPOList);
            }
        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    logger.error("exportDeclareBySubject fs close error", ee);
                }
            }
            throw e;
        }
        return wb;
    }

    /**
     * 完税凭证导出
     */
    @Override
    public HSSFWorkbook exportForProof(Long subProofId) {

        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;

        try {
            //根据完税凭证子任务查询任务信息
            TaskSubProofBO taskSubProofBO = taskSubProofService.queryApplyDetailsBySubId(subProofId);
            //根据完税凭证子任务ID查询完税凭证详情
            List<TaskSubProofDetailPO> taskSubProofDetailPOList = taskSubProofService.querySubProofDetailList(subProofId);
            //文件名称
            String fileName = "";
            //用于存放模板列表头部
            Map<String, String> map = new HashMap<>(16);
            // TODO 测试代码："蓝天科技上海独立户"=>"完税凭证_三分局","中智上海财务咨询公司大库"=>"完税凭证_徐汇","蓝天科技无锡独立户"=>"完税凭证_浦东"
            //根据申报账户选择模板
            if ("联想独立户".equals(taskSubProofBO.getDeclareAccount())) {
                fileName = "完税凭证_三分局.xls";
                //获取POIFSFileSystem对象
                fs = getFSFileSystem(fileName, "voucher");
                //通过POIFSFileSystem对象获取WB对象
                wb = getHSSFWorkbook(fs);
                //扣缴义务人名称
                map.put("withholdingAgent", "上海中智");
                //扣缴义务人代码(税务电脑编码)
                map.put("withholdingAgentCode", "BM123456789");
                //扣缴义务人电话
                map.put("withholdingAgentPhone", "18201880000");
                //换开人姓名
                map.put("changePersonName", "admin");
                //换开人身份证号码
                map.put("changePersonIdNo", "321281199001011234");
                //根据不同的业务需要处理wb
                this.exportAboutSFJ(wb, map, taskSubProofDetailPOList);
            } else if ("西门子独立户".equals(taskSubProofBO.getDeclareAccount())) {
                fileName = "完税凭证_徐汇.xls";
                //获取POIFSFileSystem对象
                fs = getFSFileSystem(fileName, "voucher");
                //通过POIFSFileSystem对象获取WB对象
                wb = getHSSFWorkbook(fs);
                //单位税号（必填）
                map.put("unitNumber", "TEST123456");
                //单位名称（必填）
                map.put("unitName", "上海中智");
                //根据不同的业务需要处理wb
                this.exportAboutXH(wb, map, taskSubProofDetailPOList);
            } else if ("蓝天科技独立户".equals(taskSubProofBO.getDeclareAccount())) {
                fileName = "完税凭证_浦东.xls";
                //获取POIFSFileSystem对象
                fs = getFSFileSystem(fileName, "voucher");
                //通过POIFSFileSystem对象获取WB对象
                wb = getHSSFWorkbook(fs);
                //扣缴单位
                map.put("withholdingUnit", "上海中智");
                //电脑编码
                map.put("withholdingCode", "123456789");
                //通用缴款书流水号
                map.put("generalPaymentBook", "147258369");
                //办税人员
                map.put("taxationPersonnel", "admin");
                //联系电话
                map.put("phone", "18201886666");
                //换开份数
                map.put("changeNum", "2");
                //换开原因
                map.put("changeReason", "重新申报");
                //根据不同的业务需要处理wb
                this.exportAboutPD(wb, map, taskSubProofDetailPOList);
            } else {
                fileName = "完税凭证_浦东.xls";
                //获取POIFSFileSystem对象
                fs = getFSFileSystem(fileName, "voucher");
                //通过POIFSFileSystem对象获取WB对象
                wb = getHSSFWorkbook(fs);
                //扣缴单位
                map.put("withholdingUnit", "上海中智");
                //电脑编码
                map.put("withholdingCode", "123456789");
                //通用缴款书流水号
                map.put("generalPaymentBook", "147258369");
                //办税人员
                map.put("taxationPersonnel", "admin");
                //联系电话
                map.put("phone", "18201886666");
                //换开份数
                map.put("changeNum", "2");
                //换开原因
                map.put("changeReason", "重新申报");
                //根据不同的业务需要处理wb
                this.exportAboutPD(wb, map, taskSubProofDetailPOList);
            }
        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    logger.error("exportSubTaskProof fs close error", ee);
                }
            }
            throw e;
        }

        return wb;
    }

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
            super.insertRow(sheet, 8, rows);
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
            super.insertRow(sheet, 14, rows);
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
            super.insertRow(sheet, 8, rows);
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
                if (incomeStart.equals(incomeEnd) || "".equals(incomeEnd)) {
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
                if (incomeStart.equals(incomeEnd) || "".equals(incomeEnd)) {
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
     * 申报导出报税数据(上海地区个税表)
     *
     * @param wb
     * @param taskSubDeclareDetailPOList
     */
    @Override
    public void exportAboutSubject(HSSFWorkbook wb, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList) {
        Map<String, List<TaskSubDeclareDetailPO>> taskSubDeclareDetailPOMap =
                taskSubDeclareDetailPOList.stream().collect(groupingBy(TaskSubDeclareDetailPO::getIncomeSubject));
        //页签一 "正常工资薪金收入"
        if (taskSubDeclareDetailPOMap.containsKey(INCOME_SUBJECT_01)) {
            //在相应的单元格进行赋值
            int sheet1RowIndex = 1;
            HSSFSheet sheet1 = wb.getSheetAt(0);
            for (TaskSubDeclareDetailPO po : taskSubDeclareDetailPOMap.get(INCOME_SUBJECT_01)) {
                HSSFRow row = sheet1.getRow(sheet1RowIndex);
                if (null == row) {
                    row = sheet1.createRow(sheet1RowIndex);
                }
                //工号-A列
                HSSFCell cellA = row.getCell(0);
                if (null == cellA) {
                    cellA = row.createCell(0);
                }
                cellA.setCellValue(po.getEmployeeNo());
                //姓名-B列
                HSSFCell cellB = row.getCell(1);
                if (null == cellB) {
                    cellB = row.createCell(1);
                }
                cellB.setCellValue(po.getEmployeeName());
                //*证照类型-C列
                HSSFCell cellC = row.getCell(2);
                if (null == cellC) {
                    cellC = row.createCell(2);
                }
                cellC.setCellValue(EnumUtil.getMessage(EnumUtil.IT_TYPE, po.getIdType()));
                //*证照号码-D列
                HSSFCell cellD = row.getCell(3);
                if (null == cellD) {
                    cellD = row.createCell(3);
                }
                cellD.setCellValue(po.getIdNo());
                //税款负担方式-E列

                //*收入额-F列
                HSSFCell cellF = row.getCell(5);
                if (null == cellF) {
                    cellF = row.createCell(5);
                }
                cellF.setCellValue(po.getIncomeTotal() == null ? "" : po.getIncomeTotal().toString());
                //免税所得-G列
                HSSFCell cellG = row.getCell(6);
                if (null == cellG) {
                    cellG = row.createCell(6);
                }
                cellG.setCellValue(po.getIncomeDutyfree() == null ? "" : po.getIncomeDutyfree().toString());
                //基本养老保险费-H列
                HSSFCell cellH = row.getCell(7);
                if (null == cellH) {
                    cellH = row.createCell(7);
                }
                cellH.setCellValue(po.getDeductRetirementInsurance() == null ? "" : po.getDeductRetirementInsurance().toString());
                //基本医疗保险费-I列
                HSSFCell cellI = row.getCell(8);
                if (null == cellI) {
                    cellI = row.createCell(8);
                }
                cellI.setCellValue(po.getDeductMedicalInsurance() == null ? "" : po.getDeductMedicalInsurance().toString());
                //失业保险费-J列
                HSSFCell cellJ = row.getCell(9);
                if (null == cellJ) {
                    cellJ = row.createCell(9);
                }
                cellJ.setCellValue(po.getDeductDlenessInsurance() == null ? "" : po.getDeductDlenessInsurance().toString());
                //住房公积金-K列
                HSSFCell cellK = row.getCell(10);
                if (null == cellK) {
                    cellK = row.createCell(10);
                }
                cellK.setCellValue(po.getDeductHouseFund() == null ? "" : po.getDeductHouseFund().toString());
                //允许扣除的税费-L列
                HSSFCell cellL = row.getCell(11);
                if (null == cellL) {
                    cellL = row.createCell(11);
                }
                cellL.setCellValue(po.getDeductTakeoff() == null ? "" : po.getDeductTakeoff().toString());
                //年金-M列
                //商业健康保险费-N列
                //其他扣除-O列
                HSSFCell cellO = row.getCell(14);
                if (null == cellO) {
                    cellO = row.createCell(14);
                }
                cellO.setCellValue(po.getDeductOther() == null ? "" : po.getDeductOther().toString());
                //减除费用-P列
                HSSFCell cellP = row.getCell(15);
                if (null == cellP) {
                    cellP = row.createCell(15);
                }
                cellP.setCellValue(po.getDeduction() == null ? "" : po.getDeduction().toString());
                //实际捐赠额-Q列
                //允许列支的捐赠比例-R列
                //准予扣除的捐赠额-S列
                HSSFCell cellS = row.getCell(18);
                if (null == cellS) {
                    cellS = row.createCell(18);
                }
                cellS.setCellValue(po.getDonation() == null ? "" : po.getDonation().toString());
                //减免税额-T列
                HSSFCell cellT = row.getCell(19);
                if (null == cellT) {
                    cellT = row.createCell(19);
                }
                cellT.setCellValue(po.getTaxDeduction() == null ? "" : po.getTaxDeduction().toString());
                //已扣缴税额-U列
                HSSFCell cellU = row.getCell(20);
                if (null == cellU) {
                    cellU = row.createCell(20);
                }
                cellU.setCellValue(po.getTaxWithholdedAmount() == null ? "" : po.getTaxWithholdedAmount().toString());
                //备注-V列
                sheet1RowIndex++;
            }
        }
        //页签二 "外籍人员正常工资薪金"
        if (taskSubDeclareDetailPOMap.containsKey(INCOME_SUBJECT_02)) {
            //在相应的单元格进行赋值
            int sheet2RowIndex = 1;
            HSSFSheet sheet2 = wb.getSheetAt(1);
            for (TaskSubDeclareDetailPO po : taskSubDeclareDetailPOMap.get(INCOME_SUBJECT_02)) {
                HSSFRow row = sheet2.getRow(sheet2RowIndex);
                if (null == row) {
                    row = sheet2.createRow(sheet2RowIndex);
                }
                //工号-A列
                HSSFCell cellA = row.getCell(0);
                if (null == cellA) {
                    cellA = row.createCell(0);
                }
                cellA.setCellValue(po.getEmployeeNo());
                //姓名-B列
                HSSFCell cellB = row.getCell(1);
                if (null == cellB) {
                    cellB = row.createCell(1);
                }
                cellB.setCellValue(po.getEmployeeName());
                //*证照类型-C列
                HSSFCell cellC = row.getCell(2);
                if (null == cellC) {
                    cellC = row.createCell(2);
                }
                cellC.setCellValue(EnumUtil.getMessage(EnumUtil.IT_TYPE, po.getIdType()));
                //*证照号码-D列
                HSSFCell cellD = row.getCell(3);
                if (null == cellD) {
                    cellD = row.createCell(3);
                }
                cellD.setCellValue(po.getIdNo());
                //*适用公式-E列
                //境内天数-F列
                //境外天数-G列
                //境内所得境内支付-H列
                //境内所得境外支付-I列
                //境外所得境内支付-J列
                //境外所得境外支付-K列
                //基本养老保险费-L列
                HSSFCell cellL = row.getCell(11);
                if (null == cellL) {
                    cellL = row.createCell(11);
                }
                cellL.setCellValue(po.getDeductRetirementInsurance() == null ? "" : po.getDeductRetirementInsurance().toString());
                //基本医疗保险费-M列
                HSSFCell cellM = row.getCell(12);
                if (null == cellM) {
                    cellM = row.createCell(12);
                }
                cellM.setCellValue(po.getDeductMedicalInsurance() == null ? "" : po.getDeductMedicalInsurance().toString());
                //失业保险费-N列
                HSSFCell cellN = row.getCell(13);
                if (null == cellN) {
                    cellN = row.createCell(13);
                }
                cellN.setCellValue(po.getDeductDlenessInsurance() == null ? "" : po.getDeductDlenessInsurance().toString());
                //住房公积金-O列
                HSSFCell cellO = row.getCell(14);
                if (null == cellO) {
                    cellO = row.createCell(14);
                }
                cellO.setCellValue(po.getDeductHouseFund() == null ? "" : po.getDeductHouseFund().toString());
                //允许扣除的税费-P列
                HSSFCell cellP = row.getCell(15);
                if (null == cellP) {
                    cellP = row.createCell(15);
                }
                cellP.setCellValue(po.getDeductTakeoff() == null ? "" : po.getDeductTakeoff().toString());
                //年金-Q列
                //商业健康保险费-R列
                //其他>其他扣除-S列
                //住房补贴-T列
                //伙食补贴-U列
                //洗衣费-V列
                //搬迁费-W列
                //出差补贴-X列
                //探亲费-Y列
                //语言培训费-Z列
                //子女教育经费-AA列
                //免税所得>其他费用-AB列
                //实际捐赠额-AC列
                //允许列支的捐赠比例-AD列
                //准予扣除的捐赠额-AE列
                HSSFCell cellAE = row.getCell(30);
                if (null == cellAE) {
                    cellAE = row.createCell(30);
                }
                cellAE.setCellValue(po.getDonation() == null ? "" : po.getDonation().toString());
                //减免税额-AF列
                HSSFCell cellAF = row.getCell(31);
                if (null == cellAF) {
                    cellAF = row.createCell(31);
                }
                cellAF.setCellValue(po.getTaxDeduction() == null ? "" : po.getTaxDeduction().toString());
                //备注-AG列
                sheet2RowIndex++;
            }
        }
        //页签三 "劳务报酬所得"
        if (taskSubDeclareDetailPOMap.containsKey(INCOME_SUBJECT_03)) {
            //在相应的单元格进行赋值
            int sheet3RowIndex = 1;
            HSSFSheet sheet3 = wb.getSheetAt(2);
            logger.debug("03 size = " + taskSubDeclareDetailPOMap.get(INCOME_SUBJECT_03).size());
            for (TaskSubDeclareDetailPO po : taskSubDeclareDetailPOMap.get(INCOME_SUBJECT_03)) {
                HSSFRow row = sheet3.getRow(sheet3RowIndex);
                if (null == row) {
                    row = sheet3.createRow(sheet3RowIndex);
                }
                //工号-A列
                HSSFCell cellA = row.getCell(0);
                if (null == cellA) {
                    cellA = row.createCell(0);
                }
                cellA.setCellValue(po.getEmployeeNo());
                //姓名-B列
                HSSFCell cellB = row.getCell(1);
                if (null == cellB) {
                    cellB = row.createCell(1);
                }
                cellB.setCellValue(po.getEmployeeName());
                //*证照类型-C列
                HSSFCell cellC = row.getCell(2);
                if (null == cellC) {
                    cellC = row.createCell(2);
                }
                cellC.setCellValue(EnumUtil.getMessage(EnumUtil.IT_TYPE, po.getIdType()));
                //*证照号码-D列
                HSSFCell cellD = row.getCell(3);
                if (null == cellD) {
                    cellD = row.createCell(3);
                }
                cellD.setCellValue(po.getIdNo());
                //税款负担方式-E列
                //*收入额-F列
                HSSFCell cellF = row.getCell(5);
                if (null == cellF) {
                    cellF = row.createCell(5);
                }
                cellF.setCellValue(po.getIncomeTotal() == null ? "" : po.getIncomeTotal().toString());
                //免税所得-G列
                HSSFCell cellG = row.getCell(6);
                if (null == cellG) {
                    cellG = row.createCell(6);
                }
                cellG.setCellValue(po.getIncomeDutyfree() == null ? "" : po.getIncomeDutyfree().toString());
                //基本养老保险费-H列
                HSSFCell cellH = row.getCell(7);
                if (null == cellH) {
                    cellH = row.createCell(7);
                }
                cellH.setCellValue(po.getDeductRetirementInsurance() == null ? "" : po.getDeductRetirementInsurance().toString());
                //基本医疗保险费-I列
                HSSFCell cellI = row.getCell(8);
                if (null == cellI) {
                    cellI = row.createCell(8);
                }
                cellI.setCellValue(po.getDeductMedicalInsurance() == null ? "" : po.getDeductMedicalInsurance().toString());
                //失业保险费-J列
                HSSFCell cellJ = row.getCell(9);
                if (null == cellJ) {
                    cellJ = row.createCell(9);
                }
                cellJ.setCellValue(po.getDeductDlenessInsurance() == null ? "" : po.getDeductDlenessInsurance().toString());
                //住房公积金-K列
                HSSFCell cellK = row.getCell(10);
                if (null == cellK) {
                    cellK = row.createCell(10);
                }
                cellK.setCellValue(po.getDeductHouseFund() == null ? "" : po.getDeductHouseFund().toString());
                //允许扣除的税费-L列
                HSSFCell cellL = row.getCell(11);
                if (null == cellL) {
                    cellL = row.createCell(11);
                }
                cellL.setCellValue(po.getDeductTakeoff() == null ? "" : po.getDeductTakeoff().toString());
                //商业健康保险费-M列
                //其他扣除-N列
                //实际捐赠额-O列
                //允许列支的捐赠比例-P列
                //准予扣除的捐赠额-Q列
                HSSFCell cellQ = row.getCell(16);
                if (null == cellQ) {
                    cellQ = row.createCell(16);
                }
                cellQ.setCellValue(po.getDonation() == null ? "" : po.getDonation().toString());
                //减免税额-R列
                HSSFCell cellR = row.getCell(17);
                if (null == cellR) {
                    cellR = row.createCell(17);
                }
                cellR.setCellValue(po.getTaxDeduction() == null ? "" : po.getTaxDeduction().toString());
                //备注-S列
                sheet3RowIndex++;
            }
        }
    }

    /**
     * 导出离职人员信息
     *
     * @param subDeclareId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public HSSFWorkbook exportQuitPerson(Long subDeclareId) {
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //根据申报ID查询申报信息
            TaskSubDeclarePO taskSubDeclarePO = taskSubDeclareService.queryTaskSubDeclaresById(subDeclareId);
            //根据申报子任务ID查询申报明细
            List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOListCurrent = taskSubDeclareDetailService.querySubDeclareDetailList(subDeclareId);
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubDeclareDetailPO());
            //申报账户
            wrapper.and("declare_account = {0}", taskSubDeclarePO.getDeclareAccount());
            //个税期间(上个月)
            wrapper.and("period = {0} ", taskSubDeclarePO.getPeriod().minusMonths(1));
            //根据申报账户和个税期间，查询出上个月的申报人员
            List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOListLast = taskSubDeclareDetailService.selectList(wrapper);
            //筛选出离职人员(即上个月有的申报人员，申报ID查询出来的结果没有的)
            List<TaskSubDeclareDetailPO> quitTaskSubDeclareDetailList = taskSubDeclareDetailPOListLast.stream().filter(item -> !taskSubDeclareDetailPOListCurrent.contains(item)).collect(Collectors.toList());
            //申报明细计算批次明细ID
            List<Long> taskSubDeclareDetailIdList = quitTaskSubDeclareDetailList.stream().map(TaskSubDeclareDetailPO::getCalculationBatchDetailId).collect(Collectors.toList());
            //获取雇员个税信息
            EntityWrapper wrapperEmployee = new EntityWrapper();
            wrapperEmployee.setEntity(new EmployeeInfoBatchPO());
            wrapperEmployee.in("cal_batch_detail_id", taskSubDeclareDetailIdList);
            //查询出离职人员信息集合
            List<EmployeeInfoBatchPO> employeeInfoBatchPOList = employeeInfoBatchImpl.selectList(wrapperEmployee);
            //文件名称
            String fileName = "";
            //TODO 独立户和大库
            //00-独立户,01-独立户
            if ("00".equals(taskSubDeclarePO.getAccountType())) {
                fileName = "人员信息-离职.xls";
                //获取POIFSFileSystem对象
                fs = getFSFileSystem(fileName, "voucher");
                //通过POIFSFileSystem对象获取WB对象
                wb = getHSSFWorkbook(fs);
                //根据不同的业务需要处理wb
                this.exportAboutQuit(wb, quitTaskSubDeclareDetailList, employeeInfoBatchPOList);
            } else {
                fileName = "人员信息-离职.xls";
                //获取POIFSFileSystem对象
                fs = getFSFileSystem(fileName, "voucher");
                //通过POIFSFileSystem对象获取WB对象
                wb = getHSSFWorkbook(fs);
                //根据不同的业务需要处理wb
                this.exportAboutQuit(wb, quitTaskSubDeclareDetailList, employeeInfoBatchPOList);
            }

        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    logger.error("exportQuitPerson fs close error", ee);
                }
            }
            throw e;
        }
        return wb;
    }

    /**
     * 导出离职人员信息
     *
     * @param wb
     * @param taskSubDeclareDetailPOList
     */
    public void exportAboutQuit(HSSFWorkbook wb, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList) {
        // 读取了模板内所有sheet内容
        HSSFSheet sheet = wb.getSheetAt(0);
        //在相应的单元格进行赋值
        int sheetRowIndex = 1;
        for (TaskSubDeclareDetailPO po : taskSubDeclareDetailPOList) {
            List<EmployeeInfoBatchPO> employeeInfoBatchPOS = employeeInfoBatchPOList.stream().filter(item -> po.getCalculationBatchDetailId().equals(item.getCalBatchDetailId())).collect(Collectors.toList());
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
            //*姓名-B列
            HSSFCell cellB = row.getCell(1);
            if (null == cellB) {
                cellB = row.createCell(1);
            }
            cellB.setCellValue(po.getEmployeeName());
            //*证照类型-C列
            HSSFCell cellC = row.getCell(2);
            if (null == cellC) {
                cellC = row.createCell(2);
            }
            cellC.setCellValue("11111");
            System.out.println("222222222222222222111111111");
            //*证照号码-D列
            HSSFCell cellD = row.getCell(3);
            if (null == cellD) {
                cellD = row.createCell(3);
            }
            cellD.setCellValue(po.getIdNo());
            //*国籍(地区)-E列
            HSSFCell cellE = row.getCell(4);
            if (null == cellE) {
                cellE = row.createCell(4);
            }
            cellE.setCellValue(employeeInfoBatchPO.getNationality());
            //性别-F列
            HSSFCell cellF = row.getCell(5);
            if (null == cellF) {
                cellF = row.createCell(5);
            }
            cellF.setCellValue(EnumUtil.getMessage(EnumUtil.GENDER_TYPE, employeeInfoBatchPO.getGender()));
            //出生年月-G列
            HSSFCell cellG = row.getCell(6);
            if (null == cellG) {
                cellG = row.createCell(6);
            }
            cellG.setCellValue(employeeInfoBatchPO.getBirthday() == null ? "" : DateTimeFormatter.ofPattern("yyyy-MM-dd").format(employeeInfoBatchPO.getBirthday()));
            //*人员状态-H列
            HSSFCell cellH = row.getCell(7);
            if (null == cellH) {
                cellH = row.createCell(7);
            }
            cellH.setCellValue("正常");
            //*是否残疾烈属孤老-I列
            HSSFCell cellI = row.getCell(8);
            if (null == cellI) {
                cellI = row.createCell(8);
            }
            cellI.setCellValue(employeeInfoBatchPO.getDisability() == null ? "" : employeeInfoBatchPO.getDisability() ? "是" : "否");
            //是否特定行业-J列
            //*是否雇员-K列
            HSSFCell cellK = row.getCell(10);
            if (null == cellK) {
                cellK = row.createCell(10);
            }
            cellK.setCellValue(employeeInfoBatchPO.getEmployee() == null ? "" : employeeInfoBatchPO.getEmployee() ? "是" : "否");
            //是否股东、投资者-L列
            HSSFCell cellL = row.getCell(11);
            if (null == cellL) {
                cellL = row.createCell(11);
            }
            cellL.setCellValue(employeeInfoBatchPO.getInvestor() == null ? "" : employeeInfoBatchPO.getInvestor() ? "是" : "否");
            //是否境外人员-M列
            HSSFCell cellM = row.getCell(12);
            if (null == cellM) {
                cellM = row.createCell(12);
            }
            cellM.setCellValue(employeeInfoBatchPO.getOverseas() == null ? "" : employeeInfoBatchPO.getOverseas() ? "是" : "否");
            //个人股本（投资）额-N列
            HSSFCell cellN = row.getCell(13);
            if (null == cellN) {
                cellN = row.createCell(13);
            }
            cellN.setCellValue(employeeInfoBatchPO.getPersonalInvestment() == null ? "" : employeeInfoBatchPO.getPersonalInvestment().toString());
            //是否天使投资个人-O列
            //*联系电话-P列
            HSSFCell cellP = row.getCell(15);
            if (null == cellP) {
                cellP = row.createCell(15);
            }
            cellP.setCellValue(employeeInfoBatchPO.getMobile());
            //联系地址-Q列
            //邮政编码-R列
            //备注-S列
            //电子邮箱-T列
            //工作单位-U列
            //姓名（中文）-V列
            HSSFCell cellV = row.getCell(21);
            if (null == cellV) {
                cellV = row.createCell(21);
            }
            cellV.setCellValue(employeeInfoBatchPO.getChineseName());
            //来华时间-W列
            HSSFCell cellW = row.getCell(22);
            if (null == cellW) {
                cellW = row.createCell(22);
            }
            cellW.setCellValue(employeeInfoBatchPO.getComingToChinaDate() == null ? "" : DateTimeFormatter.ofPattern("yyyy-MM-dd").format(employeeInfoBatchPO.getComingToChinaDate()));
            //任职期限-X列
            HSSFCell cellX = row.getCell(23);
            if (null == cellX) {
                cellX = row.createCell(23);
            }
            cellX.setCellValue(employeeInfoBatchPO.getTermOfService() == null ? "" : DateTimeFormatter.ofPattern("yyyy-MM-dd").format(employeeInfoBatchPO.getTermOfService()));
            //预计离境时间-Y列
            HSSFCell cellY = row.getCell(24);
            if (null == cellY) {
                cellY = row.createCell(24);
            }
            cellY.setCellValue(employeeInfoBatchPO.getExpectedLeaveDate() == null ? "" : DateTimeFormatter.ofPattern("yyyy-MM-dd").format(employeeInfoBatchPO.getExpectedLeaveDate()));
            //预计离境地点-Z列
            HSSFCell cellZ = row.getCell(25);
            if (null == cellZ) {
                cellZ = row.createCell(25);
            }
            cellZ.setCellValue(employeeInfoBatchPO.getExpectedLeavePlace());
            //境内职务-AA
            HSSFCell cellAA = row.getCell(26);
            if (null == cellAA) {
                cellAA = row.createCell(26);
            }
            cellAA.setCellValue(employeeInfoBatchPO.getDomesticDuty());
            //境外职务-AB
            HSSFCell cellAB = row.getCell(27);
            if (null == cellAB) {
                cellAB = row.createCell(27);
            }
            cellAB.setCellValue(employeeInfoBatchPO.getOverseasDuty());
            //支付地-AC
            HSSFCell cellAC = row.getCell(28);
            if (null == cellAC) {
                cellAC = row.createCell(28);
            }
            cellAC.setCellValue(employeeInfoBatchPO.getPaymentPlace());
            //境外支付地（国别/地区-AD
            HSSFCell cellAD = row.getCell(29);
            if (null == cellAD) {
                cellAD = row.createCell(29);
            }
            cellAD.setCellValue(employeeInfoBatchPO.getPaymentOverseasPlace());
            sheetRowIndex++;
        }
    }

    /**
     * 导出线上文件
     *
     * @param subDeclareId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String,Object> getCompressedFileByte(Long subDeclareId, String tempFileName, String dateStrFolder) throws Exception {
        Map<String,Object> map = new HashMap<>();
        //根据申报ID查询申报信息
        TaskSubDeclarePO taskSubDeclarePO = taskSubDeclareService.queryTaskSubDeclaresById(subDeclareId);
        //根据申报子任务ID查询申报明细
        List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList = taskSubDeclareDetailService.querySubDeclareDetailList(subDeclareId);
        //申报明细计算批次明细ID
        List<Long> taskSubDeclareDetailIdList = taskSubDeclareDetailPOList.stream().map(TaskSubDeclareDetailPO::getCalculationBatchDetailId).collect(Collectors.toList());
        //获取雇员个税信息
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new EmployeeInfoBatchPO());
        wrapper.in("cal_batch_detail_id", taskSubDeclareDetailIdList);
        List<EmployeeInfoBatchPO> employeeInfoBatchPOList = employeeInfoBatchImpl.selectList(wrapper);
        String tempFilePath = this.getResourcePath() + dateStrFolder;
        //判断临时文件是否存在,不存在,则创建
        File fileTemp = new File(tempFilePath);
        if (!fileTemp.exists()) {
            fileTemp.mkdir();
        }
        //excel临时文件夹
        String excelFilePath = tempFilePath + File.separator + "excel";
        File excelFileTemp = new File(excelFilePath);
        if (!excelFileTemp.exists()) {
            excelFileTemp.mkdir();
        }
        //获取数据源,填充模板数据
        String zipName = generateTempAllExcelFile(taskSubDeclarePO, taskSubDeclareDetailPOList, employeeInfoBatchPOList, excelFilePath);
        //压缩文件目录
        String destFilePath = tempFilePath + File.separator + "zip";

        File destFileTemp = new File(destFilePath);
        if (!destFileTemp.exists()) {
            destFileTemp.mkdir();
        }
        String destFile = destFilePath + File.separator + tempFileName;
        //生成压缩文件
        createCompressedFile(excelFilePath, destFile);
        //返回压缩文件byte数组
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            File file = new File(destFile + ".zip");
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } finally {
            if (bos != null) {
                bos.close();
            }
            if (fis != null) {
                fis.close();
            }
            if (fileTemp.exists()) {
                deleteAll(fileTemp);
            }
        }
        map.put("zipName",zipName);
        map.put("byte",buffer);
        return map;
    }

    /**
     * 生成临时文件
     *
     * @param taskSubDeclarePO
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     * @param tempFilePath
     * @throws Exception
     */
    public String generateTempAllExcelFile(TaskSubDeclarePO taskSubDeclarePO, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList, String tempFilePath) throws Exception {
        String zipName = "";
        // TODO 根据地区生成临时文件
        List<TemplateFileBO> templateFileBOList = null;
        //00:本地,01:异地
        if("00".equals(taskSubDeclarePO.getAreaType())){
            zipName = "上海地区个税模板对应关系.zip";
            //获取上海本地根据模板生产的临时文件集合
            templateFileBOList = getTemplateFileListBySubDeclareIdAboutSH(taskSubDeclareDetailPOList, employeeInfoBatchPOList);
        }else{
//            // TODO 江苏
//            zipName = "江苏模板.zip";
//            templateFileBOList = getTemplateFileListBySubDeclareIdAboutJS(taskSubDeclareDetailPOList, employeeInfoBatchPOList);
//            // TODO 深圳金三软件
//            zipName = "深圳金三软件版.zip";
//            templateFileBOList = getTemplateFileListBySubDeclareIdAboutSZ(taskSubDeclareDetailPOList, employeeInfoBatchPOList);
        }

        try {
            for (TemplateFileBO templateFileBO : templateFileBOList) {
                //如果是文件则生成文件
                if (templateFileBO.getType()) {
                    FileOutputStream fileOut = new FileOutputStream(new File(tempFilePath + File.separator + templateFileBO.getTemplateName()));
                    templateFileBO.getWb().write(fileOut);
                    fileOut.close();
                } else {
                    File file = new File(tempFilePath + File.separator + templateFileBO.getTemplateName());
                    if (!file.exists()) {
                        file.mkdirs();//创建目录
                    }
                    for (TemplateFileBO templateFileBOSub : templateFileBO.getTemplateFileBOList()) {
                        FileOutputStream fileOut = new FileOutputStream(new File(tempFilePath + File.separator + templateFileBO.getTemplateName() + File.separator + templateFileBOSub.getTemplateName()));
                        templateFileBOSub.getWb().write(fileOut);
                        fileOut.close();
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return zipName;
    }

    /**
     * 根据申报ID获取线上文件集合(上海)
     *
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     * @return
     */
    public List<TemplateFileBO> getTemplateFileListBySubDeclareIdAboutSH(List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList) {
        List<TemplateFileBO> templateFileBOList = new ArrayList<>();

        //减免事项附表
        TemplateFileBO templateFileReductionExemption = new TemplateFileBO();
        templateFileReductionExemption.setTemplateName("减免事项附表.xls");
        templateFileReductionExemption.setWb(exportAboutReductionExemptionSh.getReductionExemptionWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "减免事项附表.xls", "sh"));
        templateFileReductionExemption.setType(true);
        templateFileBOList.add(templateFileReductionExemption);
        //解除劳动合同一次性补偿金
        TemplateFileBO templateFileLabor = new TemplateFileBO();
        templateFileLabor.setTemplateName("解除劳动合同一次性补偿金.xls");
        templateFileLabor.setWb(exportAboutLaborRescission.getLaborRescissionWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "解除劳动合同一次性补偿金.xls","sh"));
        templateFileLabor.setType(true);
        templateFileBOList.add(templateFileLabor);
        //劳务报酬所得
        TemplateFileBO templateFileLaborIncome = new TemplateFileBO();
        templateFileLaborIncome.setTemplateName("劳务报酬所得.xls");
        templateFileLaborIncome.setWb(exportAboutLaborIncome.getLaborIncomeWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "劳务报酬所得.xls","sh"));
        templateFileLaborIncome.setType(true);
        templateFileBOList.add(templateFileLaborIncome);
        //利息、股息、红利所得
        TemplateFileBO templateFileIDDIncome = new TemplateFileBO();
        templateFileIDDIncome.setTemplateName("利息、股息、红利所得.xls");
        templateFileIDDIncome.setWb(exportAboutIDDIncome.getIDDIncomeWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "利息、股息、红利所得.xls","sh"));
        templateFileIDDIncome.setType(true);
        templateFileBOList.add(templateFileIDDIncome);
        //偶然所得
        TemplateFileBO templateFileFortuitousIncome = new TemplateFileBO();
        templateFileFortuitousIncome.setTemplateName("偶然所得.xls");
        templateFileFortuitousIncome.setWb(exportAboutFortuitousIncome.getFortuitousIncomeWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "偶然所得.xls","sh"));
        templateFileFortuitousIncome.setType(true);
        templateFileBOList.add(templateFileFortuitousIncome);
        //全年一次性奖金收入
        TemplateFileBO templateFileBonusIncomeYear = new TemplateFileBO();
        templateFileBonusIncomeYear.setTemplateName("全年一次性奖金收入.xls");
        templateFileBonusIncomeYear.setWb(exportAboutBonusIncomeYear.getBonusIncomeYearWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "全年一次性奖金收入.xls","sh"));
        templateFileBonusIncomeYear.setType(true);
        templateFileBOList.add(templateFileBonusIncomeYear);
        //人员信息
        TemplateFileBO templateFilePerson = new TemplateFileBO();
        templateFilePerson.setTemplateName("人员信息.xls");
        templateFilePerson.setWb(exportAboutPersonInfo.getPersonInfoWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "人员信息.xls","sh"));
        templateFilePerson.setType(true);
        templateFileBOList.add(templateFilePerson);
        //商业保险commercial insurance
        TemplateFileBO templateFileCI = new TemplateFileBO();
        templateFileCI.setTemplateName("商业保险.xls");
        templateFileCI.setWb(exportAboutCommercialInsuranceSh.getCommercialInsuranceWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "商业保险.xls","sh"));
        templateFileCI.setType(true);
        templateFileBOList.add(templateFileCI);
        //外籍人员正常工资薪金
        TemplateFileBO templateFileForeignNormalSalary = new TemplateFileBO();
        templateFileForeignNormalSalary.setTemplateName("外籍人员正常工资薪金.xls");
        templateFileForeignNormalSalary.setWb(exportAboutForeignNormalSalarySh.getForeignNormalSalaryWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "外籍人员正常工资薪金.xls","sh"));
        templateFileForeignNormalSalary.setType(true);
        templateFileBOList.add(templateFileForeignNormalSalary);
        //正常工资薪金
        TemplateFileBO templateFileNormalSalary = new TemplateFileBO();
        templateFileNormalSalary.setTemplateName("正常工资薪金.xls");
        templateFileNormalSalary.setWb(exportAboutNormalSalary.getNormalSalaryWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "正常工资薪金.xls","sh"));
        templateFileNormalSalary.setType(true);
        templateFileBOList.add(templateFileNormalSalary);
        //个人股票期权行权收入(文件夹)Personal stock option revenue
        TemplateFileBO templateFileStock = new TemplateFileBO();
        templateFileStock.setTemplateName("个人股票期权行权收入");
        templateFileStock.setType(false);
        List<TemplateFileBO> templateFileStockSubList = new ArrayList<>();
        //个人股票期权行权收入-股票期权
        TemplateFileBO templateFileStockOption = new TemplateFileBO();
        templateFileStockOption.setTemplateName("个人股票期权行权收入-股票期权.xls");
        templateFileStockOption.setType(true);
        templateFileStockOption.setWb(exportAboutStockOption.getStockOptionWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "个人股票期权行权收入-股票期权.xls","sh"));
        templateFileStockSubList.add(templateFileStockOption);
        //个人股票期权行权收入-股票增值权
        TemplateFileBO templateFileStockAR = new TemplateFileBO();
        templateFileStockAR.setTemplateName("个人股票期权行权收入-股票增值权.xls");
        templateFileStockAR.setType(true);
        templateFileStockAR.setWb(exportAboutStockAppreciation.getStockAppreciationWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "个人股票期权行权收入-股票增值权.xls","sh"));
        templateFileStockSubList.add(templateFileStockAR);
        //个人股票期权行权收入-限制性股票
        TemplateFileBO templateFileRStock = new TemplateFileBO();
        templateFileRStock.setTemplateName("个人股票期权行权收入-限制性股票.xls");
        templateFileRStock.setType(true);
        templateFileRStock.setWb(exportAboutRestrictiveStock.getRestrictiveStockWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "个人股票期权行权收入-限制性股票.xls","sh"));
        templateFileStockSubList.add(templateFileRStock);

        templateFileStock.setTemplateFileBOList(templateFileStockSubList);
        templateFileBOList.add(templateFileStock);
        return templateFileBOList;
    }


    /**
     * 根据申报ID获取线上文件集合(江苏)
     *
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     * @return
     */
    public List<TemplateFileBO> getTemplateFileListBySubDeclareIdAboutJS(List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList){
        List<TemplateFileBO> templateFileBOList = new ArrayList<>();

        //解除劳动合同一次性补偿金
        TemplateFileBO templateFileLabor = new TemplateFileBO();
        templateFileLabor.setTemplateName("解除劳动合同一次性补偿金.xls");
        templateFileLabor.setWb(exportAboutLaborRescission.getLaborRescissionWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "解除劳动合同一次性补偿金.xls","js"));
        templateFileLabor.setType(true);
        templateFileBOList.add(templateFileLabor);

        //劳务报酬所得
        TemplateFileBO templateFileLaborIncome = new TemplateFileBO();
        templateFileLaborIncome.setTemplateName("劳务报酬所得.xls");
        templateFileLaborIncome.setWb(exportAboutLaborIncome.getLaborIncomeWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "劳务报酬所得.xls","js"));
        templateFileLaborIncome.setType(true);
        templateFileBOList.add(templateFileLaborIncome);

        //利息、股息、红利所得
        TemplateFileBO templateFileIDDIncome = new TemplateFileBO();
        templateFileIDDIncome.setTemplateName("利息、股息、红利所得.xls");
        templateFileIDDIncome.setWb(exportAboutIDDIncome.getIDDIncomeWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "利息、股息、红利所得.xls","js"));
        templateFileIDDIncome.setType(true);
        templateFileBOList.add(templateFileIDDIncome);

        //偶然所得
        TemplateFileBO templateFileFortuitousIncome = new TemplateFileBO();
        templateFileFortuitousIncome.setTemplateName("偶然所得.xls");
        templateFileFortuitousIncome.setWb(exportAboutFortuitousIncome.getFortuitousIncomeWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "偶然所得.xls","js"));
        templateFileFortuitousIncome.setType(true);
        templateFileBOList.add(templateFileFortuitousIncome);

        //全年一次性奖金收入
        TemplateFileBO templateFileBonusIncomeYear = new TemplateFileBO();
        templateFileBonusIncomeYear.setTemplateName("全年一次性奖金收入.xls");
        templateFileBonusIncomeYear.setWb(exportAboutBonusIncomeYear.getBonusIncomeYearWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "全年一次性奖金收入.xls","js"));
        templateFileBonusIncomeYear.setType(true);
        templateFileBOList.add(templateFileBonusIncomeYear);

        //人员信息
        TemplateFileBO templateFilePerson = new TemplateFileBO();
        templateFilePerson.setTemplateName("人员信息.xls");
        templateFilePerson.setWb(exportAboutPersonInfoJs.getPersonInfoWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "人员信息.xls","js"));
        templateFilePerson.setType(true);
        templateFileBOList.add(templateFilePerson);

        //外籍人员正常工资薪金
        TemplateFileBO templateFileForeignNormalSalary = new TemplateFileBO();
        templateFileForeignNormalSalary.setTemplateName("外籍人员正常工资薪金.xls");
        templateFileForeignNormalSalary.setWb(exportAboutForeignNormalSalaryJs.getForeignNormalSalaryWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "外籍人员正常工资薪金.xls","js"));
        templateFileForeignNormalSalary.setType(true);
        templateFileBOList.add(templateFileForeignNormalSalary);

        //正常工资薪金
        TemplateFileBO templateFileNormalSalary = new TemplateFileBO();
        templateFileNormalSalary.setTemplateName("正常工资薪金.xls");
        templateFileNormalSalary.setWb(exportAboutNormalSalary.getNormalSalaryWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "正常工资薪金.xls","js"));
        templateFileNormalSalary.setType(true);
        templateFileBOList.add(templateFileNormalSalary);

        //个人股票期权行权收入(文件夹)Personal stock option revenue
        TemplateFileBO templateFileStock = new TemplateFileBO();
        templateFileStock.setTemplateName("个人股票期权行权收入");
        templateFileStock.setType(false);
        List<TemplateFileBO> templateFileStockSubList = new ArrayList<>();
        //个人股票期权行权收入-股票期权
        TemplateFileBO templateFileStockOption = new TemplateFileBO();
        templateFileStockOption.setTemplateName("个人股票期权行权收入-股票期权.xls");
        templateFileStockOption.setType(true);
        templateFileStockOption.setWb(exportAboutStockOption.getStockOptionWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "个人股票期权行权收入-股票期权.xls","js"));
        templateFileStockSubList.add(templateFileStockOption);
        //个人股票期权行权收入-股票增值权
        TemplateFileBO templateFileStockAR = new TemplateFileBO();
        templateFileStockAR.setTemplateName("个人股票期权行权收入-股票增值权.xls");
        templateFileStockAR.setType(true);
        templateFileStockAR.setWb(exportAboutStockAppreciation.getStockAppreciationWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "个人股票期权行权收入-股票增值权.xls","js"));
        templateFileStockSubList.add(templateFileStockAR);
        //个人股票期权行权收入-限制性股票
        TemplateFileBO templateFileRStock = new TemplateFileBO();
        templateFileRStock.setTemplateName("个人股票期权行权收入-限制性股票.xls");
        templateFileRStock.setType(true);
        templateFileRStock.setWb(exportAboutRestrictiveStock.getRestrictiveStockWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "个人股票期权行权收入-限制性股票.xls","js"));
        templateFileStockSubList.add(templateFileRStock);

        templateFileStock.setTemplateFileBOList(templateFileStockSubList);
        templateFileBOList.add(templateFileStock);
        return templateFileBOList;
    }

    /**
     * 根据申报ID获取线上文件集合(深圳金三)
     *
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     * @return
     */
    public List<TemplateFileBO> getTemplateFileListBySubDeclareIdAboutSZ(List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList){
        List<TemplateFileBO> templateFileBOList = new ArrayList<>();

        //解除劳动合同一次性补偿金
        TemplateFileBO templateFileLabor = new TemplateFileBO();
        templateFileLabor.setTemplateName("解除劳动合同一次性补偿金.xls");
        templateFileLabor.setWb(exportAboutLaborRescissionSz.getLaborRescissionWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "解除劳动合同一次性补偿金.xls","sz"));
        templateFileLabor.setType(true);
        templateFileBOList.add(templateFileLabor);

        //劳务报酬所得
        TemplateFileBO templateFileLaborIncome = new TemplateFileBO();
        templateFileLaborIncome.setTemplateName("劳务报酬所得.xls");
        templateFileLaborIncome.setWb(exportAboutLaborIncomeSz.getLaborIncomeWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "劳务报酬所得.xls","sz"));
        templateFileLaborIncome.setType(true);
        templateFileBOList.add(templateFileLaborIncome);

        //利息、股息、红利所得
        TemplateFileBO templateFileIDDIncome = new TemplateFileBO();
        templateFileIDDIncome.setTemplateName("利息、股息、红利所得.xls");
        templateFileIDDIncome.setWb(exportAboutIDDIncomeSz.getIDDIncomeWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "利息、股息、红利所得.xls","sz"));
        templateFileIDDIncome.setType(true);
        templateFileBOList.add(templateFileIDDIncome);

        //偶然所得
        TemplateFileBO templateFileFortuitousIncome = new TemplateFileBO();
        templateFileFortuitousIncome.setTemplateName("偶然所得.xls");
        templateFileFortuitousIncome.setWb(exportAboutFortuitousIncomeSz.getFortuitousIncomeWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "偶然所得.xls","sz"));
        templateFileFortuitousIncome.setType(true);
        templateFileBOList.add(templateFileFortuitousIncome);

        //全年一次性奖金收入
        TemplateFileBO templateFileBonusIncomeYear = new TemplateFileBO();
        templateFileBonusIncomeYear.setTemplateName("全年一次性奖金收入.xls");
        templateFileBonusIncomeYear.setWb(exportAboutBonusIncomeYearSz.getBonusIncomeYearWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "全年一次性奖金收入.xls","sz"));
        templateFileBonusIncomeYear.setType(true);
        templateFileBOList.add(templateFileBonusIncomeYear);

        //人员信息
        TemplateFileBO templateFilePerson = new TemplateFileBO();
        templateFilePerson.setTemplateName("人员信息.xls");
        templateFilePerson.setWb(exportAboutPersonInfo.getPersonInfoWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "人员信息.xls","sz"));
        templateFilePerson.setType(true);
        templateFileBOList.add(templateFilePerson);

        //外籍人员正常工资薪金
        TemplateFileBO templateFileForeignNormalSalary = new TemplateFileBO();
        templateFileForeignNormalSalary.setTemplateName("外籍人员正常工资薪金.xls");
        templateFileForeignNormalSalary.setWb(exportAboutForeignNormalSalarySz.getForeignNormalSalaryWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "外籍人员正常工资薪金.xls","sz"));
        templateFileForeignNormalSalary.setType(true);
        templateFileBOList.add(templateFileForeignNormalSalary);

        //正常工资薪金
        TemplateFileBO templateFileNormalSalary = new TemplateFileBO();
        templateFileNormalSalary.setTemplateName("正常工资薪金.xls");
        templateFileNormalSalary.setWb(exportAboutNormalSalarySz.getNormalSalaryWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "正常工资薪金.xls","sz"));
        templateFileNormalSalary.setType(true);
        templateFileBOList.add(templateFileNormalSalary);

        //个人股票期权行权收入(文件夹)Personal stock option revenue
        TemplateFileBO templateFileStock = new TemplateFileBO();
        templateFileStock.setTemplateName("个人股票期权行权收入");
        templateFileStock.setType(false);
        List<TemplateFileBO> templateFileStockSubList = new ArrayList<>();
        //个人股票期权行权收入-股票期权
        TemplateFileBO templateFileStockOption = new TemplateFileBO();
        templateFileStockOption.setTemplateName("个人股票期权行权收入-股票期权.xls");
        templateFileStockOption.setType(true);
        templateFileStockOption.setWb(exportAboutStockOptionSz.getStockOptionWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "个人股票期权行权收入-股票期权.xls","sz"));
        templateFileStockSubList.add(templateFileStockOption);
        //个人股票期权行权收入-股票增值权
        TemplateFileBO templateFileStockAR = new TemplateFileBO();
        templateFileStockAR.setTemplateName("个人股票期权行权收入-股票增值权.xls");
        templateFileStockAR.setType(true);
        templateFileStockAR.setWb(exportAboutStockAppreciationSz.getStockAppreciationWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "个人股票期权行权收入-股票增值权.xls","sz"));
        templateFileStockSubList.add(templateFileStockAR);
        //个人股票期权行权收入-限制性股票
        TemplateFileBO templateFileRStock = new TemplateFileBO();
        templateFileRStock.setTemplateName("个人股票期权行权收入-限制性股票.xls");
        templateFileRStock.setType(true);
        templateFileRStock.setWb(exportAboutRestrictiveStockSz.getRestrictiveStockWB(taskSubDeclareDetailPOList, employeeInfoBatchPOList, "个人股票期权行权收入-限制性股票.xls","sz"));
        templateFileStockSubList.add(templateFileRStock);

        templateFileStock.setTemplateFileBOList(templateFileStockSubList);
        templateFileBOList.add(templateFileStock);
        return templateFileBOList;

    }

    /**
     * * 生产压缩文件
     *
     * @param resourceFilePath 压缩文件目录
     * @param destFile         压缩后文件路径
     * @throws Exception
     */
    public void createCompressedFile(String resourceFilePath, String destFile) throws Exception {
        File zipFile = new File(resourceFilePath);
        File file = new File(destFile + ".zip");
        if (file.exists()) {
            file.delete();
        }
        ZipOutputStream zipOut = null;
        try {
            logger.info("file compress begin");
            //生成ZipOutputStream，会把压缩的内容全都通过这个输出流输出，最后写到压缩文件中去
            zipOut = new ZipOutputStream(new FileOutputStream(file));
            //设置压缩的编码，如果要压缩的路径中有中文，就用下面的编码
            zipOut.setEncoding("GBK");
            //启用压缩
            zipOut.setMethod(ZipOutputStream.DEFLATED);
            //压缩级别为最强压缩，但时间要花得多一点
            zipOut.setLevel(Deflater.BEST_COMPRESSION);
            handleFile(zipFile, zipOut, "");
            logger.info("file compress end");
        } catch (Exception e) {
            throw e;
        } finally {
            //处理完成后关闭我们的输出流
            try {
                if (zipOut != null) {
                    zipOut.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 递归完成目录文件读取
     *
     * @param zipFile
     * @param zipOut
     * @param dirName 这个主要是用来记录压缩文件的一个目录层次结构的
     * @throws IOException
     */
    private void handleFile(File zipFile, ZipOutputStream zipOut, String dirName) throws IOException {
        logger.info("遍历文件：" + zipFile.getName());
        byte[] buf = new byte[4096];
        //如果是一个目录，则遍历
        if (zipFile.isDirectory()) {
            File[] files = zipFile.listFiles();
            // 如果目录为空,则单独创建之.
            if (files.length == 0) {
                //只是放入了空目录的名字
                zipOut.putNextEntry(new ZipEntry(dirName + zipFile.getName() + File.separator));
                zipOut.closeEntry();
            } else {// 如果目录不为空,则进入递归，处理下一级文件
                for (File file : files) {
                    // 进入递归，处理下一级的文件
                    handleFile(file, zipOut, dirName + zipFile.getName() + File.separator);
                }
            }
        } else {//如果是文件，则直接压缩
            FileInputStream fileIn = new FileInputStream(zipFile);
            //放入一个ZipEntry
            zipOut.putNextEntry(new ZipEntry(dirName + zipFile.getName()));
            int length = 0;
            //放入压缩文件的流
            while ((length = fileIn.read(buf)) > 0) {
                zipOut.write(buf, 0, length);
            }
            //关闭ZipEntry，完成一个文件的压缩
            fileIn.close();
            zipOut.closeEntry();
        }
    }

    /**
     * 删除文件夹下所有文件
     *
     * @param file
     */
    public static void deleteAll(File file) {
        if (file.isFile() || file.list().length == 0) {
            file.delete();
        } else {
            for (File f : file.listFiles()) {
                // 递归删除每一个文件
                deleteAll(f);
            }
            // 删除文件夹
            file.delete();
        }
    }
}
