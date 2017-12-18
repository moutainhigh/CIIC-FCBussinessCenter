package com.ciicsh.gto.salarymanagementcommandservice.dao.mongo;

import com.ciicsh.gt1.BaseOpt;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bill on 17/12/5.
 */
@Component
public class NormalBathOpt extends BaseOpt {

    private static final String NORMAL_BATCH_TABLE = "normal_batch_table";

    public NormalBathOpt() {
        super(NORMAL_BATCH_TABLE);
    }
}
