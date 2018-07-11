package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.online;

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

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 人员信息
 *
 * @author yuantongqing 2018-05-31
 */
@Service
public class ExportAboutPersonInfo extends BaseService {
    /**
     * 导出人员信息excel
     *
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     * @param fileName
     * @param type
     * @return
     */
    public HSSFWorkbook getPersonInfoWB(List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList, String fileName, String type) {
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //获取POIFSFileSystem对象
            fs = getFSFileSystem(fileName, type);
            //通过POIFSFileSystem对象获取WB对象
            wb = getHSSFWorkbook(fs);
            //根据不同的业务需要处理wb
            this.handlePersonInfoWB(wb, taskSubDeclareDetailPOList, employeeInfoBatchPOList);
        } catch (Exception e) {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception ee) {
                    Map<String, String> tags = new HashMap<>(16);
                    //日志工具类返回
                    LogTaskFactory.getLogger().error(ee, "ExportAboutPersonInfo.getPersonInfoWB", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.OTHER, tags);
                }
            }
            throw e;
        }
        return wb;
    }

    /**
     * 导出人员信息
     *
     * @param wb
     * @param taskSubDeclareDetailPOList
     * @param employeeInfoBatchPOList
     */
    public void handlePersonInfoWB(HSSFWorkbook wb, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList) {
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
            //*国籍(地区)-E列
            HSSFCell cellE = row.getCell(4);
            if (null == cellE) {
                cellE = row.createCell(4);
            }
            cellE.setCellValue(EnumUtil.getMessage(EnumUtil.COUNTRY_ONLINE_COMMON, employeeInfoBatchPO.getNationality()));
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
            cellAA.setCellValue(EnumUtil.getMessage(EnumUtil.POST_LEVEL_ONLINE_COMMON, employeeInfoBatchPO.getDomesticDuty()));
            //境外职务-AB
            HSSFCell cellAB = row.getCell(27);
            if (null == cellAB) {
                cellAB = row.createCell(27);
            }
            cellAB.setCellValue(EnumUtil.getMessage(EnumUtil.POST_LEVEL_ONLINE_COMMON, employeeInfoBatchPO.getOverseasDuty()));
            //支付地-AC
            HSSFCell cellAC = row.getCell(28);
            if (null == cellAC) {
                cellAC = row.createCell(28);
            }
            cellAC.setCellValue(EnumUtil.getMessage(EnumUtil.PAY_PLACE_ONLINE_COMMON, employeeInfoBatchPO.getPaymentPlace()));
            //境外支付地（国别/地区-AD
            HSSFCell cellAD = row.getCell(29);
            if (null == cellAD) {
                cellAD = row.createCell(29);
            }
            cellAD.setCellValue(employeeInfoBatchPO.getPaymentOverseasPlace());
            sheetRowIndex++;
        }
    }
}
