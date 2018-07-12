package com.ciicsh.caldispatchjob.compute.service;

import com.alibaba.fastjson.JSON;
import com.ciicsh.caldispatchjob.compute.messageBus.BatchSender;
import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmployeeContractProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmployeeServiceProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.EmployeeContractsRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.FcEmployeeRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.GetEmployeeContractsDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.EmployeeContractResponseDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.FcEmployeeResponseDTO;
import com.ciicsh.gto.fcbusinesscenter.entity.CancelClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.entity.ClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.util.constants.EventName;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.entity.DistributedTranEntity;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.*;
import com.ciicsh.gto.salarymanagement.entity.enums.AdvanceEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.DataTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrNormalBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.common.CommonServiceImpl;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.BizArith;
import com.ciicsh.gto.salecenter.apiservice.api.dto.company.*;
import com.ciicsh.gto.salecenter.apiservice.api.proxy.CompanyProxy;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private FCBizTransactionMongoOpt bizTransactionMongoOpt;

    @Autowired
    private CompanyProxy companyProxy;

    @Autowired
    private PrNormalBatchService normalBatchService;

    @Autowired
    private CommonServiceImpl commonService;

    public void processCompleteCompute(String batchCode, int batchType, String userID, String userName){

        List<DBObject> batchList = null;

        Criteria criteria = Criteria.where("batch_code").is(batchCode);
        Query query = new Query(criteria);

        query.fields().
                include(PayItemName.EMPLOYEE_CODE_CN)
                .include(PayItemName.EMPLOYEE_COMPANY_ID)
                .include("catalog.emp_info")
                .include("catalog.emp_info.country_code")
                .include("catalog.batch_info.actual_period")
                .include("catalog.batch_info.management_ID")
                .include("catalog.batch_info.management_Name")
                .include("catalog.pay_items.data_type")
                .include("catalog.pay_items.item_name")
                .include("catalog.pay_items.cal_precision")
                .include("catalog.pay_items.item_value")
        ;

        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
              /*DBCursor cursor = normalBatchMongoOpt.getMongoTemplate().getCollection("").find().skip().limit().sort().sort();
              while (cursor.hasNext()){
                  DBObject dbObject = cursor.next();
              }*/
            batchList = normalBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,NormalBatchMongoOpt.PR_NORMAL_BATCH);

        }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
            query.fields().include("origin_batch_code").include("net_pay").include("show_adj");
            batchList = adjustBatchMongoOpt.getMongoTemplate().find(query,DBObject.class, AdjustBatchMongoOpt.PR_ADJUST_BATCH);

        }else {
            query.fields().include("origin_batch_code");
            batchList = backTraceBatchMongoOpt.getMongoTemplate().find(query,DBObject.class, BackTraceBatchMongoOpt.PR_BACK_TRACE_BATCH);
        }

        List<DBObject> list = batchList.stream().map(item -> {
            DBObject catalog = (DBObject)item.get("catalog");
            String empCode = (String) item.get(PayItemName.EMPLOYEE_CODE_CN);
            String companyId = item.get(PayItemName.EMPLOYEE_COMPANY_ID) == null ? "" : (String) item.get(PayItemName.EMPLOYEE_COMPANY_ID);
            DBObject batchInfo = (DBObject)catalog.get("batch_info");
            String mgrId = batchInfo.get("management_ID") == null ? "" : (String) batchInfo.get("management_ID");
            DBObject empInfo = getEmployeeInfo(catalog,empCode,mgrId,companyId);
            List<DBObject> payItems = (List<DBObject>)catalog.get("pay_items");
            payItems = payItems.stream().map(pItem -> {
                        DBObject tranItem = new BasicDBObject();
                        int dataType = pItem.get("data_type") == null ? 0 : (int) pItem.get("data_type");
                        int precision = pItem.get("cal_precision") == null ? 2 : (int) pItem.get("cal_precision");
                        Object itemVal =  pItem.get("item_value");
                        if (dataType == DataTypeEnum.NUM.getValue()) {
                            try {
                                tranItem.put("item_value_str", BizArith.roundStr(itemVal, precision));
                            }catch (Exception e){
                                logger.info(e.getMessage());
                            }
                        }
                        tranItem.put("item_name",pItem.get("item_name"));
                        tranItem.put("item_value",pItem.get("item_value"));
                        return tranItem;
                    }
                ).collect(Collectors.toList());
            DBObject mapObj = new BasicDBObject();
            mapObj.put("batch_type", batchType); // 批次类型
            mapObj.put("batch_id", batchCode);  // 批次ID

            long version = commonService.getBatchVersion(batchCode) + 1;
            mapObj.put("version", version); // 批次版本号

            mapObj.put("batch_ref_id",item.get("origin_batch_code") == null ? "" :item.get("origin_batch_code")); // 该批次引用ID
            mapObj.put("mgr_id", mgrId); // 管理方ID
            mapObj.put("mgr_name", batchInfo.get("management_Name") == null ? "" : batchInfo.get("management_Name")); // 管理方名称
            mapObj.put("emp_id", empCode); // 中智雇员ID
            mapObj.put("emp_info",empInfo); // 雇员信息
            mapObj.put("income_year_month", batchInfo.get("actual_period") == null ? "" : batchInfo.get("actual_period")); //工资年月
            //mapObj.put("leaving_years", empInfo.get(PayItemName.LEAVE_DATE) == null ? "" : empInfo.get(PayItemName.LEAVE_DATE)); // 离职年限
            //mapObj.put("net_pay", findValByName(payItems,PayItemName.EMPLOYEE_NET_PAY) == null ? "" : findValByName(payItems,PayItemName.EMPLOYEE_NET_PAY)); //实发工资
            //mapObj.put("actual_pay", findValByName(payItems,PayItemName.ACTUAL_PAY) == null ? "" : findValByName(payItems,PayItemName.EMPLOYEE_NET_PAY)); //应发工资
            /*mapObj.put("tax", findValByName(payItems,PayItemName.TAX_TOTAL) == null ? "" : findValByName(payItems,PayItemName.TAX_TOTAL)); //个人所得税
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
            mapObj.put("annuity", ""); //年金*/
            //mapObj.put("contract_first_party", ""); //合同我方：分三种 - AF、FC、BPO，销售中心报价单-》雇员服务协议
            mapObj.put("salary_calc_result_items", payItems);
            processAdjustFields(batchCode,batchType,item); // 处理调整计算逻辑

            return mapObj;

        }).collect(Collectors.toList());

        doBizTransaction(batchCode, batchType,list, userID, userName);
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

    private DBObject getEmployeeInfo(DBObject catalog, String empCode, String mgrId,String companyId) {

        DBObject basicDBObject = null;
        FcEmployeeResponseDTO item = null;

        if (catalog.get("emp_info") == null) {
            Criteria criteria = Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode)
                    .andOperator(
                            Criteria.where(PayItemName.EMPLOYEE_COMPANY_ID).is(companyId)
                            );
            Query query = Query.query(criteria);
            query.fields().include("base_info");
            basicDBObject = empGroupMongoOpt.getMongoTemplate().findOne(query, DBObject.class, EmpGroupMongoOpt.PR_EMPLOYEE_GROUP); // 从其他雇员组查找

            if(basicDBObject == null){ // 其他雇员组不存在
                //DBObject emp_Identity = (DBObject)catalog.get("emp_identity");
                basicDBObject = new BasicDBObject();
                item = getRemotedEmployee(empCode, mgrId, companyId);
                //填充雇员其他属性
                Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                basicDBObject.put(PayItemName.EMPLOYEE_CODE_CN, item.getEmployeeId());
                basicDBObject.put(PayItemName.EMPLOYEE_COMPANY_ID, companyId);
                basicDBObject.put(PayItemName.EMPLOYEE_ID, item.getEmpCode());
                basicDBObject.put(PayItemName.EMPLOYEE_NAME_CN,item.getEmployeeName());
                basicDBObject.put(PayItemName.EMPLOYEE_BIRTHDAY_CN, item.getBirthday() == null ? "" : formatter.format(item.getBirthday()));
                basicDBObject.put(PayItemName.EMPLOYEE_SEX_CN, item.getGender()? "男":"女");
                basicDBObject.put(PayItemName.EMPLOYEE_ID_TYPE_CN,item.getIdCardType());
                basicDBObject.put(PayItemName.EMPLOYEE_ONBOARD_CN, item.getInDate() == null ? "" : formatter.format(item.getInDate()));
                basicDBObject.put(PayItemName.EMPLOYEE_ID_NUM_CN,item.getIdNum());
                basicDBObject.put(PayItemName.EMPLOYEE_FORMER_CN, item.getFormerName());
                basicDBObject.put(PayItemName.EMPLOYEE_COUNTRY_CODE_CN,item.getCountryCode());
                basicDBObject.put(PayItemName.EMPLOYEE_PROVINCE_CODE_CN,item.getProvinceCode());
                basicDBObject.put(PayItemName.EMPLOYEE_CITY_CODE_CN,item.getCityCode());
                basicDBObject.put(PayItemName.NATIONALITY, item.getCountryName());
                basicDBObject.put(PayItemName.LEAVE_DATE, item.getOutDate() == null ? "" : formatter.format(item.getOutDate()));
            }
        } else {
            basicDBObject = (DBObject) catalog.get("emp_info");
        }

        basicDBObject.put(PayItemName.EMPLOYEE_SERVICE_AGREE, getEmpAgreement(empCode,companyId)); //获取雇员服务协议

        if(item == null) {
            item = getRemotedEmployee(empCode, mgrId, companyId);
        }
        logger.info("公司编号：" + companyId);
        if(item != null) {
            basicDBObject.put("联系电话", item.getMobile()); //获取雇员联系电话
            List<FcEmployeeResponseDTO.PersonalTaxInfo> taxInfos = item.getPersonalTaxInfos();
            if(taxInfos != null && taxInfos.size() > 0) { // 个税信息处理
                FcEmployeeResponseDTO.PersonalTaxInfo taxInfo = taxInfos.get(0);
                //String taxStr = com.alibaba.fastjson.JSONObject.toJSONString(taxInfo);
                DBObject tax = new BasicDBObject();
                tax.put("reportTaxCertId", taxInfo.getReportTaxCertId());
                tax.put("reportTaxCertNo", taxInfo.getReportTaxCertNo());
                tax.put("reportTaxCertDueDate",taxInfo.getReportTaxCertDueDate());
                tax.put("reportTaxCountryId", taxInfo.getReportTaxCountryId());
                tax.put("cmyFcEmpPersonalTaxId",taxInfo.getCmyFcEmpPersonalTaxId());
                tax.put("annualPremium", taxInfo.getAnnualPremium());
                tax.put("companyId", taxInfo.getCompanyId());
                tax.put("taxTerm", taxInfo.getTaxTerm());
                tax.put("taxReturnName", taxInfo.getTaxReturnName());
                tax.put("isSpecialStatus", taxInfo.getIsSpecialStatus());
                tax.put("isEmployee", taxInfo.getIsEmployee());
                tax.put("isPartner", taxInfo.getIsPartner());
                tax.put("isForeign", taxInfo.getIsForeign());
                tax.put("preInvestAmount", taxInfo.getPreInvestAmount());
                tax.put("cnName", taxInfo.getCnName());
                tax.put("arriveCnTime", taxInfo.getArriveCnTime());
                tax.put("officeTerm", taxInfo.getOfficeTerm());
                tax.put("prevLeaveTime", taxInfo.getPrevLeaveTime());
                tax.put("prevLeavePlace", taxInfo.getPrevLeavePlace());
                tax.put("churchyardJob", taxInfo.getChurchyardJob());
                tax.put("overseasJob", taxInfo.getOverseasJob());
                tax.put("payPlace", taxInfo.getPayPlace());
                tax.put("overseasPayPlace", taxInfo.getOverseasPayPlace());
                tax.put("taxPayType", taxInfo.getTaxPayType());
                tax.put("taxSuperCode", taxInfo.getTaxSuperCode());
                tax.put("monthlyPremium", taxInfo.getMonthlyPremium());
                tax.put("stockOptionType", taxInfo.getStockOptionType());
                tax.put("stockName", taxInfo.getStockName());
                tax.put("stockCode", taxInfo.getStockCode());
                tax.put("stockType", taxInfo.getStockType());
                tax.put("optStrikePrice", taxInfo.getOptStrikePrice() == null ? "" : taxInfo.getOptStrikePrice().doubleValue());
                tax.put("optPowerPrice", taxInfo.getOptPowerPrice() == null ? "" : taxInfo.getOptPowerPrice().doubleValue());
                tax.put("optStockAmount", taxInfo.getOptStockAmount());
                tax.put("optMonthlyStrikeIncome", taxInfo.getOptMonthlyStrikeIncome() == null ? "" : taxInfo.getOptMonthlyStrikeIncome().doubleValue());
                tax.put("appStrikePrice", taxInfo.getAppStrikePrice() == null ? "" : taxInfo.getAppStrikePrice().doubleValue());
                tax.put("appPowerPrice", taxInfo.getAppPowerPrice() == null ? "" : taxInfo.getAppPowerPrice().doubleValue());
                tax.put("appStockAmount", taxInfo.getAppStockAmount());
                tax.put("appMonthlyStrikeIncome", taxInfo.getAppMonthlyStrikeIncome() == null ? "" : taxInfo.getAppMonthlyStrikeIncome().doubleValue());
                tax.put("resRegistPrice", taxInfo.getResRegistPrice() == null ? "" : taxInfo.getResRegistPrice().doubleValue());
                tax.put("resJjPrice", taxInfo.getResJjPrice() == null ? "":taxInfo.getResJjPrice().doubleValue());
                tax.put("resJjAmount", taxInfo.getResJjAmount());
                tax.put("resPayAmount", taxInfo.getResPayAmount() == null ? "" : taxInfo.getResPayAmount().doubleValue());
                tax.put("resMonthlyStrikeIncome", taxInfo.getResMonthlyStrikeIncome() == null? "" : taxInfo.getResMonthlyStrikeIncome().doubleValue());
                tax.put("fitFormula", taxInfo.getFitFormula());

                basicDBObject.put("tax_info", tax);

            }
        }

        return basicDBObject;
    }

    private FcEmployeeResponseDTO getRemotedEmployee(String empCode, String mgrId, String companyId) {
        FcEmployeeRequestDTO employeeRequestDTO = new FcEmployeeRequestDTO();
        employeeRequestDTO.setEmployeeIds(empCode);
        employeeRequestDTO.setManagementId(mgrId);
        employeeRequestDTO.setCompanyId(companyId);
        JsonResult<List<FcEmployeeResponseDTO>> result = employeeServiceProxy.getFcEmployeeInfos(employeeRequestDTO);
        if (result.isSuccess() && result.getData() != null && result.getData().size() > 0) {
            return result.getData().get(0);
        }
        return null;
    }

    private Object getEmpAgreement(String empCode, String companyId){
        EmployeeContractsRequestDTO contractsRequestDTO = new EmployeeContractsRequestDTO();
        List<EmployeeContractsRequestDTO.EmployeeContractsRequestWrapper> list = new ArrayList<>();
        EmployeeContractsRequestDTO.EmployeeContractsRequestWrapper wrapper = new EmployeeContractsRequestDTO.EmployeeContractsRequestWrapper();
        wrapper.setCompanyId(companyId);
        wrapper.setEmpId(empCode);
        list.add(wrapper);
        contractsRequestDTO.setEmployeeContractsRequestWrapperList(list);
        JsonResult<List<EmployeeContractResponseDTO>> result = employeeContractProxy.getEmployeeContractsByEmpIdAndCompanyId(contractsRequestDTO);
        if(result.isSuccess() && result.getData() != null && result.getData().size() > 0){
            return com.alibaba.fastjson.JSON.toJSON(result.getData().get(0));
        }
        return "";
    }

    /**
     * 初始化薪资计算后续分布式事务
     */
    private int initializeDistributedTransaction(String batchCode, int batchType){
        DistributedTranEntity tranEntity = new DistributedTranEntity();
        tranEntity.setTransactionName(EventName.FC_TRANSACTION_NAME);
        tranEntity.setBatchCode(batchCode);
        tranEntity.setBatchType(batchType);
        tranEntity.setCompleted(false);
        List<DBObject> events = new ArrayList<>();
        DBObject event = new BasicDBObject();
        event.put("event_name",EventName.FC_GRANT_EVENT);
        event.put("event_status", 0 );
        events.add(event);

        event = new BasicDBObject();
        event.put("event_name",EventName.FC_BILL_EVENT);
        event.put("event_status", 0 );
        events.add(event);

        event = new BasicDBObject();
        event.put("event_name",EventName.FC_SLIP_EVENT);
        event.put("event_status", 0 );
        events.add(event);

        event = new BasicDBObject();
        event.put("event_name",EventName.FC_TAX_EVENT);
        event.put("event_status", 0 );
        events.add(event);

        tranEntity.setEvents(events);
        return bizTransactionMongoOpt.initDistributedTransaction(tranEntity);

    }

    @Transactional(rollbackFor = RuntimeException.class)
    protected void doBizTransaction(String batchCode, int batchType,List<DBObject> list,String userID, String userName){

        int deletedAffected = closeAccountMongoOpt.batchDelete(Criteria.where("batch_id").is(batchCode)); // 幂等操作
        logger.info("幂等删除：" + String.valueOf(deletedAffected));

        int rowAffected = closeAccountMongoOpt.batchInsert(list);

        logger.info("批量插入成功：" + String.valueOf(rowAffected));

        initializeDistributedTransaction(batchCode,batchType);

        logger.info("初始化分布式事务：" + String.valueOf(rowAffected));

        if(rowAffected > 0){

            /*int affected = updateAdvance(companyIds,batchCode,batchType); //周期垫付逻辑处理

            if(affected > 0){
                logger.info("产生周期垫付");
            }*/

            ClosingMsg closingMsg = new ClosingMsg();
            closingMsg.setBatchCode(batchCode);
            closingMsg.setBatchType(batchType);
            closingMsg.setOptID(userID);
            closingMsg.setOptName(userName);

            long version = commonService.getBatchVersion(batchCode);
            closingMsg.setVersion(version);

            sender.SendComputeClose(closingMsg);
            logger.info("发送关帐通知各个业务部门 : " + closingMsg.toString());


        }
    }


}