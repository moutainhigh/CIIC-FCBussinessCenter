package com.ciicsh.caldispatchjob.Controller;

import com.ciicsh.caldispatchjob.compute.service.ComputeServiceImpl;
import com.ciicsh.caldispatchjob.compute.service.NormalBatchServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


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

    @PostMapping("/updateEmpAgreement")
    public void updateEmpAgreement(){
        /*String batchCode = "5bb57dfe-1dcb-447e-9179-a4d4010085f5";
        String empGroupId = "19";
        String groupCode = "";
        String empId = "YYA14369";
        List<DBObject> list = new ArrayList<>();
        DBObject  object = new BasicDBObject();
        object.put("产品名称", "薪酬福利计算");
        object.put("金额",2220.8);
        object.put("频率","次/月");
        object.put("时间","2017-08-09");*/

        //normalBatchService.associateEmpAgreements(batchCode,empGroupId,groupCode,empId,object);
    }

    @PostMapping("/doCompute")
    public void doCompute(){
        computeService.fire();
    }
}
