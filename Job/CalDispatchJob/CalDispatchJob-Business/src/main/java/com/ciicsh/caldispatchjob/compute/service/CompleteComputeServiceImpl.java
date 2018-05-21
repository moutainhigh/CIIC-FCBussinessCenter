package com.ciicsh.caldispatchjob.compute.service;

import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.*;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public void processCompleteCompute(String batchCode, int batchType){

        List<DBObject> batchList = null;

        Criteria criteria = Criteria.where("batch_code").is(batchCode);//.and("catalog.emp_info.is_active").is(true);
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
            batchList = normalBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,normalBatchMongoOpt.PR_NORMAL_BATCH);

        }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
            query.fields().include("net_pay").include("show_adj");
            batchList = adjustBatchMongoOpt.getMongoTemplate().find(query,DBObject.class, AdjustBatchMongoOpt.PR_ADJUST_BATCH);

        }else {
            batchList = backTraceBatchMongoOpt.getMongoTemplate().find(query,DBObject.class, BackTraceBatchMongoOpt.PR_BACK_TRACE_BATCH);
        }

        List<DBObject> list = batchList.stream().map(item -> {
            DBObject catalog = (DBObject)item.get("catalog");
            String empCode = (String) item.get(PayItemName.EMPLOYEE_CODE_CN);
            String empGroupCode = (String) catalog.get("emp_group_code");
            DBObject emp = getEmployeeInfo(catalog,empCode,empGroupCode);
            DBObject batchInfo = (DBObject)catalog.get("batch_info");
            List<DBObject> payItems = (List<DBObject>)catalog.get("pay_items");
            DBObject mapObj = new BasicDBObject();
            mapObj.put("emp_id", empCode);
            mapObj.put("emp_name",emp.get(PayItemName.EMPLOYEE_NAME_CN));
            //mapObj.put("payroll_type", ""); // 工资单类型
            mapObj.put("company_id",emp.get(PayItemName.EMPLOYEE_COMPANY_ID));
            mapObj.put("batch_type", batchType); // 批次类型
            mapObj.put("mgr_id", batchInfo.get("management_ID")); // 管理方ID
            mapObj.put("mgr_name", batchInfo.get("management_Name")); // 管理方名称
            mapObj.put("department", emp.get(PayItemName.EMPLOYEE_DEP_CN)); // 部门名称
            mapObj.put("income_year_month", batchInfo.get("period")); //工资年月
            mapObj.put("batch_id", batchCode);
            mapObj.put("country_code", emp.get(PayItemName.EMPLOYEE_COUNTRY_CODE_CN));  // 国家代码
            mapObj.put("ref_batch_id", "");  // 引用 BATCH
            mapObj.put("leaving_years", emp.get(PayItemName.LEAVE_DATE)); // 离职年限
            mapObj.put("net_pay", findValByName(payItems,PayItemName.EMPLOYEE_NET_PAY)); //实发工资
            mapObj.put("tax", findValByName(payItems,PayItemName.TAX_TOTAL)); //个人所得税
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
            mapObj.put("employee_service_agreement", emp.get(PayItemName.EMPLOYEE_SERVICE_AGREE));

            processAdjustFields(batchCode,batchType,item); // 处理调整计算逻辑

            return mapObj;

        }).collect(Collectors.toList());

        int rowAffected = closeAccountMongoOpt.batchInsert(list);

        logger.info("批量插入成功：" + String.valueOf(rowAffected));
    }

    /**
     * 处理调整规则 比较这次调整批次与上次调整批次的实发工资
     */
   private void processAdjustFields(String bathCode,int batchType, DBObject item) {
       if(batchType == BatchTypeEnum.ADJUST.getValue()){
           DBObject catalog = (DBObject) item.get("catalog");
           String empCode = (String) item.get(PayItemName.EMPLOYEE_CODE_CN);
           Object netPay = findValByName(catalog,PayItemName.EMPLOYEE_NET_PAY); // 先实发工资
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
        return null;
    }

    private DBObject getEmployeeInfo(DBObject catalog, String empCode, String empGroupCode){

       DBObject basicDBObject = null;
       if(catalog.get("emp_info") == null) {
           Criteria criteria = Criteria.where("emp_group_code").is(empGroupCode)
                   .andOperator(Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode));
           Query query = Query.query(criteria);
           query.fields().include("base_info");

           basicDBObject = empGroupMongoOpt.getMongoTemplate().findOne(query, DBObject.class, EmpGroupMongoOpt.PR_EMPLOYEE_GROUP);
       }else {
            basicDBObject = (DBObject)catalog.get("emp_info");
       }
       return basicDBObject;

    }
}