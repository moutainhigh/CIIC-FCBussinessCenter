package com.ciicsh.caldispatchjob.compute.service;

import com.alibaba.fastjson.JSON;
import com.ciicsh.caldispatchjob.compute.messageBus.BatchSender;
import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmployeeContractProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmployeeServiceProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.FcEmployeeRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.GetEmployeeContractsDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.EmployeeContractResponseDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.FcEmployeeResponseDTO;
import com.ciicsh.gto.fcbusinesscenter.entity.CancelClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.entity.ClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.*;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by bill on 18/3/23.
 */
@Service
public class CompleteComputeServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(CompleteComputeServiceImpl.class);


    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @Autowired
    private AdjustBatchMongoOpt adjustBatchMongoOpt;

    @Autowired
    private BackTraceBatchMongoOpt backTraceBatchMongoOpt;

    @Autowired
    private CloseAccountMongoOpt closeAccountMongoOpt;

    @Autowired
    private EmpGroupMongoOpt empGroupMongoOpt;

    @Autowired
    private EmployeeServiceProxy employeeServiceProxy;

    @Autowired
    private EmployeeContractProxy employeeContractProxy;

    @Autowired
    private BatchSender sender;


    public void processCompleteCompute(String batchCode, int batchType, String userID, String userName){

        List<DBObject> batchList = null;

        Criteria criteria = Criteria.where("batch_code").is(batchCode);
        Query query = new Query(criteria);

        query.fields().
                include(PayItemName.EMPLOYEE_CODE_CN)
                .include("catalog.emp_info")
                .include("catalog.emp_info.country_code")
                .include("catalog.batch_info.period")
                .include("catalog.batch_info.management_ID")
                .include("catalog.batch_info.management_Name")
                .include("catalog.pay_items.item_name")
                .include("catalog.pay_items.item_code")
                .include("catalog.pay_items.item_value")
        ;

        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
            batchList = normalBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,NormalBatchMongoOpt.PR_NORMAL_BATCH);

        }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
            query.fields().include("net_pay").include("show_adj");
            batchList = adjustBatchMongoOpt.getMongoTemplate().find(query,DBObject.class, AdjustBatchMongoOpt.PR_ADJUST_BATCH);

        }else {
            batchList = backTraceBatchMongoOpt.getMongoTemplate().find(query,DBObject.class, BackTraceBatchMongoOpt.PR_BACK_TRACE_BATCH);
        }

        List<DBObject> list = batchList.stream().map(item -> {
            DBObject catalog = (DBObject)item.get("catalog");
            String empCode = (String) item.get(PayItemName.EMPLOYEE_CODE_CN);
            DBObject batchInfo = (DBObject)catalog.get("batch_info");
            String mgrId = batchInfo.get("management_ID") == null ? "" : (String) batchInfo.get("management_ID");
            //String companyId = emp.get(PayItemName.EMPLOYEE_COMPANY_ID) == null ? "" : emp.get(PayItemName.EMPLOYEE_COMPANY_ID)
            DBObject emp = getEmployeeInfo(catalog,empCode,mgrId);

            List<DBObject> payItems = (List<DBObject>)catalog.get("pay_items");
            DBObject mapObj = new BasicDBObject();
            mapObj.put("tax_info",emp.get("tax_info") == null ? "" : emp.get("tax_info"));
            mapObj.put("emp_id", empCode);
            mapObj.put("emp_name",emp.get(PayItemName.EMPLOYEE_NAME_CN) == null ? "" : emp.get(PayItemName.EMPLOYEE_NAME_CN));
            mapObj.put("id_card_type",emp.get(PayItemName.EMPLOYEE_ID_TYPE_CN) == null ? "" : emp.get(PayItemName.EMPLOYEE_ID_TYPE_CN)); //证件类型
            mapObj.put("id_num",emp.get(PayItemName.IDENTITY_NUM) == null ? "" : emp.get(PayItemName.IDENTITY_NUM)); //证件号
            mapObj.put("company_id",emp.get(PayItemName.EMPLOYEE_COMPANY_ID) == null ? "" : emp.get(PayItemName.EMPLOYEE_COMPANY_ID));
            mapObj.put("batch_type", batchType); // 批次类型
            mapObj.put("mgr_id", mgrId); // 管理方ID
            mapObj.put("mgr_name", batchInfo.get("management_Name") == null ? "" : batchInfo.get("management_Name")); // 管理方名称
            //mapObj.put("department", emp.get(PayItemName.EMPLOYEE_DEP_CN)); // 部门名称
            mapObj.put("income_year_month", batchInfo.get("period") == null ? "" : batchInfo.get("period")); //工资年月
            mapObj.put("batch_id", batchCode);
            mapObj.put("country_code", emp.get(PayItemName.EMPLOYEE_COUNTRY_CODE_CN) == null ? "" : emp.get(PayItemName.EMPLOYEE_COUNTRY_CODE_CN));  // 国家代码
            mapObj.put("leaving_years", emp.get(PayItemName.LEAVE_DATE) == null ? "" : emp.get(PayItemName.LEAVE_DATE)); // 离职年限
            mapObj.put("net_pay", findValByName(payItems,PayItemName.EMPLOYEE_NET_PAY) == null ? "" : findValByName(payItems,PayItemName.EMPLOYEE_NET_PAY)); //实发工资
            mapObj.put("actual_pay", findValByName(payItems,PayItemName.ACTUAL_PAY) == null ? "" : findValByName(payItems,PayItemName.EMPLOYEE_NET_PAY)); //应发工资
            mapObj.put("tax", findValByName(payItems,PayItemName.TAX_TOTAL) == null ? "" : findValByName(payItems,PayItemName.TAX_TOTAL)); //个人所得税
            mapObj.put("wage_before_tax", ""); //个人收入（薪金）- 税前
            mapObj.put("wage_after_tax", ""); //个人收入（薪金）- 税后
            mapObj.put("bonus_before_tax", ""); //个人收入（年奖）- 税前
            mapObj.put("bonus_after_tax", ""); //个人收入（年奖）- 税后
            mapObj.put("labor_before_tax", ""); //个人收入（劳务费）- 税前
            mapObj.put("labor_after_tax", ""); //个人收入（劳务费）- 税后
            mapObj.put("accident_before_tax", ""); //个人收入（偶然所得）- 税前
            mapObj.put("accident_after_tax", ""); //个人收入（偶然所得）- 税后
            mapObj.put("compension_before_tax", ""); // 个人收入（一次性补偿）- 税前
            mapObj.put("compension_after_tax", ""); //个人收入（一次性补偿）- 税后
            mapObj.put("interest_before_tax", ""); //个人收入（利息、股息、红利）- 税前
            mapObj.put("interest_after_tax", ""); //个人收入（利息、股息、红利）- 税后
            mapObj.put("stock_option_before_tax", ""); // 个人收入（股票期权）- 税前
            mapObj.put("stock_option_after_tax", ""); // 个人收入（股票期权）- 税后
            mapObj.put("transfer_before_tax", ""); //个人收入（财产转让所得）- 税前
            mapObj.put("transfer_after_tax", ""); //个人收入（财产转让所得）- 税后
            mapObj.put("social_security", ""); //当前批次雇员的个人社保总额（包括医疗保险、养老保险、失业保险）
            mapObj.put("provident_fund", ""); //当前批次中此雇员的个人公积金总额（包括住房公积金、补充公积金）
            mapObj.put("tax_exemption", ""); //个人免税额度
            mapObj.put("tax_year_month", ""); //报税年月
            mapObj.put("annuity", ""); //年金
            mapObj.put("contract_first_party", ""); //合同我方：分三种 - AF、FC、BPO，销售中心报价单-》雇员服务协议
            mapObj.put("salary_calc_result_items", payItems);
            mapObj.put("employee_service_agreement", emp.get(PayItemName.EMPLOYEE_SERVICE_AGREE) == null ? getEmpAgreement(empCode) : emp.get(PayItemName.EMPLOYEE_SERVICE_AGREE));

            processAdjustFields(batchCode,batchType,item); // 处理调整计算逻辑

            return mapObj;

        }).collect(Collectors.toList());

        int deletedAffected = closeAccountMongoOpt.batchDelete(Criteria.where("batch_id").is(batchCode)); // 幂等操作
        logger.info("幂等删除：" + String.valueOf(deletedAffected));

        int rowAffected = closeAccountMongoOpt.batchInsert(list);

        logger.info("批量插入成功：" + String.valueOf(rowAffected));

        /*if(rowAffected > 0){
            ClosingMsg closingMsg = new ClosingMsg();
            closingMsg.setBatchCode(batchCode);
            closingMsg.setBatchType(batchType);
            closingMsg.setOptID(userID);
            closingMsg.setOptName(userName);

            logger.info("发送关帐通知各个业务部门 : " + closingMsg.toString());

            sender.SendComputeClose(closingMsg);
        }*/
    }

    /**
     * 处理调整规则 比较这次调整批次与上次调整批次的实发工资
     */
   private void processAdjustFields(String bathCode,int batchType, DBObject item) {
       if(batchType == BatchTypeEnum.ADJUST.getValue()){
           DBObject catalog = (DBObject) item.get("catalog");
           String empCode = (String) item.get(PayItemName.EMPLOYEE_CODE_CN);
           Object netPay = findValByName(catalog,PayItemName.EMPLOYEE_NET_PAY); // 现实发工资
           Object originNetPay = item.get("net_pay");                           // 原实发工资

           BigDecimal nowNetPay = new BigDecimal(String.valueOf(netPay));
           BigDecimal orgNetPay = new BigDecimal(String.valueOf(originNetPay));

           if(nowNetPay.compareTo(orgNetPay) < 0){ //原实发工资 比 先实发工资 大
               adjustBatchMongoOpt.update(Criteria.where("batch_code").is(bathCode)
                       .and(PayItemName.EMPLOYEE_CODE_CN).is(empCode), "show_adj", true);
           }
       }
    }

    private Object findValByName(DBObject catalog, String payItemName) {
        List<DBObject> list = (List<DBObject>) catalog.get("pay_items");
        return findValByName(list,payItemName);
    }

    private Object findValByName(List<DBObject> list, String payItemName) {
        Optional<DBObject> find = list.stream().filter(p -> p.get("item_name").equals(payItemName)).findFirst();
        if (find.isPresent()) {
            return find.get().get("item_value");
        }
        return "";
    }

    private DBObject getEmployeeInfo(DBObject catalog, String empCode, String mgrId) {

        DBObject basicDBObject = null;
        String companyId = null;
        FcEmployeeResponseDTO item = null;

        if (catalog.get("emp_info") == null) {
            Criteria criteria = Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode);
            Query query = Query.query(criteria);
            query.fields().include("base_info");
            basicDBObject = empGroupMongoOpt.getMongoTemplate().findOne(query, DBObject.class, EmpGroupMongoOpt.PR_EMPLOYEE_GROUP); // 从其他雇员组查找

            if(basicDBObject == null){ // 其他雇员组不存在
                DBObject emp_Identity = (DBObject)catalog.get("emp_identity");
                basicDBObject = new BasicDBObject();
                basicDBObject.put(PayItemName.EMPLOYEE_CODE_CN, emp_Identity.get("emp_code"));
                basicDBObject.put(PayItemName.EMPLOYEE_COMPANY_ID, emp_Identity.get("companyId"));
                basicDBObject.put(PayItemName.EMPLOYEE_ID, emp_Identity.get("emp_Id"));
                companyId = emp_Identity.get("companyId") == null ? "" : (String)emp_Identity.get("companyId");
            }else { // 其他雇员组存在
                DBObject baseInfo = (DBObject)basicDBObject.get("base_info");
                companyId = baseInfo.get(PayItemName.EMPLOYEE_COMPANY_ID) == null ? "" : (String)baseInfo.get(PayItemName.EMPLOYEE_COMPANY_ID);
            }
        } else {
            basicDBObject = (DBObject) catalog.get("emp_info");
            companyId = basicDBObject.get(PayItemName.EMPLOYEE_COMPANY_ID) == null ? "" : (String)basicDBObject.get(PayItemName.EMPLOYEE_COMPANY_ID);
        }
        item = getRemotedEmployee(empCode, mgrId, companyId);
        logger.info("公司编号：" + companyId);
        if(item != null) {
            List<FcEmployeeResponseDTO.PersonalTaxInfo> taxInfos = item.getPersonalTaxInfos();
            if(taxInfos != null && taxInfos.size() > 0) {
                FcEmployeeResponseDTO.PersonalTaxInfo taxInfo = taxInfos.get(0);
                String taxStr = com.alibaba.fastjson.JSONObject.toJSONString(taxInfo);
                basicDBObject.put("tax_info", taxStr);
            }
        }

        return basicDBObject;
    }

    private FcEmployeeResponseDTO getRemotedEmployee(String empCode, String mgrId, String companyId) {
        FcEmployeeRequestDTO employeeRequestDTO = new FcEmployeeRequestDTO();
        List<String> empCodes = new ArrayList<>();
        empCodes.add(empCode);
        employeeRequestDTO.setEmployeeIds(empCodes);
        employeeRequestDTO.setManagementId(mgrId);
        employeeRequestDTO.setManagementId(companyId);
        JsonResult<List<FcEmployeeResponseDTO>> result = employeeServiceProxy.getFcEmployeeInfos(employeeRequestDTO);
        if (result.isSuccess() && result.getData() != null && result.getData().size() > 0) {
            return result.getData().get(0);
        }
        return null;
    }

    private String getEmpAgreement(String empCode ){
        GetEmployeeContractsDTO contractsDTO = new GetEmployeeContractsDTO();
        List<String> empCodes = new ArrayList<>();
        empCodes.add(empCode);
        contractsDTO.setEmpIds(empCodes);
        JsonResult<List<EmployeeContractResponseDTO>> result = employeeContractProxy.getEmployeeContracts(contractsDTO);
        if(result.isSuccess() && result.getData() != null && result.getData().size() > 0){
            return com.alibaba.fastjson.JSON.toJSONString(result.getData().get(0));
        }
        return "";
    }

}