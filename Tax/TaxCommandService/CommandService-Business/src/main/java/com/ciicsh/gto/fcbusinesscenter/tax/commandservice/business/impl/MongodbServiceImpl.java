package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gt1.BaseOpt;
import com.ciicsh.gto.fcbusinesscenter.entity.CancelClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.entity.ClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.MongodbService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.CalculationBatchMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.EmployeeInfoBatchMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.EmployeeServiceBatchMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.frombatch.*;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.EmployeeInfoBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.EmployeeServiceBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.BatchNoStatus;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.BatchType;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.IncomeSubject;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.DateTimeKit;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import com.mongodb.DBObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class MongodbServiceImpl extends BaseOpt implements MongodbService{

    public static final String PR_PAYROLL_CAL_RESULT = "fc_payroll_calc_result_table";

    public MongodbServiceImpl() {
        super(PR_PAYROLL_CAL_RESULT);
    }

    @Autowired
    private CalculationBatchServiceImpl calculationBatchService;

    @Autowired
    private CalculationBatchDetailServiceImpl calculationBatchDetailService;

    @Autowired
    private EmployeeInfoBatchMapper employeeInfoBatchMapper;

    @Autowired
    private EmployeeServiceBatchMapper employeeServiceBatchMapper;

    @Autowired
    private CalculationBatchMapper calculationBatchMapper;

    @Transactional(rollbackFor = Exception.class)
    public void acquireBatch(ClosingMsg closingMsg){

         MainBO mainBO = new MainBO();//批次基本信息（mongodb）

         TaxInfoBO taxInfoBO = new TaxInfoBO();//个税信息（mongodb）

         AgreementBO agreementBO = new AgreementBO();//服务协议（mongodb）

         CalResultBO calResultBO = new CalResultBO();//计算结果（薪资项）（mongodb）

         EmpInfoBO empInfoBO = new EmpInfoBO();//雇员基础信息（mongodb）

         CalculationBatchPO newCal = new CalculationBatchPO();//批次主信息

        //关账消息对象
        if(closingMsg == null){
            return;
        }

        //批次号
        String batchCode = closingMsg.getBatchCode();

        //批次查询条件
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.eq("batch_no",batchCode);

        //根据批次号查询批次
        CalculationBatchPO calculationBatchPO = calculationBatchService.selectOne(wrapper);

        //如果批次已存在，且状态不为“取消关账”，则不更新批次信息
        if(calculationBatchPO != null && !(calculationBatchPO.getStatus().equals(BatchNoStatus.unclose.getCode()))){
            return;
        }

        //处理mongodb数据
        Criteria criteria = Criteria.where("batch_id").is(batchCode);
        List<DBObject> list = list(criteria);
        if(list != null && list.size()>0){

            //批次主信息
            DBObject dBObject = list.get(0);
            mainBO.setBatchNo(batchCode);//计算批次号
            mainBO.setManagerNo(convert(dBObject,"mgr_id",String.class));//管理方编号
            mainBO.setManagerName(convert(dBObject,"mgr_name",String.class));//管理方名称
            String status = BatchNoStatus.close.getCode();//状态：已关账
            mainBO.setIncomeYearMonth(str2Date(convert(dBObject,"income_year_month",String.class),"yyyyMM"));//薪资期间
            int version_no = 1;//初始版本为1

            //todo  总税金、总人数、中方人数、外方人数

            //如果批次不存在，新增批次；否则更新批次主信息
            if(calculationBatchPO == null){
                newCal.setBatchNo(mainBO.getBatchNo());
                newCal.setManagerNo(mainBO.getManagerNo());
                newCal.setManagerName(mainBO.getManagerName());
                newCal.setStatus(status);
                newCal.setBatchType(this.getBatchType(closingMsg.getBatchType()));
                newCal.setParentBatchNo(convert(dBObject,"batch_ref_id",String.class));
                newCal.setVersionNo(version_no);
                this.calculationBatchService.insert(newCal);
            }else{
                newCal.setId(calculationBatchPO.getId());
                newCal.setBatchNo(mainBO.getBatchNo());
                newCal.setManagerNo(mainBO.getManagerNo());
                newCal.setManagerName(mainBO.getManagerName());
                newCal.setStatus(status);
                newCal.setBatchType(this.getBatchType(closingMsg.getBatchType()));
                newCal.setParentBatchNo(convert(dBObject,"batch_ref_id",String.class));
                newCal.setVersionNo(newCal.getVersionNo()+1);
                this.calculationBatchService.updateById(newCal);
            }

            //批次明细
            list.stream().forEach(item -> {
                //雇员基本信息
                DBObject empInfo = (DBObject)item.get("emp_info");

                empInfoBO.setEmployeeNo(convert(empInfo,"雇员编号",String.class));//雇员编号
                empInfoBO.setEmployeeName(convert(empInfo,"雇员名称",String.class));//雇员名称
                empInfoBO.setGender(convert(empInfo,"性别",String.class));//性别
                empInfoBO.setBirthday(str2Date(convert(empInfo,"出生日期",String.class),"yyyy-MM-dd"));//出生日期
                empInfoBO.setMobile(convert(empInfo,"联系电话",String.class));//联系电话
                empInfoBO.setCompanyNo(convert(empInfo,"公司编号",String.class));//公司编号
                empInfoBO.setEntryDate(str2Date(convert(empInfo,"入职日期",String.class),"yyyy-MM-dd"));//入职日期
                empInfoBO.setLeaveDate(str2Date(convert(empInfo,"离职日期",String.class),"yyyy-MM-dd"));//离职日期

                //个税信息
                DBObject taxInfo = (DBObject)empInfo.get("tax_info");
                taxInfoBO.setCertType(convert(taxInfo,"reportTaxCertId",Integer.class)==null? null :convert(taxInfo,"reportTaxCertId",Integer.class).toString());//报税证件类型
                taxInfoBO.setCertNo(convert(taxInfo,"reportTaxCertNo",String.class));//报税证件号
                taxInfoBO.setNationality(convert(taxInfo,"reportTaxCountryId",String.class));//国籍
                taxInfoBO.setDisability(convert(taxInfo,"isSpecialStatus",Boolean.class));//是否残疾烈属孤老
                taxInfoBO.setEmployee(convert(taxInfo,"isEmployee",Boolean.class));//是否雇员
                taxInfoBO.setInvestor(convert(taxInfo,"isPartner",Boolean.class));//是否股东投资者
                taxInfoBO.setOverseas(convert(taxInfo,"isForeign",Boolean.class));//是否境外人员
                taxInfoBO.setPersonalInvestment(convert(taxInfo,"preInvestAmount",BigDecimal.class));//个人股本(投资)额
                taxInfoBO.setChineseName(convert(taxInfo,"cnName",String.class));//外籍员工中文名
                taxInfoBO.setComingToChinaDate(convert(taxInfo,"arriveCnTime",Date.class));//来华时间
                //taxInfoBO.setTermOfService(convert(taxInfo,"officeTerm",Date.class));//任职期限
                taxInfoBO.setExpectedLeaveDate(convert(taxInfo,"prevLeaveTime",Date.class ));//预计离境时间
                taxInfoBO.setExpectedLeavePlace(convert(taxInfo,"prevLeavePlace",String.class));//预计离境地点
                taxInfoBO.setDomesticDuty(convert(taxInfo,"churchyardJob",String.class));//境内职务
                taxInfoBO.setOverseasDuty(convert(taxInfo,"overseasJob",String.class));//境外职务
                taxInfoBO.setPaymentPlace(convert(taxInfo,"payPlace",String.class));//支付地
                taxInfoBO.setPaymentOverseasPlace(convert(taxInfo,"overseasPayPlace",String.class));//境外支付地(国家/地区)
                taxInfoBO.setBurden(convert(taxInfo,"taxPayType",Integer.class )==null?null:convert(taxInfo,"taxPayType",Integer.class).toString());//税款负担方式
                taxInfoBO.setRecognitionCode(convert(taxInfo,"taxSuperCode",String.class));//税优识别码
                taxInfoBO.setAnnualPremium(convert(taxInfo,"annualPremium",BigDecimal.class));//年度保费
                taxInfoBO.setMonthlyPremium(convert(taxInfo,"monthlyPremium",BigDecimal.class));//月度保费
                taxInfoBO.setStockOptionCategory(convert(taxInfo,"stockOptionType",String.class));//股票期权收入类型
                taxInfoBO.setStockName(convert(taxInfo,"stockName",String.class));//股票名称(简称)
                taxInfoBO.setStockCode(convert(taxInfo,"stockCode",String.class));//股票代码
                taxInfoBO.setStockType(convert(taxInfo,"stockType",String.class));//股票类型
                taxInfoBO.setOptionMarketValue(convert(taxInfo,"optStrikePrice",BigDecimal.class ));//(期权性)行权股票的每股市场价
                taxInfoBO.setOptionExerciseValue(convert(taxInfo,"optPowerPrice",BigDecimal.class));//(期权性)股票期权支付的每股施权价
                taxInfoBO.setOptionQuantity(convert(taxInfo,"optStockAmount",Integer.class));//(期权性)股票数量
                taxInfoBO.setOptionExerciseIncome(convert(taxInfo,"optMonthlyStrikeIncome",BigDecimal.class));//(期权性)本月行权收入
                taxInfoBO.setIncrementExerciseValue(convert(taxInfo,"appStrikePrice",BigDecimal.class));//(增值性)行权日股票价格
                taxInfoBO.setIncrementImplementValue(convert(taxInfo,"appPowerPrice",BigDecimal.class));//(增值性)施权日股票价格
                taxInfoBO.setIncrementQuantity(convert(taxInfo,"appStockAmount",Integer.class));//(增值性)行权股票份数
                taxInfoBO.setIncrementExerciseIncome(convert(taxInfo,"appMonthlyStrikeIncome",BigDecimal.class));//(增值性)本月行权收入
                taxInfoBO.setRestrictRegisterValue(convert(taxInfo,"resRegistPrice",BigDecimal.class));//(限制性)股票登记日股票市价
                taxInfoBO.setRestrictRelieveValue(convert(taxInfo,"resJjPrice",BigDecimal.class));//(限制性)本批次解禁股票当日市价
                taxInfoBO.setRestrictRelieveQuantity(convert(taxInfo,"resJjAmount",Integer.class ));//(限制性)本批次解禁股票份数
                taxInfoBO.setRestrictTotal(convert(taxInfo,"resAllAmount",Integer.class));//(限制性)被激励对象获得的限制性股票总份数
                taxInfoBO.setRestrictPayment(convert(taxInfo,"resPayAmount",BigDecimal.class));//(限制性)被激励对象实际支付的资金总额
                taxInfoBO.setRestrictExerciseIncome(convert(taxInfo,"resMonthlyStrikeIncome",BigDecimal.class));//(限制性)本月行权收入
                taxInfoBO.setApplicableFormula(convert(taxInfo,"applicableFormula",String.class));//适用公式

                //服务协议
                DBObject empAgreement = (DBObject)empInfo.get("雇员服务协议");
                DBObject taxInfo_agreement = (DBObject)empAgreement.get("taxInfo");
                DBObject decAccount = (DBObject)taxInfo_agreement.get("declarationAccount");
                DBObject conAccount = (DBObject)taxInfo_agreement.get("contributionAccount");

                agreementBO.setDeclareAccount(convert(decAccount,"accountname",String.class));//申报账户
                agreementBO.setPayAccount(convert(conAccount,"accountname",String.class));//缴纳账户

                Integer taxPeriod = convert(taxInfo_agreement,"taxPeriod",Integer.class);//个税期间（0、1、2）
                if(taxPeriod != null && mainBO.getIncomeYearMonth()!=null){
                    agreementBO.setPeriod(DateTimeKit.offsiteMonth(mainBO.getIncomeYearMonth(),taxPeriod));//个税期间
                }
                List<Integer> taxServiceTypes = (List<Integer>)taxInfo_agreement.get("taxServiceType");
                taxServiceTypes.stream().forEach(taxServiceType -> {
                    if(taxServiceType==0){
                        agreementBO.setDeclare(true); //申报
                    }
                    if(taxServiceType==1){
                        agreementBO.setPay(true);//缴纳
                    }
                    if(taxServiceType==2){
                        agreementBO.setTransfer(true);//划款
                    }
                    if(taxServiceType==3){
                        agreementBO.setProof(true);//完税凭证
                    }
                });
                agreementBO.setSupport(convert(taxInfo_agreement,"isSupplier",Boolean.class));//是否供应商处理
                agreementBO.setReceiptAccount(convert(taxInfo_agreement,"supplierAccountReceivale",String.class));//供应商收款账户
                agreementBO.setSupportName(convert(taxInfo_agreement,"supplierName",String.class));//供应商名称
                //String supplierNo =
                List<Integer> supplierServiceTypes = (List<Integer>)taxInfo_agreement.get("supplierServiceType");
                supplierServiceTypes.stream().forEach(supplierServiceType -> {

                    if(supplierServiceType==0){
                        agreementBO.setDeclareSupported(true); //供应商申报
                    }
                    if(supplierServiceType==1){
                        agreementBO.setPaySupported(true);//供应商缴纳
                    }
                    if(supplierServiceType==2){
                        agreementBO.setTransferSupported(true);//供应商划款
                    }
                    if(supplierServiceType==3){
                        agreementBO.setProofSupported(true);//供应商完税凭证
                    }
                });

                //计算结果
                List<DBObject> results = (List<DBObject>)item.get("salary_calc_result_items");

                results.stream().forEach(result -> {

                    //薪资项(item_name,item_code,item_value)
                    String item_name = convert(result,"item_name",String.class);

                    if(StrKit.isNotEmpty(item_name)){
                        switch(item_name){
                            case "薪金个税" : calResultBO.setAmountSalary(convert(result,"item_value",BigDecimal.class));
                            case "劳务个税" : calResultBO.setAmountService(convert(result,"item_value",BigDecimal.class));
                            case "年终奖税" : calResultBO.setAmountBonus(convert(result,"item_value",BigDecimal.class));
                            case "离职金税" : calResultBO.setAmountLeave(convert(result,"item_value",BigDecimal.class));
                            case "股票期权税" : calResultBO.setAmountOption(convert(result,"item_value",BigDecimal.class));
                            case "利息、股息、红利所得税" : calResultBO.setAmountStock(convert(result,"item_value",BigDecimal.class));
                            case "偶然所得税" : calResultBO.setAmountAcc(convert(result,"item_value",BigDecimal.class));
                            case "收入额" : calResultBO.setIncomeTotal(convert(result,"item_value",BigDecimal.class));
                            case "免税所得" : calResultBO.setIncomeDutyfree(convert(result,"item_value",BigDecimal.class));
                            case "养老保险费合计_报税用" : calResultBO.setDeductRetirementInsurance(convert(result,"item_value",BigDecimal.class));
                            case "医疗保险费合计_报税用" : calResultBO.setDeductMedicalInsurance(convert(result,"item_value",BigDecimal.class));
                            case "失业保险费合计_报税用" : calResultBO.setDeductDlenessInsurance(convert(result,"item_value",BigDecimal.class));
                            case "住房公积金合计（报税用）" : calResultBO.setDeductHouseFund(convert(result,"item_value",BigDecimal.class));
                            case "允许扣除的税费" : calResultBO.setDeductTakeoff(convert(result,"item_value",BigDecimal.class));
                            case "企业年金个人部分" : calResultBO.setAnnuity(convert(result,"item_value",BigDecimal.class));
                            case "商业保险" : calResultBO.setBusinessHealthInsurance(convert(result,"item_value",BigDecimal.class));
                            case "税延养老保险费" : calResultBO.setEndowmentInsurance(convert(result,"item_value",BigDecimal.class));
                            case "免抵额" : calResultBO.setDeduction(convert(result,"item_value",BigDecimal.class));
                            case "准予扣除的捐赠额" : calResultBO.setDonation(convert(result,"item_value",BigDecimal.class));
                            case "减免税额" : calResultBO.setTaxDeduction(convert(result,"item_value",BigDecimal.class));
                            case "境内天数" : calResultBO.setDomesticDays(convert(result,"item_value",Integer.class));
                            case "境外天数" : calResultBO.setOverseasDays(convert(result,"item_value",Integer.class));
                            case "境内所得境内支付" : calResultBO.setDomesticIncomeDomesticPayment(convert(result,"item_value",BigDecimal.class));
                            case "境内所得境外支付" : calResultBO.setDomesticIncomeOverseasPayment(convert(result,"item_value",BigDecimal.class));
                            case "境外所得境内支付" : calResultBO.setOverseasIncomeDomesticPayment(convert(result,"item_value",BigDecimal.class));
                            case "境外所得境外支付" : calResultBO.setOverseasIncomeOverseasPayment(convert(result,"item_value",BigDecimal.class));
                            case "其它扣款_报税用" : calResultBO.setOtherDeductions(convert(result,"item_value",BigDecimal.class));
                            case "免税住房补贴" : calResultBO.setHousingSubsidy(convert(result,"item_value",BigDecimal.class));
                            case "免税伙食补贴" : calResultBO.setMealAllowance(convert(result,"item_value",BigDecimal.class));
                            case "免税洗衣费" : calResultBO.setLaundryFee(convert(result,"item_value",BigDecimal.class));
                            case "免税搬迁费" : calResultBO.setRemovingIndemnityFee(convert(result,"item_value",BigDecimal.class));
                            case "免税出差补贴" : calResultBO.setMissionallowance(convert(result,"item_value",BigDecimal.class));
                            case "免税探亲费" : calResultBO.setVisitingRelativesFee(convert(result,"item_value",BigDecimal.class));
                            case "免税语言培训费" : calResultBO.setLanguageTrainingFee(convert(result,"item_value",BigDecimal.class));
                            case "免税子女教育经费" : calResultBO.setEducationFunds(convert(result,"item_value",BigDecimal.class));
                            case "年度奖金" : calResultBO.setAnnualBonus(convert(result,"item_value",BigDecimal.class));
                            case "偶然所得" : calResultBO.setFortuitousIncome(convert(result,"item_value",BigDecimal.class));
                            case "利息、股息、红利所得" : calResultBO.setIncomeFromInterest(convert(result,"item_value",BigDecimal.class));
                            case "劳务费" : calResultBO.setServiceCharge(convert(result,"item_value",BigDecimal.class));
                            case "劳务费_允许扣除的税费" : calResultBO.setServiceDeductTakeoff(convert(result,"item_value",BigDecimal.class));
                            case "离职金" : calResultBO.setSeparationPayment(convert(result,"item_value",BigDecimal.class));
                            case "离职金免税额" : calResultBO.setSeparationPaymentTaxFee(convert(result,"item_value",BigDecimal.class));
                            case "实际工作年限数" : calResultBO.setWorkingYears(convert(result,"item_value",Integer.class));
                            case "本月行权收入" : calResultBO.setExerciseIncomeMonth(convert(result,"item_value",BigDecimal.class));
                            case "本年度累计行权收入(不含本月)" : calResultBO.setExerciseIncomeYear(convert(result,"item_value",BigDecimal.class));
                            case "规定月份数" : calResultBO.setNumberOfMonths(convert(result,"item_value",Integer.class));
                            case "本年累计已纳税额" : calResultBO.setExerciseTaxAmount(convert(result,"item_value",BigDecimal.class));
                        }
                    }

                });

                //根据税种生成明细
                //正常薪金税
                if(this.isExistTaxSalaryChinese(calResultBO, taxInfoBO)){
                    CalculationBatchDetailPO calculationBatchDetailPO = this.getBatchDetailPO( empInfoBO, taxInfoBO, agreementBO, newCal);
                    calculationBatchDetailPO.setTaxReal(calResultBO.getAmountSalary());//税金
                    calculationBatchDetailPO.setPeriod(this.UDateToLocalDate(agreementBO.getPeriod()));//所得期间
                    calculationBatchDetailPO.setIncomeSubject(IncomeSubject.NORMALSALARY.getCode());//所得项目
                    calculationBatchDetailPO.setIncomeTotal(calResultBO.getIncomeTotal());//收入额
                    calculationBatchDetailPO.setIncomeDutyfree(calResultBO.getIncomeDutyfree());//免税所得
                    calculationBatchDetailPO.setDeductRetirementInsurance(calResultBO.getDeductRetirementInsurance());//养老保险费合计_报税用
                    calculationBatchDetailPO.setDeductMedicalInsurance(calResultBO.getDeductMedicalInsurance());//医疗保险费合计_报税用
                    calculationBatchDetailPO.setDeductDlenessInsurance(calResultBO.getDeductDlenessInsurance());//失业保险费合计_报税用
                    calculationBatchDetailPO.setDeductHouseFund(calResultBO.getDeductHouseFund());//住房公积金合计（报税用）
                    calculationBatchDetailPO.setDeductTakeoff(calResultBO.getDeductTakeoff());//允许扣除的税费
                    calculationBatchDetailPO.setAnnuity(calResultBO.getAnnuity());//企业年金个人部分
                    calculationBatchDetailPO.setBusinessHealthInsurance(calResultBO.getAnnuity());//商业保险
                    calculationBatchDetailPO.setEndowmentInsurance(calResultBO.getEndowmentInsurance());//税延养老保险费
                    calculationBatchDetailPO.setDeduction(calResultBO.getDeduction().abs());//免抵额
                    calculationBatchDetailPO.setDonation(calResultBO.getDonation());//准予扣除的捐赠额
                    if(calResultBO.getAmountSalary()!=null && calResultBO.getTaxDeduction()!=null){//减免税额（薪金个税,减免税额相比取小）
                        if(calResultBO.getAmountSalary().compareTo(calResultBO.getTaxDeduction())==1){
                            calculationBatchDetailPO.setTaxDeduction(calResultBO.getTaxDeduction());
                        }else{
                            calculationBatchDetailPO.setTaxDeduction(calResultBO.getAmountSalary());
                        }
                    }
                    //其他（税前扣除项目）= 年金 + 商业健康保险费 + 税延养老保险费
                    calculationBatchDetailPO.setDeductOther(this.getValue(calculationBatchDetailPO.getAnnuity())
                            .add(this.getValue(calculationBatchDetailPO.getBusinessHealthInsurance()))
                            .add(this.getValue(calculationBatchDetailPO.getEndowmentInsurance())));
                    //合计（税前扣除项目）= 基本养老保险费 + 基本医疗保险费 + 失业保险费 + 住房公积金 + 财产原值 + 允许扣除的税费
                    calculationBatchDetailPO.setDeductTotal(this.getValue(calculationBatchDetailPO.getDeductRetirementInsurance())
                            .add(this.getValue(calculationBatchDetailPO.getDeductMedicalInsurance()))
                            .add(this.getValue(calculationBatchDetailPO.getDeductDlenessInsurance()))
                            .add(this.getValue(calculationBatchDetailPO.getDeductHouseFund()))
                            .add(this.getValue(calculationBatchDetailPO.getDeductProperty()))
                            .add(this.getValue(calculationBatchDetailPO.getDeductTakeoff())));
                    //应纳税所得额= 收入额 - 免税所得 - 合计 - 减除费用 - 准予扣除的捐赠额
                    calculationBatchDetailPO.setIncomeForTax(this.getValue(calculationBatchDetailPO.getIncomeTotal())
                            .subtract(this.getValue(calculationBatchDetailPO.getIncomeDutyfree()))
                            .subtract(this.getValue(calculationBatchDetailPO.getDeductTotal()))
                            .subtract(this.getValue(calculationBatchDetailPO.getDeduction()))
                            .subtract(this.getValue(calculationBatchDetailPO.getDonation())));
                    //应纳税额 = 税金
                    calculationBatchDetailPO.setTaxAmount(calculationBatchDetailPO.getTaxReal());
                    //应扣缴税额 = 应纳税额 - 减免税额
                    calculationBatchDetailPO.setTaxWithholdAmount(this.getValue(calculationBatchDetailPO.getTaxAmount()).subtract(this.getValue(calculationBatchDetailPO.getTaxDeduction())));
                    calculationBatchDetailPO.setBatchNo(newCal.getBatchNo());//批次号
                    this.addDetailPO( calculationBatchDetailPO, taxInfoBO, empInfoBO, agreementBO);
                }
                //外籍人员正常薪金税
                if(this.isExistTaxSalaryForeigner( calResultBO,taxInfoBO)){
                    CalculationBatchDetailPO calculationBatchDetailPO = this.getBatchDetailPO(empInfoBO, taxInfoBO, agreementBO, newCal);
                    calculationBatchDetailPO.setTaxReal(calResultBO.getAmountSalary());//税金
                    calculationBatchDetailPO.setPeriod(this.UDateToLocalDate(agreementBO.getPeriod()));//所得期间
                    calculationBatchDetailPO.setIncomeSubject(IncomeSubject.FOREIGNNORMALSALARY.getCode());//所得项目
                    calculationBatchDetailPO.setDomesticDays(calResultBO.getDomesticDays());//境内天数
                    calculationBatchDetailPO.setOverseasDays(calResultBO.getOverseasDays());//境外天数
                    calculationBatchDetailPO.setDomesticIncomeDomesticPayment(calResultBO.getDomesticIncomeDomesticPayment());//境内所得境内支付
                    calculationBatchDetailPO.setDomesticIncomeOverseasPayment(calResultBO.getDomesticIncomeOverseasPayment());//境内所得境外支付
                    calculationBatchDetailPO.setOverseasIncomeDomesticPayment(calResultBO.getOverseasIncomeDomesticPayment());//境外所得境内支付
                    calculationBatchDetailPO.setOverseasIncomeOverseasPayment(calResultBO.getOverseasIncomeOverseasPayment());//境外所得境外支付
                    calculationBatchDetailPO.setDeductRetirementInsurance(calResultBO.getDeductRetirementInsurance());//养老保险费合计_报税用
                    calculationBatchDetailPO.setDeductMedicalInsurance(calResultBO.getDeductMedicalInsurance());//医疗保险费合计_报税用
                    calculationBatchDetailPO.setDeductDlenessInsurance(calResultBO.getDeductDlenessInsurance());//失业保险费合计_报税用
                    calculationBatchDetailPO.setDeductHouseFund(calResultBO.getDeductHouseFund());//住房公积金合计（报税用）
                    calculationBatchDetailPO.setDeductTakeoff(calResultBO.getDeductTakeoff());//允许扣除的税费
                    calculationBatchDetailPO.setAnnuity(calResultBO.getAnnuity());//企业年金个人部分
                    calculationBatchDetailPO.setDeductOther(calResultBO.getOtherDeductions());//其它扣款_报税用
                    calculationBatchDetailPO.setHousingSubsidy(calResultBO.getHousingSubsidy());//免税住房补贴
                    calculationBatchDetailPO.setMealAllowance(calResultBO.getMealAllowance());//免税伙食补贴
                    calculationBatchDetailPO.setLaundryFee(calResultBO.getLaundryFee());//免税洗衣费
                    calculationBatchDetailPO.setRemovingIndemnityFee(calResultBO.getRemovingIndemnityFee());//免税搬迁费
                    calculationBatchDetailPO.setMissionallowance(calResultBO.getMissionallowance());//免税出差补贴
                    calculationBatchDetailPO.setVisitingRelativesFee(calResultBO.getVisitingRelativesFee());//免税探亲费
                    calculationBatchDetailPO.setLanguageTrainingFee(calResultBO.getLanguageTrainingFee());//免税语言培训费
                    calculationBatchDetailPO.setEducationFunds(calResultBO.getEducationFunds());//子女教育经费
                    calculationBatchDetailPO.setDonation(calResultBO.getDonation());//准予扣除的捐赠额
                    if(calResultBO.getAmountSalary()!=null && calResultBO.getTaxDeduction()!=null){//减免税额（薪金个税,减免税额相比取小）
                        if(calResultBO.getAmountSalary().compareTo(calResultBO.getTaxDeduction())==1){
                            calculationBatchDetailPO.setTaxDeduction(calResultBO.getTaxDeduction());
                        }else{
                            calculationBatchDetailPO.setTaxDeduction(calResultBO.getAmountSalary());
                        }
                    }
                    //其他（税前扣除项目）= 年金 + 商业健康保险费 + 税延养老保险费
                    calculationBatchDetailPO.setDeductOther(this.getValue(calculationBatchDetailPO.getAnnuity())
                            .add(this.getValue(calculationBatchDetailPO.getBusinessHealthInsurance()))
                            .add(this.getValue(calculationBatchDetailPO.getEndowmentInsurance())));
                    //合计（税前扣除项目）= 基本养老保险费 + 基本医疗保险费 + 失业保险费 + 住房公积金 + 财产原值 + 允许扣除的税费
                    calculationBatchDetailPO.setDeductTotal(this.getValue(calculationBatchDetailPO.getDeductRetirementInsurance())
                            .add(this.getValue(calculationBatchDetailPO.getDeductMedicalInsurance()))
                            .add(this.getValue(calculationBatchDetailPO.getDeductDlenessInsurance()))
                            .add(this.getValue(calculationBatchDetailPO.getDeductHouseFund()))
                            .add(this.getValue(calculationBatchDetailPO.getDeductProperty()))
                            .add(this.getValue(calculationBatchDetailPO.getDeductTakeoff())));
                    //应纳税所得额= 收入额 - 免税所得 - 合计 - 减除费用 - 准予扣除的捐赠额
                    calculationBatchDetailPO.setIncomeForTax(this.getValue(calculationBatchDetailPO.getIncomeTotal())
                            .subtract(this.getValue(calculationBatchDetailPO.getIncomeDutyfree()))
                            .subtract(this.getValue(calculationBatchDetailPO.getDeductTotal()))
                            .subtract(this.getValue(calculationBatchDetailPO.getDeduction()))
                            .subtract(this.getValue(calculationBatchDetailPO.getDonation())));
                    //应纳税额 = 税金
                    calculationBatchDetailPO.setTaxAmount(calculationBatchDetailPO.getTaxReal());
                    //应扣缴税额 = 应纳税额 - 减免税额
                    calculationBatchDetailPO.setTaxWithholdAmount(this.getValue(calculationBatchDetailPO.getTaxAmount()).subtract(this.getValue(calculationBatchDetailPO.getTaxDeduction())));
                    calculationBatchDetailPO.setBatchNo(newCal.getBatchNo());//批次号
                    this.addDetailPO( calculationBatchDetailPO, taxInfoBO, empInfoBO, agreementBO);
                }
                //年奖税
                if(calResultBO.getAmountBonus()!=null && calResultBO.getAmountBonus().compareTo(BigDecimal.ZERO)==1){
                    CalculationBatchDetailPO calculationBatchDetailPO = this.getBatchDetailPO( empInfoBO, taxInfoBO, agreementBO, newCal);
                    calculationBatchDetailPO.setTaxReal(calResultBO.getAmountBonus());//税金
                    calculationBatchDetailPO.setPeriod(this.UDateToLocalDate(agreementBO.getPeriod()));//所得期间
                    calculationBatchDetailPO.setIncomeSubject(IncomeSubject.BONUSINCOMEYEAR.getCode());//所得项目
                    calculationBatchDetailPO.setIncomeTotal(calResultBO.getAnnualBonus());//年度奖金
                    //减免税额（在薪金个税<减免税额时，年终奖税和（减免税额-薪金个税）相比取小）
                    if(calResultBO.getAmountSalary()!=null && calResultBO.getAmountBonus()!=null && calResultBO.getTaxDeduction()!=null){
                        if(calResultBO.getAmountSalary().compareTo(calResultBO.getTaxDeduction())==-1){
                            if(calResultBO.getAmountBonus().compareTo(calResultBO.getTaxDeduction().subtract(calResultBO.getAmountSalary()))==-1){
                                calResultBO.setTaxDeduction(calResultBO.getAmountBonus());
                            }else{
                                calResultBO.setTaxDeduction(calResultBO.getTaxDeduction().subtract(calResultBO.getAmountSalary()));
                            }
                        }
                    }
                    calculationBatchDetailPO.setBatchNo(newCal.getBatchNo());//批次号
                    this.addDetailPO( calculationBatchDetailPO, taxInfoBO, empInfoBO, agreementBO);
                }
                //劳务税
                if(calResultBO.getAmountService()!=null && calResultBO.getAmountService().compareTo(BigDecimal.ZERO)==1
                        && taxInfoBO.getEmployee()!=null && !(taxInfoBO.getEmployee())){
                    CalculationBatchDetailPO calculationBatchDetailPO = this.getBatchDetailPO(empInfoBO, taxInfoBO, agreementBO, newCal);
                    calculationBatchDetailPO.setTaxReal(calResultBO.getAmountService());//税金
                    calculationBatchDetailPO.setPeriod(this.UDateToLocalDate(agreementBO.getPeriod()));//所得期间
                    calculationBatchDetailPO.setIncomeSubject(IncomeSubject.LABORINCOME.getCode());//所得项目
                    calculationBatchDetailPO.setIncomeTotal(calResultBO.getServiceCharge());//劳务费
                    calculationBatchDetailPO.setDeductTakeoff(calResultBO.getServiceDeductTakeoff());//劳务费_允许扣除的税费
                    calculationBatchDetailPO.setTaxDeduction(calResultBO.getTaxDeduction()==null?BigDecimal.ZERO:calResultBO.getTaxDeduction().abs());//减免税额
                    calculationBatchDetailPO.setBatchNo(newCal.getBatchNo());//批次号
                    this.addDetailPO( calculationBatchDetailPO, taxInfoBO, empInfoBO, agreementBO);
                }
                //离职金税
                if(calResultBO.getSeparationPayment()!=null && calResultBO.getSeparationPayment().compareTo(BigDecimal.ZERO)==1){
                    CalculationBatchDetailPO calculationBatchDetailPO = this.getBatchDetailPO(empInfoBO, taxInfoBO, agreementBO, newCal);
                    calculationBatchDetailPO.setTaxReal(calResultBO.getAmountLeave());//税金
                    calculationBatchDetailPO.setPeriod(this.UDateToLocalDate(agreementBO.getPeriod()));//所得期间
                    calculationBatchDetailPO.setIncomeSubject(IncomeSubject.LABORCONTRACT.getCode());//所得项目
                    calculationBatchDetailPO.setIncomeTotal(calResultBO.getSeparationPayment());//离职金
                    //免税所得 : 当离职金 ≥ 离职金免税额时，=离职金免税额；当离职金<离职金免税额时，=离职金
                    if(calResultBO.getSeparationPayment()!=null && calResultBO.getSeparationPaymentTaxFee()!=null
                            && calResultBO.getSeparationPayment().compareTo(calResultBO.getSeparationPaymentTaxFee())==-1){
                        calculationBatchDetailPO.setIncomeDutyfree(calResultBO.getSeparationPayment());
                    }else{
                        calculationBatchDetailPO.setIncomeDutyfree(calResultBO.getSeparationPaymentTaxFee());
                    }
                    calculationBatchDetailPO.setWorkingYears(calResultBO.getWorkingYears());//实际工作年限数
                    calculationBatchDetailPO.setBatchNo(newCal.getBatchNo());//批次号
                    this.addDetailPO( calculationBatchDetailPO, taxInfoBO, empInfoBO, agreementBO);
                }
                //利息、股息、红利所得税
                if(calResultBO.getIncomeFromInterest()!=null && calResultBO.getIncomeFromInterest().compareTo(BigDecimal.ZERO)==1){
                    CalculationBatchDetailPO calculationBatchDetailPO = this.getBatchDetailPO(empInfoBO, taxInfoBO, agreementBO, newCal);
                    calculationBatchDetailPO.setTaxReal(calResultBO.getAmountStock());//税金
                    calculationBatchDetailPO.setPeriod(this.UDateToLocalDate(agreementBO.getPeriod()));//所得期间
                    calculationBatchDetailPO.setIncomeSubject(IncomeSubject.IDDINCOME.getCode());//所得项目
                    calculationBatchDetailPO.setIncomeTotal(calResultBO.getIncomeFromInterest());//利息、股息、红利所得
                    calculationBatchDetailPO.setBatchNo(newCal.getBatchNo());//批次号
                    this.addDetailPO( calculationBatchDetailPO, taxInfoBO, empInfoBO, agreementBO);
                }
                //偶然所得税
                if(calResultBO.getFortuitousIncome()!=null && calResultBO.getFortuitousIncome().compareTo(BigDecimal.ZERO)==1){
                    CalculationBatchDetailPO calculationBatchDetailPO = this.getBatchDetailPO(empInfoBO, taxInfoBO, agreementBO, newCal);
                    calculationBatchDetailPO.setTaxReal(calResultBO.getAmountAcc());//税金
                    calculationBatchDetailPO.setPeriod(this.UDateToLocalDate(agreementBO.getPeriod()));//所得期间
                    calculationBatchDetailPO.setIncomeSubject(IncomeSubject.FORTUITOUSINCOME.getCode());//所得项目
                    calculationBatchDetailPO.setIncomeTotal(calResultBO.getFortuitousIncome());//偶然所得
                    calculationBatchDetailPO.setBatchNo(newCal.getBatchNo());//批次号
                    this.addDetailPO( calculationBatchDetailPO, taxInfoBO, empInfoBO, agreementBO);
                }
                //股票期权税
                if(calResultBO.getExerciseIncomeMonth()!=null && calResultBO.getExerciseIncomeMonth().compareTo(BigDecimal.ZERO)==1){
                    CalculationBatchDetailPO calculationBatchDetailPO = this.getBatchDetailPO(empInfoBO, taxInfoBO, agreementBO, newCal);
                    calculationBatchDetailPO.setTaxReal(calResultBO.getAmountOption());//税金
                    calculationBatchDetailPO.setPeriod(this.UDateToLocalDate(agreementBO.getPeriod()));//所得期间
                    calculationBatchDetailPO.setIncomeSubject(IncomeSubject.STOCKOPTIONINCOME.getCode());//所得项目
                    calculationBatchDetailPO.setIncomeTotal(calResultBO.getExerciseIncomeMonth());//本月行权收入
                    calculationBatchDetailPO.setExerciseIncomeYear(calResultBO.getExerciseIncomeYear());//本年度累计行权收入(不含本月)
                    calculationBatchDetailPO.setNumberOfMonths(calResultBO.getNumberOfMonths());//规定月份数
                    calculationBatchDetailPO.setExerciseTaxAmount(calResultBO.getExerciseTaxAmount());//本年累计已纳税额
                    calculationBatchDetailPO.setBatchNo(newCal.getBatchNo());//批次号
                    this.addDetailPO( calculationBatchDetailPO, taxInfoBO, empInfoBO, agreementBO);
                }

            });
        }
    }

    //取消关账处理
    @Transactional(rollbackFor = Exception.class)
    public void cancelBatch(CancelClosingMsg cancelClosingMsg){
        if(cancelClosingMsg!=null && StrKit.isNotEmpty(cancelClosingMsg.getBatchCode())){

            //批次查询条件
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.eq("batch_no",cancelClosingMsg.getBatchCode());

            //根据批次号查询批次
            CalculationBatchPO calculationBatchPO = calculationBatchService.selectOne(wrapper);

            //状态为已关账，则可以取消
            if(calculationBatchPO!=null && calculationBatchPO.getStatus().equals(BatchNoStatus.close.getCode())){
                calculationBatchPO.setStatus(BatchNoStatus.unclose.getCode());
                calculationBatchService.updateById(calculationBatchPO);
                this.cancelBatchInfos(calculationBatchPO.getId());
            }
        }
    }

    //删除批次相关信息
    private void cancelBatchInfos(Long batchId){
        this.calculationBatchMapper.cancelDetailsByCalBatchId(batchId);//删除批次明细
        this.calculationBatchMapper.cancelInfosByCalBatchId(batchId);//删除个税信息
        this.calculationBatchMapper.cancelServicesByCalBatchId(batchId);//删除服务信息
    }

    //取值
    private <T> T convert(DBObject dBObject,String key,Class<T> clazz){

        try {

            //key不存在的处理
            if(!dBObject.containsField(key) || dBObject.get(key) == null){
                if(clazz==BigDecimal.class){
                    return clazz.cast(BigDecimal.ZERO);
                }else{
                    return null;
                }
            }

            //各类型的处理
            if(dBObject.get(key).getClass()==Double.class && clazz == BigDecimal.class){
                return clazz.cast(new BigDecimal((Double)dBObject.get(key)));
            }else if(dBObject.get(key).getClass()==String.class && clazz == BigDecimal.class){
                return clazz.cast(new BigDecimal((String)dBObject.get(key)));
            }else if(dBObject.get(key).getClass()==String.class && clazz == Integer.class){
                return clazz.cast(new Integer((String)dBObject.get(key)));
            }else if(dBObject.get(key).getClass()==String.class && clazz == Boolean.class){
                return clazz.cast(new Boolean((String)dBObject.get(key)));
            }else if(dBObject.get(key).getClass()==String.class && clazz == String.class){
                if(((String)dBObject.get(key)).trim().equals("null")){
                    return null;
                }else{
                    return clazz.cast(dBObject.get(key));
                }
            }else{
                return clazz.cast(dBObject.get(key));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 字符转日期
     * @param dateStr
     * @param format
     * @return
     */
    private Date str2Date(String dateStr,String format){

        if(StrKit.isEmpty(dateStr)){
            return null;
        }

        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        try {
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //是否存在正常薪金税（中方）
    private boolean isExistTaxSalaryChinese(CalResultBO calResultBO,TaxInfoBO taxInfoBO){

        boolean flag = false;

        if(calResultBO.getDeduction()!=null
                && calResultBO.getDeduction().abs().compareTo(new BigDecimal(3500))==0
                && taxInfoBO.getEmployee()!=null &&  taxInfoBO.getEmployee()
                && calResultBO.getIncomeTotal()!=null
                && (calResultBO.getIncomeTotal().compareTo(BigDecimal.ZERO)==1 || (calResultBO.getIncomeTotal().compareTo(BigDecimal.ZERO)==0
                        && calResultBO.getAmountService()==null && calResultBO.getAmountLeave()==null
                        && calResultBO.getAmountStock()==null && calResultBO.getAmountAcc()==null
                        && calResultBO.getAmountOption()==null))){
            flag = true;
        }

        return flag;
    }

    //是否存在正常薪金税（外籍）
    private boolean isExistTaxSalaryForeigner(CalResultBO calResultBO,TaxInfoBO taxInfoBO){

        boolean flag = false;

        if(calResultBO.getDeduction()!=null
                && calResultBO.getDeduction().abs().compareTo(new BigDecimal(4800))==0
                && taxInfoBO.getEmployee()!=null &&  taxInfoBO.getEmployee()
                && calResultBO.getIncomeTotal()!=null
                && (calResultBO.getIncomeTotal().compareTo(BigDecimal.ZERO)==1 || (calResultBO.getIncomeTotal().compareTo(BigDecimal.ZERO)==0
                        && calResultBO.getAmountService()==null && calResultBO.getAmountLeave()==null
                        && calResultBO.getAmountStock()==null && calResultBO.getAmountAcc()==null
                        && calResultBO.getAmountOption()==null))){
            flag = true;
        }

        return flag;
    }

    //批次明细PO对象
    private CalculationBatchDetailPO getBatchDetailPO(EmpInfoBO empInfoBO,TaxInfoBO taxInfoBO,AgreementBO agreementBO,CalculationBatchPO newCal){
        CalculationBatchDetailPO calculationBatchDetailPO = new CalculationBatchDetailPO();
        calculationBatchDetailPO.setEmployeeNo(empInfoBO.getEmployeeNo());//雇员编号
        calculationBatchDetailPO.setEmployeeName(empInfoBO.getEmployeeName());//雇员名称
        calculationBatchDetailPO.setIdType(taxInfoBO.getCertType());//报税证件类型
        calculationBatchDetailPO.setIdNo(taxInfoBO.getCertNo());//报税证件号
        calculationBatchDetailPO.setDeclareAccount(agreementBO.getDeclareAccount());//申报账户
        calculationBatchDetailPO.setPayAccount(agreementBO.getPayAccount());//缴纳账户
        calculationBatchDetailPO.setCalculationBatchId(newCal.getId());//批次id
        return calculationBatchDetailPO;
    }

    //个税信息PO对象
    private EmployeeInfoBatchPO getInfoBatchPO(TaxInfoBO taxInfoBO,EmpInfoBO empInfoBO){
        EmployeeInfoBatchPO employeeInfoBatchPO = new EmployeeInfoBatchPO();
        BeanUtils.copyProperties(taxInfoBO, employeeInfoBatchPO);
        BeanUtils.copyProperties(empInfoBO, employeeInfoBatchPO);
        return employeeInfoBatchPO;
    }

    //服务协议PO对象
    private EmployeeServiceBatchPO getServiceBatchPO(AgreementBO agreementBO,EmpInfoBO empInfoBO){
        EmployeeServiceBatchPO employeeServiceBatchPO = new EmployeeServiceBatchPO();
        BeanUtils.copyProperties(agreementBO, employeeServiceBatchPO);
        employeeServiceBatchPO.setEmployeeNo(empInfoBO.getEmployeeNo());
        return employeeServiceBatchPO;
    }

    //新增批次明细及相关PO
    private void addDetailPO(CalculationBatchDetailPO calculationBatchDetailPO,TaxInfoBO taxInfoBO,EmpInfoBO empInfoBO,AgreementBO agreementBO){
        this.calculationBatchDetailService.insert(calculationBatchDetailPO);//新增批次明细
        EmployeeInfoBatchPO employeeInfoBatchPO = this.getInfoBatchPO( taxInfoBO, empInfoBO);
        EmployeeServiceBatchPO employeeServiceBatchPO = this.getServiceBatchPO( agreementBO, empInfoBO);
        employeeInfoBatchPO.setCalBatchDetailId(calculationBatchDetailPO.getId());
        employeeServiceBatchPO.setCalBatchDetailId(calculationBatchDetailPO.getId());
        employeeInfoBatchMapper.insert(employeeInfoBatchPO);//新增个税信息
        employeeServiceBatchMapper.insert(employeeServiceBatchPO);//新增服务协议
    }

    private String getBatchType(int type){

        String batchType = null;

        if(type==1){
            batchType = BatchType.normal.getCode();
        }else if(type==2){
            batchType = BatchType.ajust.getCode();
        }else if(type==3){
            batchType = BatchType.backdate.getCode();
        }

        return batchType;
    }

    //java.util.Date --> java.time.LocalDate
    private LocalDate UDateToLocalDate(Date date) {
        if(date!=null){
            Instant instant = date.toInstant();
            ZoneId zone = ZoneId.systemDefault();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
            return localDateTime.toLocalDate();
        }
        return null;
    }

    //对象null返回0
    private BigDecimal getValue(BigDecimal amount){

        BigDecimal bd = BigDecimal.ZERO;
        if(amount != null ){
            bd = amount;
        }
        return bd;

    }
}
