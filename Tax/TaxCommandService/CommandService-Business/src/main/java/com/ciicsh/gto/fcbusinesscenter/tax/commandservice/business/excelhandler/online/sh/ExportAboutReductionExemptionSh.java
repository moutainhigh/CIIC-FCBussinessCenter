package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.online.sh;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 减免事项模板(上海)
 * @author yuantongqing 2018-05-31
 */
@Service
public class ExportAboutReductionExemptionSh extends BaseService{

    /**
     * 导出减免事项
     *
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     * @param fileName
     * @param type
     * @return
     */
    public HSSFWorkbook getReductionExemptionWB(List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList, String fileName,String type) {
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //获取POIFSFileSystem对象
            fs = getFSFileSystem(fileName, type);
            //通过POIFSFileSystem对象获取WB对象
            wb = getHSSFWorkbook(fs);
            //根据不同的业务需要处理wb
            this.handleReductionExemptionWB(wb, taskSubDeclareDetailPOList, employeeInfoBatchPOList);
        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    Map<String, String> tags = new HashMap<>(16);
                    //日志工具类返回
                    LogTaskFactory.getLogger().error(ee, "ExportAboutReductionExemptionSh.getReductionExemptionWB", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.OTHER, tags);
                }
            }
            throw e;
        }
        return wb;
    }

    /**
     * 导出减免事项
     *
     * @param wb
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     */
    public void handleReductionExemptionWB(HSSFWorkbook wb, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList) {
        //筛选出减免事项减免金额大于0的详细信息tax_deduction
        List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOS = taskSubDeclareDetailPOList.stream().filter(item -> item.getTaxDeduction() != null && item.getTaxDeduction().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        // 读取了模板内所有sheet内容
        HSSFSheet sheet = wb.getSheetAt(0);
        //在相应的单元格进行赋值
        int sheetRowIndex = 1;
        for (TaskSubDeclareDetailPO po : taskSubDeclareDetailPOS) {
            HSSFRow row = sheet.getRow(sheetRowIndex);
            if (null == row) {
                row = sheet.createRow(sheetRowIndex);
            }
            //*报表类别-A列
            HSSFCell cellA = row.getCell(0);
            if (null == cellA) {
                cellA = row.createCell(0);
            }
            cellA.setCellValue("扣缴所得税报告表");
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
            cellC.setCellValue(EnumUtil.getMessage(EnumUtil.ID_TYPE_ONLINE_SIMPLE_COMMON, po.getIdType()));
            //*证照号码-D列
            HSSFCell cellD = row.getCell(3);
            if (null == cellD) {
                cellD = row.createCell(3);
            }
            cellD.setCellValue(po.getIdNo());
            //*所得项目-E列
            HSSFCell cellE = row.getCell(4);
            if (null == cellE) {
                cellE = row.createCell(4);
            }
            cellE.setCellValue(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT, po.getIncomeSubject()));
            //*减免事项-F列
            HSSFCell cellF = row.getCell(5);
            if (null == cellF) {
                cellF = row.createCell(5);
            }
            cellF.setCellValue("SXA031900042");
            //*减免性质-G列
            HSSFCell cellG = row.getCell(6);
            if (null == cellG) {
                cellG = row.createCell(6);
            }
            cellG.setCellValue("0005012710");
            //*减免税额-H列
            HSSFCell cellH = row.getCell(7);
            if (null == cellH) {
                cellH = row.createCell(7);
            }
            cellH.setCellValue(po.getTaxDeduction() == null ? "" : po.getTaxDeduction().toString());
            sheetRowIndex++;
        }
    }
}
