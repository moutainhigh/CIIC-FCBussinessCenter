package com.ciicsh.caldispatchjob.compute.mongo;

import com.ciicsh.gt1.BaseOpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by bill on 17/12/15.
 */
public class PrGroupMongoOpt extends BaseOpt {

    private final static Logger logger = LoggerFactory.getLogger(EmpGroupMongoOpt.class);

    private static final String PR_GROUP  = "pr_group_table";

    @Autowired
    private MongoTemplate mongoTemplate;

    public PrGroupMongoOpt() {
        super(PR_GROUP);
    }
}
