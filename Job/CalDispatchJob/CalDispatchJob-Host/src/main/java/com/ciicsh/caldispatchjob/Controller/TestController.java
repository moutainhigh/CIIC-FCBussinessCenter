package com.ciicsh.caldispatchjob.Controller;

import com.ciicsh.caldispatchjob.compute.Cal.ComputeServiceImpl;
import com.ciicsh.caldispatchjob.compute.service.NormalBatchServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by bill on 17/12/12.
 */
@RestController
public class TestController {

    @Autowired
    private NormalBatchServiceImpl normalBatchService;

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ComputeServiceImpl computeService;

    @PostMapping("/updatePayItem")
    public void updatePayItem(){

        List<DBObject> list = normalBatchMongoOpt.list(Criteria.where("batch_code").is("GL000007_201811_0000000180"));
        MongoTemplate template = normalBatchMongoOpt.getMongoTemplate();

        int k = 0;
        for (DBObject item: list) {
            item.put("雇员编号","A" + String.valueOf(k));
            k++;
            System.out.println(k);
        }
        int delCount = normalBatchMongoOpt.batchDelete(Criteria.where("batch_code").is("GL000007_201811_0000000180"));
        int insertCount = normalBatchMongoOpt.batchInsert(list);

        System.out.println("delete:  " + String.valueOf(delCount) + " insert: " + String.valueOf(insertCount));

    }

    @PostMapping("/doBatch")
    public void updateEmpAgreement(){

        BasicDBObject firstObj = (BasicDBObject)normalBatchMongoOpt.list(Criteria.where("batch_code").is("GL000007_201712_0000000176")).get(0);

        List<DBObject> list = new ArrayList<>();

        for (int i = 0; i< 2010; i++){

            DBObject clone = (DBObject)firstObj.clone();
            clone.put("_id",new ObjectId());
            clone.put("雇员编号","A001" + String.valueOf(i));

            list.add(clone);
        }

        normalBatchMongoOpt.batchInsert(list);


        /*for (int i=0; i< 11; i++){
            List<DBObject> list = normalBatchMongoOpt.list(Criteria.where("batch_code").is("GL000007_201801_0000000179"));
            int j = 0;
            for (DBObject p: list) {
                p.put("_id", new ObjectId());
                p.put("雇员编号","A001" + String.valueOf(j));
                j++;
            }
            normalBatchMongoOpt.batchInsert(list);
        }*/

    }

    @PostMapping("/delete")
    public int delete(){
        int rowAffected = normalBatchMongoOpt.delete(Criteria.where("_id").gt(new ObjectId("5ab36576270c214517f50fbf")));
        return rowAffected;
    }

    @PostMapping("/doCompute")
    public void doCompute(){
        computeService.fire();
    }
}
