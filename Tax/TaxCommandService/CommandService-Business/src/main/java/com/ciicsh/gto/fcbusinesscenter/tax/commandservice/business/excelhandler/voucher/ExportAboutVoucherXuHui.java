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

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 完税凭证模板(徐汇)
 *
 * @author yuantongqing on 2018-06-13
 */
@Service
public class ExportAboutVoucherXuHui extends BaseService {

    /**
     * 完税凭证徐汇模板列表初始大小20
     */
    private static final int INITIAL_XH_SIZE = 20;

    /**
     * 获取徐汇完税凭证WB
     * @param taskSubProofDetailPOList
     * @param fileName
     * @param type
     * @return
     */
    public HSSFWorkbook getXuHuiVoucherWB(List<TaskSubProofDetailPO> taskSubProofDetailPOList, CalculationBatchAccountPO calculationBatchAccountPO, String fileName, String type) {
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //获取POIFSFileSystem对象
            fs = getFSFileSystem(fileName, type);
            //通过POIFSFileSystem对象获取WB对象
            wb = getHSSFWorkbook(fs);
            // TODO 完税凭证模板(徐汇)表头信息
            //用于存放模板列表头部
            Map<String, String> map = new HashMap<>(16);
            //单位税号（必填）
            map.put("unitNumber", "TEST123456");
            //单位名称（必填）
            map.put("unitName", "上海中智");
            //根据不同的业务需要处理wb
            this.handleXuHuiVoucherWB(wb, map, taskSubProofDetailPOList);
        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    Map<String, String> tags = new HashMap<>(16);
                    //日志工具类返回
                    LogTaskFactory.getLogger().error(ee, "ExportAboutVoucherXuHui.getXuHuiVoucherWB", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.OTHER, tags);
                }
            }
            throw e;
        }
        return wb;
    }

    /**
     * 处理徐汇完税凭证WB
     * @param wb
     * @param map
     * @param taskSubProofDetailPOList
     */
    public void handleXuHuiVoucherWB(HSSFWorkbook wb, Map<String, String> map, List<TaskSubProofDetailPO> taskSubProofDetailPOList) {
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
            String incomeStart = taskSubProofDetailPO.getIncomeStart() == null ? "" : DateTimeFormatter.ofPattern("yyyy-MM").format(taskSubProofDetailPO.getIncomeStart());
            String incomeEnd = taskSubProofDetailPO.getIncomeEnd() == null ? "" : DateTimeFormatter.ofPattern("yyyy-MM").format(taskSubProofDetailPO.getIncomeEnd());
            if (incomeStart.equals(incomeEnd) || "".equals(incomeEnd)) {
                period = incomeStart;
            } else {
                period = incomeStart + "~" + incomeEnd;
            }
            cellD.setCellValue(period);
            rowIndex++;
        }
    }
}
