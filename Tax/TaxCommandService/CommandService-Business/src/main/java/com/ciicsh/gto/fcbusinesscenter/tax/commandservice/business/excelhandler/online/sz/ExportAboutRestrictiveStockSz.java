package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.online.sz;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.BaseService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.EmployeeInfoBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.StockIncomeType;
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
 * 个人股票期权行权收入-限制性股票(深圳金三)
 * @author yuantongqing on 2018-06-01
 */
@Service
public class ExportAboutRestrictiveStockSz extends BaseService {
    /**
     * 导出个人股票期权行权收入-限制性股票
     *
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     * @param fileName
     * @param type
     * @return
     */
    public HSSFWorkbook getRestrictiveStockWB(List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList, String fileName, String type) {
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //获取POIFSFileSystem对象
            fs = getFSFileSystem(fileName, type);
            //通过POIFSFileSystem对象获取WB对象
            wb = getHSSFWorkbook(fs);
            //根据不同的业务需要处理wb
            this.handleRestrictiveStockWB(wb, taskSubDeclareDetailPOList, employeeInfoBatchPOList);
        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    Map<String, String> tags = new HashMap<>(16);
                    //日志工具类返回
                    LogTaskFactory.getLogger().error(ee, "ExportAboutRestrictiveStockSz.getRestrictiveStockWB", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.OTHER, tags);
                }
            }
            throw e;
        }
        return wb;
    }

    /**
     * 导出个人股票期权行权收入-限制性股票
     *
     * @param wb
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     */
    public void handleRestrictiveStockWB(HSSFWorkbook wb, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList) {
        //根据股权收入类型,筛选出股票期权的股权收入
        List<EmployeeInfoBatchPO> employeeInfoBatchPOS = employeeInfoBatchPOList.stream().filter(item -> StockIncomeType.RESTRICTIVESTOCK.getCode().equals(item.getStockType())).collect(Collectors.toList());
        // 读取了模板内所有sheet内容
        HSSFSheet sheet = wb.getSheetAt(0);
        //在相应的单元格进行赋值
        int sheetRowIndex = 1;
        for (EmployeeInfoBatchPO employeeInfoBatchPO : employeeInfoBatchPOS) {
            List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOListS = taskSubDeclareDetailPOList.stream().filter(item -> employeeInfoBatchPO.getCalBatchDetailId().equals(item.getCalculationBatchDetailId())).collect(Collectors.toList());
            TaskSubDeclareDetailPO po = new TaskSubDeclareDetailPO();
            if (taskSubDeclareDetailPOListS.size() > 0) {
                po = taskSubDeclareDetailPOListS.get(0);
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
            //*类型-E列
            HSSFCell cellE = row.getCell(4);
            if (null == cellE) {
                cellE = row.createCell(4);
            }
            cellE.setCellValue("限制性股票");
            //解禁日-F列
            //*股票名称(简称)-G列
            HSSFCell cellG = row.getCell(6);
            if (null == cellG) {
                cellG = row.createCell(6);
            }
            cellG.setCellValue(employeeInfoBatchPO.getStockName());
            //*股票代码-H列
            HSSFCell cellH = row.getCell(7);
            if (null == cellH) {
                cellH = row.createCell(7);
            }
            cellH.setCellValue(employeeInfoBatchPO.getStockCode());
            //*股票类型-I列
            HSSFCell cellI = row.getCell(8);
            if (null == cellI) {
                cellI = row.createCell(8);
            }
            cellI.setCellValue(employeeInfoBatchPO.getStockType());
            //*股票登记日股票市价-J列
            HSSFCell cellJ = row.getCell(9);
            if (null == cellJ) {
                cellJ = row.createCell(9);
            }
            cellJ.setCellValue(employeeInfoBatchPO.getRestrictRegisterValue() == null ? "" : employeeInfoBatchPO.getRestrictRegisterValue().toString());
            //*本批次解禁股票当日市价-K列restrict_relieve_value
            HSSFCell cellK = row.getCell(10);
            if (null == cellK) {
                cellK = row.createCell(10);
            }
            cellK.setCellValue(employeeInfoBatchPO.getRestrictRelieveValue() == null ? "" : employeeInfoBatchPO.getRestrictRelieveValue().toString());
            //*本批次解禁股票份数-L列restrict_relieve_quantity
            HSSFCell cellL = row.getCell(11);
            if (null == cellL) {
                cellL = row.createCell(11);
            }
            cellL.setCellValue(employeeInfoBatchPO.getRestrictRelieveQuantity());
            //*被激励对象获得的限制性股票总份数-M列restrict_total
            HSSFCell cellM = row.getCell(12);
            if (null == cellM) {
                cellM = row.createCell(12);
            }
            cellM.setCellValue(employeeInfoBatchPO.getRestrictTotal());
            //被激励对象实际支付的资金总额-N列restrict_payment
            HSSFCell cellN = row.getCell(13);
            if (null == cellN) {
                cellN = row.createCell(13);
            }
            cellN.setCellValue(employeeInfoBatchPO.getRestrictPayment() == null ? "" : employeeInfoBatchPO.getRestrictPayment().toString());
            //本年度累计行权收入(不含本月)-O列exercise_income_year
            HSSFCell cellO = row.getCell(14);
            if (null == cellO) {
                cellO = row.createCell(14);
            }
            cellO.setCellValue(po.getExerciseIncomeYear() == null ? "" : po.getExerciseIncomeYear().toString());
            //*规定月份数-P列number_of_months
            HSSFCell cellP = row.getCell(15);
            if (null == cellP) {
                cellP = row.createCell(15);
            }
            cellP.setCellValue(po.getNumberOfMonths() == null ? "" : po.getNumberOfMonths().toString() +"个月");
            //免税所得-Q列
            //通讯费-R列
            //商业健康保险费-S列
            //其他扣除-T列
            //本年累计准予扣除的捐赠额-U列
            //本年累计减免税额-V列
            //本年累计已纳税额-W列exercise_tax_amount
            HSSFCell cellW = row.getCell(22);
            if (null == cellW) {
                cellW = row.createCell(22);
            }
            cellW.setCellValue(po.getExerciseTaxAmount() == null ? "" : po.getExerciseTaxAmount().toString());
            //备注-X列
            sheetRowIndex++;
        }
    }
}
