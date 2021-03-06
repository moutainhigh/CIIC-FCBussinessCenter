package com.ciicsh.gto.fcbusinesscenter.util.mongo;

import com.ciicsh.gt1.BaseOpt;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.stereotype.Component;

/**
 * Created by bill on 17/12/19.
 */
@Component
public class EmpAgreementMongoOpt extends BaseOpt {

    private final static Logger logger = LoggerFactory.getLogger(EmpGroupMongoOpt.class);

    private static final String PR_EMPLOYEE_SERVICE_AGREEMENT  = "emp_extends_table";

    public EmpAgreementMongoOpt() {
        super(PR_EMPLOYEE_SERVICE_AGREEMENT);
    }

    public void createIndex(){
        DBObject indexOptions = new BasicDBObject();
        indexOptions.put("employee_id",1);
        CompoundIndexDefinition indexDefinition = new CompoundIndexDefinition(indexOptions);
        mongoTemplate.indexOps(PR_EMPLOYEE_SERVICE_AGREEMENT).ensureIndex(indexDefinition);
    }
}
