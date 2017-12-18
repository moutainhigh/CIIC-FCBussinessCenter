package com.ciicsh.caldispatchjob.compute.mongo;

import com.ciicsh.gt1.BaseOpt;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
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

    @Autowired
    private MongoTemplate mongoTemplate;

    public NormalBatchMongoOpt() {
        super(PR_NORMAL_BATCH);
    }

    public void batchInsertNormalBatch(String batchCode, String empGroupId){

        createIndex();

        List<DBObject> employees = empGroupOpt.getEmloyeesByGroupId(empGroupId);

        /*DBObject dbObject = new BasicDBObject();
        dbObject.put("batch_code",batchCode);
        dbObject.put("emp_group_id",empGroupId);

        dbObject.put("employees", employees);

        mongoTemplate.insert(dbObject,PR_NORMAL_BATCH);*/


        employees.forEach( emp -> {
            emp.put("batch_code",batchCode);
        });
        try {
            this.batchInsert(employees);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
}

    private void createIndex(){
        DBObject indexOptions = new BasicDBObject();
        indexOptions.put("batch_code",1);
        indexOptions.put("emp_group_id",1);
        indexOptions.put("employee_id",1);
        CompoundIndexDefinition indexDefinition = new CompoundIndexDefinition(indexOptions);
        mongoTemplate.indexOps(PR_NORMAL_BATCH).ensureIndex(indexDefinition);
    }
}
