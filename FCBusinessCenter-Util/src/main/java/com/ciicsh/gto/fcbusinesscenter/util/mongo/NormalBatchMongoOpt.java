package com.ciicsh.gto.fcbusinesscenter.util.mongo;

import com.ciicsh.gt1.BaseOpt;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
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

    private final static Logger logger = LoggerFactory.getLogger(EmpGroupMongoOpt.class);

    public static final String PR_NORMAL_BATCH  = "pr_normal_batch_table";

    @Autowired
    private EmpGroupMongoOpt empGroupOpt;

    public NormalBatchMongoOpt() {
        super(PR_NORMAL_BATCH);
    }

    public MongoTemplate getMongoTemplate(){
        return mongoTemplate;
    }

    public void createIndex(){
        MongoUtil.createIndex(mongoTemplate,PR_NORMAL_BATCH);
    }
}
