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
 * Created by bill on 17/12/14.
 */
@Component
public class AdjustBatchMongoOpt extends BaseOpt {

    private final static Logger logger = LoggerFactory.getLogger(AdjustBatchMongoOpt.class);

    private static final String PR_ADJUST_BATCH  = "pr_adjust_batch_table";

    public AdjustBatchMongoOpt() {
        super(PR_ADJUST_BATCH);
    }

    public MongoTemplate getMongoTemplate(){
        return mongoTemplate;
    }

    public void createIndex(){
        MongoUtil.createIndex(mongoTemplate,PR_ADJUST_BATCH);
    }

}