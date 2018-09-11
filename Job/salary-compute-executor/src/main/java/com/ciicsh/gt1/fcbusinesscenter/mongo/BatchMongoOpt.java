package com.ciicsh.gt1.fcbusinesscenter.mongo;

import com.ciicsh.gt1.BaseOpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class BatchMongoOpt extends BaseOpt {

    private final static Logger logger = LoggerFactory.getLogger(BatchMongoOpt.class);

    public static final String PR_NORMAL_BATCH  = "pr_normal_batch_table";

    public BatchMongoOpt() {
        super(PR_NORMAL_BATCH);
    }

    public MongoTemplate getMongoTemplate(){
        return mongoTemplate;
    }

}
