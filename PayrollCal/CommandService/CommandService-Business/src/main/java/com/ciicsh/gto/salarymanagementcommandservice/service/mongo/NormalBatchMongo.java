package com.ciicsh.gto.salarymanagementcommandservice.service.mongo;

import com.ciicsh.gt1.BaseOpt;
import com.ciicsh.gto.salarymanagementcommandservice.dao.mongo.NormalBathOpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by bill on 17/12/5.
 */
@Service
public class NormalBatchMongo extends BaseOpt {

    private final static Logger logger = LoggerFactory.getLogger(NormalBatchMongo.class);

    private static final String PR_NORMAL_BATCH  = "pr_normal_batch_table";

    @Autowired
    private MongoTemplate mongoTemplate;

    public NormalBatchMongo() {
        super(PR_NORMAL_BATCH);
    }
}
