package com.ciicsh.caldispatchjob.compute.service;

import com.ciicsh.gt1.BathUpdateOptions;
import com.ciicsh.gto.fcsupportcenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.po.PayrollGroupExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollItemMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bill on 17/12/19.
 */
@Service
public class NormalBatchServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(NormalBatchServiceImpl.class);

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @Autowired
    private EmpGroupServiceImpl empGroupService;

    @Autowired
    private PrPayrollItemMapper prPayrollItemMapper;

    @Autowired
    private PrNormalBatchMapper normalBatchMapper;

    /**
     * 批量更新或者插入数据
     * @param batchCode
     * @return
     */
    public int batchInsertOrUpdateNormalBatch(String batchCode){


        //BEGIN
        //根据batch code 获取 PrCustBatchPO
        PrCustBatchPO initialPO = new PrCustBatchPO();
        initialPO.setCode(batchCode);
        PrCustBatchPO batchPO = normalBatchMapper.selectBatchListByUseLike(initialPO).get(0);
        //END

        List<DBObject> employees = empGroupService.getEmployeesByEmpGroupCode(batchPO.getEmpGroupCode());

        List<BathUpdateOptions> options = new ArrayList<>();

        List<DBObject> payItems = getPayItemList(batchPO.isTemplate(), batchPO.getPrGroupCode(),batchPO.getManagementId());

        employees.forEach( item -> {
            BathUpdateOptions opt = new BathUpdateOptions();
            opt.setQuery(Query.query(Criteria.where("batch_code").is(batchPO.getCode())  //批次号
                    .andOperator(
                            Criteria.where("pr_group_code").is(batchPO.getPrGroupCode()),   //薪资组或薪资组模版编码
                            Criteria.where("emp_group_code").is(batchPO.getEmpGroupCode()), //雇员组编码
                            Criteria.where("雇员编号").is(item.get("雇员编号"))
                            )
                    ));
            DBObject basicDBObject = new BasicDBObject();
            basicDBObject.put("batch_info",BatchPOToDBObject(batchPO));
            basicDBObject.put("emp_info",item.get("base_info"));
            basicDBObject.put("pay_items",payItems);

            opt.setUpdate(Update.update("catalog", basicDBObject));
            opt.setMulti(true);
            opt.setUpsert(true);
            options.add(opt);

        });
        try {
            //create index
            normalBatchMongoOpt.createIndex();
            int rowAffected = normalBatchMongoOpt.doBathUpdate(options,true);
            logger.info("row affected : " + String.valueOf(rowAffected));
            return rowAffected;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return 0;
    }

    /**
     * 删除批次
     * @param batchCodes
     * @return
     */
    public int deleteNormalBatch(String batchCodes) {
        String[] codes = batchCodes.split(",");
        if (codes.length > 0) {
            int rowAffected = normalBatchMongoOpt.delete(Criteria.where("batch_code").in(Arrays.asList(codes)));
            logger.info("row affected : " + String.valueOf(rowAffected));
            return rowAffected;
        }
        return 0;
    }

    private DBObject BatchPOToDBObject(PrCustBatchPO batchPO){
        DBObject dbObject = new BasicDBObject();
        dbObject.put("pr_group_name",batchPO.getPrGroupName());
        dbObject.put("emp_group_name",batchPO.getEmpGroupName());
        dbObject.put("account_set_code",batchPO.getAccountSetCode());
        dbObject.put("account_set_name",batchPO.getAccountSetName());
        dbObject.put("begin_date",batchPO.getBeginDate());
        dbObject.put("end_date",batchPO.getEndDate());
        dbObject.put("begin_day",batchPO.getStartDay());
        dbObject.put("end_day",batchPO.getEndDay());
        dbObject.put("management_ID",batchPO.getManagementId());
        dbObject.put("management_Name",batchPO.getManagementName());
        dbObject.put("payroll_type",batchPO.getPayrollType());
        dbObject.put("period",batchPO.getPeriod());
        dbObject.put("status",batchPO.getStatus());
        return dbObject;
    }

    private List<DBObject> getPayItemList(Boolean isTemplate, String pr_code, String manageId){
        List<PrPayrollItemPO> prList = null;
        List<DBObject> list = new ArrayList<>();

        PayrollGroupExtPO payrollGroupExtPO = new PayrollGroupExtPO();
        payrollGroupExtPO.setManagementId(manageId);
        if(isTemplate) {
            payrollGroupExtPO.setPayrollGroupCode("");
            payrollGroupExtPO.setPayrollGroupTemplateCode(pr_code);
        }else {
            payrollGroupExtPO.setPayrollGroupCode(pr_code);
            payrollGroupExtPO.setPayrollGroupTemplateCode("");

        }
        prList = prPayrollItemMapper.getPayrollItems(payrollGroupExtPO);

        //计算顺序由小到大排列，越小计算优先级越高
        prList = prList.stream().sorted(Comparator.comparingInt(PrPayrollItemPO::getCalPriority)).collect(Collectors.toList());

        prList.forEach(item ->{
            DBObject dbObject = new BasicDBObject();
            dbObject.put("item_code",item.getItemCode());
            dbObject.put("item_name",item.getItemName());
            dbObject.put("item_value",item.getItemValue());
            dbObject.put("item_type",item.getItemType());
            dbObject.put("data_type",item.getDataType());
            dbObject.put("management_id",item.getManagementId());
            dbObject.put("item_condition",item.getItemCondition());
            dbObject.put("formula_content",item.getFormulaContent());
            dbObject.put("origin_formula",item.getOriginFormula());
            dbObject.put("cal_precision",item.getCalPrecision());
            dbObject.put("cal_priority",item.getCalPriority());
            dbObject.put("default_value_style",item.getDefaultValueStyle());
            dbObject.put("default_value",item.getDefaultValue());
            dbObject.put("decimal_process_type",item.getDecimalProcessType());
            dbObject.put("display_priority",item.getDisplayPriority());
            list.add(dbObject);
        });

        return list;
    }
}
