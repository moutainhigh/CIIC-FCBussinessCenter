package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.online.sz;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.BaseService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.EmployeeInfoBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
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
 * 个人股票期权行权收入-股票期权(深圳金三)
 *
 * @author yuantongqing on 2018-06-01
 */
@Service
public class ExportAboutStockOptionSz extends BaseService {
    /**
     * 导出个人股票期权行权收入-股票期权
     *
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     * @param fileName
     * @param type
     * @return
     */
    public HSSFWorkbook getStockOptionWB(List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList, String fileName, String type) {
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //获取POIFSFileSystem对象
            fs = getFSFileSystem(fileName, type);
            //通过POIFSFileSystem对象获取WB对象
            wb = getHSSFWorkbook(fs);
            //根据不同的业务需要处理wb
            this.handleStockOptionWB(wb, taskSubDeclareDetailPOList, employeeInfoBatchPOList);
        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    Map<String, String> tags = new HashMap<>(16);
                    //日志工具类返回
                    LogTaskFactory.getLogger().error(ee, "ExportAboutStockOptionSz.getStockOptionWB", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.OTHER, tags);
                }
            }
            throw e;
        }
        return wb;
    }

    /**
     * 导出个人股票期权行权收入-股票期权
     *
     * @param wb
     * @param taskSubDeclareDetailPOList
     */
    public void handleStockOptionWB(HSSFWorkbook wb, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList) {
        // 读取了模板内所有sheet内容
        HSSFSheet sheet = wb.getSheetAt(0);
        //在相应的单元格进行赋值
        int sheetRowIndex = 1;
        for (EmployeeInfoBatchPO employeeInfoBatchPO : employeeInfoBatchPOList) {
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
            //*类型-E列
            HSSFCell cellE = row.getCell(4);
            if (null == cellE) {
                cellE = row.createCell(4);
            }
            cellE.setCellValue("股票期权");
            //行权日-F列
            //施权日-G列
            //*股票名称(简称)-H列
            HSSFCell cellH = row.getCell(7);
            if (null == cellH) {
                cellH = row.createCell(7);
            }
            cellH.setCellValue(employeeInfoBatchPO.getStockName() == null ? "" : employeeInfoBatchPO.getStockName());
            //*股票代码-I列
            HSSFCell cellI = row.getCell(8);
            if (null == cellI) {
                cellI = row.createCell(8);
            }
            cellI.setCellValue(employeeInfoBatchPO.getStockCode() == null ? "" : employeeInfoBatchPO.getStockCode());
            //*股票类型-J列
            HSSFCell cellJ = row.getCell(9);
            if (null == cellJ) {
                cellJ = row.createCell(9);
            }
            cellJ.setCellValue(employeeInfoBatchPO.getStockType() == null ? "" : employeeInfoBatchPO.getStockType());
            //*行权股票的每股市场价-K列
            HSSFCell cellK = row.getCell(10);
            if (null == cellK) {
                cellK = row.createCell(10);
            }
            cellK.setCellValue(employeeInfoBatchPO.getOptionMarketValue() == null ? "" : employeeInfoBatchPO.getOptionMarketValue().toString());
            //股票期权支付的每股施权价-L列
            HSSFCell cellL = row.getCell(11);
            if (null == cellL) {
                cellL = row.createCell(11);
            }
            cellL.setCellValue(employeeInfoBatchPO.getOptionExerciseValue() == null ? "" : employeeInfoBatchPO.getOptionExerciseValue().toString());
            //*股票数量-M列
            HSSFCell cellM = row.getCell(12);
            if (null == cellM) {
                cellM = row.createCell(12);
            }
            cellM.setCellValue(employeeInfoBatchPO.getOptionQuantity() == null ? "" : employeeInfoBatchPO.getOptionQuantity().toString());
            //本年度累计行权收入(不含本月)-N列
            HSSFCell cellN = row.getCell(13);
            if (null == cellN) {
                cellN = row.createCell(13);
            }
            cellN.setCellValue(po.getExerciseIncomeYear() == null ? "" : po.getExerciseIncomeYear().toString());
            //*规定月份数-O列
            HSSFCell cellO = row.getCell(14);
            if (null == cellO) {
                cellO = row.createCell(14);
            }
            cellO.setCellValue(po.getNumberOfMonths() == null ? "" : StrKit.strToInt(po.getNumberOfMonths()) > 12 ? "12个月以上" : StrKit.strToInt(po.getNumberOfMonths())  +"个月");
            //免税所得-P列
            //通讯费-Q列
            //商业健康保险费-R列
            //其他扣除-S列
            //本年累计准予扣除的捐赠额-T列
            //本年累计减免税额-U列
            //本年累计已纳税额-V列
            HSSFCell cellV = row.getCell(21);
            if (null == cellV) {
                cellV = row.createCell(21);
            }
            cellV.setCellValue(po.getExerciseTaxAmount() == null ? "" : po.getExerciseTaxAmount().toString());
            //备注-W列
            sheetRowIndex++;
        }
    }
}
