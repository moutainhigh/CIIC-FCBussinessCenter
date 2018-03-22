package com.ciicsh.caldispatchjob.Controller;

import com.ciicsh.caldispatchjob.compute.service.ComputeServiceImpl;
import com.ciicsh.caldispatchjob.compute.service.NormalBatchServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


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

        //int affected = normalBatchMongoOpt.batchUpdate(Criteria.where("batch_code").is("ymx-0001-201801-0000000043").and("calalog.pay_items")
        //        .elemMatch(Criteria.where("cal_priority").is(null)),"cal_priority",0);

        mongoTemplate.updateMulti(
                Query.query(
                Criteria.where("batch_code").is("ymx-0001-201801-0000000043").and("calalog.pay_items").elemMatch(
                        Criteria.where("cal_priority").is(null)
                )), Update.update("calalog.pay_items.cal_priority",0),"pr_normal_batch_table"
        );

    }

    @PostMapping("/doBatch")
    public void updateEmpAgreement(){
        for (int i=0; i< 11; i++){
            List<DBObject> list = normalBatchMongoOpt.list(Criteria.where("batch_code").is("GL000007_201811_0000000180"));
            int j = 0;
            list.forEach(p-> {
                p.put("_id", new ObjectId());
            });
            normalBatchMongoOpt.batchInsert(list);
        }

    }

    @PostMapping("/delete")
    public int delete(){
        int rowAffected = normalBatchMongoOpt.delete(Criteria.where("_id").gt(new ObjectId("5aa63094270c214517e8e8e9")));
        return rowAffected;
    }

    @PostMapping("/doCompute")
    public void doCompute(){
        computeService.fire();
    }
}
