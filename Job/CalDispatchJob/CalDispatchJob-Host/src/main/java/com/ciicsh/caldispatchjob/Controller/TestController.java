package com.ciicsh.caldispatchjob.Controller;

import com.ciicsh.caldispatchjob.compute.mongo.EmpGroupMongoOpt;
import com.ciicsh.caldispatchjob.compute.mongo.NormalBatchMongoOpt;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by bill on 17/12/12.
 */
@RestController
public class TestController {

    @Autowired
    private EmpGroupMongoOpt empGroupOpt;

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @PostMapping("/batchInsert")
    public void batchInsert(){
        String empGroupId = UUID.randomUUID().toString();
        List<DBObject> list = new ArrayList<>();
        for (int i=0; i< 2000; i++){
            DBObject emp = new BasicDBObject();
            emp.put("emp_group_id", empGroupId);
            emp.put("emp_id",UUID.randomUUID().toString());
            emp.put("name", "bill" + String.valueOf(i));
            emp.put("department", "department" + String.valueOf(i));
            emp.put("birthDay", "2017-09-09");
            emp.put("country","country" + String.valueOf(i));

            DBObject empPayItems = new BasicDBObject();
            empPayItems.put("baseSalary",1500.00);
            empPayItems.put("OT",200.00);

            emp.put("payItems",empPayItems);

            list.add(emp);
        }

        empGroupOpt.batchInsert(list);
    }

    @PostMapping("/batchDel")
    public void batchDel(){

        List<String> groupIDs = new ArrayList<>();
        groupIDs.add("19");

        List<String> empIDs = new ArrayList<>();
        //empIDs.add("YYA14502");
        //empIDs.add("YYA14106");
        //empIDs.add("YYA14183");
        empIDs.add("YYA14276,YYA14292,YYA14341");

        empGroupOpt.batchDelGroupEmployees(groupIDs,empIDs);
    }

    @PostMapping("/normalBatchInsert")
    public void normalBatchInsert(){
        String batchCode = UUID.randomUUID().toString();
        String empGroupId = "19";

        normalBatchMongoOpt.batchInsertNormalBatch(batchCode,empGroupId);
    }
}
