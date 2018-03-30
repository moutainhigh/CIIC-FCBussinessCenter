package com.ciicsh.gto.fcbusinesscenter.util.mongo;

import com.ciicsh.gt1.BaseOpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by bill on 18/3/23.
 */
@Component
public class CloseAccountMongoOpt extends BaseOpt {

    private final static Logger logger = LoggerFactory.getLogger(CloseAccountMongoOpt.class);

    public static final String PR_PAYROLL_CAL_RESULT = "fc_payroll_calc_result_table";

    public CloseAccountMongoOpt() {
        super(PR_PAYROLL_CAL_RESULT);
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void createIndex() {
        MongoUtil.createIndex(mongoTemplate, PR_PAYROLL_CAL_RESULT);
    }
}
