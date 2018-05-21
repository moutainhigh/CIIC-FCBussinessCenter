package com.ciicsh.gto.fcbusinesscenter.util.mongo;

import com.ciicsh.gt1.BaseOpt;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.stereotype.Component;

/**
 * Created by gaoyang on 18/05/21.
 */
@Component
public class SalaryGrantEmpHisOpt extends BaseOpt {

    public static final String SG_EMP_HIS  = "sg_emp_his_table";
    public SalaryGrantEmpHisOpt(){
        super(SG_EMP_HIS);
    }

    public MongoTemplate getMongoTemplate(){
        return mongoTemplate;
    }

    public void createIndex(){
        DBObject indexOptions = new BasicDBObject();
        indexOptions.put("task_his_id",1);    //任务单历史编号

        CompoundIndexDefinition indexDefinition = new CompoundIndexDefinition(indexOptions);
        mongoTemplate.indexOps(SG_EMP_HIS).ensureIndex(indexDefinition);
    }
}
