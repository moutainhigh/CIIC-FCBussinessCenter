package com.ciicsh.gto.fcbusinesscenter.util.mongo;

import com.ciicsh.gt1.BaseOpt;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.stereotype.Component;

/**
 * Created by Rock.Jiang on 18/08/01.
 */
@Component
public class TestBatchMongoOpt extends BaseOpt {

    private final static Logger logger = LoggerFactory.getLogger(TestBatchMongoOpt.class);

    public static final String PR_TEST_BATCH  = "pr_test_batch_table";

    public TestBatchMongoOpt() {
        super(PR_TEST_BATCH);
    }

    public MongoTemplate getMongoTemplate(){
        return mongoTemplate;
    }

    public void createIndex(){
        DBObject indexOptions = new BasicDBObject();
        indexOptions.put("batch_code",1);    //批次编号
        indexOptions.put("emp_group_code",1);  //雇员组编号
        indexOptions.put("pr_group_code", 1);     //薪资组或者薪资组模版CODE
        indexOptions.put(PayItemName.EMPLOYEE_CODE_CN, 1);     //雇员CODE
        indexOptions.put(PayItemName.EMPLOYEE_COMPANY_ID, 1);  //公司ID

        CompoundIndexDefinition indexDefinition = new CompoundIndexDefinition(indexOptions);
        mongoTemplate.indexOps(PR_TEST_BATCH).ensureIndex(indexDefinition);
    }
}
