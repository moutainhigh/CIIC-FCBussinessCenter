package com.ciicsh.gto.fcbusinesscenter.util.mongo;

import com.ciicsh.gt1.BaseOpt;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.stereotype.Component;

/**
 * Created by bill on 17/12/9.
 */
@Component
public class EmpGroupMongoOpt extends BaseOpt {

    private final static Logger logger = LoggerFactory.getLogger(EmpGroupMongoOpt.class);

    private static final String PR_EMPLOYEE_GROUP  = "pr_emp_group_table";

    public void createIndex(){
        DBObject indexOptions = new BasicDBObject();
        indexOptions.put("emp_group_id",1);
        indexOptions.put("雇员编号",1);
        CompoundIndexDefinition indexDefinition = new CompoundIndexDefinition(indexOptions);
        mongoTemplate.indexOps(PR_EMPLOYEE_GROUP).ensureIndex(indexDefinition);
    }

    public EmpGroupMongoOpt() {
        super(PR_EMPLOYEE_GROUP);
    }
}
