package com.ciicsh.gto.fcsupportcenter.util.mongo;

import com.ciicsh.gt1.BaseOpt;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by bill on 17/12/14.
 */
@Component
public class NormalBatchMongoOpt extends BaseOpt {

    private final static Logger logger = LoggerFactory.getLogger(EmpGroupMongoOpt.class);

    private static final String PR_NORMAL_BATCH  = "pr_normal_batch_table";

    @Autowired
    private EmpGroupMongoOpt empGroupOpt;

    public NormalBatchMongoOpt() {
        super(PR_NORMAL_BATCH);
    }

    public void createIndex(){
        DBObject indexOptions = new BasicDBObject();
        indexOptions.put("batch_code",1);    //批次编号
        indexOptions.put("emp_group_id",1);  //雇员组编号
        indexOptions.put("employee_id",1);   //雇员编号
        indexOptions.put("pr_group_code", 1);     //薪资组或者薪资组模版CODE
        CompoundIndexDefinition indexDefinition = new CompoundIndexDefinition(indexOptions);
        mongoTemplate.indexOps(PR_NORMAL_BATCH).ensureIndex(indexDefinition);
    }

    //输入格式：empId: PayItems[] 雇员薪资项列表
    public void associateEmpPayItems(String batchCode, String empGroupId, String groupCode, String empId,DBObject payItems){
        mongoTemplate.upsert(
                Query.query(
                        Criteria.where("batch_code").is(batchCode)
                        .andOperator(Criteria.where("emp_group_id").is(empGroupId),
                                Criteria.where("employee_id").is(empId))
                        ),
                Update.update("pay_items",payItems),PR_NORMAL_BATCH);
    }

    //输入格式：empId: Agreements[] 雇员服务协议列表
    public void associateEmpAgreements(String batchCode, String empGroupId, String groupCode, String empId, DBObject empAgreements){
        mongoTemplate.upsert(
                Query.query(
                        Criteria.where("batch_code").is(batchCode)
                                .andOperator(Criteria.where("emp_group_id").is(empGroupId),
                                        Criteria.where("pr_group_code").is(groupCode),
                                        Criteria.where("employee_id").is(empId))
                ),
                Update.update("agreements",empAgreements),PR_NORMAL_BATCH);
    }
}
