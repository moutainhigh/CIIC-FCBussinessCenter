package com.ciicsh.gto.fcbusinesscenter.util.mongo;

import com.ciicsh.gt1.BaseOpt;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.stereotype.Component;

/**
 * Created by bill on 17/12/14.
 */
@Component
public class NormalBatchMongoOpt extends BaseOpt {

    private final static Logger logger = LoggerFactory.getLogger(NormalBatchMongoOpt.class);

    public static final String PR_NORMAL_BATCH  = "pr_normal_batch_table";

    public NormalBatchMongoOpt() {
        super(PR_NORMAL_BATCH);
    }

    public MongoTemplate getMongoTemplate(){
        return mongoTemplate;
    }

    public void createIndex(){
        DBObject indexOptions = new BasicDBObject();
        indexOptions.put("batch_code",1);    //批次编号
        //indexOptions.put("emp_group_code",1);  //雇员组编号
        //indexOptions.put("pr_group_code", 1);     //薪资组或者薪资组模版CODE
        //indexOptions.put("catalog.emp_info.is_active", 1);     //雇员是否可用

        CompoundIndexDefinition indexDefinition = new CompoundIndexDefinition(indexOptions);
        mongoTemplate.indexOps(PR_NORMAL_BATCH).ensureIndex(indexDefinition);
    }
}
