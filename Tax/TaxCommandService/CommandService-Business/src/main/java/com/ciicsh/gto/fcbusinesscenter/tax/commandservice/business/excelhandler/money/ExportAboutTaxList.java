package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.excelhandler.money;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.BaseService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchAccountPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.EmployeeInfoBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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

    //大库
    public static final String SOURCE_BIG = "0";
    //列表数据初始大小
    private static final int TAX_LIST_SIZE = 1;

    //TODO 中智识别号
    public static final String CIIC_RECOGNITION_NO = "123456";
    //TODO 财务识别号
    public static final String FINANCE_RECOGNITION_NO = "1234567";

    /**
     * 中智公司数组
     */
    public static final String[] CIIC_COMPANY = {"中智公司建行徐汇"};

    /**
     * 财务公司数组
     */
    public static final String[] FINANCE_COMPANY={"财务公司建行徐汇","财务公司中信徐汇","财务公司北京账户"};

    /**
     * 获取员工个税申报明细WB
     * @param map
     * @param calculationBatchDetailPOList
     * @param employeeInfoBatchPOList
     * @param accountMap
     * @param fileName
     * @param type
     * @return
     */
    public HSSFWorkbook getTaxListReportWB(Map<String,String> map, List<CalculationBatchDetailPO> calculationBatchDetailPOList, List<EmployeeInfoBatchPO> employeeInfoBatchPOList, Map<String,CalculationBatchAccountPO> accountMap, String fileName, String type) {
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        try {
            //获取POIFSFileSystem对象
            fs = getFSFileSystem(fileName, type);
            //通过POIFSFileSystem对象获取WB对象
            wb = getHSSFWorkbook(fs);
            //根据不同的业务需要处理wb
            this.handleTaxListReportWB(wb, map, calculationBatchDetailPOList, employeeInfoBatchPOList,accountMap);
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
     * 处理员工个税申报明细WB
     * @param wb
     * @param map
     * @param calculationBatchDetailPOList
     * @param employeeInfoBatchPOList
     * @param accountMap
     */
    private void handleTaxListReportWB(HSSFWorkbook wb, Map<String, String> map, List<CalculationBatchDetailPO> calculationBatchDetailPOList,List<EmployeeInfoBatchPO> employeeInfoBatchPOList,Map<String,CalculationBatchAccountPO> accountMap){
        // 读取了模板内所有sheet内容
        HSSFSheet sheet = wb.getSheetAt(0);
        //第2行
        HSSFRow row2 = sheet.getRow(1);
        if (null == row2) {
            row2 = sheet.createRow(1);
        }
        //流水号-C2
        HSSFCell cellC2 = row2.getCell(2);
        if (null == cellC2) {
            cellC2 = row2.createCell(2);
        }
        cellC2.setCellValue(map.get("flowNum"));
        //打印日期-J2
        HSSFCell cellJ2 = row2.getCell(9);
        if (null == cellJ2) {
            cellJ2 = row2.createCell(9);
        }
        cellJ2.setCellValue(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
        //第3行
        HSSFRow row3 = sheet.getRow(2);
        if (null == row3) {
            row3 = sheet.createRow(2);
        }
        //发放批次-C3
        HSSFCell cellC3 = row3.getCell(2);
        if (null == cellC3) {
            cellC3 = row3.createCell(2);
        }
        cellC3.setCellValue(map.get("batchNo"));
        //个税期间-F3
        HSSFCell cellF3 = row3.getCell(5);
        if (null == cellF3) {
            cellF3 = row3.createCell(5);
        }
        cellF3.setCellValue(map.get("period"));
        //第4行
        HSSFRow row4 = sheet.getRow(3);
        if (null == row4) {
            row4 = sheet.createRow(3);
        }
        //公司-B4
        HSSFCell cellB4 = row4.getCell(1);
        if (null == cellB4) {
            cellB4 = row4.createCell(1);
        }
        cellB4.setCellValue(map.get("company"));
        //服务中心-H4
        HSSFCell cellH4 = row4.getCell(7);
        if (null == cellH4) {
            cellH4 = row4.createCell(7);
        }
        cellH4.setCellValue(map.get("serviceCenter"));
        //客户经理-J4
        HSSFCell cellJ4 = row4.getCell(9);
        if (null == cellJ4) {
            cellJ4 = row4.createCell(9);
        }
        cellJ4.setCellValue(map.get("serviceManager"));
        //第5行
        HSSFRow row5 = sheet.getRow(4);
        if (null == row5) {
            row5 = sheet.createRow(4);
        }
        //原公司编号-C5
        HSSFCell cellC5 = row5.getCell(2);
        if (null == cellC5) {
            cellC5 = row5.createCell(2);
        }
        cellC5.setCellValue(map.get("oldCompanyNo"));

        //将结果集按照公司分类
        Map<String,List<EmployeeInfoBatchPO>> employeeMapAboutCompany = employeeInfoBatchPOList.stream().collect(Collectors.groupingBy(EmployeeInfoBatchPO::getCompanyNo));
        int mapSize = employeeMapAboutCompany.size();
        //如果结果集大于模板列表初始大小
        if (calculationBatchDetailPOList.size() > TAX_LIST_SIZE) {
            int rows = calculationBatchDetailPOList.size() - TAX_LIST_SIZE + (mapSize - 1);
            //插入行数据
            super.insertRow(sheet, 7, rows);
        }
        //薪金所属期合计
        String periodTotal = "";
        //雇员数目合计
        int empNumTotal = 0;
        //中智公司金额合计
        BigDecimal ciicAmountMoneyTotal = new BigDecimal(0);
        //财务公司金额合计
        BigDecimal financeMoneyTotal = new BigDecimal(0);
        //独立库金额合计
        BigDecimal amountIndependentMoneyTotal = new BigDecimal(0);
        //应纳税所得额合计
        BigDecimal incomeForTaxTotal = new BigDecimal(0);

        //在相应的单元格进行赋值
        int rowIndex = 6;
        //序号
        int num = 1;
        //列表
        for (String key : employeeMapAboutCompany.keySet()) {
            List<EmployeeInfoBatchPO> employeeInfoBatchPOS = employeeMapAboutCompany.get(key);
            //薪金所属期小计
            String periodSmall = "";
            //公司编号小计
            String companyNoSmall = key;
            //雇员数目小计
            int empNumSmall = 0;
            //中智公司金额小计
            BigDecimal ciicAmountMoneySmall = new BigDecimal(0);
            //财务公司金额小计
            BigDecimal financeMoneySmall = new BigDecimal(0);
            //独立库金额小计
            BigDecimal amountIndependentMoneySmall = new BigDecimal(0);
            //应纳税所得额小计
            BigDecimal incomeForTaxSmall = new BigDecimal(0);
            for(EmployeeInfoBatchPO employeeInfoBatchPO: employeeInfoBatchPOS){
                CalculationBatchDetailPO calculationBatchDetailPO = calculationBatchDetailPOList.stream().filter(item -> item.getId().equals(employeeInfoBatchPO.getCalBatchDetailId())).findFirst().orElse(new CalculationBatchDetailPO());
                CalculationBatchAccountPO calculationBatchAccountPO = accountMap.get(calculationBatchDetailPO.getDeclareAccount());
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
                //薪金所属期-B列
                HSSFCell cellB = row.getCell(1);
                if (null == cellB) {
                    cellB = row.createCell(1);
                }
                periodSmall = calculationBatchDetailPO.getPeriod() == null ? "" : DateTimeFormatter.ofPattern("yyyyMM").format(calculationBatchDetailPO.getPeriod());
                cellB.setCellValue(periodSmall);
                //公司编号-C列
                HSSFCell cellC = row.getCell(2);
                if (null == cellC) {
                    cellC = row.createCell(2);
                }
                cellC.setCellValue(employeeInfoBatchPO.getCompanyNo());
                //雇员编号-D列
                HSSFCell cellD = row.getCell(3);
                if (null == cellD) {
                    cellD = row.createCell(3);
                }
                cellD.setCellValue(calculationBatchDetailPO.getEmployeeNo());
                //雇员姓名-E列
                HSSFCell cellE = row.getCell(4);
                if (null == cellE) {
                    cellE = row.createCell(4);
                }
                cellE.setCellValue(calculationBatchDetailPO.getEmployeeName());
                //服务类型-F列

                //中智公司金额
                String amountCiic = "0";
                //财务公司金额
                String amountFinance = "0";
                //独立库金额
                String amountIndependent = "0";
                //判断是否是大库
                if(calculationBatchAccountPO != null && calculationBatchAccountPO.getSource() != null){
                    if(calculationBatchAccountPO.getSource().equals(SOURCE_BIG)){
                        //判断是中智公司还是财务公司
                        List<String> ciicList = Arrays.asList(CIIC_COMPANY).stream().filter(item -> item.contains(calculationBatchAccountPO.getAccountName())).collect(Collectors.toList());
                        List<String> financeList = Arrays.asList(FINANCE_COMPANY).stream().filter(item -> item.contains(calculationBatchAccountPO.getAccountName())).collect(Collectors.toList());
                        if(ciicList.size() > 0){
                            amountCiic = calculationBatchDetailPO.getTaxReal() == null ? "0" : calculationBatchDetailPO.getTaxReal().toString();
                            ciicAmountMoneySmall = ciicAmountMoneySmall.add(calculationBatchDetailPO.getTaxReal() == null ? new BigDecimal(0) : calculationBatchDetailPO.getTaxReal() );
                        }else if(financeList.size() > 0){
                            amountFinance = calculationBatchDetailPO.getTaxReal() == null ? "0" : calculationBatchDetailPO.getTaxReal().toString();
                            financeMoneySmall = financeMoneySmall.add(calculationBatchDetailPO.getTaxReal() == null ? new BigDecimal(0) : calculationBatchDetailPO.getTaxReal() );
                        }
                    }else{
                        amountIndependent = calculationBatchDetailPO.getTaxReal() == null ? "0" : calculationBatchDetailPO.getTaxReal().toString();
                        amountIndependentMoneySmall = amountIndependentMoneySmall.add(calculationBatchDetailPO.getTaxReal() == null ? new BigDecimal(0) : calculationBatchDetailPO.getTaxReal() );
                    }
                }
                //扣个人所得税(中智公司)-G列
                HSSFCell cellG = row.getCell(6);
                if (null == cellG) {
                    cellG = row.createCell(6);
                }
                cellG.setCellValue(amountCiic);
                //扣个人所得税(财务公司)-H列
                HSSFCell cellH = row.getCell(7);
                if (null == cellH) {
                    cellH = row.createCell(7);
                }
                cellH.setCellValue(amountFinance);
                //扣个人所得税(独立库)-I列
                HSSFCell cellI = row.getCell(8);
                if (null == cellI) {
                    cellI = row.createCell(8);
                }
                cellI.setCellValue(amountIndependent);
                //应纳税所得额-J列
                HSSFCell cellJ = row.getCell(9);
                if (null == cellJ) {
                    cellJ = row.createCell(9);
                }
                cellJ.setCellValue(calculationBatchDetailPO.getIncomeForTax() == null ? "0" : calculationBatchDetailPO.getIncomeForTax().toString());
                incomeForTaxSmall = incomeForTaxSmall.add(calculationBatchDetailPO.getIncomeForTax() == null ? new BigDecimal(0) : calculationBatchDetailPO.getIncomeForTax() );
                empNumSmall++;
                rowIndex++;
                num++;
            }
            periodTotal = periodSmall;
            //小计
            HSSFRow rowSmall = sheet.getRow(rowIndex);
            if (null == rowSmall) {
                rowSmall = sheet.createRow(rowIndex);
            }
            //薪金所属期-B列
            HSSFCell cellB = rowSmall.getCell(1);
            if (null == cellB) {
                cellB = rowSmall.createCell(1);
            }
            cellB.setCellValue(periodSmall);
            //公司编号-C列
            HSSFCell cellC = rowSmall.getCell(2);
            if (null == cellC) {
                cellC = rowSmall.createCell(2);
            }
            cellC.setCellValue(companyNoSmall+"小计");
            //雇员姓名-E列
            HSSFCell cellE = rowSmall.getCell(4);
            if (null == cellE) {
                cellE = rowSmall.createCell(4);
            }
            cellE.setCellValue(empNumSmall);
            empNumTotal = empNumTotal + empNumSmall;
            //扣个人所得税(中智公司)-G列
            HSSFCell cellG = rowSmall.getCell(6);
            if (null == cellG) {
                cellG = rowSmall.createCell(6);
            }
            cellG.setCellValue(ciicAmountMoneySmall.toString());
            //合计金额为小计和
            ciicAmountMoneyTotal = ciicAmountMoneyTotal.add(ciicAmountMoneySmall);
            //扣个人所得税(财务公司)-H列
            HSSFCell cellH = rowSmall.getCell(7);
            if (null == cellH) {
                cellH = rowSmall.createCell(7);
            }
            cellH.setCellValue(financeMoneySmall.toString());
            //合计金额为小计和
            financeMoneyTotal = financeMoneyTotal.add(financeMoneySmall);
            //扣个人所得税(独立库)-I列
            HSSFCell cellI = rowSmall.getCell(8);
            if (null == cellI) {
                cellI = rowSmall.createCell(8);
            }
            cellI.setCellValue(amountIndependentMoneySmall.toString());
            //合计金额为小计和
            amountIndependentMoneyTotal = amountIndependentMoneyTotal.add(amountIndependentMoneySmall);
            //应纳税所得额-J列
            HSSFCell cellJ = rowSmall.getCell(9);
            if (null == cellJ) {
                cellJ = rowSmall.createCell(9);
            }
            cellJ.setCellValue(incomeForTaxSmall.toString());
            //合计金额为小计和
            incomeForTaxTotal = incomeForTaxTotal.add(incomeForTaxSmall);
            rowIndex++;
            num++;
        }
        //总计
        HSSFRow rowTotal = sheet.getRow(rowIndex);
        if (null == rowTotal) {
            rowTotal = sheet.createRow(rowIndex);
        }
        //薪金所属期-B列
        HSSFCell cellB = rowTotal.getCell(1);
        if (null == cellB) {
            cellB = rowTotal.createCell(1);
        }
        cellB.setCellValue(periodTotal);
        //雇员姓名-E列
        HSSFCell cellE = rowTotal.getCell(4);
        if (null == cellE) {
            cellE = rowTotal.createCell(4);
        }
        cellE.setCellValue(empNumTotal);
        //扣个人所得税(中智公司)-G列
        HSSFCell cellG = rowTotal.getCell(6);
        if (null == cellG) {
            cellG = rowTotal.createCell(6);
        }
        cellG.setCellValue(ciicAmountMoneyTotal.toString());
        //扣个人所得税(财务公司)-H列
        HSSFCell cellH = rowTotal.getCell(7);
        if (null == cellH) {
            cellH = rowTotal.createCell(7);
        }
        cellH.setCellValue(financeMoneyTotal.toString());
        //扣个人所得税(独立库)-I列
        HSSFCell cellI = rowTotal.getCell(8);
        if (null == cellI) {
            cellI = rowTotal.createCell(8);
        }
        cellI.setCellValue(amountIndependentMoneyTotal.toString());
        //应纳税所得额-J列
        HSSFCell cellJ = rowTotal.getCell(9);
        if (null == cellJ) {
            cellJ = rowTotal.createCell(9);
        }
        cellJ.setCellValue(incomeForTaxTotal.toString());
        //表格尾部
        HSSFRow rowOperator = sheet.getRow(rowIndex+3);
        if (null == rowOperator) {
            rowOperator = sheet.createRow(rowIndex+3);
        }
        //操作员-C列
        HSSFCell cellC = rowOperator.getCell(2);
        if (null == cellC) {
            cellC = rowOperator.createCell(2);
        }
        cellC.setCellValue(map.get("operator"));
    }
}
