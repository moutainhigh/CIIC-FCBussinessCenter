package com.ciicsh.gto.fcbusinesscenter.util.mongo;

import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexDefinition;

/**
 * Created by bill on 18/1/24.
 */
public class MongoUtil {

    public static void createIndex(MongoTemplate mongoTemplate, String table){
        DBObject indexOptions = new BasicDBObject();
        indexOptions.put("batch_code",1);    //批次编号
        indexOptions.put("emp_group_code",1);  //雇员组编号
        indexOptions.put(PayItemName.EMPLOYEE_CODE_CN,1);   //雇员编号
        indexOptions.put("pr_group_code", 1);     //薪资组或者薪资组模版CODE
        indexOptions.put("catalog.emp_info.is_active", 1);     //雇员是否可用

        CompoundIndexDefinition indexDefinition = new CompoundIndexDefinition(indexOptions);
        mongoTemplate.indexOps(table).ensureIndex(indexDefinition);
    }
}
