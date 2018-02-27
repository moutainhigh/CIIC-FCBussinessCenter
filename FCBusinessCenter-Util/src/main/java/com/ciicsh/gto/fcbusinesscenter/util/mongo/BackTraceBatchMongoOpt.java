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
public class BackTraceBatchMongoOpt extends BaseOpt {

    private final static Logger logger = LoggerFactory.getLogger(BackTraceBatchMongoOpt.class);

    public static final String PR_BACK_TRACE_BATCH  = "pr_back_trace_batch_table";

    public BackTraceBatchMongoOpt() {
        super(PR_BACK_TRACE_BATCH);
    }

    public MongoTemplate getMongoTemplate(){
        return mongoTemplate;
    }

    public void createIndex(){
        MongoUtil.createIndex(mongoTemplate,PR_BACK_TRACE_BATCH);
    }

}
