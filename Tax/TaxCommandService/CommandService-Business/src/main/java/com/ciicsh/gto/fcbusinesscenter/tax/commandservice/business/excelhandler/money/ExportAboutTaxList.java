package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.money;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.BaseService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.EmployeeInfoBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubMoneyDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubMoneyPO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工资清单
 *
 * @author yuantongqing on 2018-07-03
 */
@Service
public class ExportAboutTaxList extends BaseService {

    /**
     * 获取工资清单WB
     * @param taskSubMoneyPO
     * @param taskSubMoneyDetailPOList
     * @param employeeInfoBatchPOList
     * @param fileName
     * @param type
     * @return
     */
    public HSSFWorkbook getTaxListReportWB(TaskSubMoneyPO taskSubMoneyPO, List<TaskSubMoneyDetailPO> taskSubMoneyDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList, String fileName, String type) {
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //获取POIFSFileSystem对象
            fs = getFSFileSystem(fileName, type);
            //通过POIFSFileSystem对象获取WB对象
            wb = getHSSFWorkbook(fs);
            // TODO 工资清单头部信息
            //用于存放模板列表头部
            Map<String, String> map = new HashMap<>(16);
            //计算批次
            map.put("batchNo", "1212212122121212");
            //管理方名称
            map.put("managerName",taskSubMoneyPO.getManagerName());
            //根据不同的业务需要处理wb
            this.handleTaxListReportWB(wb, map, taskSubMoneyDetailPOList, employeeInfoBatchPOList);
        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    Map<String, String> tags = new HashMap<>(16);
                    //日志工具类返回
                    LogTaskFactory.getLogger().error(ee, "ExportAboutTaxList.getTaxListReportWB", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.OTHER, tags);
                }
            }
            throw e;
        }
        return wb;
    }

    /**
     * 处理工资清单WB
     * @param wb
     * @param map
     * @param taskSubMoneyDetailPOList
     * @param employeeInfoBatchPOList
     */
    private void handleTaxListReportWB(HSSFWorkbook wb, Map<String, String> map, List<TaskSubMoneyDetailPO> taskSubMoneyDetailPOList,List<EmployeeInfoBatchPO> employeeInfoBatchPOList){
        // 读取了模板内所有sheet内容
        HSSFSheet sheet = wb.getSheetAt(0);
        //第2行
        HSSFRow row2 = sheet.getRow(1);
        if (null == row2) {
            row2 = sheet.createRow(1);
        }
        //计算批次-C2
        HSSFCell cellC2 = row2.getCell(2);
        if (null == cellC2) {
            cellC2 = row2.createCell(2);
        }
        cellC2.setCellValue(map.get("batchNo"));
        //第3行
        HSSFRow row3 = sheet.getRow(2);
        if (null == row3) {
            row3 = sheet.createRow(2);
        }
        //管理方名称-C3
        HSSFCell cellC3 = row3.getCell(2);
        if (null == cellC3) {
            cellC3 = row3.createCell(2);
        }
        cellC3.setCellValue(map.get("managerName"));
        //在相应的单元格进行赋值
        int rowIndex = 5;
        //序号
        int num = 1;
        for (TaskSubMoneyDetailPO taskSubMoneyDetailPO : taskSubMoneyDetailPOList) {
            List<EmployeeInfoBatchPO> employeeInfoBatchPOS = employeeInfoBatchPOList.stream().filter(item -> taskSubMoneyDetailPO.getCalculationBatchDetailId().equals(item.getCalBatchDetailId())).collect(Collectors.toList());
            EmployeeInfoBatchPO employeeInfoBatchPO = new EmployeeInfoBatchPO();
            if (employeeInfoBatchPOS.size() > 0) {
                employeeInfoBatchPO = employeeInfoBatchPOS.get(0);
            }
            HSSFRow row = sheet.getRow(rowIndex);
            if (null == row) {
                row = sheet.createRow(rowIndex);
            }
            //序号-A列
            HSSFCell cellA = row.getCell(0);
            if (null == cellA) {
                cellA = row.createCell(0);
            }
            cellA.setCellValue(num);
            //公司编号-B列
            HSSFCell cellB = row.getCell(1);
            if (null == cellB) {
                cellB = row.createCell(1);
            }
            cellB.setCellValue(employeeInfoBatchPO.getCompanyNo());
            //雇员编号-C列
            HSSFCell cellC = row.getCell(2);
            if (null == cellC) {
                cellC = row.createCell(2);
            }
            cellC.setCellValue(taskSubMoneyDetailPO.getEmployeeNo());
            //雇员姓名-D列
            HSSFCell cellD = row.getCell(3);
            if (null == cellD) {
                cellD = row.createCell(3);
            }
            cellD.setCellValue(taskSubMoneyDetailPO.getEmployeeName());
            //扣个人所得税(中智公司)-E列
            //扣个人所得税(财务公司)-F列
            //扣个人所得税(独立库)-G列
            rowIndex++;
            num++;
        }
    }
}
