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
 * 完税凭证模板(浦东)
 *
 * @author yuantongqing on 2018-06-13
 */
@Service
public class ExportAboutVoucherPuDong extends BaseService {
    /**
     * 完税凭证浦东模板列表初始大小18*2
     */
    private static final int INITIAL_PD_SIZE = 36;

    /**
     * 获取浦东完税凭证模板WB
     *
     * @param taskSubProofDetailPOList
     * @param fileName
     * @param type
     * @return
     */
    public HSSFWorkbook getPuDongVoucherWB(List<TaskSubProofDetailPO> taskSubProofDetailPOList, CalculationBatchAccountPO calculationBatchAccountPO, String fileName, String type) {
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //获取POIFSFileSystem对象
            fs = getFSFileSystem(fileName, type);
            //通过POIFSFileSystem对象获取WB对象
            wb = getHSSFWorkbook(fs);
            //用于存放模板列表头部
            Map<String, String> map = new HashMap<>(16);
            //通过POIFSFileSystem对象获取WB对象
            wb = getHSSFWorkbook(fs);
            //扣缴单位
            map.put("withholdingUnit", calculationBatchAccountPO.getAccountName());
            //电脑编码
            map.put("withholdingCode", calculationBatchAccountPO.getAccountNumber());
            //通用缴款书流水号
            map.put("generalPaymentBook", calculationBatchAccountPO.getCommissionContractSerialNumber());
            // TODO 办税人员
            map.put("taxationPersonnel", "admin");
            // TODO 联系电话
            map.put("phone", "18201886666");
            // TODO 换开份数
            map.put("changeNum", "2");
            // TODO 换开原因
            map.put("changeReason", "重新申报");
            //根据不同的业务需要处理wb
            this.handlePuDongVoucherWB(wb, map, taskSubProofDetailPOList);
        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    Map<String, String> tags = new HashMap<>(16);
                    //日志工具类返回
                    LogTaskFactory.getLogger().error(ee, "ExportAboutVoucherPuDong.getPuDongVoucherWB", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.OTHER, tags);
                }
            }
            throw e;
        }
        return wb;
    }

    /**
     * 处理浦东完税凭证模板WB
     *
     * @param wb
     * @param map
     * @param taskSubProofDetailPOList
     */
    public void handlePuDongVoucherWB(HSSFWorkbook wb, Map<String, String> map, List<TaskSubProofDetailPO> taskSubProofDetailPOList) {
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
                String incomeStart = taskSubProofDetailPO.getIncomeStart() == null ? "" : DateTimeFormatter.ofPattern("yyyy-MM").format(taskSubProofDetailPO.getIncomeStart());
                String incomeEnd = taskSubProofDetailPO.getIncomeEnd() == null ? "" : DateTimeFormatter.ofPattern("yyyy-MM").format(taskSubProofDetailPO.getIncomeEnd());
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
}
