package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.voucher;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.BaseService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchAccountPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 完税凭证模板(三分局)
 *
 * @author yuantongqing on 2018-06-13
 */
@Service
public class ExportAboutVoucherSanFenJu extends BaseService {

    /**
     * 完税凭证三分局模板列表初始大小19
     */
    private static final int INITIAL_SFJ_SIZE = 19;

    /**
     * 获取三分局模板WB
     *
     * @param taskSubProofDetailPOList
     * @param fileName
     * @param type
     * @return
     */
    public HSSFWorkbook getSanFenJuVoucherWB(List<TaskSubProofDetailPO> taskSubProofDetailPOList, CalculationBatchAccountPO calculationBatchAccountPO, String fileName, String type) {
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //获取POIFSFileSystem对象
            fs = getFSFileSystem(fileName, type);
            //通过POIFSFileSystem对象获取WB对象
            wb = getHSSFWorkbook(fs);
            //用于存放模板列表头部
            Map<String, String> map = new HashMap<>(16);
            //扣缴义务人名称
            map.put("withholdingAgent", calculationBatchAccountPO.getAccountName());
            //扣缴义务人代码(税务电脑编码)
            map.put("withholdingAgentCode", calculationBatchAccountPO.getAccountNumber());
            // TODO 扣缴义务人电话
            map.put("withholdingAgentPhone", "18201880000");
            // TODO 换开人姓名
            map.put("changePersonName", "admin");
            // TODO 换开人身份证号码
            map.put("changePersonIdNo", "321281199001011234");
            //根据不同的业务需要处理wb
            this.handleSanFenJuVoucherWB(wb, map, taskSubProofDetailPOList);
        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    Map<String, String> tags = new HashMap<>(16);
                    //日志工具类返回
                    LogTaskFactory.getLogger().error(ee, "ExportAboutVoucherSanFenJu.getSanFenJuVoucherWB", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.OTHER, tags);
                }
            }
            throw e;
        }
        return wb;
    }

    /**
     * 处理三分局模板WB
     *
     * @param wb
     * @param map
     * @param taskSubProofDetailPOList
     */
    public void handleSanFenJuVoucherWB(HSSFWorkbook wb, Map<String, String> map, List<TaskSubProofDetailPO> taskSubProofDetailPOList) {
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
}
