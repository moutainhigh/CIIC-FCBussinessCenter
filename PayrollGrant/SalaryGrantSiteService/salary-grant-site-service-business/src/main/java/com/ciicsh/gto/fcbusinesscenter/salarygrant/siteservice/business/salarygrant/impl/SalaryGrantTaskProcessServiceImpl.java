package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.JsonParseConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantMainTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.CalcResultItemBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.EmployeeChangeLogBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
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
    public void toClosing(ClosingMsg closingMsg) {
        // 1、接收消息返回的计算批次号、批次类型、版本号
        String batchCode = closingMsg.getBatchCode();
        Integer batchType = closingMsg.getBatchType();
        Long version = closingMsg.getVersion();

        Map batchParam = new HashMap();
        batchParam.put("batchCode",batchCode);
        batchParam.put("batchType",batchType);
        batchParam.put("version",version);
        // 2、根据计算批次号查询批次业务表的批次数据信息，包括计算结果数据及雇员服务协议信息。发起薪资发放任务单。
        SalaryGrantMainTaskPO salaryGrantMainTaskPO;
        SalaryGrantMainTaskPO condition = new SalaryGrantMainTaskPO();
        condition.setBatchCode(batchCode);
        condition.setGrantType(batchType);
        condition.setTaskType(SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK);
        condition.setActive(true);
        salaryGrantMainTaskPO = this.getSalaryGrantMainTaskPO(condition);
        if(!ObjectUtils.isEmpty(salaryGrantMainTaskPO)){
            if (Long.valueOf(version).compareTo(salaryGrantMainTaskPO.getBatchVersion()) > 0){
                salaryGrantMainTaskPO.setBatchVersion(version);
                this.modifySalaryGrantMainTask(salaryGrantMainTaskPO);
            }
        }else{
            this.createSalaryGrantMainTask(batchParam);
        }
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
            // 任务单主表已存在，当其他模块对批次数据要求重算，需要对任务单进行退回至草稿状态，对已存在的任务单需要根据最新批次信息进行修改。
            return true;
        }else{
            // 计算批次对应的任务单信息不存在，根据业务批次信息创建任务单主表信息及雇员信息。
            return false;
        }
    }

    /*
      2018-06-26 最新调整
      对于已存在的任务单主表信息（任务单状态为5-待生成），关账动作，相当于计算引擎重新计算后重新发送计算结果数据，
      发放模块的任务单编号不变，任务单中的汇总数据和雇员信息是重新从mongodb获取的。
      因此修改任务单方法中，包括修改任务单汇总数据、修改version、修改任务单状态（从5-待生成改为0-草稿），重新生成发放雇员数据。
   */
    @Override
    public void modifySalaryGrantMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO) {
        // 修改流程表中的任务单主表状态为草稿
        salaryGrantMainTaskPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_DRAFT);
        // 重新从mongodb获取雇员信息
        // 根据重算批次的计算信息进行汇总到任务单主表,修改薪资发放任务单
        boolean isModified = this.toModifySalaryGrantMainTask(salaryGrantMainTaskPO);
        if(isModified){
            // 重新生成发放雇员数据
            Map batchParam = new HashMap(2);
            batchParam.put("batchCode", salaryGrantMainTaskPO.getBatchCode());
            batchParam.put("batchType", salaryGrantMainTaskPO.getGrantType());
            List<PayrollCalcResultDTO> payrollCalcResultDTOList = this.listPayrollCalcResult(batchParam);
            this.toCreateSalaryGrantEmployee(salaryGrantMainTaskPO,payrollCalcResultDTOList);
        }else{
            logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("修改薪资发放任务单主表信息，计算批次号为："+salaryGrantMainTaskPO.getBatchCode()).setContent("修改薪资发放任务单记录失败，导致无法生成薪资发放雇员信息！"));
        }
    }

    @Override
    public void createSalaryGrantMainTask(Map batchParam) {
        SalaryGrantMainTaskPO salaryGrantMainTaskPO = new SalaryGrantMainTaskPO();

        // 1、接收计算引擎消息，获取计算批次号、批次类型、版本号
        salaryGrantMainTaskPO.setBatchCode((String)batchParam.get("batchCode"));
        salaryGrantMainTaskPO.setGrantType((Integer)batchParam.get("batchType"));
        salaryGrantMainTaskPO.setBatchVersion((Long)batchParam.get("version"));
        // 2、查询批次计算结果
        List<PayrollCalcResultDTO> payrollCalcResultDTOList = this.listPayrollCalcResult(batchParam);

        // 3、创建薪资发放任务单
        boolean isCreated = this.toCreateSalaryGrantMainTask(salaryGrantMainTaskPO, payrollCalcResultDTOList);
        // 4、生成薪资发放雇员信息
        if(isCreated){
            this.toCreateSalaryGrantEmployee(salaryGrantMainTaskPO, payrollCalcResultDTOList);
        }else{
            logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("新增薪资发放任务单主表信息，计算批次号为："+salaryGrantMainTaskPO.getBatchCode()).setContent("插入薪资发放任务单记录失败，导致无法生成薪资发放雇员信息！"));
        }
    }

    /**
     * 新增薪资发放任务单主表信息
     * @param salaryGrantMainTaskPO
     * @return SalaryGrantMainTaskPO
     */
    @Transactional(rollbackFor = Exception.class)
    protected boolean toCreateSalaryGrantMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO,List<PayrollCalcResultDTO> payrollCalcResultDTOList){
        // 1、 根据批次编号batch_code，批次对应的计算类型（正常、调整、回溯），查询批次表信息
        // 调用计算引擎提供查询批次信息的接口方法
        PrBatchDTO PrBatchDTO = batchProxy.getBatchInfo(salaryGrantMainTaskPO.getBatchCode(), salaryGrantMainTaskPO.getGrantType());
        salaryGrantMainTaskPO = this.convertBatchInfoToSalaryGrantMainTask(salaryGrantMainTaskPO, PrBatchDTO);
        if(!CollectionUtils.isEmpty(payrollCalcResultDTOList)){
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

    @Override
    public List<PayrollCalcResultDTO> listPayrollCalcResult(Map batchParam) {
        String batchCode = (String)batchParam.get("batchCode");
        Integer batchType = (Integer)batchParam.get("batchType");
        List<PayrollCalcResultDTO> payrollCalcResultDTOList = new ArrayList<PayrollCalcResultDTO>();
        // 调用计算引擎提供的mongodb数据库查询工具类
        List<DBObject> list  = calResultMongoOpt.list(Criteria.where("batch_id").is(batchCode).and("batch_type").is(batchType));
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
     *  新建薪资发放任务单，把查询的批次业务表中数据赋值到主表明细对应字段中。
     *  插入新增记录，包括entityId、批次信息、雇员数据汇总信息
     *  字段包括：管理方编号、管理方名称、薪酬计算批次号、参考批次号、薪资周期、薪资发放日期、发放类型（默认1）、状态（默认0）、任务单类型（0）、是否有效（1）、创建人、创建时间、修改人、修改时间。
     *  汇总信息：薪资发放总金额（RMB）--直接从计算批次汇总、发薪人数、中方发薪人数、外方发薪人数、中智上海发薪人数--包括大库和独立库、委托机构发薪人数、发放方式。
     * @param payrollCalcResultDTOList
     * @param salaryGrantMainTaskPO
     * @return SalaryGrantMainTaskPO
     */
    private SalaryGrantMainTaskPO toResolvePayrollCalcResultForTask(List<PayrollCalcResultDTO> payrollCalcResultDTOList, SalaryGrantMainTaskPO salaryGrantMainTaskPO){
        // 设置计数器
        // 薪资发放总金额，查询雇员服务协议中的发放服务标识（0、2），对雇员的实发工资累计汇总
        BigDecimal paymentTotalSumCount = new BigDecimal("0.00");
        Double totalSumResult = 0.00;
       /* // 发薪人数，查询雇员服务协议中的发放服务标识（0、2）
        int totalPersonCount = 0;
        // 中方发薪人数，根据发放服务标识（0、2）、国籍（country_code=CN）进行累计
        int chineseCount = 0;
        // 外方发薪人数，根据发放服务标识（0、2）、国籍（country_code!=CN）进行累计
        int foreignerCount = 0;
        // 中智上海发薪人数，根据发放服务标识（0、2）、发放方式(1)进行累计
        int localGrantCount = 0;
        // 中智代发（委托机构）发薪人数，根据发放服务标识（0、2）、发放方式(2)进行累计
        int supplierGrantCount = 0;*/
        // 统计发放方式
        StringBuffer grantModeForTask = new StringBuffer();
        // 薪资发放日期
        String grantDate = "";
        // 薪资发放时段:1-上午，2-下午
        Integer grantTime = SalaryGrantBizConsts.GRANT_TIME_AM;
        // 参考批次号
        String originBatchCode = "";

        // 遍历批次数据信息PayrollCalcResultDTO，把雇员相关的信息存储在自己的数据结构中PayrollCalcResultBO，包括一个雇员计算结果数据的信息、服务协议、日期信息等
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
                // 查询服务周期规则
                /*CmyFcCycleRuleResponseDTO cmyFcCycleRuleResponseDTO = this.getCycleRule(employeeServiceAgreementDTOOne.getCycleRuleId());
                if(!ObjectUtils.isEmpty(cmyFcCycleRuleResponseDTO)){
                    if(!StringUtils.isEmpty(cmyFcCycleRuleResponseDTO.getSalaryDayDate())){
                        grantDate = salaryGrantMainTaskPO.getGrantCycle() + this.toZeroizeForDate(cmyFcCycleRuleResponseDTO.getSalaryDayDate());
                    }
                    if(!StringUtils.isEmpty(cmyFcCycleRuleResponseDTO.getSalaryDayTime())){
                        grantTime = Integer.valueOf(cmyFcCycleRuleResponseDTO.getSalaryDayTime());
                    }
                }else{
                    logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("新增薪资发放任务单主表信息，计算批次号为："+salaryGrantMainTaskPO.getBatchCode()).setContent("查询服务周期规则为空记录！"));
                }*/

                if(SalaryGrantBizConsts.GRANT_TYPE_ADJUST.equals(salaryGrantMainTaskPO.getGrantType()) || SalaryGrantBizConsts.GRANT_TYPE_BACK_TRACE.equals(salaryGrantMainTaskPO.getGrantType())){
                    if(StringUtils.isEmpty(payrollCalcResultDTOOne.getRefBatchId())){
                        originBatchCode = payrollCalcResultDTOOne.getRefBatchId();
                    }else{
                        logClientService.info(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("新增薪资发放任务单主表信息，计算批次号为："+salaryGrantMainTaskPO.getBatchCode()).setContent("调整批次或回溯批次的参考批次号为空！"));
                    }
                }

                for(PayrollCalcResultDTO payrollCalcResultDTO : payrollCalcResultDTOList){
                    // 赋值国籍
                    //countryCode = payrollCalcResultDTO.getCountryCode();
                    // 1、获取雇员服务协议字段信息
                    EmployeeServiceAgreementDTO employeeServiceAgreementDTO = payrollCalcResultDTO.getEmployeeServiceAgreement();
                    grantServiceType = employeeServiceAgreementDTO.getGrantServiceType();
                    grantMode = employeeServiceAgreementDTO.getGrantType();
                    // 2、遍历查询只有发放或者发放+个税的雇员信息。
                /* 3、解析批次结果数据，根据雇员服务协议
                  汇总数据信息：薪资发放总金额（RMB）、发薪人数、中方发薪人数、外方发薪人数、
                                中智上海发薪人数（汇总）、中智代发（委托机构）发薪人数（汇总）、
                                薪资发放日期、薪资发放时段、发放方式（汇总）
                  后台代码用计数器统计，不用数据库查询语句。计数器只记录雇员服务协议中发放服务标识为发放或发放+个税的人员个数。
                  把汇总信息相应字段对应放入SalaryGrantMainTaskPO中
                */
                    if(grantServiceType != null && (SalaryGrantBizConsts.GRANT_SERVICE_TYPE_GRANT.equals(grantServiceType) || SalaryGrantBizConsts.GRANT_SERVICE_TYPE_GRANT_AND_TAX.equals(grantServiceType))){
                        totalSumResult = totalSumResult + Double.valueOf(paymentTotalSumCount.add(payrollCalcResultDTO.getPersonnelIncomeNetPay()).toString());
                        /*totalPersonCount ++;
                        if(SalaryGrantBizConsts.COUNTRY_CODE_CHINA.equals(countryCode)){
                            chineseCount ++;
                        }else{
                            foreignerCount ++;
                        }
                        grantMode = employeeServiceAgreementDTO.getGrantType();
                        if(SalaryGrantBizConsts.GRANT_MODE_LOCAL.equals(grantMode)){
                            localGrantCount ++;
                        }
                        if(SalaryGrantBizConsts.GRANT_MODE_SUPPLIER.equals(grantMode)){
                            supplierGrantCount ++;
                        }*/
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
            /*salaryGrantMainTaskPO.setTotalPersonCount(totalPersonCount);
            salaryGrantMainTaskPO.setChineseCount(chineseCount);
            salaryGrantMainTaskPO.setForeignerCount(foreignerCount);
            salaryGrantMainTaskPO.setLocalGrantCount(localGrantCount);
            salaryGrantMainTaskPO.setSupplierGrantCount(supplierGrantCount);*/
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
            salaryGrantMainTaskPO.setCreatedBy("system");
        }else{
            salaryGrantMainTaskPO.setModifiedBy("system");
            salaryGrantMainTaskPO.setModifiedTime(new Date());
        }
        return insertOrUpdate(salaryGrantMainTaskPO);
    }

    /**
     * 生成薪资发放雇员信息
     * 1、分别对批次计算结果数据及雇员服务协议信息进行json转换解析
     2、查询服务周期规则信息:cmy_fc_cycle_rule 服务周期规则表  薪资发放日_日salary_day_date varchar(2) 薪资发放日_时段：1上午 2下午salary_day_time char(1)
     3、把薪资发放日、时段回填到薪资发放任务主表中
     4、查询雇员的银行卡信息
     5、查询雇员的薪资发放规则
     6、查询币种对应的汇率
     7、查询雇员服务协议
     8、调用后台接口方法创建薪资发放雇员信息，带入任务单编号salaryGrantMainTaskCode
     9、新增薪资发放雇员信息表SalaryGrantEmployeePO（记录任务单主表编号）
     10、根据计算批次数据拆分雇员数据，插入到薪资发放雇员信息表，银行卡信息、薪资发放规则、多币种对应的汇率（拆分发放金额）
     * @param salaryGrantMainTaskPO
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    protected boolean toCreateSalaryGrantEmployee(SalaryGrantMainTaskPO salaryGrantMainTaskPO,List<PayrollCalcResultDTO> payrollCalcResultDTOList){
        // 1、查询批次计算
        // 2、解析payrollCalcResultDTOList批次数据信息
        List<SalaryGrantEmployeePO> salaryGrantEmployeePOList = this.toResolvePayrollCalcResultForEmployee(payrollCalcResultDTOList, salaryGrantMainTaskPO);
        if(!CollectionUtils.isEmpty(salaryGrantEmployeePOList)){
            // 3、插入薪资发放雇员信息表数据。批量插入，调用接口方法新增薪资发放雇员信息，带入任务单编号salaryGrantMainTaskCode。
            salaryGrantEmployeeService.insertBatch(salaryGrantEmployeePOList);
            // 4、是否有本地外币发放，对主表外币发放标识进行回写。
            List<SalaryGrantEmployeePO> localList = salaryGrantEmployeePOList.stream().filter(SalaryGrantEmployeePO -> SalaryGrantEmployeePO.getGrantMode().equals(SalaryGrantBizConsts.GRANT_MODE_LOCAL)).collect(Collectors.toList());
            Long ltwCount = localList.stream().filter(SalaryGrantEmployeePO -> !SalaryGrantEmployeePO.getCurrencyCode().equals(SalaryGrantBizConsts.CURRENCY_CNY)).count();

            // 对相同发放账户的雇员信息遍历list，统计总人数
            salaryGrantMainTaskPO.setTotalPersonCount(Integer.valueOf(salaryGrantEmployeePOList.size()));
            // 针对发放账户统计中、外方人数，根据country_code
            Long chineseCount = salaryGrantEmployeePOList.stream().filter(SalaryGrantEmployeeBO -> SalaryGrantEmployeeBO.getCountryCode().equals(SalaryGrantBizConsts.COUNTRY_CODE_CHINA)).count();
            salaryGrantMainTaskPO.setChineseCount(ObjectUtils.isEmpty(chineseCount) ? 0 : Integer.valueOf(chineseCount.toString()));
            Long foreignerCount = salaryGrantEmployeePOList.stream().filter(SalaryGrantEmployeeBO -> !SalaryGrantEmployeeBO.getCountryCode().equals(SalaryGrantBizConsts.COUNTRY_CODE_CHINA)).count();
            salaryGrantMainTaskPO.setForeignerCount(ObjectUtils.isEmpty(foreignerCount) ? 0 : Integer.valueOf(foreignerCount.toString()));
            // 中智上海发薪人数，发放方式(1)进行累计
            Long localGrantCount = salaryGrantEmployeePOList.stream().filter(SalaryGrantEmployeeBO -> SalaryGrantEmployeeBO.getGrantMode().equals(SalaryGrantBizConsts.GRANT_MODE_LOCAL)).count();
            salaryGrantMainTaskPO.setLocalGrantCount(ObjectUtils.isEmpty(localGrantCount) ? 0 : Integer.valueOf(localGrantCount.toString()));
            // 中智代发（委托机构）发薪人数，发放方式(2)进行累计
            Long supplierGrantCount = salaryGrantEmployeePOList.stream().filter(SalaryGrantEmployeeBO -> SalaryGrantEmployeeBO.getGrantMode().equals(SalaryGrantBizConsts.GRANT_MODE_SUPPLIER)).count();
            salaryGrantMainTaskPO.setSupplierGrantCount(ObjectUtils.isEmpty(supplierGrantCount) ? 0 : Integer.valueOf(supplierGrantCount.toString()));
            salaryGrantMainTaskPO.setGrantDate(salaryGrantEmployeePOList.get(0).getGrantDate());
            salaryGrantMainTaskPO.setGrantTime(salaryGrantEmployeePOList.get(0).getGrantTime());
            if(ltwCount > 0L){
                salaryGrantMainTaskPO.setIncludeForeignCurrency(true);
            }
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
        // 解析payrollCalcResultDTOList批次数据信息
        // 1、遍历批次数据信息PayrollCalcResultDTO，把雇员相关的信息存储在自己的数据结构中PayrollCalcResultBO
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
                            grantDate = salaryGrantMainTaskPO.getGrantCycle() + this.toZeroizeForDate(cycleRuleResponseDTO.getSalaryDayDate());
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
                        // 2、根据自己的雇员数据结构，遍历查询只有发放或者发放+个税的雇员信息，放到最终的SalaryGrantEmployeePO结果中。
                    /*查询批次结果数据，对雇员银行卡信息、币种、发放数据进行拆分
                      中间过程需要查询多张表
                      雇员的银行卡信息、雇员服务协议、服务周期规则、薪资发放规则、汇率表
                    */
                        salaryGrantEmployeePO.setGrantStatus(SalaryGrantBizConsts.GRANT_STATUS_NORMAL);
                        salaryGrantEmployeePO.setActive(true);
                        salaryGrantEmployeePO.setCreatedBy("system");
                        salaryGrantEmployeePO.setCreatedTime(new Date());
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
                    // 雇员必须有一张默认银行卡，由雇员中心控制逻辑。
                    logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("查询雇员银行卡信息，雇员编号为：" + salaryGrantEmployeePO.getEmployeeId()).setContent("该雇员无默认银行卡！"));
                }
                // 雇员的发放金额结算结果为0，则默认工资卡的发放金额为0，不进行遍历其他银行卡和发放规则
                if(paymentAmountRMB.equals(sumDefalutZero)) {
                    resultList.add(salaryGrantEmployeePODefault);
                }else{
                    // 拆分雇员数据银行卡信息、币种、汇率发放金额，根据拆分的个数建立SalaryGrantEmployeePO（一个雇员根据银行卡对应的发放规则，发放金额会随币种*汇率拆分多条发放数据）
                    for(EmployeeBankcardDTO employeeBankcardDTO : employeeBankcardDTOList){
                        // 对非默认卡的发放规则进行处理
                        if(employeeBankcardDTO.getDefaultCard() != null && !employeeBankcardDTO.getDefaultCard()){
                            SalaryGrantEmployeePO salaryGrantEmployeePOTemp = (SalaryGrantEmployeePO) salaryGrantEmployeePO.clone();
                            // 添加雇员的银行卡信息
                            salaryGrantEmployeePOTemp = this.convertEmployeeBankcardToSalaryGrantEmployee(salaryGrantEmployeePOTemp, employeeBankcardDTO);
                            // 2、根据雇员编号查询薪资发放规则SalaryGrantRuleDTO--PayrollCalcResultBO（调用FC客服中心接口--cmy_fc_employee_salary_grant_rule）
                            //    根据雇员编号和银行卡号查询银行卡对应的发放规则，一张银行卡可能对应多个发放规则，有按金额或比例及币种信息对银行卡发放金额进行设置。
                            // 规则类型（0-固定金额、1-固定比例）
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
                    // 发放金额的剩余金额放到默认工资卡，币种为人民币。
                    // 雇员中心银行卡信息字段信息：默认卡，默认币种，对应汇率（客户约定、实时），对剩余金额按默认汇率折合默认币种。
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
            // 如果规则比例不为0，页面输入值例如：90.50，需要在计算是除以100进行换算再计算。
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
     * 查询雇员服务协议EmployeeServiceAgreementDTO--exchangeCalcMode根据汇率计算方式（0 - 固定， 1 - 实时，2-记账）-- 客户约定汇率/实时汇率/财务记账汇率
     * 如果是客户约定汇率，查询雇员服务协议EmployeeServiceAgreementDTO--customerAgreedExchangeRate
     * 如果是实时汇率查询币种对应的汇率ExchangeManager.exchange（调用实时汇率接口）
     * 如果是财务记账汇率查询币种对应的汇率ExchangeProxy.getExchange（调用结算中心接口）
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

    /**
     * 修改薪资发放任务单主表信息
     * @param salaryGrantMainTaskPO
     * @return
     */
    private void toUpdateSalaryGrantMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO){
        // todo 添加逻辑处理
        this.updateSalaryGrantMainTask(salaryGrantMainTaskPO);
    }


    @Override
    public Integer updateSalaryGrantMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO) {
        return salaryGrantMainTaskMapper.updateById(salaryGrantMainTaskPO);
    }

    /**
     * 计算批次信息转换到任务单主表对应字段
     * 查询信息包括：管理方编号、管理方名称、薪资期间（年月）、批次状态、是否代垫、是否来款
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
        //salaryGrantEmployeePO.setEmployeeServiceAgreementId(employeeServiceAgreementDTO.getEmployeeServiceAgreementId()); // 雇员服务协议编号 目前mongodb中无此信息
        String grantCycle = payrollCalcResultDTO.getPersonnelIncomeYearMonth();
        salaryGrantEmployeePO.setGrantCycle(grantCycle);
        Integer grantMode = employeeServiceAgreementDTO.getGrantType();
        String grantAccountCode = null;
        String grantAccountName = null;
        // 上海本地发放：发放账户=发放方式=中智上海账户
        if(grantMode != null){
            if(SalaryGrantBizConsts.GRANT_MODE_LOCAL.equals(grantMode)){
                grantAccountCode = String.valueOf(grantMode);
                grantAccountName = SalaryGrantBizConsts.GRANT_ACCOUNT_NAME_LOCAL;
            }else if(SalaryGrantBizConsts.GRANT_MODE_SUPPLIER.equals(grantMode)){
                // 供应商发放：发放账户=雇员服务协议中的supplierAccountReceivale供应商收款账户，配合grantType=2是否供应商进行判断赋值,按照供应商收款账户拆分。
                grantAccountCode = employeeServiceAgreementDTO.getSupplierAccountReceivale();
                grantAccountName = employeeServiceAgreementDTO.getSupplierAccountReceivaleName();
            }else if(SalaryGrantBizConsts.GRANT_MODE_SUPPLIER.equals(grantMode)){
                // 中智代发（客户账户）发放：发放账户=companyCode 按照公司进行拆分。
                grantAccountCode = employeeServiceAgreementDTO.getCompanyId();
                grantAccountName = employeeServiceAgreementDTO.getCompanyName();
            }else{
                // 客户自行发放：发放账户=companyCode 按照公司进行拆分。
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
        /* in bankcard process method need to split
        salaryGrantEmployeePO.setSalaryGrantRuleId(employeeServiceAgreementDTO.getSalaryGrantRuleId());
        salaryGrantEmployeePO.setDefaultCard(true); */
        /* 从雇员服务协议获取的币种和汇率，作为参考，看后面是否需要使用。目前币种和汇率没有指定到银行卡。*/
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
                if(taxPeriod == SalaryGrantBizConsts.TAX_PERIOD_CURRENT_MONTH ){
                    salaryGrantEmployeePO.setTaxCycle(grantCycle);
                }else if(taxPeriod == SalaryGrantBizConsts.TAX_PERIOD_NEXT_MONTH){
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

        // 根据雇员编号和公司编号查询雇员的银行卡信息
        // select t1.bankcard_id,t1.employee_id,t1.bankcard_type,t1.deposit_bank,t1.bank_code,t1.account_name,t1.card_num,
        // t2.usage,t2.is_defalut
        // from emp_bankcard t1,emp_company_bankcard_relation t2
        // where t1.bankcard_id=t2.bankcard_id and t1.status=1 and t2.is_active=1 and t2.usage in(1,3) and t1.employee_id=#employeeId# and t2.company_id=#companyId#
        // and t1.card_num=#cardNum#
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
        Boolean isDefault = true;
        if(ObjectUtils.isEmpty(cmyFcBankCardResponseDTO.getDefult())){
            isDefault = false;
        }else if(cmyFcBankCardResponseDTO.getDefult() == 1){
            isDefault = true;
        }else{
            isDefault = false;
        }
        employeeBankcardDTO.setDefaultCard(isDefault);
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
        // 根据雇员编号和银行卡号查询银行卡对应的发放规则
        // 调用薪资发放规则API查询（调用FC客服中心接口）
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

    // todo
    // 写一个总的控制提交逻辑的方法供toSubmitMainTask调用，内部调用子方法：查询薪资发放日是否为空、查询批次对应的账单是否已核销/已代垫、如果是外区是否要发起代垫流程

    // todo
    // 查询批次对应的账单是否已核销/已代垫（调用结算中心接口--如果批次业务表已经有代垫/来款标识，就不需要这个查询方法了，可以归在17个方法查询计算批次里做。2018-02-05后提供接口）

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
     * 查询草稿状态的薪资发放任务单时，需要查询雇员信息是否有变更，如果有变更对雇员信息进行修改和金额拆分。需要分页方法调用此方法。
     * @param
     * @return
     */
    public void  toDealSalaryGrantMainTaskPOByStatusOfDraft(){
        // todo 前端页面调用分页
        Map paramMap = new HashMap(1);
        Map resultMap = this.listEmployeeChangeInfo(paramMap);
        List<SalaryGrantMainTaskPO> taskList = this.toRegroupEmployeeChangeInfo(resultMap);
        // toRegroupEmployeeChangeInfo -> toSplitChangeInfo
        //                             -> toResolveEmployeeChangeLog
        //                             -> toSplitAmountByBankCard
        //                                -> toResolveDefaultCard
        //                                -> toResolveNotDefaultCard
        //                             -> toSplitAmountByGrantRule
        //                                -> toResolveDefaultCard
        //                                -> toResolveNotDefaultCard
        this.toUpdateSalaryGrantMainTaskForAlterEmployee(taskList);
        // toUpdateSalaryGrantMainTaskForAlterEmployee -> toGatherSalaryGrantMainTaskForAlterEmployeeSingle
    }

    /**
     * 根据修改后的雇员数据，再重新统计各汇总字段信息，修改任务单信息表。
     * 如果在生成任务单主表后，有雇员的发放方式、国籍信息有修改变化的,可能单个雇员，也可能批量多个雇员。
     * 需要增加方法，对修改后的雇员信息再进行统计后，修改任务单主表信息。
     * 修改字段包括：发薪人数（一般不会变，除非计算引擎计算的人数发生变化）、中方发薪人数、外方发薪人数、
     *               中智上海发薪人数、中智代发（委托机构）发薪人数、发放方式（对生成的组合条件字符串进行修改调整）。
     *  1、国籍变化影响的字段：发薪人数（一般不会变，除非计算引擎计算的人数发生变化）、中方发薪人数、外方发薪人数
     *  2、发放方式变化影响的字段：中智上海发薪人数、中智代发（委托机构）发薪人数、发放方式
     * @param salaryGrantMainTaskCode
     * @param remark
     * @return SalaryGrantMainTaskPO
     */
    private SalaryGrantMainTaskPO toGatherSalaryGrantMainTaskForAlterEmployeeSingle(String salaryGrantMainTaskCode, String remark){
        SalaryGrantMainTaskPO queryCondition = new SalaryGrantMainTaskPO();
        queryCondition.setSalaryGrantMainTaskCode(salaryGrantMainTaskCode);
        SalaryGrantMainTaskPO salaryGrantMainTaskPO = this.getSalaryGrantMainTaskPO(queryCondition);
        List<SalaryGrantEmployeePO> salaryGrantEmployeePOList = this.listSalaryGrantEmployeePOForAlter(salaryGrantMainTaskCode, SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN);
        // select t.payment_amount_rmb as totalsum,t.grant_mode,t.grant_date,t.grant_time,t.country_code
        // 设置计数器
        // 薪资发放总金额，查询雇员服务协议中的发放服务标识（0、2），对雇员的实发工资累计汇总
        BigDecimal paymentTotalSumCount = new BigDecimal(0);
        // 发薪人数，查询雇员服务协议中的发放服务标识（0、2）
        Long totalPersonCount = 0L;
        // 中方发薪人数，根据发放服务标识（0、2）、国籍（country_code=china）进行累计
        Long chineseCount = 0L;
        // 外方发薪人数，根据发放服务标识（0、2）、国籍（country_code!=china）进行累计
        Long foreignerCount = 0L;
        // 中智上海发薪人数，根据发放服务标识（0、2）、发放方式(0)进行累计
        Long localGrantCount = 0L;
        // 中智代发（委托机构）发薪人数，根据发放服务标识（0、2）、发放方式(1)进行累计
        Long supplierGrantCount = 0L;
        // 统计发放方式
        StringBuffer grantModeForTask = new StringBuffer("");
        // 薪资发放日期
        String grantDate = "";
        // 薪资发放时段:1-上午，2-下午
        Integer grantTime = SalaryGrantBizConsts.GRANT_TIME_AM;

        // 1、遍历批次数据信息PayrollCalcResultDTO，把雇员相关的信息存储在自己的数据结构中PayrollCalcResultBO，包括一个雇员计算结果数据的信息、服务协议、日期信息等
        if(salaryGrantEmployeePOList != null && salaryGrantEmployeePOList.size() > 0) {
            paymentTotalSumCount = salaryGrantEmployeePOList.stream().map(SalaryGrantEmployeePO::getPaymentAmountRMB).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalPersonCount = salaryGrantEmployeePOList.stream().map(SalaryGrantEmployeePO::getEmployeeId).count();
            chineseCount = salaryGrantEmployeePOList.stream().filter(SalaryGrantEmployeePO -> SalaryGrantEmployeePO.getCountryCode().equals(SalaryGrantBizConsts.COUNTRY_CODE_CHINA))
                    .map(SalaryGrantEmployeePO::getEmployeeId).count();
            foreignerCount = salaryGrantEmployeePOList.stream().filter(salaryGrantEmployeePO -> !salaryGrantEmployeePO.getCountryCode().equals(SalaryGrantBizConsts.COUNTRY_CODE_CHINA))
                    .map(SalaryGrantEmployeePO::getEmployeeId).count();
            localGrantCount = salaryGrantEmployeePOList.stream().filter(salaryGrantEmployeePO -> !salaryGrantEmployeePO.getGrantMode().equals(SalaryGrantBizConsts.GRANT_MODE_LOCAL))
                    .map(SalaryGrantEmployeePO::getEmployeeId).count();
            supplierGrantCount = salaryGrantEmployeePOList.stream().filter(salaryGrantEmployeePO -> !salaryGrantEmployeePO.getGrantMode().equals(SalaryGrantBizConsts.GRANT_MODE_SUPPLIER))
                    .map(SalaryGrantEmployeePO::getEmployeeId).count();
            SalaryGrantEmployeePO salaryGrantEmployeePO = salaryGrantEmployeePOList.get(0);
            grantDate = salaryGrantEmployeePO.getGrantDate();
            grantTime = salaryGrantEmployeePO.getGrantTime();
            salaryGrantEmployeePOList.forEach(salaryGrantEmployeePOTemp -> {
                if(grantModeForTask.indexOf(String.valueOf(salaryGrantEmployeePOTemp.getGrantMode())) < 0){
                    grantModeForTask.append(String.valueOf(salaryGrantEmployeePOTemp.getGrantMode()));
                }
            });
        }
        salaryGrantMainTaskPO.setPaymentTotalSum(paymentTotalSumCount);
        salaryGrantMainTaskPO.setTotalPersonCount(Integer.valueOf(totalPersonCount.toString()));
        salaryGrantMainTaskPO.setChineseCount(Integer.valueOf(chineseCount.toString()));
        salaryGrantMainTaskPO.setForeignerCount(Integer.valueOf(foreignerCount.toString()));
        salaryGrantMainTaskPO.setLocalGrantCount(Integer.valueOf(localGrantCount.toString()));
        salaryGrantMainTaskPO.setSupplierGrantCount(Integer.valueOf(supplierGrantCount.toString()));
        salaryGrantMainTaskPO.setGrantDate(grantDate);
        salaryGrantMainTaskPO.setGrantTime(grantTime);
        salaryGrantMainTaskPO.setGrantMode(grantModeForTask.toString());
        salaryGrantMainTaskPO.setRemark(remark);
        return salaryGrantMainTaskPO;
    }

    /**
     * 根据修改后的雇员数据，再重新统计各汇总字段信息，批量修改任务单主表信息。
     * @param taskList
     * @return
     */
    private void toUpdateSalaryGrantMainTaskForAlterEmployee(List<SalaryGrantMainTaskPO> taskList){
        List<SalaryGrantMainTaskPO> updateList = new ArrayList<>();
        taskList.forEach(SalaryGrantMainTaskPO -> {
            SalaryGrantMainTaskPO updatePO = this.toGatherSalaryGrantMainTaskForAlterEmployeeSingle(SalaryGrantMainTaskPO.getSalaryGrantMainTaskCode(), SalaryGrantMainTaskPO.getRemark());
            updateList.add(updatePO);
        });

        // todo 批量修改任务单信息
        // salaryGrantMainTaskMapper.batchUpdate(updateList);
    }

    // todo 查询任务单列表时，对草稿状态的任务单需要查询任务单下雇员的信息是否有变更，调用接口方法查询雇员变更日志信息表，
    //       有变更则需要刷新薪资发放雇员信息表中的雇员信息，同时对任务单主表的明细信息（中方发薪人数、外方发薪人数、中智上海发薪人数、中智代发（委托机构）发薪人数、发放方式、备注）进行修改。
    //       提供给任务列表页面和雇员列表的备注显示变更信息链接。
    /*
     * 1、查询状态是草稿的任务单列表时，调用雇员变更日志信息表查询方法，遍历任务单雇员编号查询雇员变更日志信息表中是否有计算批次号对应的雇员信息。
     * 2、对存在变更的雇员，记录变更字段信息，对应变更类型中。
     * 3、根据雇员信息变更的类型进行归类，归类的信息包括employeeId列表、雇员变更类型（1 - 国籍；2 - 发放方式；3 - 发放账户；4 - 银行卡；5 - 雇员服务协议；6 - 薪资发放规则）、变更类型原来值、变更类型新的值。
     *    基本信息（1 - 国籍）
     *    银行卡信息（1 - 收款人账号即卡号；2 - 收款人姓名；3 - 收款行行号；4 - 收款行名称；5 - 银行国际代码；6 - 国际银行账户号码；7 - 银行卡种类）
     *    -- 银行卡的卡号信息变化了，需要把原来雇员按照银行卡拆分的信息需要都查出来重新拆分。--不需要重新拆分，只有银行卡删除，新增一个银行卡和发放规则绑定，需要删除后重新按照新银行卡和规则拆分。
     *    薪资发放规则（1 - 银行卡号；2 - 币种；3 - 汇率；4 - 金额；5 - 比例）
     *    -- 发放规则变了，需要重新拆分雇员信息的发放金额，可能多币种。把原来雇员拆分的信息需要都查出来重新拆分。
     *    雇员服务协议（1 - 发放方式；2 - 发放账户）
     * 4、刷新薪资发放雇员信息表中的雇员信息,涉及薪资发放规则变更，可能会导致已拆分的雇员发放数据重新拆分。
     * 5、修改薪资发放任务单主表的明细信息
     * 6、根据雇员变更类型组织备注链接信息，修改任务主表的备注字段，给页面提供备注显示的链接信息。
     * 7、根据雇员变更类型组织备注链接信息，修改薪资发放雇员信息表的备注字段，给页面提供备注显示的链接信息。
     *    -- 组织json，直接放到备注信息中，不再进行第二次遍历查找，浪费资源。把原来薪资发放雇员信息表中的从批次数据中获取的存一份，把从雇员信息表中查询的数据放一份新的。
     *       保存到变更日志字段中，再把薪资发放雇员信息的数据刷新。
     * */

    // 1、查询状态是草稿的任务单列表
    // 2、调用雇员变更日志信息表查询方法
    // 3、修改薪资发放雇员信息表中的雇员信息
    // 4、修改薪资发放任务单主表的明细信息，根据刷新后的雇员信息，重新统计发放方式。
    // 5、修改任务主表的备注字段
    // 6、修改薪资发放雇员信息表的备注字段
    // 7、业务逻辑处理：对存在变更的雇员，记录变更字段信息，对应变更类型中。根据雇员信息变更的类型进行归类，归类的信息包括employeeId列表、雇员变更类型（1 - 国籍；2 - 发放方式；3 - 发放账户）、变更类型原来值、变更类型新的值。

    /**
     * 查询状态是草稿的任务单列表，任务单中薪资发放雇员信息关联雇员变更日志信息表查询，根据传入的变更类型返回雇员变更信息结果
     * @param paraMap
     * @return Map
     */
    private Map listEmployeeChangeInfo(Map paraMap){
        Map resultMap = new HashMap(2);
        // 查询返回的结果是所有草稿状态任务单及薪资发放雇员信息的变更雇员列表
        List<EmployeeChangeLogBO> employeeChangeLogBOList = new ArrayList<>();
        String changeType = (String)paraMap.get("changeType");
        /*
            查询语句放到薪资发放雇员信息表的mapper的xml中
            select t3.employee_id,t3.change_table_name,t3.change_table_id,t3.change_field,t3.change_value,t3.change_type,t3.change_operation,t3.modified_time,
            t4.salary_grant_main_task_code, t4.salary_grant_employee_id,t4.grant_account_code,t4.grant_mode,t4.salary_grant_rule_id,
            t4.bankcard_id,t4.card_num,t4.account_name,t4.bank_code,t4.deposit_bank,t4.bankcard_type,t4.country_code,t4.change_log,t4.created_time,
            t4.empremark,t4.taskremark,t4.payment_amount_rmb,t4.payment_amount,t4.company_id,max(t3.modified_time)
            from employee_change_log t3,
            (select t2.salary_grant_main_task_code, t2.salary_grant_employee_id,t2.grant_account_code,t2.grant_mode,
               t2.salary_grant_rule_id,t2.bankcard_id,t2.card_num,t2.account_name,t2.bank_code,t2.deposit_bank,t2.bankcard_type,
               t2.country_code,t2.change_log,t2.remark empremark,t2.payment_amount_rmb,t2.payment_amount,t2.company_id
               t1.created_time,t1.remark taskremark
            from sg_salary_grant_main_task t1, sg_salary_grant_employee t2
            where t1.salary_grant_main_task_code=t2.salary_grant_main_task_code and t1.task_status='0' and t1.is_active='1'
            and t2.is_active='1' ) t4
            and t3.employee_id and t4.employee_id
            and t3.modified_time > t4.created_time
            group by t3.employee_id, max(t3.modified_time);

            change_type
            country: select t3.country_code
            bankcard:select t3.card_num,t3.account_name,t3.bank_code,t3.deposit_bank
            grantrule:select t3.grant_rule_id,t3.card_num,t3.currency,t3.rule_amount,t3.rule_ratio
            serviceagreement:select t3.payrollType,t3.payrollAccount
        * */
        // 雇员变更日志信息表:变更类型、变更信息（根据不同类型，放入不同格式的json串，对每个修改的字段存放2个值，修改前和修改后的值，便于后面对比）、修改时间。
        // todo 通过查询语句，获取草稿状态的任务单下的雇员信息是否有变更，在雇员变更日志信息表可以查询到结果
        // 暂定：传入参数change_type，根据不同的变更类型查询日志表中不同的变更字段信息。如果变更信息采用json串，需要解析json中的值。
        resultMap.put("employeeChangeLogBOList", employeeChangeLogBOList);
        resultMap.put("changeType", changeType);
        return resultMap;
    }

    /**
     * 根据雇员信息变更的类型进行归类，归类的信息包括employeeId列表、雇员变更类型（1 - 国籍；2 - 发放方式；3 - 发放账户；4 - 银行卡；5 - 雇员服务协议；6 - 薪资发放规则）、变更类型原来值、变更类型新的值。
     * @param resultMap
     * @return List<SalaryGrantMainTaskPO>
     */
    private List<SalaryGrantMainTaskPO> toRegroupEmployeeChangeInfo(Map resultMap){
        Map changeMap = new HashMap(2);
        List<EmployeeChangeLogBO> employeeChangeLogBOList = (List<EmployeeChangeLogBO>) resultMap.get("employeeChangeLogBOList");
        //String changeType = (String)resultMap.get("changeType");
        // 任务单主表编号列表，用来提供修改任务单信息的查询条件，雇员信息修改后，任务单需要重新统计汇总信息和修改备注字段信息
        List<SalaryGrantMainTaskPO> taskList = new ArrayList();

        // 先按照变更类型分组对查询结果进行拆分
        Map<String, List<EmployeeChangeLogBO>> groupByChangeType = employeeChangeLogBOList.stream().collect(Collectors.groupingBy(EmployeeChangeLogBO::getChangeType));
        groupByChangeType.forEach((changeType,changeInfoByTaskCodeList) -> {

            // 再按薪资发放任务单编号对结果进行分组
            Map<String, List<EmployeeChangeLogBO>> groupByTaskCode = changeInfoByTaskCodeList.stream().collect(Collectors.groupingBy(EmployeeChangeLogBO::getSalaryGrantMainTaskCode));
            groupByTaskCode.forEach((salaryGrantMainTaskCode, employeeChangeLogBOList1) -> {
                // 修改薪资发放任务单列表备注字段
                SalaryGrantMainTaskPO salaryGrantMainTaskPO = new SalaryGrantMainTaskPO();
                salaryGrantMainTaskPO.setSalaryGrantMainTaskCode(salaryGrantMainTaskCode);
                salaryGrantMainTaskPO.setRemark(SalaryGrantBizConsts.TASK_REMARK);
                // 修改薪资发放雇员信息列表
                //List<SalaryGrantEmployeeBO> salaryGrantEmployeeBOList = new ArrayList<>();
                // 遍历变更的雇员信息，包括雇员的变更信息和薪资发放雇员的信息
                employeeChangeLogBOList1.forEach(employeeChangeLogBO ->{
                    // 查询变更信息拆分为薪资发放雇员的信息
                    SalaryGrantEmployeePO salaryGrantEmployeePO = this.toSplitChangeInfo(employeeChangeLogBO);
                    Integer changeOperation = employeeChangeLogBO.getChangeOperation();
                    // 针对修改雇员信息的记录，进行变更日志字段和备注字段信息修改
                    if(SalaryGrantBizConsts.CHANGE_OPERATION_UPDATE.equals(changeOperation)){
                        // 修改变更日志字段json、修改薪资发放雇员的信息相应变更的字段值、修改备注字段信息，添加变更类型信息，用#分割
                        salaryGrantEmployeePO = this.toResolveEmployeeChangeLog(employeeChangeLogBO,salaryGrantEmployeePO);
                    }
                    // todo 雇员变更日志表的变更类型、变更字段需要和运营中心确定好接口,最好changeType定位到小类上，比如银行卡信息的银行卡号（新增、修改、删除）
                    // 根据changeType变更类型，修改变更日志字段信息。如果银行卡删除、薪资发放规则进行修改（更换银行卡、规则变化-比例、币种、金额），需要单独进行处理。
                    // 根据changeType进行判断，不同变更类型，采用不同方法进行处理（仅仅修改现有信息、对原拆分数据进行逻辑删除、新增雇员拆分信息）
                    // 增、删、改是用salary_grant_employee_id保证唯一标识，查询用employee_id可能是多条原拆分的记录。
                    // 相同的changeType变更类型，可能会有不同的change_operation操作类型（增、删、改），所以还是单条雇员信息处理比较准确，不采用批量处理了。
                    /*if(SalaryGrantBizConsts.EMPLOYEE_ALTER_TYPE_COUNTRY.equals(changeType)){
                        // 修改现有信息--国籍
                        // todo 修改薪资发放雇员信息
                        // salaryGrantEmployeeMapper.update(salaryGrantEmployeePO)
                    }
                    if(SalaryGrantBizConsts.EMPLOYEE_ALTER_TYPE_GRANT_MODE.equals(changeType)){
                        // 修改现有信息--发放方式
                    }
                    if(SalaryGrantBizConsts.EMPLOYEE_ALTER_TYPE_GRANT_ACCOUNT.equals(changeType)){
                        // 修改现有信息--发放账户
                    }
                    if("cycle_rule".equals(changeType)){
                        // 修改现有信息-服务周期规则
                        // 修改现有信息-薪资发放日期
                        // 修改现有信息-薪资发放时段
                    }
                    if("bank_card".equals(changeType)){

                        if("update".equals("change_operation")){
                            // 修改现有信息
                        }
                        if("delete".equals("change_operation")){
                            // changeType子类为银行卡号
                            // 删除字段为银行卡号
                            // 删除现有信息，逻辑删除薪资发放雇员信息
                        }
                        if("insert".equals("change_operation")){
                            // 新增字段为银行卡号
                            // 新增薪资发放雇员信息，根据银行卡及对应的发放规则。
                        }
                    }
                    if("grant_rule".equals(changeType)){

                        if("update".equals("change_operation")){
                            // 修改现有信息
                        }
                        if("delete".equals("change_operation")){
                            // changeType子类为规则id
                            // 删除规则id
                            // 删除现有信息，逻辑删除薪资发放雇员信息
                        }
                        if("insert".equals("change_operation")){
                            // 新增规则id
                            // 新增薪资发放雇员信息，根据银行卡及对应的发放规则。
                        }
                    }*/
                    //salaryGrantEmployeeBOList.add(salaryGrantEmployeeBO);
                });



                // 涉及相同employeeId的数据先全部逻辑删除，再根据新的银行卡和发放规则对雇员的发放金额重新拆分，对重新拆分的雇员信息记录变更日志，原银行卡和新银行卡，原发放规则和新的发放规则。
                taskList.add(salaryGrantMainTaskPO);
            });
            // TODO 批量修改薪资发放任务单备注信息
            // salaryGrantMainTaskMapper.batchUpdate(taskList);
        });

        //String[] employeeIdArray = employeeChangeLogBOList1.stream().map(EmployeeChangeLogBO::getEmployeeId).toArray(String[]::new);
        return taskList;
    }

    /**
     * 把雇员变更的信息添加至雇员信息实体对象中
     * @param employeeChangeLogBO
     * @return SalaryGrantEmployeePO
     */
    private SalaryGrantEmployeePO toSplitChangeInfo(EmployeeChangeLogBO employeeChangeLogBO){
        // t2.salary_grant_main_task_code, t2.salary_grant_employee_id,t2.grant_account_code,t2.grant_mode,t2.salary_grant_rule_id,
        // t2.bankcard_id,t2.card_num,t2.account_name,t2.bank_code,t2.deposit_bank,t2.bankcard_type,t2.country_code,t2.change_log,
        SalaryGrantEmployeePO salaryGrantEmployeePO = new SalaryGrantEmployeePO();
        salaryGrantEmployeePO.setSalaryGrantEmployeeId(employeeChangeLogBO.getSalaryGrantEmployeeId());
        salaryGrantEmployeePO.setSalaryGrantMainTaskCode(employeeChangeLogBO.getSalaryGrantMainTaskCode());
        salaryGrantEmployeePO.setGrantAccountCode(employeeChangeLogBO.getGrantAccountCode());
        salaryGrantEmployeePO.setGrantMode(employeeChangeLogBO.getGrantMode());
        salaryGrantEmployeePO.setSalaryGrantRuleId(employeeChangeLogBO.getSalaryGrantRuleId());
        salaryGrantEmployeePO.setBankcardId(employeeChangeLogBO.getBankcardId());
        salaryGrantEmployeePO.setCardNum(employeeChangeLogBO.getCardNum());
        salaryGrantEmployeePO.setAccountName(employeeChangeLogBO.getAccountName());
        salaryGrantEmployeePO.setBankCode(employeeChangeLogBO.getBankCode());
        salaryGrantEmployeePO.setDepositBank(employeeChangeLogBO.getDepositBank());
        salaryGrantEmployeePO.setBankcardType(employeeChangeLogBO.getBankcardType());
        salaryGrantEmployeePO.setCountryCode(employeeChangeLogBO.getCountryCode());
        salaryGrantEmployeePO.setChangeLog(employeeChangeLogBO.getChangeLog());
        salaryGrantEmployeePO.setRemark(employeeChangeLogBO.getEmpRemark());
        salaryGrantEmployeePO.setPaymentAmountRMB(employeeChangeLogBO.getPaymentAmountRMB());
        salaryGrantEmployeePO.setPaymentAmount(employeeChangeLogBO.getPaymentAmount());
        salaryGrantEmployeePO.setCompanyId(employeeChangeLogBO.getCompanyId());
        return salaryGrantEmployeePO;
    }

    /**
     * 处理变更日志字段json的逻辑。修改薪资发放雇员信息表的变更日志信息，对存在变更的信息进行修改，对不存在的信息进行添加。更新雇员备注字段.
     * @param employeeChangeLogBO
     * @param salaryGrantEmployeePO
     * @return SalaryGrantEmployeePO
     */
    private SalaryGrantEmployeePO toResolveEmployeeChangeLog(EmployeeChangeLogBO employeeChangeLogBO, SalaryGrantEmployeePO salaryGrantEmployeePO){
        // 解析changeLog的json格式，遍历字段名称，与employeeChangeLogBO.change_field对比
        String changeLog = employeeChangeLogBO.getChangeLog();
        String changeType = employeeChangeLogBO.getChangeType();
        JSONObject dataObject = JSON.parseObject(changeLog);
        JSONObject obj = new JSONObject();
        if(dataObject == null){
            // 如果没有数据，则新增一条对应修改字段的新老字段的值。
            // todo 待雇员变更日志类型和变更字段确定后，根据不同的changeType即字段类型，取不同的雇员属性CountryCode、grant_mode等。
            obj.put(JsonParseConsts.EMLOYEE_CHANGE_LOG_FIELD14, employeeChangeLogBO.getCountryCode());
            obj.put(JsonParseConsts.EMLOYEE_CHANGE_LOG_FIELD16, employeeChangeLogBO.getCreatedTime());
        }else{
            //JSONObject fieldName = dataObject.getJSONObject(JsonParseConsts.EMLOYEE_CHANGE_LOG_FIELD1);//最好把changeType和常量解析的字段名保持一致
            JSONObject fieldName = dataObject.getJSONObject(changeType);

            // 如果有数据则判断修改的字段是否在其中，不在则新增一条，存在则修改new的信息。
            if(fieldName != null){
                String old_value = fieldName.getString(JsonParseConsts.EMLOYEE_CHANGE_LOG_FIELD14);
                String old_date = fieldName.getString(JsonParseConsts.EMLOYEE_CHANGE_LOG_FIELD16);
                //t3.change_field,t3.change_value,t3.change_type,t3.change_operation,t3.modified_time,
                obj.put(JsonParseConsts.EMLOYEE_CHANGE_LOG_FIELD14, old_value);
                obj.put(JsonParseConsts.EMLOYEE_CHANGE_LOG_FIELD16, old_date);
                dataObject.remove(changeType);
            }else{
                // 如果没有数据，则新增一条对应修改字段的新老字段的值。
                // todo 待雇员变更日志类型和变更字段确定后，根据不同的changeType即字段类型，取不同的雇员属性CountryCode、grant_mode等。
                obj.put(JsonParseConsts.EMLOYEE_CHANGE_LOG_FIELD14, employeeChangeLogBO.getCountryCode());
                obj.put(JsonParseConsts.EMLOYEE_CHANGE_LOG_FIELD16, employeeChangeLogBO.getCreatedTime());
            }
        }
        obj.put(JsonParseConsts.EMLOYEE_CHANGE_LOG_FIELD15, employeeChangeLogBO.getChangeValue());
        obj.put(JsonParseConsts.EMLOYEE_CHANGE_LOG_FIELD17, employeeChangeLogBO.getModifiedTime());
        obj.put(JsonParseConsts.EMLOYEE_CHANGE_LOG_FIELD18, employeeChangeLogBO.getChangeType());
        obj.put(JsonParseConsts.EMLOYEE_CHANGE_LOG_FIELD19, employeeChangeLogBO.getChangeOperation());
        dataObject.put(changeType,obj);
        changeLog = dataObject.toString();
        salaryGrantEmployeePO.setChangeLog(changeLog);
        // todo 待雇员变更日志类型和变更字段确定后，根据不同的changeType即字段类型，取不同的雇员属性CountryCode、grant_mode等。
        salaryGrantEmployeePO.setCountryCode(employeeChangeLogBO.getCountryCode());
        String remark = employeeChangeLogBO.getEmpRemark();
        // 更新雇员备注字段，如果变更类型之前没有，需要添加到备注中，用"#"分割。
        if(!remark.contains(changeType)){
            remark = "".equals(remark) || remark == null ? changeType : ("#" + changeType);
        }
        salaryGrantEmployeePO.setRemark(remark);
        return salaryGrantEmployeePO;
    }

    /**
     * 根据雇员银行卡信息变更，修改薪资发放雇员的银行卡相关信息。
     * @param salaryGrantEmployeePO
     * @param bankcardId
     * @param changeOperation
     * @return
     */
    private void toSplitAmountByBankCard(SalaryGrantEmployeePO salaryGrantEmployeePO,Long bankcardId,Integer changeOperation){
        String salaryGrantMainTaskCode = salaryGrantEmployeePO.getSalaryGrantMainTaskCode();
        String employeeId = salaryGrantEmployeePO.getEmployeeId();
        // 1、删除原银行卡,把相同银行卡拆分的雇员信息逻辑删除
        if(SalaryGrantBizConsts.CHANGE_OPERATION_DELETE.equals(changeOperation)) {
            salaryGrantEmployeePO.setActive(false);
            salaryGrantEmployeeMapper.updateById(salaryGrantEmployeePO);
            if(!salaryGrantEmployeePO.getDefaultCard()){
                // 如果是默认卡，不做处理；如果不是默认卡，则需要查询该雇员的默认银行卡，把发放金额加上当前删除银行卡的发放金额
                SalaryGrantEmployeePO queryCondition = new SalaryGrantEmployeePO();
                queryCondition.setDefaultCard(true);
                queryCondition.setActive(true);
                queryCondition.setSalaryGrantMainTaskCode(salaryGrantMainTaskCode);
                queryCondition.setEmployeeId(employeeId);
                // 根据薪资发放任务单主表编号、雇员编号，查询任务单下的默认银行卡雇员信息，默认卡就是人民币
                /*
                 select * from sg_salary_grant_employee t
                 where t.is_default_card=1 and t.salary_grant_main_task_code=#salaryGrantMainTaskCode# and t.employee_id=#employeeId# and t.is_active=1
                */
                SalaryGrantEmployeePO salaryGrantEmployeePODefaultCard = this.getSalaryGrantEmployeePO(queryCondition);
                BigDecimal paymentAmount = salaryGrantEmployeePODefaultCard.getPaymentAmount();
                // 折合人民币后进行添加到默认卡
                paymentAmount.add(salaryGrantEmployeePO.getPaymentAmount().multiply(salaryGrantEmployeePO.getExchange()));
                salaryGrantEmployeePODefaultCard.setPaymentAmount(paymentAmount);
                salaryGrantEmployeeMapper.updateById(salaryGrantEmployeePODefaultCard);
            }
        }

        // 2、新增银行卡，按照新增银行卡进行拆分，并查询是否有对应的发放规则，一并拆分。其实新增的银行卡，要么是删除原默认卡新增一个默认卡，要么对新增卡添加发放规则，否则没有规则没法拆分发放金额。
        // 2.1、根据新银行卡id，查询银行卡信息，赋值到雇员信息中
        // 2.2、根据新银行卡id，查询关联的薪资发放规则
        // 2.3、根据发放规则，对雇员的发放金额进行拆分
        if(SalaryGrantBizConsts.CHANGE_OPERATION_INSERT.equals(changeOperation)){
            EmployeeBankcardDTO employeeBankcardDTO = this.getEmployeeBankcardInfo(bankcardId, employeeId, salaryGrantEmployeePO.getCompanyId());
            SalaryGrantEmployeePO salaryGrantEmployeePONew = salaryGrantEmployeePO;
            // 赋值新银行卡信息
            salaryGrantEmployeePONew = this.convertEmployeeBankcardToSalaryGrantEmployee(salaryGrantEmployeePONew, employeeBankcardDTO);
            if(salaryGrantEmployeePONew.getDefaultCard()){
                this.toResolveDefaultCard(salaryGrantMainTaskCode, employeeId, salaryGrantEmployeePONew);
            }else{
                // 如果不是默认卡，则需要查询该雇员的默认银行卡，先按发放规则进行拆分，同时记录拆分的金额折合人民币汇总，在默认卡上减去汇总金额。
                this.toResolveNotDefaultCard(salaryGrantMainTaskCode, employeeId, bankcardId, salaryGrantEmployeePONew);
            }
        }
    }

    /**
     * 针对同一个雇员进行发放金额拆分处理，根据修改的银行卡和发放规则重新拆分。
     * @param salaryGrantEmployeePO
     * @param salaryGrantRuleId
     * @param changeOperation
     * @return
     */
    private void toSplitAmountByGrantRule(SalaryGrantEmployeePO salaryGrantEmployeePO, Long salaryGrantRuleId,Integer changeOperation){
        String salaryGrantMainTaskCode = salaryGrantEmployeePO.getSalaryGrantMainTaskCode();
        String employeeId = salaryGrantEmployeePO.getEmployeeId();
        // todo 后期需要确认，一个发放规则，是否会对应多张银行卡进行设置？一张银行卡只设置一个发放规则，还是可以设置多个规则？
        // 删除薪资发放规则，查询关联的银行卡，,把相同银行卡拆分的雇员信息逻辑删除
        if(SalaryGrantBizConsts.CHANGE_OPERATION_DELETE.equals(changeOperation)){
            SalaryGrantEmployeePO queryCondition = new SalaryGrantEmployeePO();
            queryCondition.setActive(true);
            queryCondition.setSalaryGrantMainTaskCode(salaryGrantMainTaskCode);
            queryCondition.setEmployeeId(employeeId);
            queryCondition.setSalaryGrantRuleId(salaryGrantRuleId);
            SalaryGrantEmployeePO salaryGrantEmployeePODel = this.getSalaryGrantEmployeePO(queryCondition);
            if(!salaryGrantEmployeePODel.getDefaultCard()){
                // 如果不是默认卡，则查询该雇员的默认卡记录，默认卡的发放金额中加上删除薪资发放规则对应银行卡的发放金额，添加时要折合人民币
                queryCondition.setSalaryGrantRuleId(null);
                queryCondition.setDefaultCard(true);
                SalaryGrantEmployeePO salaryGrantEmployeePODefaultCard = this.getSalaryGrantEmployeePO(queryCondition);
                BigDecimal paymentAmount = salaryGrantEmployeePODefaultCard.getPaymentAmount();
                paymentAmount.add(salaryGrantEmployeePODel.getPaymentAmount().multiply(salaryGrantEmployeePODel.getExchange()));
                salaryGrantEmployeePODefaultCard.setPaymentAmount(paymentAmount);
                salaryGrantEmployeeMapper.updateById(salaryGrantEmployeePODefaultCard);

            }
            // 如果是默认卡，则直接逻辑删除
            salaryGrantEmployeePODel.setActive(false);
            salaryGrantEmployeeMapper.updateById(salaryGrantEmployeePODel);
        }

        // 新增薪资发放规则,查询关联的银行卡，按照规则对银行卡信息进行拆分，新增薪资发放雇员信息，同时查询默认卡的发放金额并进行同步修改。
        if(SalaryGrantBizConsts.CHANGE_OPERATION_INSERT.equals(changeOperation)){
            // 先根据id查询新增的发放规则，再根据规则对应的银行卡id，查询对应银行卡信息，把发放规则和银行卡信息赋值到salaryGrantEmployeePO。
            Map paramMap = new HashMap(2);
            paramMap.put("salaryGrantRuleId", salaryGrantRuleId);
            List<SalaryGrantRuleDTO> salaryGrantRuleDTOList = this.listSalaryGrantRuleInfo(paramMap);
            if(salaryGrantRuleDTOList != null && salaryGrantRuleDTOList.size() > 0){
                SalaryGrantRuleDTO salaryGrantRuleDTO = salaryGrantRuleDTOList.get(0);
                paramMap.clear();
                paramMap.put("employeeId", employeeId);
                paramMap.put("companyId", salaryGrantEmployeePO.getCompanyId());
                paramMap.put("cardNum", salaryGrantRuleDTO.getCardNum());
                List<EmployeeBankcardDTO> employeeBankcardDTOList = this.listEmployeeBankcardInfo(paramMap);
                EmployeeBankcardDTO employeeBankcardDTO = employeeBankcardDTOList.get(0);

                // 添加雇员的银行卡信息
                salaryGrantEmployeePO = this.convertEmployeeBankcardToSalaryGrantEmployee(salaryGrantEmployeePO, employeeBankcardDTO);
                if(employeeBankcardDTO.getDefaultCard()){
                    // 如果是默认卡，需要查询任务单下该雇员所有记录，汇总发放金额折合人民币，用总发放金额减去汇总金额赋值给默认卡的发放金额，且币种为人民币。
                    this.toResolveDefaultCard(salaryGrantMainTaskCode, employeeId, salaryGrantEmployeePO);
                }else{
                    // 如果不是默认卡，一条雇员记录，查询默认卡，默认卡的发放金额减去新增雇员折合人民币的发放金额
                    this.toResolveNotDefaultCard(salaryGrantMainTaskCode, employeeId, employeeBankcardDTO.getBankcardId(), salaryGrantEmployeePO);
                }
            }
        }
        // 修改薪资发放规则，修改发放规则对应的银行卡信息发放金额，同时查询默认卡的发放金额并进行同步修改。
        if(SalaryGrantBizConsts.CHANGE_OPERATION_UPDATE.equals(changeOperation)){
            // 根据id查询发放规则修改的是规则信息，还是关联的银行卡信息。
            // 如果是关联的银行卡信息变化了，先根据银行卡id查询银行卡信息，根据发放规则id查询雇员信息，修改雇员的银行卡信息。
            // 如果是发放规则修改，把修改的规则信息赋值到雇员信息中，根据新的规则重新计算发放金额
            // 如果以上2种情况都发生了，则2种信息同时修改。
            SalaryGrantEmployeePO queryCondition = new SalaryGrantEmployeePO();
            queryCondition.setActive(true);
            queryCondition.setSalaryGrantMainTaskCode(salaryGrantMainTaskCode);
            queryCondition.setEmployeeId(employeeId);
            queryCondition.setSalaryGrantRuleId(salaryGrantRuleId);
            // 1、查询发放规则对应的雇员信息
            SalaryGrantEmployeePO salaryGrantEmployeePOModify = this.getSalaryGrantEmployeePO(queryCondition);
            BigDecimal oldAmountForRMB = salaryGrantEmployeePOModify.getPaymentAmount().multiply(salaryGrantEmployeePOModify.getExchange());
            // 2、查询默认卡的雇员信息
            queryCondition.setDefaultCard(true);
            queryCondition.setSalaryGrantRuleId(null);
            SalaryGrantEmployeePO salaryGrantEmployeePODefaultCard = this.getSalaryGrantEmployeePO(queryCondition);
            BigDecimal paymentAmountRMB = salaryGrantEmployeePODefaultCard.getPaymentAmountRMB();
            BigDecimal paymentAmount = salaryGrantEmployeePODefaultCard.getPaymentAmount();
            // 3、根据id查询发放规则修改的规则信息
            Map paramMap = new HashMap(2);
            paramMap.put("salaryGrantRuleId", salaryGrantRuleId);
            List<SalaryGrantRuleDTO> salaryGrantRuleDTOList = this.listSalaryGrantRuleInfo(paramMap);
            if(salaryGrantRuleDTOList != null && salaryGrantRuleDTOList.size() > 0){
                SalaryGrantRuleDTO salaryGrantRuleDTO = salaryGrantRuleDTOList.get(0);
                salaryGrantEmployeePOModify.setCurrencyCode(salaryGrantRuleDTO.getCurrency());
                salaryGrantEmployeePOModify.setRuleType(salaryGrantRuleDTO.getRuleType());
                salaryGrantEmployeePOModify.setRuleAmount(salaryGrantRuleDTO.getRuleAmount());
                salaryGrantEmployeePOModify.setRuleRatio(salaryGrantRuleDTO.getRuleRatio());
                EmployeeServiceAgreementDTO employeeServiceAgreementDTO = new EmployeeServiceAgreementDTO();
                // todo 后期需要补充处理逻辑，汇率如何获取
                employeeServiceAgreementDTO.setExchangeCalcMode(SalaryGrantBizConsts.EXCHANGE_CALC_MODE_CURRENT_EXCHANGE_RATE);
                // 如果是实时汇率，需要查询汇率表，获取实时汇率的值
                BigDecimal exchange = this.getExchangeInfo(salaryGrantRuleDTO.getCurrency());
                employeeServiceAgreementDTO.setCustomerAgreedExchangeRate(exchange);
                Map calcMap = this.calcPaymentAmountBySalaryGrantRule(paymentAmountRMB, salaryGrantRuleDTO, employeeServiceAgreementDTO);
                salaryGrantEmployeePOModify.setPaymentAmount((BigDecimal) calcMap.get("splitPaymentAmount"));
                salaryGrantEmployeePOModify.setExchange((BigDecimal) calcMap.get("exchange"));
                BigDecimal newAmountForRMB = (BigDecimal) calcMap.get("paymentAmountForRMBCount");
                // 4、查询发放规则关联的银行卡信息
                paramMap.clear();
                paramMap.put("employeeId", employeeId);
                paramMap.put("companyId", salaryGrantEmployeePOModify.getCompanyId());
                paramMap.put("cardNum", salaryGrantRuleDTO.getCardNum());
                List<EmployeeBankcardDTO> employeeBankcardDTOList = this.listEmployeeBankcardInfo(paramMap);
                EmployeeBankcardDTO employeeBankcardDTO = employeeBankcardDTOList.get(0);
                // 5、添加雇员的银行卡信息
                salaryGrantEmployeePOModify = this.convertEmployeeBankcardToSalaryGrantEmployee(salaryGrantEmployeePOModify, employeeBankcardDTO);
                salaryGrantEmployeeMapper.updateById(salaryGrantEmployeePOModify);
                paymentAmount.add(oldAmountForRMB).subtract(newAmountForRMB);
                salaryGrantEmployeePODefaultCard.setPaymentAmount(paymentAmount);
                salaryGrantEmployeeMapper.updateById(salaryGrantEmployeePODefaultCard);
            }
        }

    }

    /**
     * 如果是默认卡，则遍历相同salaryGrantMainTaskCode、employeeId的信息，汇总同一雇员的发放金额折合人民币，用总发放金额减去汇总金额，赋值给新雇员信息。
     * @param salaryGrantMainTaskCode
     * @param employeeId
     * @param salaryGrantEmployeePO
     * @return
     */
    private void toResolveDefaultCard(String salaryGrantMainTaskCode, String employeeId, SalaryGrantEmployeePO salaryGrantEmployeePO){
        List<SalaryGrantEmployeeBO> salaryGrantEmployeeBOList = this.listSalaryGrantEmployeeBO(salaryGrantMainTaskCode, employeeId);
        // 汇总金额除去默认卡
        BigDecimal totalMoney = salaryGrantEmployeeBOList.stream().filter(SalaryGrantEmployeeBO -> SalaryGrantEmployeeBO.getDefaultCard().equals(false))
                .map(SalaryGrantEmployeeBO::getPaymentAmountForRMB).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal paymentAmount = salaryGrantEmployeePO.getPaymentAmount();
        paymentAmount = paymentAmount.subtract(totalMoney);
        salaryGrantEmployeePO.setPaymentAmount(paymentAmount);
        // 调用生成雇员id的唯一主键方法
        // todo 调用方法，生成自增id
        Long salaryGrantEmployeeId = 0L;
        salaryGrantEmployeePO.setSalaryGrantEmployeeId(salaryGrantEmployeeId);
        // 调用插入方法
        salaryGrantEmployeeMapper.insert(salaryGrantEmployeePO);
    }

    /**
     * 如果不是默认卡，则需要查询该雇员的默认银行卡，先按发放规则进行拆分，同时记录拆分的金额折合人民币汇总，在默认卡上减去汇总金额。
     * @param salaryGrantMainTaskCode
     * @param employeeId
     * @param bankcardId
     * @param salaryGrantEmployeePO
     * @return
     */
    private void toResolveNotDefaultCard(String salaryGrantMainTaskCode, String employeeId, Long bankcardId, SalaryGrantEmployeePO salaryGrantEmployeePO){
        SalaryGrantEmployeePO queryCondition = new SalaryGrantEmployeePO();
        queryCondition.setDefaultCard(true);
        queryCondition.setActive(true);
        queryCondition.setSalaryGrantMainTaskCode(salaryGrantMainTaskCode);
        queryCondition.setEmployeeId(employeeId);
        SalaryGrantEmployeePO salaryGrantEmployeePODefaultCard = this.getSalaryGrantEmployeePO(queryCondition);
        BigDecimal paymentAmount = salaryGrantEmployeePODefaultCard.getPaymentAmount();
        BigDecimal paymentAmountRMB = salaryGrantEmployeePODefaultCard.getPaymentAmountRMB();
        // 查询新增银行卡对应的发放规则，如果有则针对发放规则进行拆分
        Map paramMap = new HashMap(2);
        paramMap.put("employeeId", employeeId);
        paramMap.put("bankcardId", String.valueOf(bankcardId));
        List<SalaryGrantRuleDTO> salaryGrantRuleDTOList = this.listSalaryGrantRuleInfo(paramMap);
        if (salaryGrantRuleDTOList != null && salaryGrantRuleDTOList.size() > 0) {
            BigDecimal amountForRMBCount = BigDecimal.ZERO;
            EmployeeServiceAgreementDTO employeeServiceAgreementDTO = new EmployeeServiceAgreementDTO();
            // todo 后期需要补充处理逻辑，汇率如何获取
            employeeServiceAgreementDTO.setExchangeCalcMode(SalaryGrantBizConsts.EXCHANGE_CALC_MODE_CURRENT_EXCHANGE_RATE);
            for (SalaryGrantRuleDTO salaryGrantRuleDTO : salaryGrantRuleDTOList) {
                SalaryGrantEmployeePO salaryGrantEmployeePOMidTemp = salaryGrantEmployeePO;
                salaryGrantEmployeePOMidTemp.setSalaryGrantRuleId(salaryGrantRuleDTO.getSalaryGrantRuleId());
                salaryGrantEmployeePOMidTemp.setRuleType(salaryGrantRuleDTO.getRuleType());
                salaryGrantEmployeePOMidTemp.setRuleAmount(salaryGrantRuleDTO.getRuleAmount());
                salaryGrantEmployeePOMidTemp.setRuleRatio(salaryGrantRuleDTO.getRuleRatio());
                salaryGrantEmployeePOMidTemp.setCurrencyCode(salaryGrantRuleDTO.getCurrency());
                // 如果是实时汇率，需要查询汇率表，获取实时汇率的值
                BigDecimal exchange = this.getExchangeInfo(salaryGrantRuleDTO.getCurrency());
                employeeServiceAgreementDTO.setCustomerAgreedExchangeRate(exchange);
                Map calcMap = this.calcPaymentAmountBySalaryGrantRule(paymentAmountRMB, salaryGrantRuleDTO, employeeServiceAgreementDTO);
                salaryGrantEmployeePOMidTemp.setPaymentAmount((BigDecimal) calcMap.get("splitPaymentAmount"));
                salaryGrantEmployeePOMidTemp.setExchange((BigDecimal) calcMap.get("exchange"));
                amountForRMBCount.add((BigDecimal) calcMap.get("paymentAmountForRMBCount"));
                // 调用生成雇员id的唯一主键方法
                // todo 调用方法，生成自增id
                Long salaryGrantEmployeeId = 0L;
                salaryGrantEmployeePOMidTemp.setSalaryGrantEmployeeId(salaryGrantEmployeeId);
                // 调用插入方法
                salaryGrantEmployeeMapper.insert(salaryGrantEmployeePOMidTemp);
            }
            salaryGrantEmployeePODefaultCard.setPaymentAmount(paymentAmount.subtract(amountForRMBCount));
            salaryGrantEmployeeMapper.updateById(salaryGrantEmployeePODefaultCard);
        }
    }

    /**
     * 任务单子表刷新总入口方法，刷新雇员信息及任务单子表信息后的数据，供前端任务列表分页查询草稿状态的任务单。
     * @param taskType
     * @return
     */
    private void toDealSalaryGrantSubTaskPOByStatusOfDraft(String taskType){

        List<LogResponseDTO> logServiceInfoList = this.listEmployeeLogServiceInfo();
        List<EmployeeChangeLogDTO> employeeChangeLogDTOList = this.convertLogServiceInfoToEmployeeChangeLogDTO(logServiceInfoList);
        List<EmployeeChangeLogBO> EmployeeChangeLogBOList = this.listSalaryGrantEmployeeBySubTask(taskType);
        List<EmployeeChangeLogBO> mergeList = this.toMergeChangeLogInfo(employeeChangeLogDTOList, EmployeeChangeLogBOList);
        Map resultMap = new HashMap();
        resultMap.put("employeeChangeLogBOList", mergeList);
        // todo 后期需要修改统一参数处理
        List<SalaryGrantMainTaskPO> taskList = this.toRegroupEmployeeChangeInfo(resultMap);
        List<SalaryGrantSubTaskPO> subTaskList = new ArrayList<>();
        this.toUpdateSalaryGrantSubTaskForAlterEmployee(subTaskList);
    }

    /**
     * 调用运营中心日志信息查询接口，查询雇员是否有变化的信息，查询最近一次修改的雇员相关信息的日志记录。
     * @param
     * @return List<LogResponseDTO>
     */
    private List<LogResponseDTO> listEmployeeLogServiceInfo(){
        // TODO 查找和雇员相关信息的表日志记录（雇员信息表、雇员银行卡信息、薪资发放规则、雇员服务协议、服务周期规则等）
        List<LogResponseDTO> logServiceInfoList = new ArrayList();
        return logServiceInfoList;
    }

    /**
     * 根据运营中心查询的日志记录结果，组织数据为EmployeeChangeLogDTO类型
     * @param logServiceInfoList
     * @return List<LogResponseDTO>
     */
    private List<EmployeeChangeLogDTO> convertLogServiceInfoToEmployeeChangeLogDTO(List<LogResponseDTO> logServiceInfoList){
        List<EmployeeChangeLogDTO> employeeChangeLogDTOList = new ArrayList<>();
        // todo 把LogResponseDTO中的值转换为EmployeeChangeLogDTO的数据格式
        logServiceInfoList.forEach(logResponseDTO ->{
            EmployeeChangeLogDTO employeeChangeLogDTO = new EmployeeChangeLogDTO();
            employeeChangeLogDTO.setChangeTableId(Long.valueOf(logResponseDTO.getId()));
            // todo 后期需要把汉字对应到类型做个转换
            employeeChangeLogDTO.setChangeOperation(Integer.valueOf(logResponseDTO.getAct()));
            employeeChangeLogDTO.setChangeTableName(logResponseDTO.getObjectName());
            employeeChangeLogDTO.setEmployeeId(null);
            // todo 需要根据日志中的对象的值和原对象的值进行比对找出改变的字段
            employeeChangeLogDTO.setChangeField(null);
            // todo 需要根据日志中的对象的值和原对象的值进行比对找出改变的字段值
            employeeChangeLogDTO.setChangeValue(null);
            // todo 需要根据日志中的对象的值和原对象的值进行比对找出改变的字段属于哪种类型，最好转换成薪资发放的字段名称，细化类型的粒度。
            employeeChangeLogDTO.setChangeType(null);
            // todo 需要根据日志中的对象的值，查找对象的创建时间
            employeeChangeLogDTO.setModifiedTime(null);
        });
        return null;
    }

    /**
     * 调用雇员的银行卡信息API查询
     * @param bankcardId
     * @param employeeId
     * @param companyId
     * @return EmployeeBankcardDTO
     */
    private EmployeeBankcardDTO getEmployeeBankcardInfo(Long bankcardId, String employeeId, String companyId){
        EmployeeBankcardDTO employeeBankcardDTO = new EmployeeBankcardDTO();
        // 根据银行卡编号查询雇员的银行卡信息
        // todo
        // select t1.bankcard_id,t1.employee_id,t1.card_num,t1.account_name,t1.bank_code,t1.deposit_bank,t1.bankcard_type,
        // t2.usage,t2.is_defalut
        // from emp_bankcard t1,emp_company_bankcard_relation t2
        // where t1.bankcard_id=t2.bankcard_id and t1.status=1 and t2.is_active=1 and t2.usage in(1,3) and t1.employee_id=#employeeId# and t2.company_id=#companyId#
        return employeeBankcardDTO;
    }

    /**
     * 根据雇员变更日志的信息和子表雇员信息，合并有信息变更的子表雇员信息
     * @param employeeChangeLogDTOList
     * @param employeeChangeLogBOList
     * @return List<EmployeeChangeLogBO>
     */
    private List<EmployeeChangeLogBO> toMergeChangeLogInfo(List<EmployeeChangeLogDTO> employeeChangeLogDTOList, List<EmployeeChangeLogBO> employeeChangeLogBOList){
        List<EmployeeChangeLogBO> mergeList = new ArrayList<>();
        employeeChangeLogDTOList.forEach(employeeChangeLogDTO -> {
            employeeChangeLogBOList.forEach(employeeChangeLogBO -> {
                if(employeeChangeLogBO.getEmployeeId().equals(employeeChangeLogDTO.getEmployeeId())){
                    EmployeeChangeLogBO mergeBO = this.convertEmployeeChangeLogDTOToEmployeeChangeLogBO(employeeChangeLogBO, employeeChangeLogDTO);
                    mergeList.add(mergeBO);
                    employeeChangeLogBOList.remove(employeeChangeLogBO);
                }
            });
        });
        return mergeList;
    }

    /**
     * 将雇员变更日志信息赋值到BO对象中
     * @param employeeChangeLogBO
     * @param employeeChangeLogDTO
     * @return employeeChangeLogBO
     */
    private EmployeeChangeLogBO convertEmployeeChangeLogDTOToEmployeeChangeLogBO(EmployeeChangeLogBO employeeChangeLogBO, EmployeeChangeLogDTO employeeChangeLogDTO){
        employeeChangeLogBO.setChangeTableName(employeeChangeLogDTO.getChangeTableName());
        employeeChangeLogBO.setChangeTableId(employeeChangeLogDTO.getChangeTableId());
        employeeChangeLogBO.setChangeField(employeeChangeLogDTO.getChangeField());
        employeeChangeLogBO.setChangeValue(employeeChangeLogDTO.getChangeValue());
        employeeChangeLogBO.setChangeType(employeeChangeLogDTO.getChangeType());
        employeeChangeLogBO.setChangeOperation(employeeChangeLogDTO.getChangeOperation());
        employeeChangeLogBO.setModifiedTime(employeeChangeLogDTO.getModifiedTime());
        return employeeChangeLogBO;
    }

    /**
     * 批量修改子表任务单信息
     * @param taskList
     * @return
     */
    private void toUpdateSalaryGrantSubTaskForAlterEmployee(List<SalaryGrantSubTaskPO> taskList){
        List<SalaryGrantSubTaskPO> updateList = new ArrayList<>();
        taskList.forEach(SalaryGrantSubTaskPO -> {
            SalaryGrantSubTaskPO updatePO = this.toGatherSalaryGrantSubTaskForAlterEmployeeSingle(SalaryGrantSubTaskPO.getSalaryGrantSubTaskCode(), SalaryGrantSubTaskPO.getRemark());
            updateList.add(updatePO);
        });

        // todo 批量修改任务单信息
        // salaryGrantSubTaskMapper.batchUpdate(updateList);
    }

    /**
     * 根据修改后的雇员数据，再重新统计各汇总字段信息，修改任务单子表。
     * @param remark
     * @return SalaryGrantSubTaskPO
     */
    private SalaryGrantSubTaskPO toGatherSalaryGrantSubTaskForAlterEmployeeSingle(String salaryGrantSubTaskCode, String remark){
        SalaryGrantSubTaskPO queryCondition = new SalaryGrantSubTaskPO();
        queryCondition.setSalaryGrantMainTaskCode(salaryGrantSubTaskCode);
        SalaryGrantSubTaskPO salaryGrantSubTaskPO = this.getSalaryGrantSubTaskPO(queryCondition);
        List<SalaryGrantEmployeePO> salaryGrantEmployeePOList = this.listSalaryGrantEmployeePOForAlter(salaryGrantSubTaskCode, SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_SUB);
        // select t.payment_amount_rmb as totalsum,t.grant_mode,t.grant_date,t.grant_time,t.country_code
        // 设置计数器
        // 薪资发放总金额，查询雇员服务协议中的发放服务标识（0、2），对雇员的实发工资累计汇总
        BigDecimal paymentTotalSumCount = new BigDecimal(0);
        // 发薪人数，查询雇员服务协议中的发放服务标识（0、2）
        Long totalPersonCount = 0L;
        // 中方发薪人数，根据发放服务标识（0、2）、国籍（country_code=china）进行累计
        Long chineseCount = 0L;
        // 外方发薪人数，根据发放服务标识（0、2）、国籍（country_code!=china）进行累计
        Long foreignerCount = 0L;
        // 薪资发放日期
        String grantDate = "";
        // 薪资发放时段:1-上午，2-下午
        Integer grantTime = SalaryGrantBizConsts.GRANT_TIME_AM;

        // 1、遍历批次数据信息PayrollCalcResultDTO，把雇员相关的信息存储在自己的数据结构中PayrollCalcResultBO，包括一个雇员计算结果数据的信息、服务协议、日期信息等
        if(salaryGrantEmployeePOList != null && salaryGrantEmployeePOList.size() > 0) {
            paymentTotalSumCount = salaryGrantEmployeePOList.stream().map(SalaryGrantEmployeePO::getPaymentAmountRMB).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalPersonCount = salaryGrantEmployeePOList.stream().map(SalaryGrantEmployeePO::getEmployeeId).count();
            chineseCount = salaryGrantEmployeePOList.stream().filter(SalaryGrantEmployeePO -> SalaryGrantEmployeePO.getCountryCode().equals(SalaryGrantBizConsts.COUNTRY_CODE_CHINA))
                    .map(SalaryGrantEmployeePO::getEmployeeId).count();
            foreignerCount = salaryGrantEmployeePOList.stream().filter(salaryGrantEmployeePO -> !salaryGrantEmployeePO.getCountryCode().equals(SalaryGrantBizConsts.COUNTRY_CODE_CHINA))
                    .map(SalaryGrantEmployeePO::getEmployeeId).count();
            SalaryGrantEmployeePO salaryGrantEmployeePO = salaryGrantEmployeePOList.get(0);
            grantDate = salaryGrantEmployeePO.getGrantDate();
            grantTime = salaryGrantEmployeePO.getGrantTime();
        }
        salaryGrantSubTaskPO.setPaymentTotalSum(paymentTotalSumCount);
        salaryGrantSubTaskPO.setTotalPersonCount(Integer.valueOf(totalPersonCount.toString()));
        salaryGrantSubTaskPO.setChineseCount(Integer.valueOf(chineseCount.toString()));
        salaryGrantSubTaskPO.setForeignerCount(Integer.valueOf(foreignerCount.toString()));
        salaryGrantSubTaskPO.setGrantDate(grantDate);
        salaryGrantSubTaskPO.setGrantTime(grantTime);
        salaryGrantSubTaskPO.setRemark(remark);
        return salaryGrantSubTaskPO;
    }

    /**
     * 根据传入查询条件查询薪资发放任务单列表信息
     * @param paraMap
     * @return List<SalaryGrantMainTaskPO>
     */
    public List<SalaryGrantMainTaskPO> listSalaryGrantMainTaskPO(Map paraMap){
        // taskStatus = SalaryGrantBizConsts.TASK_STATUS_DRAFT
        // String taskStatus = (String)paraMap.get("taskStatus");
        List<SalaryGrantMainTaskPO> salaryGrantMainTaskPOList = salaryGrantMainTaskMapper.selectByMap(paraMap);
        return salaryGrantMainTaskPOList;
    }

    /**
     * 根据查询条件，查询任务单下的雇员信息列表。
     * @param queryCondition
     * @return List<SalaryGrantEmployeePO>
     */
    public List<SalaryGrantEmployeePO> listSalaryGrantEmployeePO(SalaryGrantEmployeePO queryCondition) {
        List<SalaryGrantEmployeePO> salaryGrantEmployeePOList = salaryGrantEmployeeMapper.selectByMap(this.transBean2Map(queryCondition));
        return salaryGrantEmployeePOList;
    }

    /**
     * 根据薪资发放任务单主表编号，查询任务单下的默认银行卡雇员信息列表，用来对雇员信息变更后任务单统计信息修改。
     * @param salaryGrantTaskCode
     * @param taskType
     * @return List<SalaryGrantEmployeePO>
     */
    public List<SalaryGrantEmployeePO> listSalaryGrantEmployeePOForAlter(String salaryGrantTaskCode, String taskType){
        // todo 根据薪资发放任务单主表编号，查询雇员信息
        List<SalaryGrantEmployeePO> salaryGrantEmployeePOList = new ArrayList<>();
        /*
         * select t.payment_amount_rmb ,t.grant_mode,t.grant_date,t.grant_time,t.country_code
         * from sg_salary_grant_employee t
         * where t.is_default_card=1 and t.is_active=1 and
         * if(taskType=main)
         * t.salary_grant_main_task_code=#salaryGrantTaskCode#
         * if(taskType=sub)
         * t.salary_grant_sub_task_code=#salaryGrantTaskCode#
         * */
        return salaryGrantEmployeePOList;
    }

    /**
     * 根据查询条件查询单条雇员信息
     * @param queryCondition
     * @return SalaryGrantEmployeePO
     */
    public SalaryGrantEmployeePO getSalaryGrantEmployeePO(SalaryGrantEmployeePO queryCondition){
        SalaryGrantEmployeePO salaryGrantEmployeePO = salaryGrantEmployeeMapper.selectOne(queryCondition);
        return salaryGrantEmployeePO;
    }

    /**
     * 根据薪资发放任务单主表编号、雇员编号，查询任务单下的雇员信息
     * @param salaryGrantMainTaskCode
     * @param employeeId
     * @return List<SalaryGrantEmployeeBO>
     */
    public List<SalaryGrantEmployeeBO> listSalaryGrantEmployeeBO(String salaryGrantMainTaskCode, String employeeId){
        List<SalaryGrantEmployeeBO> salaryGrantEmployeeBOList = new ArrayList<>();
        // todo 补充查询语句和mapper
        // select salary_grant_employee_id,is_default_card,payment_amount*exchange as paymentAmountForRMB
        // from sg_salary_grant_employee where salary_grant_main_task_code=#salaryGrantMainTaskCode# and employee_id=#employeeId# and is_active=1
        Map paramMap = new HashMap(2);
        paramMap.put("salaryGrantMainTaskCode", salaryGrantMainTaskCode);
        paramMap.put("employeeId", employeeId);
        // salaryGrantEmployeePOList = salaryGrantEmployeeMapper.selectByMap(paramMap);
        return salaryGrantEmployeeBOList;
    }

    /**
     * 查询草稿状态薪资发放任务子表的相关雇员信息
     * @param taskType
     * @return List<EmployeeChangeLogBO>
     */
    public List<EmployeeChangeLogBO> listSalaryGrantEmployeeBySubTask(String taskType){
        List<EmployeeChangeLogBO> EmployeeChangeLogBOList = new ArrayList<>();
        // todo 补充查询语句和mapper
        /*select t2.salary_grant_sub_task_code, t2.salary_grant_employee_id,t2.grant_account_code,t2.grant_mode,
                t2.salary_grant_rule_id,t2.bankcard_id,t2.card_num,t2.account_name,t2.bank_code,t2.deposit_bank,t2.bankcard_type,
                t2.country_code,t2.change_log,t2.remark empremark,t2.payment_amount_rmb,t2.payment_amount,t2.company_id
        t1.created_time,t1.remark taskremark
        from sg_salary_grant_sub_task t1, sg_salary_grant_employee t2
        where t1.salary_grant_sub_task_code=t2.salary_grant_sub_task_code and t1.task_status='0' and t1.is_active='1'
        and t2.is_active='1' and t1.task_type=#taskType#*/
        return EmployeeChangeLogBOList;
    }

    /**
     * 根据其他模块消息订阅，批次重算，需要对任务单主表进行修改。根据计算批次号和计算类型，查询批次业务数据表，根据最新批次的计算信息进行汇总到任务单主表。
     * @param salaryGrantMainTaskPO
     * @return SalaryGrantMainTaskPO
     */
    private boolean toModifySalaryGrantMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO){
        Map batchParam = new HashMap(2);
        batchParam.put("batchCode", salaryGrantMainTaskPO.getBatchCode());
        batchParam.put("batchType", salaryGrantMainTaskPO.getGrantType());
        // 1、 根据批次编号batch_code，批次对应的计算类型（正常、调整、回溯），查询批次表信息
        // 调用计算引擎提供查询批次信息的接口方法
        PrBatchDTO PrBatchDTO = batchProxy.getBatchInfo(salaryGrantMainTaskPO.getBatchCode(), salaryGrantMainTaskPO.getGrantType());
        salaryGrantMainTaskPO = this.convertBatchInfoToSalaryGrantMainTask(salaryGrantMainTaskPO, PrBatchDTO);
        // 2、查询批次计算结果
        List<PayrollCalcResultDTO> payrollCalcResultDTOList = this.listPayrollCalcResult(batchParam);
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


    /**
     * 提供外部接口API，对选择的暂缓雇员进行薪资发放
     * @param employeeIdList
     * @return
     */
    public void salaryGrantForReprieveEmployee(List employeeIdList){
        // todo
        List<SalaryGrantEmployeePO> employeeForReprieveList = this.listEmployeeForReprieveGrant(employeeIdList);
        this.toCreateOrModifySalaryGrantMainTaskForReprieve(employeeForReprieveList);
    }

    /**
     * 调用外部接口API，根据勾选的暂缓再发放的雇员信息，查找对应暂缓雇员在暂缓池中的信息，并转换为薪资发放暂缓雇员实体类型。
     * @param employeeIdList
     * @return List<SalaryGrantEmployeePO>
     */
    public List<SalaryGrantEmployeePO> listEmployeeForReprieveGrant(List employeeIdList){
        // todo 根据雇员id列表信息，查找对应暂缓雇员在暂缓池中的信息
        List employeeForReprievePoolList = new ArrayList();
        // 根据前端勾选雇员信息（batch_code、employee_id、grant_status、card_num、reprieve_type、payment_amount、currency_code），关联薪资发放雇员信息表，查询原薪资发放任务单对雇员的拆分记录。
        // 根据暂缓雇员在暂缓池中的信息转换为薪资发放暂缓雇员实体类型
        List<SalaryGrantEmployeePO> employeeForReprieveList = new ArrayList<>();

        return employeeForReprieveList;
    }

    /**
     * 根据暂缓再发放雇员信息，系统自动创建薪资发放任务单主表信息
     * @param employeeForReprieveList
     * @return List<SalaryGrantMainTaskPO>
     */
    private List<SalaryGrantMainTaskPO> toCreateOrModifySalaryGrantMainTaskForReprieve(List<SalaryGrantEmployeePO> employeeForReprieveList){
        // 进入暂缓池中的雇员所在的薪资发放任务是审批通过后，暂缓雇员才进入到暂缓池的。但是，当任务单被撤回或驳回时，任务单的状态会修改为草稿。
        // 如果任务单被撤回或驳回，操作员在暂缓池要对某些雇员进行暂缓发放，需要把这部分雇员重新加入到原来的任务单中。
        // 添加逻辑判断，如果选择的雇员所在的任务单状态是草稿状态，则修改薪资发放雇员信息表中对应雇员的发放状态为正常，同时判断是对原发放金额进行部分发放，还是全部发放。
        //    暂缓再发放的雇员可能来自不同的计算批次和任务单，查询记录结果按照计算批次进行分组处理。
        Map<String, List<SalaryGrantEmployeePO>> taskCodeMap = employeeForReprieveList.stream().collect(Collectors.groupingBy(SalaryGrantEmployeePO::getSalaryGrantMainTaskCode));
        // todo 如果有SalaryGrantMainTaskCode为空的记录，会有什么异常情况产生？map只允许一个null的key是空指针。如果有多个导入暂缓的雇员没有批次信息怎么办？自己写分组方法。
        // 如果通过文件在暂缓池导入的暂缓名单，也可能没有计算批次和任务单信息，需要先到薪资发放雇员信息查询是否存在该雇员，如果存在继续查询相应任务单信息（可能对应多个批次任务），任务单的状态是草稿还是非草稿状态，根据上面逻辑处理。
        taskCodeMap.forEach((salaryGrantMainTaskCode,taskList) -> {
            SalaryGrantMainTaskPO condition = new SalaryGrantMainTaskPO();
            condition.setSalaryGrantMainTaskCode(salaryGrantMainTaskCode);
            SalaryGrantMainTaskPO salaryGrantMainTaskPO = this.getSalaryGrantMainTaskPO(condition);
            // 任务单状态是草稿状态，则修改薪资发放雇员信息表中对应雇员的发放状态为正常，同时判断是对原发放金额进行部分发放，还是全部发放。
            if(SalaryGrantBizConsts.TASK_STATUS_DRAFT.equals(salaryGrantMainTaskPO.getTaskStatus())){
                // 如果原来任务单中的雇员是全部暂缓，或部分暂缓，暂缓池中暂缓再发放的金额是原暂缓金额的部分，需要修改雇员的发放状态4-部分发放。
                // 如果对原来暂缓的金额全部发放，则修改雇员的发放状态为正常。
            }else{
                // 如果选择的雇员所在的任务单状态是不是草稿状态，则需要新建薪资发放任务单主表。
                // 如果暂缓再发放的雇员不存在薪资发放雇员信息表中，新建薪资发放任务单，前提条件雇员信息是有计算结果的，如果无计算结果发放金额、发放账户等信息，抛错误信息至前端，不能创建任务单进行发放。
                // 参考toCreateSalaryGrantMainTask，后期考虑添加创建任务单类型参数，根据1-计算引擎、2-暂缓发放、3-退票发放，根据不同雇员查询的信息，对雇员信息进行汇总操作
                // 查询对应雇员是否有信息的变更（国籍、银行卡、薪资发放规则、发放方式、发放账户、薪资发放日等），如果信息有变更，刷新雇员信息，记录变更日志，把刷新后的雇员信息插入到新生成的任务单主表中。
                // 解析雇员计算结果数据，过滤薪资发放的雇员信息，汇总相关数据。
                // salaryGrantMainTaskPO = this.toResolveReprieveResultForTask(salaryGrantEmployeePOList, salaryGrantMainTaskPO);
            }
        });

        return null;
    }

    /**
     * 根据退票再发放雇员信息，系统自动创建薪资发放任务单主表信息
     * @param salaryGrantMainTaskPO
     * @return SalaryGrantMainTaskPO
     */
    private SalaryGrantMainTaskPO toCreateSalaryGrantMainTaskForBounce(SalaryGrantMainTaskPO salaryGrantMainTaskPO){
        // todo
        return null;
    }

    /**
     * 查询雇员的信息是否缺失的方法，遍历查询雇员相关信息（国籍、基本信息、银行卡、服务协议等），待调研统计。
     * @param salaryGrantEmployeePO
     * @return boolean
     */
    private boolean isLackOfEmployeeRelatedInfo(SalaryGrantEmployeePO salaryGrantEmployeePO){
        // 在解析雇员计算结果时调用，把缺失信息的雇员发放状态为自动暂缓。同时雇员信息拷贝进入薪资发放暂缓池。
        // todo
        return true;
    }

    private Map<String, Object> transBean2Map(Object obj){
        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        try{
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for(PropertyDescriptor property : propertyDescriptors){
                String key = property.getName();
                if(!key.equals("class")){
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        }catch (Exception e){

        }
        return map;
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

    private String toZeroizeForDate(String dateStr){
        if(dateStr.length() == 1){
            dateStr = "0" + dateStr;
        }
        return dateStr;
    }

}
