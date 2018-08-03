package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gt1.CalResultMongoOpt;
import com.ciicsh.gt1.exchangerate.Currencies;
import com.ciicsh.gt1.exchangerate.ExchangeManager;
import com.ciicsh.gto.companycenter.webcommandservice.api.CycleRuleProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmpFcBankProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmployeeGrantRuleProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.EmployeeGrantRuleDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.CmyFcBankCardRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.EmployeeGrantRuleRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.CmyFcBankCardResponseDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.CycleRuleResponseDTO;
import com.ciicsh.gto.fcbusinesscenter.entity.ClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.EmployeeBankcardDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.EmployeeServiceAgreementDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.PayrollCalcResultDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantRuleDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.JsonParseConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantMainTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.CalcResultItemBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.logservice.client.LogClientService;
import com.ciicsh.gto.salarymanagementcommandservice.api.BatchProxy;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrBatchDTO;
import com.ciicsh.gto.salecenter.apiservice.api.proxy.ManagementProxy;
import com.ciicsh.gto.settlementcenter.gathering.queryapi.ExchangeProxy;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 薪资发放任务单处理 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-01-22
 */
@Service
public class SalaryGrantTaskProcessServiceImpl extends ServiceImpl<SalaryGrantMainTaskMapper, SalaryGrantMainTaskPO> implements SalaryGrantTaskProcessService {
    @Autowired
    SalaryGrantMainTaskMapper salaryGrantMainTaskMapper;
    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;
    @Autowired
    CommonService commonService;
    @Autowired
    SalaryGrantSubTaskWorkFlowService salaryGrantSubTaskWorkFlowService;
    @Autowired
    CalResultMongoOpt calResultMongoOpt;
    @Autowired
    ManagementProxy managementProxy;
    @Autowired
    BatchProxy batchProxy;
    @Autowired
    LogClientService logClientService;
    @Autowired
    CycleRuleProxy cycleRuleProxy;
    @Autowired
    EmpFcBankProxy empFcBankProxy;
    @Autowired
    EmployeeGrantRuleProxy employeeGrantRuleProxy;
    @Autowired
    ExchangeProxy exchangeProxy;
    @Autowired
    SalaryGrantEmployeeService salaryGrantEmployeeService;
    @Autowired
    SalaryGrantEmployeeCommandService salaryGrantEmployeeCommandService;

    @Override
    public void closing(ClosingMsg closingMsg) {
        // 接收关账参数并设置主任务单查询条件
        SalaryGrantMainTaskPO param = BeanUtils.instantiate(SalaryGrantMainTaskPO.class);
        param.setBatchCode(closingMsg.getBatchCode());
        param.setGrantType(closingMsg.getBatchType());
        param.setTaskType(SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK);
        param.setActive(true);
        //查询主任务单
        SalaryGrantMainTaskPO salaryGrantMainTaskPO = this.getSalaryGrantMainTaskPO(param);
        if(!ObjectUtils.isEmpty(salaryGrantMainTaskPO) && Long.valueOf(closingMsg.getVersion()).compareTo(salaryGrantMainTaskPO.getBatchVersion()) > 0) {
            salaryGrantMainTaskPO.setBatchVersion(closingMsg.getVersion());
            this.modifySalaryGrantMainTask(salaryGrantMainTaskPO);
        } else {
            //设置关账版本号
            param.setBatchVersion(closingMsg.getVersion());
            this.createSalaryGrantMainTask(param);
        }
    }

    /**
      2018-06-26 最新调整
      对于已存在的任务单主表信息（任务单状态为5-待生成），关账动作，相当于计算引擎重新计算后重新发送计算结果数据，
      发放模块的任务单编号不变，任务单中的汇总数据和雇员信息是重新从mongodb获取的。
      因此修改任务单方法中，包括修改任务单汇总数据、修改version、修改任务单状态（从5-待生成改为0-草稿），重新生成发放雇员数据。
   */
    public void modifySalaryGrantMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO) {
        // 重新从mongodb获取雇员信息
        // 根据重算批次的计算信息进行汇总到任务单主表,修改薪资发放任务单
        boolean isModified = this.modifyMainTask(salaryGrantMainTaskPO);
        if(isModified){
            // 重新生成发放雇员数据
            List<PayrollCalcResultDTO> payrollCalcResultDTOList = this.listPayrollCalcResult(salaryGrantMainTaskPO);
            this.createEmployee(salaryGrantMainTaskPO,payrollCalcResultDTOList);
        }else{
            logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("修改薪资发放任务单主表信息，计算批次号为："+salaryGrantMainTaskPO.getBatchCode()).setContent("修改薪资发放任务单记录失败，导致无法生成薪资发放雇员信息！"));
        }
    }

    public void createSalaryGrantMainTask(SalaryGrantMainTaskPO po) {
        // 2、查询批次计算结果
        List<PayrollCalcResultDTO> payrollCalcResultDTOList = this.listPayrollCalcResult(po);
        // 3、创建薪资发放任务单
        boolean isCreated = this.createMainTask(po, payrollCalcResultDTOList);
        // 4、生成薪资发放雇员信息
        if(isCreated){
            this.createEmployee(po, payrollCalcResultDTOList);
        }else{
            logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("新增薪资发放任务单主表信息，计算批次号为："+po.getBatchCode()).setContent("插入薪资发放任务单记录失败，导致无法生成薪资发放雇员信息！"));
        }
    }

    /**
     * 新增薪资发放任务单主表信息
     * @param salaryGrantMainTaskPO
     * @return SalaryGrantMainTaskPO
     */
    @Transactional(rollbackFor = Exception.class)
    protected boolean createMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO,List<PayrollCalcResultDTO> payrollCalcResultDTOList){
        if(!CollectionUtils.isEmpty(payrollCalcResultDTOList)){
            // 1、 根据批次编号batch_code，批次对应的计算类型（正常、调整、回溯），查询批次表信息
            // 调用计算引擎提供查询批次信息的接口方法
            PrBatchDTO PrBatchDTO = batchProxy.getBatchInfo(salaryGrantMainTaskPO.getBatchCode(), salaryGrantMainTaskPO.getGrantType());
            salaryGrantMainTaskPO = this.convertBatchInfoToSalaryGrantMainTask(salaryGrantMainTaskPO, PrBatchDTO);
            // 3、解析雇员计算结果数据，过滤薪资发放的雇员信息，汇总相关数据。
            salaryGrantMainTaskPO = this.toResolvePayrollCalcResultForTask(payrollCalcResultDTOList, salaryGrantMainTaskPO);
            // 4、生成薪资发放任务单的entity_id
            Map entityParam = new HashMap(2);
            // 薪资发放任务单编号, 在EntityID编号规则中进行定义的前缀。写在常量类中。
            entityParam.put("idCode",SalaryGrantBizConsts.SALARY_GRANT_MAIN_TASK_ENTITY_PREFIX);
            String entityId = commonService.getEntityIdForSalaryGrantTask(entityParam);
            if(!StringUtils.isEmpty(entityId)){
                salaryGrantMainTaskPO.setSalaryGrantMainTaskCode(entityId);
            }else{
                logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("新增薪资发放任务单主表信息，计算批次号为："+salaryGrantMainTaskPO.getBatchCode()).setContent("调用公共服务生成entity_id失败！"));
                return false;
            }
            salaryGrantMainTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_DRAFT);
            salaryGrantMainTaskPO.setTaskType(SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK);
            // 5、新建薪资发放任务单，把查询的批次业务表中数据赋值到主表明细对应字段中。
            boolean insertFlag = this.insertOrUpdateSalaryGrantMainTask(salaryGrantMainTaskPO);
            if(!insertFlag){
                logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("新增薪资发放任务单主表信息，计算批次号为："+salaryGrantMainTaskPO.getBatchCode()).setContent("插入薪资发放任务单记录失败！"));
                return false;
            }
        }else{
            logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("新增薪资发放任务单主表信息，计算批次号为："+salaryGrantMainTaskPO.getBatchCode()).setContent("查询批次计算结果为空，生成薪资发放任务单失败！"));
            return false;
        }
        return true;
    }

    /**
     * 查询mongo薪资发放数据
     * @param po
     * @return
     */
    private List<PayrollCalcResultDTO> listPayrollCalcResult(SalaryGrantMainTaskPO po) {
        List<PayrollCalcResultDTO> payrollCalcResultDTOList = new ArrayList<PayrollCalcResultDTO>();
        // 调用计算引擎提供的mongodb数据库查询工具类
        List<DBObject> list  = calResultMongoOpt.list(Criteria.where("batch_id").is(po.getBatchCode()).and("batch_type").is(po.getGrantType()));
        if (list != null && list.size() > 0) {
            list.forEach(dbObject ->{
                PayrollCalcResultDTO payrollCalcResultDTO = this.toConvertToPayrollCalcResultDTO(dbObject);
                payrollCalcResultDTOList.add(payrollCalcResultDTO);
            });
        }
        return payrollCalcResultDTOList;
    }

    private PayrollCalcResultDTO toConvertToPayrollCalcResultDTO(DBObject dbObject){
        PayrollCalcResultDTO payrollCalcResultDTO = new PayrollCalcResultDTO();
        payrollCalcResultDTO.setBatchId(ObjectUtils.isEmpty(dbObject.get("batch_id"))  ? "" : (String)dbObject.get("batch_id"));
        payrollCalcResultDTO.setRefBatchId(ObjectUtils.isEmpty(dbObject.get("ref_batch_id")) ? "" : (String)dbObject.get("ref_batch_id"));
        payrollCalcResultDTO.setBatchType(ObjectUtils.isEmpty(dbObject.get("batch_type")) ? null : (Integer) dbObject.get("batch_type"));
        payrollCalcResultDTO.setMgrId(ObjectUtils.isEmpty(dbObject.get("mgr_id")) ? "" : (String)dbObject.get("mgr_id"));
        payrollCalcResultDTO.setMgrName(ObjectUtils.isEmpty(dbObject.get("mgr_name")) ? "" : (String)dbObject.get("mgr_name"));
        payrollCalcResultDTO.setPersonnelIncomeYearMonth(ObjectUtils.isEmpty(dbObject.get("income_year_month")) ? "" : (String)dbObject.get("income_year_month"));
        payrollCalcResultDTO.setTaxYearMonth(ObjectUtils.isEmpty(dbObject.get("tax_year_month")) ? "" : (String)dbObject.get("tax_year_month"));
        payrollCalcResultDTO.setSalaryCalcResultItems(ObjectUtils.isEmpty(dbObject.get("salary_calc_result_items") ) ? null : (List<CalcResultItemBO>)dbObject.get("salary_calc_result_items"));
        DBObject empInfo = (DBObject) (dbObject.get("emp_info"));
        payrollCalcResultDTO.setEmpId(ObjectUtils.isEmpty(empInfo.get(JsonParseConsts.EMPLOYEE_CODE_CN)) ? "" : (String)empInfo.get(JsonParseConsts.EMPLOYEE_CODE_CN));
        payrollCalcResultDTO.setEmpName(ObjectUtils.isEmpty(empInfo.get(JsonParseConsts.EMPLOYEE_NAME_CN)) ? "" : (String)empInfo.get(JsonParseConsts.EMPLOYEE_NAME_CN));
        payrollCalcResultDTO.setCountryCode(ObjectUtils.isEmpty(empInfo.get(JsonParseConsts.EMPLOYEE_COUNTRY_CODE_CN)) ? "" : (String)empInfo.get(JsonParseConsts.EMPLOYEE_COUNTRY_CODE_CN));

        List<DBObject> resultItemsList = (List<DBObject>)dbObject.get("salary_calc_result_items");
        payrollCalcResultDTO.setPersonnelIncomeWageBeforeTax(ObjectUtils.isEmpty(findValueStrByName(resultItemsList,JsonParseConsts.ACTUAL_PAY)) ? new BigDecimal("0.00") : new BigDecimal(findValueStrByName(resultItemsList,JsonParseConsts.ACTUAL_PAY).toString()));
        payrollCalcResultDTO.setPersonnelIncomeNetPay(ObjectUtils.isEmpty(findValueStrByName(resultItemsList,JsonParseConsts.EMPLOYEE_NET_PAY)) ? new BigDecimal("0.00") : new BigDecimal(findValueStrByName(resultItemsList,JsonParseConsts.EMPLOYEE_NET_PAY).toString()));
        payrollCalcResultDTO.setPersonnelIncomeTax(ObjectUtils.isEmpty(findValueStrByName(resultItemsList,JsonParseConsts.TAX_TOTAL)) ? new BigDecimal("0.00") : new BigDecimal(findValueStrByName(resultItemsList,JsonParseConsts.TAX_TOTAL).toString()));
        payrollCalcResultDTO.setPersonnelSocialSecurity(ObjectUtils.isEmpty(findValueStrByName(resultItemsList,JsonParseConsts.EMLOYEE_RESULT_ITMES_SOCIAL_SECURITY)) ? new BigDecimal("0.00") : new BigDecimal(findValueStrByName(resultItemsList,JsonParseConsts.EMLOYEE_RESULT_ITMES_SOCIAL_SECURITY).toString()));
        payrollCalcResultDTO.setPersonnelProvidentFund(ObjectUtils.isEmpty(findValueStrByName(resultItemsList,JsonParseConsts.EMLOYEE_RESULT_ITMES_PROVIDENT_FUND)) ? new BigDecimal("0.00") : new BigDecimal(findValueStrByName(resultItemsList,JsonParseConsts.EMLOYEE_RESULT_ITMES_PROVIDENT_FUND).toString()));
        payrollCalcResultDTO.setPersonnelIncomeYearlyBonusAfterTax(ObjectUtils.isEmpty(findValueStrByName(resultItemsList,JsonParseConsts.EMLOYEE_RESULT_ITMES_YEAR_BONUS)) ? new BigDecimal("0.00") : new BigDecimal(findValueStrByName(resultItemsList,JsonParseConsts.EMLOYEE_RESULT_ITMES_YEAR_BONUS).toString()));
        payrollCalcResultDTO.setLeavingYears(ObjectUtils.isEmpty(findValueStrByName(resultItemsList,JsonParseConsts.EMLOYEE_RESULT_ITMES_LEAVING_YEARS)) ? 0 : Double.valueOf(findValueStrByName(resultItemsList,JsonParseConsts.EMLOYEE_RESULT_ITMES_LEAVING_YEARS).toString()));

        DBObject employeeServiceAgreement = (DBObject) empInfo.get(JsonParseConsts.EMPLOYEE_SERVICE_AGREE);
        EmployeeServiceAgreementDTO employeeServiceAgreementDTO = null;
        if(ObjectUtils.isEmpty(employeeServiceAgreement)){
            logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("解析雇员服务协议，计算批次号为：" + (ObjectUtils.isEmpty(dbObject.get("batch_id"))  ? "" : (String)dbObject.get("batch_id"))).setContent("获取参数为空，解析雇员服务协议失败！"));
        }else{
            employeeServiceAgreementDTO = new EmployeeServiceAgreementDTO();
            employeeServiceAgreementDTO.setEmployeeId(ObjectUtils.isEmpty(employeeServiceAgreement.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_EMLOYEE_ID)) ? "" : (String)employeeServiceAgreement.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_EMLOYEE_ID));
            employeeServiceAgreementDTO.setCompanyId(ObjectUtils.isEmpty(employeeServiceAgreement.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_COMPANY_ID)) ? "" : (String)employeeServiceAgreement.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_COMPANY_ID));
            employeeServiceAgreementDTO.setCompanyName(ObjectUtils.isEmpty(employeeServiceAgreement.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_COMPANY_NAME)) ? "" : (String)employeeServiceAgreement.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_COMPANY_NAME));
            employeeServiceAgreementDTO.setTemplateType(ObjectUtils.isEmpty(employeeServiceAgreement.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_EMPLOYED_TYPE)) ? null : (Integer) employeeServiceAgreement.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_EMPLOYED_TYPE));
            employeeServiceAgreementDTO.setCycleRuleId(ObjectUtils.isEmpty(employeeServiceAgreement.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_CYCLE_RULE_ID)) ? null : (Integer) employeeServiceAgreement.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_CYCLE_RULE_ID));

            DBObject salaryGrantInfo = (DBObject) employeeServiceAgreement.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_SALARY_GRANT_INFO);
            employeeServiceAgreementDTO.setGrantType(ObjectUtils.isEmpty(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_GRANT_TYPE)) ? null : (Integer)salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_GRANT_TYPE));
            employeeServiceAgreementDTO.setGrantServiceType(ObjectUtils.isEmpty(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_GRANT_SERVICE_TYPE)) ? null :  (Integer)salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_GRANT_SERVICE_TYPE));
            /* modified by gy on 2018-07-04 新结构已删除 grantRuleId 薪资发放规则Id */
            //employeeServiceAgreementDTO.setSalaryGrantRuleId(ObjectUtils.isEmpty(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_EMP_SALARY_GRANT_RULE_ID)) ? null : (List)salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_EMP_SALARY_GRANT_RULE_ID));
            employeeServiceAgreementDTO.setExchangeCalcMode(ObjectUtils.isEmpty(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_EXCHANGE_CALC_MODE)) ? null : (Integer)salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_EXCHANGE_CALC_MODE));
            employeeServiceAgreementDTO.setCurrencyCode(ObjectUtils.isEmpty(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_CURRENCY_TYPE)) ? "" : (String)salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_CURRENCY_TYPE));
            employeeServiceAgreementDTO.setCustomerAgreedExchangeRate(ObjectUtils.isEmpty(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_EXCHANGE_RATE)) ? BigDecimal.ONE : new BigDecimal(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_EXCHANGE_RATE).toString()));

            employeeServiceAgreementDTO.setSocialSecuritySource(ObjectUtils.isEmpty(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_SOCIAL_SECURITY_SOURCE)) ? 0 :  (Integer)salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_SOCIAL_SECURITY_SOURCE));
            employeeServiceAgreementDTO.setPersonalPension(ObjectUtils.isEmpty(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_PERSONAL_PENSION)) ? 0 :  (Integer)salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_PERSONAL_PENSION));
            employeeServiceAgreementDTO.setPersonalMedicalTreatment(ObjectUtils.isEmpty(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_PERSONAL_MEDICAL_TREATMENT)) ? 0 :  (Integer)salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_PERSONAL_MEDICAL_TREATMENT));
            employeeServiceAgreementDTO.setIndividualUnemployment(ObjectUtils.isEmpty(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_INDIVIDUAL_UNEMPLOYEMENT)) ? 0 :  (Integer)salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_INDIVIDUAL_UNEMPLOYEMENT));
            employeeServiceAgreementDTO.setIndividualProvidentFund(ObjectUtils.isEmpty(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_INDIVIDUAL_PROVIDENT_FUND)) ? 0 :  (Integer)salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_INDIVIDUAL_PROVIDENT_FUND));
            if(employeeServiceAgreementDTO.getPersonalPension() != 0 && employeeServiceAgreementDTO.getIndividualProvidentFund() != 0){
                employeeServiceAgreementDTO.setWelfareIncluded(true);
            }else{
                employeeServiceAgreementDTO.setWelfareIncluded(false);
            }
            /* modified by gy on 2018-07-04 新结构已删除 isWelfareIncluded 是否包括社保和公积金 */
            //employeeServiceAgreementDTO.setWelfareIncluded(ObjectUtils.isEmpty(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_IS_WELFARE_INCLUDED)) ? false : (Boolean) salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_IS_WELFARE_INCLUDED));

            DBObject supplierDetail = (DBObject) salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_SUPPLIER_DETAIL);
            if(!ObjectUtils.isEmpty(supplierDetail)){
                employeeServiceAgreementDTO.setSupplierName(ObjectUtils.isEmpty(supplierDetail.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_SUPPLIER_ACCOUNT_RECEIVALE_NAME)) ? "" : (String)supplierDetail.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_SUPPLIER_ACCOUNT_RECEIVALE_NAME));
                employeeServiceAgreementDTO.setSupplierAccountReceivale(ObjectUtils.isEmpty(supplierDetail.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_SUPPLIER_ACCOUNT_RECEIVALE)) ? "" : (String)supplierDetail.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_SUPPLIER_ACCOUNT_RECEIVALE));
                employeeServiceAgreementDTO.setSupplierAccountReceivaleName(ObjectUtils.isEmpty(supplierDetail.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_SUPPLIER_ACCOUNT_RECEIVALE_NAME)) ? "" : (String)supplierDetail.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_SUPPLIER_ACCOUNT_RECEIVALE_NAME));
            }
            employeeServiceAgreementDTO.setCommissionContractSerialNumber(ObjectUtils.isEmpty(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_COMMISSION_CONTRACT_SERIAL_NUMBER)) ? null : (Long)salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_COMMISSION_CONTRACT_SERIAL_NUMBER));

            employeeServiceAgreementDTO.setPaymentBankAccount(ObjectUtils.isEmpty(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_PAYMENT_BANK_ACCOUNT)) ? "" : (String)salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_PAYMENT_BANK_ACCOUNT));
            employeeServiceAgreementDTO.setPaymentBankAccountName(ObjectUtils.isEmpty(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_PAYMENT_BANK_ACCOUNT_NAME)) ? "" : (String)salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_PAYMENT_BANK_ACCOUNT_NAME));
            employeeServiceAgreementDTO.setPaymentBankName(ObjectUtils.isEmpty(salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_PAYMENT_BANK_NAME)) ? "" : (String)salaryGrantInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_PAYMENT_BANK_NAME));

            DBObject billingInfo = (DBObject) (employeeServiceAgreement.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_BILL_INFO));
            /* modified by gy on 2018-07-04 新结构已删除 contractId 业务合同编号 contractType 合同类型 */
            //employeeServiceAgreementDTO.setContractId(ObjectUtils.isEmpty(billingInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_SUPPLIER_CONTRACTID)) ? "" : (String)billingInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_SUPPLIER_CONTRACTID));
            //employeeServiceAgreementDTO.setContractType(ObjectUtils.isEmpty(billingInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_CONTRACT_TYPE)) ? 1 : (Integer)billingInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_CONTRACT_TYPE));
            employeeServiceAgreementDTO.setContractFirstParty(ObjectUtils.isEmpty(billingInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_CONTRACT_FIRST_PARTY)) ? "" : (String)billingInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_CONTRACT_FIRST_PARTY));
            payrollCalcResultDTO.setContractFirstParty(ObjectUtils.isEmpty(billingInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_CONTRACT_FIRST_PARTY)) ? "" : (String)billingInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_CONTRACT_FIRST_PARTY));

            DBObject taxInfo = (DBObject) (employeeServiceAgreement.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_TAX_INFO));
            employeeServiceAgreementDTO.setTaxPeriod(ObjectUtils.isEmpty(taxInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_TAX_PERIOD)) ? null : (Integer)taxInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_TAX_PERIOD));
            DBObject declarationAccountDetail = (DBObject) (taxInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_DECLARATION_ACCOUNT_DETAIL));
            if(!ObjectUtils.isEmpty(declarationAccountDetail)){
                employeeServiceAgreementDTO.setDeclarationAccount(ObjectUtils.isEmpty(declarationAccountDetail.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_TAX_ACCOUNT)) ? "" : (String)declarationAccountDetail.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_TAX_ACCOUNT));
                employeeServiceAgreementDTO.setDeclarationAccountCategory(ObjectUtils.isEmpty(declarationAccountDetail.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_TAX_ACCOUNT_CATEGORY)) ? null : (Integer)declarationAccountDetail.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_TAX_ACCOUNT_CATEGORY));
            }
            DBObject contributionAccountDetail = (DBObject) (taxInfo.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_CONTRIBUTION_ACCOUNT_DETAIL));
            if(!ObjectUtils.isEmpty(contributionAccountDetail)){
                employeeServiceAgreementDTO.setContributionAccount(ObjectUtils.isEmpty(contributionAccountDetail.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_TAX_ACCOUNT)) ? "" : (String)contributionAccountDetail.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_TAX_ACCOUNT));
                employeeServiceAgreementDTO.setContributionAccountCategory(ObjectUtils.isEmpty(contributionAccountDetail.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_TAX_ACCOUNT_CATEGORY)) ? null : (Integer)contributionAccountDetail.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_TAX_ACCOUNT_CATEGORY));
            }
        }
        payrollCalcResultDTO.setEmployeeServiceAgreement(employeeServiceAgreementDTO);
        return payrollCalcResultDTO;
    }

    /**
     *  创建薪资发放任务单主表，解析雇员计算结果数据，过滤薪资发放的雇员信息，汇总相关数据。
     * @param payrollCalcResultDTOList
     * @param salaryGrantMainTaskPO
     * @return SalaryGrantMainTaskPO
     */
    private SalaryGrantMainTaskPO toResolvePayrollCalcResultForTask(List<PayrollCalcResultDTO> payrollCalcResultDTOList, SalaryGrantMainTaskPO salaryGrantMainTaskPO){
        // 设置计数器
        BigDecimal paymentTotalSumCount = new BigDecimal("0.00");
        Double totalSumResult = 0.00;
        // 统计发放方式
        StringBuffer grantModeForTask = new StringBuffer();
        // 薪资发放日期
        String grantDate = "";
        // 薪资发放时段:1-上午，2-下午
        Integer grantTime = SalaryGrantBizConsts.GRANT_TIME_AM;
        // 参考批次号
        String originBatchCode = "";

        if(!CollectionUtils.isEmpty(payrollCalcResultDTOList)){
            // 发放服务标识
            Integer grantServiceType;
            // 发放方式
            Integer grantMode;
            // 国籍
            String countryCode;

            // 从第一个雇员计算结果信息获取薪资发放日期、薪资发放时段、参考批次号赋值给任务单信息。
            PayrollCalcResultDTO payrollCalcResultDTOOne = payrollCalcResultDTOList.get(0);
            // 获取雇员服务协议字段信息
            EmployeeServiceAgreementDTO employeeServiceAgreementDTOOne = payrollCalcResultDTOOne.getEmployeeServiceAgreement();
            if(!ObjectUtils.isEmpty(employeeServiceAgreementDTOOne)){
                if(SalaryGrantBizConsts.GRANT_TYPE_ADJUST.equals(salaryGrantMainTaskPO.getGrantType()) || SalaryGrantBizConsts.GRANT_TYPE_BACK_TRACE.equals(salaryGrantMainTaskPO.getGrantType())){
                    if(StringUtils.isEmpty(payrollCalcResultDTOOne.getRefBatchId())){
                        originBatchCode = payrollCalcResultDTOOne.getRefBatchId();
                    }else{
                        logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("新增薪资发放任务单主表信息，计算批次号为："+salaryGrantMainTaskPO.getBatchCode()).setContent("调整批次或回溯批次的参考批次号为空！"));
                    }
                }

                for(PayrollCalcResultDTO payrollCalcResultDTO : payrollCalcResultDTOList){
                    // 1、获取雇员服务协议字段信息
                    EmployeeServiceAgreementDTO employeeServiceAgreementDTO = payrollCalcResultDTO.getEmployeeServiceAgreement();
                    grantServiceType = employeeServiceAgreementDTO.getGrantServiceType();
                    grantMode = employeeServiceAgreementDTO.getGrantType();
                    // 2、遍历查询只有发放或者发放+个税的雇员信息。
                    if(grantServiceType != null && (SalaryGrantBizConsts.GRANT_SERVICE_TYPE_GRANT.equals(grantServiceType) || SalaryGrantBizConsts.GRANT_SERVICE_TYPE_GRANT_AND_TAX.equals(grantServiceType))){
                        totalSumResult = totalSumResult + Double.valueOf(paymentTotalSumCount.add(payrollCalcResultDTO.getPersonnelIncomeNetPay()).toString());
                        if(grantMode != null && grantModeForTask.indexOf(String.valueOf(grantMode)) < 0){
                            grantModeForTask = grantModeForTask.append(grantMode);
                        }
                    }else{
                        continue;
                    }
                }
            }else{
                logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询雇员服务协议，计算批次号为：" + salaryGrantMainTaskPO.getBatchCode()).setContent("雇员服务协议为空！"));
            }
            salaryGrantMainTaskPO.setOriginBatchCode(originBatchCode);
            salaryGrantMainTaskPO.setPaymentTotalSum(BigDecimal.valueOf(totalSumResult));
            salaryGrantMainTaskPO.setGrantDate(grantDate);
            salaryGrantMainTaskPO.setGrantTime(grantTime);
            salaryGrantMainTaskPO.setGrantMode(grantModeForTask.toString());
        }
        return salaryGrantMainTaskPO;
    }

    /**
     * 新增薪资发放任务单主表信息
     * @param salaryGrantMainTaskPO
     * @return boolean
     */
    private boolean insertOrUpdateSalaryGrantMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO) {
        if(ObjectUtils.isEmpty(salaryGrantMainTaskPO.getSalaryGrantMainTaskId())){
            salaryGrantMainTaskPO.setActive(true);
            salaryGrantMainTaskPO.setCreatedTime(new Date());
            salaryGrantMainTaskPO.setCreatedBy(SalaryGrantBizConsts.SYSTEM_EN);
            salaryGrantMainTaskPO.setModifiedTime(new Date());
            salaryGrantMainTaskPO.setModifiedBy(SalaryGrantBizConsts.SYSTEM_EN);
        }else{
            salaryGrantMainTaskPO.setModifiedBy(SalaryGrantBizConsts.SYSTEM_EN);
            salaryGrantMainTaskPO.setModifiedTime(new Date());
        }
        return insertOrUpdate(salaryGrantMainTaskPO);
    }

    /**
     * 生成薪资发放雇员信息
     * @param salaryGrantMainTaskPO
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    protected boolean createEmployee(SalaryGrantMainTaskPO salaryGrantMainTaskPO,List<PayrollCalcResultDTO> payrollCalcResultDTOList){
        // 1、查询批次计算
        // 2、解析payrollCalcResultDTOList批次数据信息
        List<SalaryGrantEmployeePO> salaryGrantEmployeePOList = this.toResolvePayrollCalcResultForEmployee(payrollCalcResultDTOList, salaryGrantMainTaskPO);
        if(!CollectionUtils.isEmpty(salaryGrantEmployeePOList)){
            // 3、插入薪资发放雇员信息表数据。批量插入，调用接口方法新增薪资发放雇员信息，带入任务单编号salaryGrantMainTaskCode。
            salaryGrantEmployeeService.insertBatch(salaryGrantEmployeePOList);
            // 4、是否有本地外币发放，对主表外币发放标识进行回写。
            List<SalaryGrantEmployeePO> localList = salaryGrantEmployeePOList.parallelStream().filter(SalaryGrantEmployeePO -> SalaryGrantEmployeePO.getGrantMode().equals(SalaryGrantBizConsts.GRANT_MODE_LOCAL)).collect(Collectors.toList());
            Long ltwCount = localList.parallelStream().filter(SalaryGrantEmployeePO -> !SalaryGrantEmployeePO.getCurrencyCode().equals(SalaryGrantBizConsts.CURRENCY_CNY)).count();
            if(ltwCount > 0L){
                salaryGrantMainTaskPO.setIncludeForeignCurrency(true);
            }
            // 对相同发放账户的雇员信息遍历list，统计总人数
            salaryGrantMainTaskPO.setTotalPersonCount(Integer.valueOf(salaryGrantEmployeePOList.size()));
            // 针对发放账户统计中、外方人数，根据country_code
            Long chineseCount = salaryGrantEmployeePOList.parallelStream().filter(SalaryGrantEmployeeBO -> SalaryGrantEmployeeBO.getCountryCode().equals(SalaryGrantBizConsts.COUNTRY_CODE_CHINA)).count();
            salaryGrantMainTaskPO.setChineseCount(Integer.valueOf(chineseCount.intValue()));
            Long foreignerCount = salaryGrantEmployeePOList.parallelStream().filter(SalaryGrantEmployeeBO -> !SalaryGrantEmployeeBO.getCountryCode().equals(SalaryGrantBizConsts.COUNTRY_CODE_CHINA)).count();
            salaryGrantMainTaskPO.setForeignerCount(Integer.valueOf(foreignerCount.intValue()));
            // 中智上海发薪人数，发放方式(1)进行累计
            Long localGrantCount = salaryGrantEmployeePOList.parallelStream().filter(SalaryGrantEmployeeBO -> SalaryGrantEmployeeBO.getGrantMode().equals(SalaryGrantBizConsts.GRANT_MODE_LOCAL)).count();
            salaryGrantMainTaskPO.setLocalGrantCount(Integer.valueOf(localGrantCount.intValue()));
            // 中智代发（委托机构）发薪人数，发放方式(2)进行累计
            Long supplierGrantCount = salaryGrantEmployeePOList.parallelStream().filter(SalaryGrantEmployeeBO -> SalaryGrantEmployeeBO.getGrantMode().equals(SalaryGrantBizConsts.GRANT_MODE_SUPPLIER)).count();
            salaryGrantMainTaskPO.setSupplierGrantCount(Integer.valueOf(supplierGrantCount.intValue()));
            salaryGrantMainTaskPO.setGrantDate(salaryGrantEmployeePOList.get(0).getGrantDate());
            salaryGrantMainTaskPO.setGrantTime(salaryGrantEmployeePOList.get(0).getGrantTime());
            this.updateSalaryGrantMainTask(salaryGrantMainTaskPO);

            // 雇员置为自动暂缓，把信息拷贝至薪资发放暂缓池中。调用客服中心暂缓池的插入方法。
            this.processAutoReprieveEmployeeToPool(salaryGrantMainTaskPO);
            // 调整从调整批次、回溯批次生成雇员信息时，调用后台插入雇员调整信息方法。
            if(SalaryGrantBizConsts.GRANT_TYPE_ADJUST.equals(salaryGrantMainTaskPO.getGrantType()) || SalaryGrantBizConsts.GRANT_TYPE_BACK_TRACE.equals(salaryGrantMainTaskPO.getGrantType())){
                salaryGrantEmployeeCommandService.saveSalaryAdjustmentInfoForEmployee(salaryGrantMainTaskPO);
            }
            return true;
        }else{
            return false;
        }
    }

    /**
     * 将自动暂缓雇员插入到FC客服中心薪资发放暂缓池
     * @param salaryGrantMainTaskPO
     * @return Boolean
     */
    private void processAutoReprieveEmployeeToPool(SalaryGrantMainTaskPO salaryGrantMainTaskPO){
        EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
        employeePOEntityWrapper.where("salary_grant_main_task_code = {0} and grant_status = 2 and is_active = 1", salaryGrantMainTaskPO.getSalaryGrantMainTaskCode());
        List<SalaryGrantEmployeePO> employeeList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper);
        // 有自动暂缓的雇员则进入薪资发放暂缓池，没有雇员信息则不做操作。
        if(!CollectionUtils.isEmpty(employeeList)){
            SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
            salaryGrantTaskBO.setTaskCode(salaryGrantMainTaskPO.getSalaryGrantMainTaskCode());
            salaryGrantTaskBO.setBatchCode(salaryGrantMainTaskPO.getBatchCode());
            salaryGrantTaskBO.setManagementId(salaryGrantMainTaskPO.getManagementId());
            salaryGrantTaskBO.setManagementName(salaryGrantMainTaskPO.getManagementName());
            salaryGrantTaskBO.setGrantCycle(salaryGrantMainTaskPO.getGrantCycle());
            commonService.addDeferredPool(salaryGrantTaskBO, employeeList);
        }
    }

    /**
     * 解析批次计算结果数据及雇员服务协议信息进行发放金额拆分
     * @param payrollCalcResultDTOList
     * @param salaryGrantMainTaskPO
     * @return List<SalaryGrantEmployeePO>
     */
    private List<SalaryGrantEmployeePO> toResolvePayrollCalcResultForEmployee(List<PayrollCalcResultDTO> payrollCalcResultDTOList, SalaryGrantMainTaskPO salaryGrantMainTaskPO){
        List<SalaryGrantEmployeePO> salaryGrantEmployeePOFinalList = new ArrayList();
        List<SalaryGrantEmployeePO> salaryGrantEmployeePOSplitList;
        String salaryGrantMainTaskCode = salaryGrantMainTaskPO.getSalaryGrantMainTaskCode();
        String batchCode = salaryGrantMainTaskPO.getBatchCode();
        if(!CollectionUtils.isEmpty(payrollCalcResultDTOList)){
            // 发放服务标识
            Integer grantServiceType = SalaryGrantBizConsts.GRANT_SERVICE_TYPE_GRANT;
            // 薪资发放日期
            String grantDate = "";
            // 薪资发放时段:1-上午，2-下午
            Integer grantTime = SalaryGrantBizConsts.GRANT_TIME_AM;

            for(PayrollCalcResultDTO payrollCalcResultDTO : payrollCalcResultDTOList){
                // 解析雇员服务协议信息
                EmployeeServiceAgreementDTO employeeServiceAgreementDTO = payrollCalcResultDTO.getEmployeeServiceAgreement();
                if(!ObjectUtils.isEmpty(employeeServiceAgreementDTO)){
                    // 查询服务周期规则
                    CycleRuleResponseDTO cycleRuleResponseDTO  = this.getCycleRule(employeeServiceAgreementDTO.getCycleRuleId());
                    if(!ObjectUtils.isEmpty(cycleRuleResponseDTO)){
                        if(!StringUtils.isEmpty(cycleRuleResponseDTO.getSalaryDayDate())){
                            grantDate = salaryGrantMainTaskPO.getGrantCycle() + this.fillZeroForDate(cycleRuleResponseDTO.getSalaryDayDate());
                        }
                        if(!StringUtils.isEmpty(cycleRuleResponseDTO.getSalaryDayTime())){
                            grantTime = Integer.valueOf(cycleRuleResponseDTO.getSalaryDayTime());
                        }
                    }else{
                        logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("新增薪资发放雇员信息，计算批次号为：" + salaryGrantMainTaskCode + "雇员编号为：" + payrollCalcResultDTO.getEmpId()).setContent("查询服务周期规则为空记录！"));
                    }
                    grantServiceType = employeeServiceAgreementDTO.getGrantServiceType();
                    if(grantServiceType != null && (SalaryGrantBizConsts.GRANT_SERVICE_TYPE_GRANT.equals(grantServiceType) || SalaryGrantBizConsts.GRANT_SERVICE_TYPE_GRANT_AND_TAX.equals(grantServiceType))){
                        SalaryGrantEmployeePO salaryGrantEmployeePO = new SalaryGrantEmployeePO();
                        salaryGrantEmployeePO.setCycleRuleId(employeeServiceAgreementDTO.getCycleRuleId());
                        salaryGrantEmployeePO.setGrantDate(grantDate);
                        salaryGrantEmployeePO.setGrantTime(grantTime);
                        salaryGrantEmployeePO.setSalaryGrantMainTaskCode(salaryGrantMainTaskCode);
                        salaryGrantEmployeePO.setBatchCode(batchCode);
                        salaryGrantEmployeePO = this.convertPayrollCalcResultToSalaryGrantEmployee(payrollCalcResultDTO, employeeServiceAgreementDTO, salaryGrantEmployeePO);

                        salaryGrantEmployeePO.setGrantStatus(SalaryGrantBizConsts.GRANT_STATUS_NORMAL);
                        salaryGrantEmployeePO.setActive(true);
                        salaryGrantEmployeePO.setCreatedBy(SalaryGrantBizConsts.SYSTEM_EN);
                        salaryGrantEmployeePO.setCreatedTime(new Date());
                        salaryGrantEmployeePO.setModifiedBy(SalaryGrantBizConsts.SYSTEM_EN);
                        salaryGrantEmployeePO.setModifiedTime(new Date());
                        salaryGrantEmployeePOSplitList = this.toDealSingleEmployeeInfo(salaryGrantEmployeePO, employeeServiceAgreementDTO);
                        salaryGrantEmployeePOFinalList.addAll(salaryGrantEmployeePOSplitList);
                    }
                }else{
                    logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("新增薪资发放雇员信息，计算批次号为：" + salaryGrantMainTaskCode).setContent("查询雇员服务协议为空记录！"));
                }
            }
        }
        return salaryGrantEmployeePOFinalList;
    }

    /**
     * 处理单个雇员信息，需要查询银行卡信息、薪资发放规则、雇员服务协议、汇率等外部接口方法
     * @param salaryGrantEmployeePO
     * @param employeeServiceAgreementDTO
     * @return List<SalaryGrantEmployeePO>
     */
    private List<SalaryGrantEmployeePO> toDealSingleEmployeeInfo(SalaryGrantEmployeePO salaryGrantEmployeePO, EmployeeServiceAgreementDTO employeeServiceAgreementDTO) {
        // lastResultList 保存最终拆分的雇员计算结果数据
        List<SalaryGrantEmployeePO> resultList = new ArrayList();
        // 发放金额(总数，后续根据发放规则进行拆分金额)
        BigDecimal paymentAmountRMB = salaryGrantEmployeePO.getPaymentAmountRMB();
        // 发放金额折合人民币计数器
        BigDecimal amountForRMBCount = new BigDecimal("0.00");
        Double amountForRMBResult = 0.00;
        BigDecimal sumDefalutZero = new BigDecimal("0.00");
        // 默认银行卡信息
        EmployeeBankcardDTO employeeBankcardDTODefault;
        // 默认银行卡雇员信息
        SalaryGrantEmployeePO salaryGrantEmployeePODefault = new SalaryGrantEmployeePO();

        // 1、根据雇员编号查询雇员的银行卡信息
        Map paramMap = new HashMap(3);
        paramMap.put("employeeId",salaryGrantEmployeePO.getEmployeeId());
        //paramMap.put("bankcardId",salaryGrantEmployeePO.getBankcardId());
        //paramMap.put("currencyCode",salaryGrantEmployeePO.getCurrencyCode());
        // 调用雇员的银行卡信息API查询
        List<EmployeeBankcardDTO> employeeBankcardDTOList = this.listEmployeeBankcardInfo(paramMap);
        if(employeeBankcardDTOList != null && employeeBankcardDTOList.size() > 0){
            try {
                List<EmployeeBankcardDTO> defaultCardList = employeeBankcardDTOList.stream().filter(a -> a.getDefaultCard().equals(true)).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(defaultCardList)){
                    employeeBankcardDTODefault = defaultCardList.get(0);
                    // 添加雇员的银行卡信息，默认币种为人民币，汇率为1。无薪资发放规则的银行卡，默认全部发放，币种为人民币。
                    salaryGrantEmployeePO = this.convertEmployeeBankcardToSalaryGrantEmployee(salaryGrantEmployeePO, employeeBankcardDTODefault);
                    // 雇员中心银行卡信息字段信息：默认卡，默认币种，对应汇率（客户约定、实时），需要对默认币种、汇率、发放金额由RMB折合对应的币种金额。
                    if(employeeBankcardDTODefault.getDefaultCardCurrencyCode() != null && !SalaryGrantBizConsts.CURRENCY_CNY.equals(employeeBankcardDTODefault.getDefaultCardCurrencyCode())){
                        salaryGrantEmployeePO.setCurrencyCode(employeeBankcardDTODefault.getDefaultCardCurrencyCode());
                        salaryGrantEmployeePO.setPaymentAmount(paymentAmountRMB.divide(employeeBankcardDTODefault.getDefaultCardExchange()));
                        salaryGrantEmployeePO.setExchange(employeeBankcardDTODefault.getDefaultCardExchange());
                    }else{
                        salaryGrantEmployeePO.setCurrencyCode(SalaryGrantBizConsts.CURRENCY_CNY);
                        salaryGrantEmployeePO.setPaymentAmount(paymentAmountRMB);
                        salaryGrantEmployeePO.setExchange(BigDecimal.ONE);
                    }
                    salaryGrantEmployeePODefault = (SalaryGrantEmployeePO) salaryGrantEmployeePO.clone();
                }else{
                    logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询雇员银行卡信息，雇员编号为：" + salaryGrantEmployeePO.getEmployeeId()).setContent("该雇员无默认银行卡！"));
                }
                // 雇员的发放金额结算结果为0，则默认工资卡的发放金额为0，不进行遍历其他银行卡和发放规则
                if(paymentAmountRMB.equals(sumDefalutZero)) {
                    resultList.add(salaryGrantEmployeePODefault);
                }else{
                    // 拆分雇员数据银行卡信息、币种、汇率发放金额
                    for(EmployeeBankcardDTO employeeBankcardDTO : employeeBankcardDTOList){
                        // 对非默认卡的发放规则进行处理
                        if(employeeBankcardDTO.getDefaultCard() != null && !employeeBankcardDTO.getDefaultCard()){
                            SalaryGrantEmployeePO salaryGrantEmployeePOTemp = (SalaryGrantEmployeePO) salaryGrantEmployeePO.clone();
                            // 添加雇员的银行卡信息
                            salaryGrantEmployeePOTemp = this.convertEmployeeBankcardToSalaryGrantEmployee(salaryGrantEmployeePOTemp, employeeBankcardDTO);
                            // 2、根据雇员编号查询薪资发放规则
                            paramMap.put("bankcardId",String.valueOf(salaryGrantEmployeePOTemp.getBankcardId()));
                            List<SalaryGrantRuleDTO> salaryGrantRuleDTOList = this.listSalaryGrantRuleInfo(paramMap);
                            // 3、根据银行卡、币种、汇率，对发放金额进行拆分
                            if (salaryGrantRuleDTOList != null && salaryGrantRuleDTOList.size() > 0) {
                                for (SalaryGrantRuleDTO salaryGrantRuleDTO : salaryGrantRuleDTOList) {
                                    SalaryGrantEmployeePO salaryGrantEmployeePOMidTemp = (SalaryGrantEmployeePO) salaryGrantEmployeePOTemp.clone();
                                    salaryGrantEmployeePOMidTemp.setSalaryGrantRuleId(salaryGrantRuleDTO.getSalaryGrantRuleId());
                                    salaryGrantEmployeePOMidTemp.setCurrencyCode(salaryGrantRuleDTO.getCurrencyCode());
                                    salaryGrantEmployeePOMidTemp.setRuleType(salaryGrantRuleDTO.getRuleType());
                                    salaryGrantEmployeePOMidTemp.setRuleAmount(salaryGrantRuleDTO.getRuleAmount());
                                    salaryGrantEmployeePOMidTemp.setRuleRatio(salaryGrantRuleDTO.getRuleRatio());
                                    Map calcMap = this.calcPaymentAmountBySalaryGrantRule(paymentAmountRMB, salaryGrantRuleDTO, employeeServiceAgreementDTO);
                                    salaryGrantEmployeePOMidTemp.setPaymentAmount((BigDecimal) calcMap.get("splitPaymentAmount"));
                                    salaryGrantEmployeePOMidTemp.setExchange((BigDecimal) calcMap.get("exchange"));
                                    amountForRMBResult = amountForRMBResult + Double.valueOf(amountForRMBCount.add((BigDecimal) calcMap.get("paymentAmountForRMBCount")).toString());
                                    resultList.add(salaryGrantEmployeePOMidTemp);
                                }
                            }else{
                                continue;
                            }
                        }else{
                            continue;
                        }
                    }

                    if(!ObjectUtils.isEmpty(salaryGrantEmployeePODefault.getCurrencyCode()) && !SalaryGrantBizConsts.CURRENCY_CNY.equals(salaryGrantEmployeePODefault.getCurrencyCode())){
                        salaryGrantEmployeePODefault.setPaymentAmount(paymentAmountRMB.subtract(BigDecimal.valueOf(amountForRMBResult)).divide(salaryGrantEmployeePODefault.getExchange() == null ? BigDecimal.ONE : salaryGrantEmployeePODefault.getExchange()));
                    }else{
                        salaryGrantEmployeePODefault.setPaymentAmount(paymentAmountRMB.subtract(BigDecimal.valueOf(amountForRMBResult)));
                    }
                    if(salaryGrantEmployeePODefault.getPaymentAmount().compareTo(sumDefalutZero) > 0){
                        resultList.add(salaryGrantEmployeePODefault);
                    }
                }
            } catch (CloneNotSupportedException e) {
                logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("克隆雇员信息异常").setContent(e.getMessage()));
            }
        }else {
            // 缺失银行卡信息，该雇员置为自动暂缓。
            salaryGrantEmployeePO.setGrantStatus(SalaryGrantBizConsts.GRANT_STATUS_AUTO_REPRIEVE);
            resultList.add(salaryGrantEmployeePO);
        }
        return resultList;
    }

    /**
     * 根据银行卡的发放规则对总发放金额进行拆分计算
     * @param paymentAmount
     * @param salaryGrantRuleDTO
     * @param employeeServiceAgreementDTO
     * @return Map
     */
    private Map calcPaymentAmountBySalaryGrantRule(BigDecimal paymentAmount, SalaryGrantRuleDTO salaryGrantRuleDTO, EmployeeServiceAgreementDTO employeeServiceAgreementDTO){
        Map calcMap = new HashMap(2);
        // 拆分金额
        Double splitPaymentAmount = 0.00;
        // 拆分金额折合人民币
        Double splitPaymentAmountForRMB = 0.00;
        // 拆分金额折合人民币返回统计
        Double paymentAmountForRMBCount = 0.00;
        // 规则金额
        BigDecimal ruleAmount = new BigDecimal("0.00");
        // 规则比例
        BigDecimal ruleRatio = BigDecimal.ONE;
        // 汇率
        BigDecimal exchange = BigDecimal.ONE;
        // 按比例设置
        if(SalaryGrantBizConsts.SALARY_GRANT_RULE_TYPE_RATIO.equals(salaryGrantRuleDTO.getRuleType())){
            ruleRatio = salaryGrantRuleDTO.getRuleRatio();

            if( ruleRatio != null && !ruleRatio.equals(BigDecimal.ZERO)){
                ruleRatio = ruleRatio.divide(new BigDecimal("100"));
            }
            exchange = this.dealMultiCurrency(salaryGrantRuleDTO, employeeServiceAgreementDTO);
            // 计算批次结果数据的发放金额的币种是RMB,对于多币种发放的比例规则，需要把人民币根据汇率折合对应外币进行发放。
            splitPaymentAmount = splitPaymentAmount + Double.valueOf(paymentAmount.multiply(ruleRatio).divide(exchange).toString());
            paymentAmountForRMBCount = paymentAmountForRMBCount +  Double.valueOf(BigDecimal.valueOf(splitPaymentAmount).multiply(exchange).toString());
        }else{
            // 按金额设置
            ruleAmount = salaryGrantRuleDTO.getRuleAmount();
            exchange = this.dealMultiCurrency(salaryGrantRuleDTO, employeeServiceAgreementDTO);

            splitPaymentAmountForRMB = splitPaymentAmountForRMB + Double.valueOf(ruleAmount.multiply(exchange).toString());
            // 如果薪资发放规则是固定金额，薪资结果小于固定金额，仅显示发放结果，不能扣为负数。
            if(BigDecimal.valueOf(splitPaymentAmountForRMB).compareTo(paymentAmount) > 0){
                // 当发放金额小于设置的固定金额，需要把发放金额根据汇率折合对应外币进行发放。
                splitPaymentAmount = splitPaymentAmount + Double.valueOf(paymentAmount.divide(exchange).toString());
            }else{
                splitPaymentAmount = splitPaymentAmount + Double.valueOf(ruleAmount.toString());
            }
            paymentAmountForRMBCount = paymentAmountForRMBCount + Double.valueOf(BigDecimal.valueOf(splitPaymentAmount).multiply(exchange).toString());
        }
        calcMap.put("splitPaymentAmount", BigDecimal.valueOf(splitPaymentAmount));
        calcMap.put("exchange", exchange);
        calcMap.put("paymentAmountForRMBCount", BigDecimal.valueOf(paymentAmountForRMBCount));
        return calcMap;
    }

    /**
     * 处理多币种，查询币种对应汇率。
     * @param salaryGrantRuleDTO
     * @param employeeServiceAgreementDTO
     * @return BigDecimal
     */
    private BigDecimal dealMultiCurrency(SalaryGrantRuleDTO salaryGrantRuleDTO , EmployeeServiceAgreementDTO employeeServiceAgreementDTO){

        // 币种对应汇率
        BigDecimal exchange = BigDecimal.ONE;
        // 非人民币需要查询汇率
        if(!SalaryGrantBizConsts.CURRENCY_CNY.equals(salaryGrantRuleDTO.getCurrencyCode())){
            // 如果是固定汇率，需要查询服务协议，获取固定汇率的值
            if(SalaryGrantBizConsts.EXCHANGE_CALC_MODE_FIXED_RATE.equals(employeeServiceAgreementDTO.getExchangeCalcMode())){
                if(employeeServiceAgreementDTO.getCustomerAgreedExchangeRate() != null){
                    exchange = employeeServiceAgreementDTO.getCustomerAgreedExchangeRate();
                }else{
                    logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询固定汇率，币种为：" + salaryGrantRuleDTO.getCurrencyCode()).setContent("固定汇率为空！"));
                }

            }else if(SalaryGrantBizConsts.EXCHANGE_CALC_MODE_CURRENT_EXCHANGE_RATE.equals(employeeServiceAgreementDTO.getExchangeCalcMode())){
                // 调用实时汇率接口
                exchange = this.getCurrentExchangeInfo(salaryGrantRuleDTO.getCurrencyCode());
            }else{
                // 如果是财务记账汇率，需要查询汇率表
                exchange = this.getExchangeInfo(salaryGrantRuleDTO.getCurrencyCode());
            }
        }
        return exchange;
    }

    private Currencies toConvertCurrencyForCurrentExchangeRate(String grantRuleCurrencyCode){
        Currencies currencies = Currencies.CNY;
        if(SalaryGrantBizConsts.CURRENCY_CNY.equals(grantRuleCurrencyCode)){
            currencies = Currencies.CNY;
        }
        if(SalaryGrantBizConsts.CURRENCY_USD.equals(grantRuleCurrencyCode)){
            currencies = Currencies.USD;
        }
        if(SalaryGrantBizConsts.CURRENCY_EUR.equals(grantRuleCurrencyCode)){
            currencies = Currencies.EUR;
        }
        return currencies;
    }

    @Override
    public Integer updateSalaryGrantMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO) {
        return salaryGrantMainTaskMapper.updateById(salaryGrantMainTaskPO);
    }

    /**
     * 计算批次信息转换到任务单主表对应字段
     * @param salaryGrantMainTaskPO 薪资发放任务单主表信息
     * @param PrBatchDTO 计算批次信息
     * @return SalaryGrantMainTaskPO
     */
    private SalaryGrantMainTaskPO convertBatchInfoToSalaryGrantMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO, PrBatchDTO PrBatchDTO){
        salaryGrantMainTaskPO.setManagementId(PrBatchDTO.getManagementId());
        salaryGrantMainTaskPO.setManagementName(PrBatchDTO.getManagementName());
        salaryGrantMainTaskPO.setGrantCycle(PrBatchDTO.getActualPeriod());
        return salaryGrantMainTaskPO;
    }

    /**
     * 查询服务周期规则
     * @param cycleRuleId
     * @return CmyFcCycleRuleResponseDTO
     */
    private CycleRuleResponseDTO getCycleRule(Integer cycleRuleId){
        if(cycleRuleId != null){
            com.ciicsh.common.entity.JsonResult<CycleRuleResponseDTO> cmyFcCycleRuleResponseDTOResult = cycleRuleProxy.selectById(String.valueOf(cycleRuleId));
            CycleRuleResponseDTO cycleRuleResponseDTO = new CycleRuleResponseDTO();
            if(!ObjectUtils.isEmpty(cmyFcCycleRuleResponseDTOResult.getData())){
                cycleRuleResponseDTO = cmyFcCycleRuleResponseDTOResult.getData();
            }
            return cycleRuleResponseDTO;
        }else{
            return null;
        }
    }

    /**
     * 根据批次雇员计算结果转换薪资发放雇员信息
     * @param payrollCalcResultDTO
     * @param employeeServiceAgreementDTO
     * @param salaryGrantEmployeePO
     * @return SalaryGrantEmployeePO
     */
    private SalaryGrantEmployeePO convertPayrollCalcResultToSalaryGrantEmployee(PayrollCalcResultDTO payrollCalcResultDTO, EmployeeServiceAgreementDTO employeeServiceAgreementDTO, SalaryGrantEmployeePO salaryGrantEmployeePO){
        salaryGrantEmployeePO.setEmployeeId(payrollCalcResultDTO.getEmpId());
        salaryGrantEmployeePO.setEmployeeName(payrollCalcResultDTO.getEmpName());
        salaryGrantEmployeePO.setTemplateType(employeeServiceAgreementDTO.getTemplateType());
        salaryGrantEmployeePO.setCompanyId(employeeServiceAgreementDTO.getCompanyId());
        salaryGrantEmployeePO.setCompanyName(employeeServiceAgreementDTO.getCompanyName());
        String grantCycle = payrollCalcResultDTO.getPersonnelIncomeYearMonth();
        salaryGrantEmployeePO.setGrantCycle(grantCycle);
        Integer grantMode = employeeServiceAgreementDTO.getGrantType();
        String grantAccountCode = null;
        String grantAccountName = null;
        // 上海本地发放
        if(grantMode != null){
            if(SalaryGrantBizConsts.GRANT_MODE_LOCAL.equals(grantMode)){
                grantAccountCode = String.valueOf(grantMode);
                grantAccountName = SalaryGrantBizConsts.GRANT_ACCOUNT_NAME_LOCAL;
            }else if(SalaryGrantBizConsts.GRANT_MODE_SUPPLIER.equals(grantMode)){
                // 供应商发放
                grantAccountCode = employeeServiceAgreementDTO.getSupplierAccountReceivale();
                grantAccountName = employeeServiceAgreementDTO.getSupplierAccountReceivaleName();
            }else if(SalaryGrantBizConsts.GRANT_MODE_SUPPLIER.equals(grantMode)){
                // 中智代发（客户账户）发放
                grantAccountCode = employeeServiceAgreementDTO.getCompanyId();
                grantAccountName = employeeServiceAgreementDTO.getCompanyName();
            }else{
                // 客户自行发放
                grantAccountCode = employeeServiceAgreementDTO.getCompanyId();
                grantAccountName = employeeServiceAgreementDTO.getCompanyName();
            }
        }
        salaryGrantEmployeePO.setGrantAccountCode(grantAccountCode);
        salaryGrantEmployeePO.setGrantAccountName(grantAccountName);
        salaryGrantEmployeePO.setPaymentAccountCode(employeeServiceAgreementDTO.getPaymentBankAccount());
        salaryGrantEmployeePO.setPaymentAccountName(employeeServiceAgreementDTO.getPaymentBankAccountName());
        salaryGrantEmployeePO.setPaymentAccountBankName(employeeServiceAgreementDTO.getPaymentBankName());
        salaryGrantEmployeePO.setGrantMode(grantMode);
        salaryGrantEmployeePO.setWagePayable(payrollCalcResultDTO.getPersonnelIncomeWageBeforeTax());
        salaryGrantEmployeePO.setPersonalSocialSecurity(payrollCalcResultDTO.getPersonnelSocialSecurity());
        salaryGrantEmployeePO.setIndividualProvidentFund(payrollCalcResultDTO.getPersonnelProvidentFund());
        salaryGrantEmployeePO.setPersonalIncomeTax(payrollCalcResultDTO.getPersonnelIncomeTax());
        salaryGrantEmployeePO.setYearEndBonus(payrollCalcResultDTO.getPersonnelIncomeYearlyBonusAfterTax());
        salaryGrantEmployeePO.setPaymentAmountRMB(payrollCalcResultDTO.getPersonnelIncomeNetPay());
        salaryGrantEmployeePO.setPaymentAmount(payrollCalcResultDTO.getPersonnelIncomeNetPay());

        salaryGrantEmployeePO.setCurrencyCode(employeeServiceAgreementDTO.getCurrencyCode());
        salaryGrantEmployeePO.setExchange(employeeServiceAgreementDTO.getCustomerAgreedExchangeRate());
        salaryGrantEmployeePO.setCountryCode(payrollCalcResultDTO.getCountryCode());
        Integer grantServiceType = employeeServiceAgreementDTO.getGrantServiceType();
        salaryGrantEmployeePO.setGrantServiceType(grantServiceType);
        salaryGrantEmployeePO.setContractType(employeeServiceAgreementDTO.getContractType());
        salaryGrantEmployeePO.setContractId(employeeServiceAgreementDTO.getContractId());
        salaryGrantEmployeePO.setContractFirstParty(employeeServiceAgreementDTO.getContractFirstParty());
        salaryGrantEmployeePO.setWelfareIncluded(employeeServiceAgreementDTO.getWelfareIncluded());

        if(SalaryGrantBizConsts.GRANT_SERVICE_TYPE_TAX.equals(grantServiceType) || SalaryGrantBizConsts.GRANT_SERVICE_TYPE_GRANT_AND_TAX.equals(grantServiceType)){
            Integer taxPeriod = employeeServiceAgreementDTO.getTaxPeriod();
            if(taxPeriod != null){
                if(SalaryGrantBizConsts.TAX_PERIOD_CURRENT_MONTH.equals(taxPeriod)){
                    salaryGrantEmployeePO.setTaxCycle(grantCycle);
                }else if( SalaryGrantBizConsts.TAX_PERIOD_NEXT_MONTH.equals(taxPeriod)){
                    DateFormat df = new SimpleDateFormat("yyyyMM");
                    try {
                        Calendar ct = Calendar.getInstance();
                        ct.setTime(df.parse(grantCycle));
                        ct.add(Calendar.MONTH, +1);
                        salaryGrantEmployeePO.setTaxCycle(df.format(ct.getTime()));
                    } catch (ParseException e) {
                        logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("处理个税期间信息转换异常").setContent(e.getMessage()));
                    }
                }else{
                    DateFormat df = new SimpleDateFormat("yyyyMM");
                    try {
                        Calendar ct = Calendar.getInstance();
                        ct.setTime(df.parse(grantCycle));
                        ct.add(Calendar.MONTH, +2);
                        salaryGrantEmployeePO.setTaxCycle(df.format(ct.getTime()));
                    } catch (ParseException e) {
                        logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("处理个税期间信息转换异常").setContent(e.getMessage()));
                    }
                }
            }
        }else{
            salaryGrantEmployeePO.setTaxCycle("");
        }
        salaryGrantEmployeePO.setDeclarationAccount(employeeServiceAgreementDTO.getDeclarationAccount());
        salaryGrantEmployeePO.setDeclarationAccountCategory(employeeServiceAgreementDTO.getDeclarationAccountCategory());
        salaryGrantEmployeePO.setContributionAccount(employeeServiceAgreementDTO.getContributionAccount());
        salaryGrantEmployeePO.setContributionAccountCategory(employeeServiceAgreementDTO.getContributionAccountCategory());

        return salaryGrantEmployeePO;
    }

    /**
     * 调用雇员的银行卡信息API查询（调用FC客服中心的接口--emp_fc_bankcard）
     * @param paramMap
     * @return List<EmployeeBankcardDTO>
     */
    private List<EmployeeBankcardDTO> listEmployeeBankcardInfo(Map paramMap){
        List<EmployeeBankcardDTO> employeeBankcardDTOList = new ArrayList();
        CmyFcBankCardRequestDTO cmyFcBankCardRequestDTO = new CmyFcBankCardRequestDTO();
        cmyFcBankCardRequestDTO.setEmployeeId((String)paramMap.get("employeeId"));
        if(StringUtils.isEmpty((String)paramMap.get("bankcardId")) ){
            cmyFcBankCardRequestDTO.setBankcardId((String)paramMap.get("bankcardId"));
        }
        if(StringUtils.isEmpty((String)paramMap.get("currencyCode")) ){
            cmyFcBankCardRequestDTO.setCurrencyCode((String)paramMap.get("currencyCode"));
        }

        JsonResult<List<CmyFcBankCardResponseDTO>> bankCardInfoResult = empFcBankProxy.getFcBankCardInfo(cmyFcBankCardRequestDTO);
        List<CmyFcBankCardResponseDTO>  cmyFcBankCardResponseDTOList = bankCardInfoResult.getData();
        if(!CollectionUtils.isEmpty(cmyFcBankCardResponseDTOList)){
            cmyFcBankCardResponseDTOList.forEach(cmyFcBankCardResponseDTO ->{
                EmployeeBankcardDTO employeeBankcardDTO = this.toConvertBankCardInfo(cmyFcBankCardResponseDTO);
                employeeBankcardDTOList.add(employeeBankcardDTO);
            });
        }
        return employeeBankcardDTOList;
    }

    private EmployeeBankcardDTO toConvertBankCardInfo(CmyFcBankCardResponseDTO cmyFcBankCardResponseDTO){
        EmployeeBankcardDTO employeeBankcardDTO = new EmployeeBankcardDTO();
        employeeBankcardDTO.setBankcardId(cmyFcBankCardResponseDTO.getBankcardId());
        employeeBankcardDTO.setEmployeeId(cmyFcBankCardResponseDTO.getEmployeeId());
        employeeBankcardDTO.setCardNum(cmyFcBankCardResponseDTO.getCardNum());
        employeeBankcardDTO.setAccountName(cmyFcBankCardResponseDTO.getAccountName());
        employeeBankcardDTO.setBankCode(cmyFcBankCardResponseDTO.getBankCode());
        employeeBankcardDTO.setDepositBank(cmyFcBankCardResponseDTO.getDepositBank());
        employeeBankcardDTO.setSwiftCode(cmyFcBankCardResponseDTO.getSwiftCode());
        employeeBankcardDTO.setIban(cmyFcBankCardResponseDTO.getIban());
        employeeBankcardDTO.setBankcardType(cmyFcBankCardResponseDTO.getBankcardType());
        employeeBankcardDTO.setUsage(cmyFcBankCardResponseDTO.getUsage());
        if(!ObjectUtils.isEmpty(cmyFcBankCardResponseDTO.getDefult()) && cmyFcBankCardResponseDTO.getDefult() == 1){
            employeeBankcardDTO.setDefaultCard(true);
        }else{
            employeeBankcardDTO.setDefaultCard(false);
        }
        employeeBankcardDTO.setDefaultCardCurrencyCode(cmyFcBankCardResponseDTO.getCurrencyCode());
        employeeBankcardDTO.setDefaultCardExchange(cmyFcBankCardResponseDTO.getExchange());
        employeeBankcardDTO.setProvinceCode(cmyFcBankCardResponseDTO.getProvinceCode());
        employeeBankcardDTO.setCityCode(cmyFcBankCardResponseDTO.getCityCode());
        return employeeBankcardDTO;
    }

    /**
     * 调用薪资发放规则API查询（调用FC客服中心接口）
     * @param paramMap
     * @return List<SalaryGrantRuleDTO>
     */
    private List<SalaryGrantRuleDTO> listSalaryGrantRuleInfo(Map paramMap){
        List<SalaryGrantRuleDTO> salaryGrantRuleDTOList = new ArrayList();

        EmployeeGrantRuleRequestDTO employeeGrantRuleRequestDTO = new EmployeeGrantRuleRequestDTO();
        if(!StringUtils.isEmpty((String)paramMap.get("bankcardId"))){
            employeeGrantRuleRequestDTO.setBankcardId((String)paramMap.get("bankcardId"));
        }
        if(!StringUtils.isEmpty((String)paramMap.get("salaryGrantRuleId"))){
            employeeGrantRuleRequestDTO.setRuleId((String)paramMap.get("salaryGrantRuleId"));
        }
        com.ciicsh.common.entity.JsonResult<List<EmployeeGrantRuleDTO>> grantRuleResult = employeeGrantRuleProxy.getGrantRule(employeeGrantRuleRequestDTO);
        List<EmployeeGrantRuleDTO> employeeGrantRuleDTOList = grantRuleResult.getData();
        if(!CollectionUtils.isEmpty(employeeGrantRuleDTOList)){
            employeeGrantRuleDTOList.forEach(employeeGrantRuleDTO ->{
                SalaryGrantRuleDTO salaryGrantRuleDTO = this.toConvertGrantRuleInfo(employeeGrantRuleDTO);
                salaryGrantRuleDTOList.add(salaryGrantRuleDTO);
            });
        }
        return salaryGrantRuleDTOList;
    }

    private SalaryGrantRuleDTO toConvertGrantRuleInfo(EmployeeGrantRuleDTO employeeGrantRuleDTO){
        SalaryGrantRuleDTO salaryGrantRuleDTO = new SalaryGrantRuleDTO();
        salaryGrantRuleDTO.setSalaryGrantRuleId(employeeGrantRuleDTO.getCmyFcEmpSalaryGrantRuleId());
        salaryGrantRuleDTO.setEmployeeId(employeeGrantRuleDTO.getEmpId());
        salaryGrantRuleDTO.setRuleType(employeeGrantRuleDTO.getRuleType());
        salaryGrantRuleDTO.setCurrencyCode(employeeGrantRuleDTO.getCurrency());
        salaryGrantRuleDTO.setRuleAmount(employeeGrantRuleDTO.getAmount());
        salaryGrantRuleDTO.setRuleRatio(employeeGrantRuleDTO.getPercent());
        salaryGrantRuleDTO.setBankcardId(employeeGrantRuleDTO.getBankcardId());
        return salaryGrantRuleDTO;
    }

    /**
     * 根据雇员的银行卡信息转换薪资发放雇员信息
     * @param salaryGrantEmployeePO
     * @param employeeBankcardDTO
     * @return SalaryGrantEmployeePO
     */
    private SalaryGrantEmployeePO convertEmployeeBankcardToSalaryGrantEmployee(SalaryGrantEmployeePO salaryGrantEmployeePO, EmployeeBankcardDTO employeeBankcardDTO){
        salaryGrantEmployeePO.setBankcardId(employeeBankcardDTO.getBankcardId());
        salaryGrantEmployeePO.setCardNum(employeeBankcardDTO.getCardNum());
        salaryGrantEmployeePO.setAccountName(employeeBankcardDTO.getAccountName());
        salaryGrantEmployeePO.setBankCode(employeeBankcardDTO.getBankCode());
        salaryGrantEmployeePO.setDepositBank(employeeBankcardDTO.getDepositBank());
        salaryGrantEmployeePO.setSwiftCode(employeeBankcardDTO.getSwiftCode());
        salaryGrantEmployeePO.setIban(employeeBankcardDTO.getIban());
        salaryGrantEmployeePO.setBankcardType(employeeBankcardDTO.getBankcardType());
        salaryGrantEmployeePO.setBankcardProvinceCode(employeeBankcardDTO.getProvinceCode());
        salaryGrantEmployeePO.setBankcardCityCode(employeeBankcardDTO.getCityCode());
        salaryGrantEmployeePO.setDefaultCard(employeeBankcardDTO.getDefaultCard() == null ? false : employeeBankcardDTO.getDefaultCard());
        salaryGrantEmployeePO.setCurrencyCode(employeeBankcardDTO.getDefaultCardCurrencyCode());
        salaryGrantEmployeePO.setExchange(employeeBankcardDTO.getDefaultCardExchange());
        return salaryGrantEmployeePO;
    }

    /**
     * 查询财务记账汇率，调用外部接口方法,查询币种对应的汇率（调用结算中心接口）
     * @param currencyCode
     * @return BigDecimal
     */
    private BigDecimal getExchangeInfo(String currencyCode){
        BigDecimal exchange = BigDecimal.ONE;
        com.ciicsh.gto.settlementcenter.gathering.queryapi.result.JsonResult<com.ciicsh.gto.settlementcenter.gathering.queryapi.dto.ExchangeDTO>  exchangeResult = exchangeProxy.getExchange(currencyCode);
        com.ciicsh.gto.settlementcenter.gathering.queryapi.dto.ExchangeDTO exchangeDTO = exchangeResult.getRecord();
        if(!ObjectUtils.isEmpty(exchangeDTO)){
            if(exchangeDTO.getExchange() != null){
                exchange = exchangeDTO.getExchange();
            }else{
                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询财务记账汇率，币种为：" + currencyCode).setContent("返回汇率为空！"));
            }
        }else{
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询财务记账汇率，币种为：" + currencyCode).setContent("返回汇率为空！"));
        }
        return exchange;
    }

    /**
     * 查询实时汇率
     * @param currencyCode
     * @return BigDecimal
     */
    private BigDecimal getCurrentExchangeInfo(String currencyCode){
        BigDecimal exchange = ExchangeManager.exchange(toConvertCurrencyForCurrentExchangeRate(currencyCode),Currencies.CNY);
        if(exchange == null){
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询实时汇率，币种为：" + currencyCode).setContent("返回汇率为空！"));
        }else{
            exchange = BigDecimal.ONE;
        }
        return exchange;
    }

    @Override
    public SalaryGrantMainTaskPO getSalaryGrantMainTaskPO(SalaryGrantMainTaskPO queryCondition){
        SalaryGrantMainTaskPO salaryGrantMainTaskPO = null;
        try{
            salaryGrantMainTaskPO = salaryGrantMainTaskMapper.selectOne(queryCondition);
        }catch (Exception e){
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询任务单主表信息失败！").setContent(e.getMessage()));
        }
        return salaryGrantMainTaskPO;
    }

    @Override
    public SalaryGrantSubTaskPO getSalaryGrantSubTaskPO(SalaryGrantSubTaskPO queryCondition){
        SalaryGrantSubTaskPO salaryGrantSubTaskPO = null;
        try{
            salaryGrantSubTaskPO = salaryGrantSubTaskMapper.selectOne(queryCondition);
        } catch(Exception e){
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询任务单子表信息失败！").setContent(e.getMessage()));
        }
        return salaryGrantSubTaskPO;
    }

    /**
     * 根据其他模块消息订阅，批次重算，需要对任务单主表进行修改。根据计算批次号和计算类型，查询批次业务数据表，根据最新批次的计算信息进行汇总到任务单主表。
     * @param salaryGrantMainTaskPO
     * @return SalaryGrantMainTaskPO
     */
    private boolean modifyMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO){
        // 修改流程表中的任务单主表状态为草稿
        salaryGrantMainTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_DRAFT);
        // 1、 根据批次编号batch_code，批次对应的计算类型（正常、调整、回溯），查询批次表信息
        // 调用计算引擎提供查询批次信息的接口方法
        PrBatchDTO PrBatchDTO = batchProxy.getBatchInfo(salaryGrantMainTaskPO.getBatchCode(), salaryGrantMainTaskPO.getGrantType());
        salaryGrantMainTaskPO = this.convertBatchInfoToSalaryGrantMainTask(salaryGrantMainTaskPO, PrBatchDTO);
        // 2、查询批次计算结果
        List<PayrollCalcResultDTO> payrollCalcResultDTOList = this.listPayrollCalcResult(salaryGrantMainTaskPO);
        if(!CollectionUtils.isEmpty(payrollCalcResultDTOList)){
            // 3、解析雇员计算结果数据，过滤薪资发放的雇员信息，汇总相关数据。
            salaryGrantMainTaskPO = this.toResolvePayrollCalcResultForTask(payrollCalcResultDTOList, salaryGrantMainTaskPO);
            salaryGrantMainTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_DRAFT);
            // 4、修改薪资发放任务单，把查询的批次业务表中数据赋值到主表明细对应字段中。
            boolean updateFlag = this.insertOrUpdateSalaryGrantMainTask(salaryGrantMainTaskPO);
            if(!updateFlag){
                logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("修改薪资发放任务单主表信息，计算批次号为："+salaryGrantMainTaskPO.getBatchCode()).setContent("修改薪资发放任务单记录失败！"));
                return false;
            }
        }else{
            logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("修改薪资发放任务单主表信息，计算批次号为："+salaryGrantMainTaskPO.getBatchCode()).setContent("查询批次计算结果为空，修改薪资发放任务单失败！"));
            return false;
        }
        return true;
    }

    @Override
    public Boolean isExistSalaryGrantMainTask(Map batchParam) {
        SalaryGrantMainTaskPO salaryGrantMainTaskPO;
        SalaryGrantMainTaskPO condition = new SalaryGrantMainTaskPO();
        // 1、接收计算引擎消息，获取计算批次号、批次类型
        condition.setBatchCode((String)batchParam.get("batchCode"));
        condition.setGrantType((Integer)batchParam.get("batchType"));
        condition.setTaskType(SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK);
        condition.setActive(true);
        salaryGrantMainTaskPO = this.getSalaryGrantMainTaskPO(condition);
        if(!ObjectUtils.isEmpty(salaryGrantMainTaskPO)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 根据薪资项名称查询薪资项列表返回对应薪资项的值
     * @param list 薪资项列表
     * @param payItemName 薪资项名称item_name
     * @return Object 薪资项的值item_value
     */
    private Object findValByName(List<DBObject> list, String payItemName) {
        Optional<DBObject> find = list.stream().filter(p -> p.get("item_name").equals(payItemName)).findFirst();
        if (find.isPresent()) {
            return find.get().get("item_value");
        }
        return "";
    }

    /**
     * 根据薪资项名称查询薪资项列表返回对应薪资项的值
     * @param list 薪资项列表
     * @param payItemName 薪资项名称item_name
     * @return Object 薪资项的值item_value
     */
    private Object findValueStrByName(List<DBObject> list, String payItemName) {
        Optional<DBObject> find = list.stream().filter(p -> p.get("item_name").equals(payItemName)).findFirst();
        if (find.isPresent()) {
            return find.get().get("item_value_str");
        }
        return "";
    }

    private String fillZeroForDate(String dateStr){
        if(dateStr.length() == 1){
            dateStr = "0" + dateStr;
        }
        return dateStr;
    }

}
