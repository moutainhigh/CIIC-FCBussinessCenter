package com.ciicsh.caldispatchjob.Controller;

import com.ciicsh.caldispatchjob.compute.service.EmpAgreementServiceImpl;
import com.ciicsh.caldispatchjob.compute.service.NormalBatchServiceImpl;
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
    private EmpAgreementServiceImpl empAgreementService;

    @Autowired
    private NormalBatchServiceImpl normalBatchService;

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
        //empGroupOpt.batchInsert(list);
    }

    @PostMapping("/batchEmpAgreement")
    public void batchEmpAgreement(){
        List<String> empIds = new ArrayList<>();
        empIds.add("YYA14369");
        empIds.add("YYA14378");
        empAgreementService.batchInsertServiceAgreement(empIds);
    }

    @PostMapping("/updatePayItem")
    public void updatePayItem(){
        String batchCode = "5bb57dfe-1dcb-447e-9179-a4d4010085f5";
        String empGroupId = "19";
        String groupCode = "";
        String empId = "YYA14369";
        List<DBObject> list = new ArrayList<>();
        DBObject  object = new BasicDBObject();
        object.put("基本工资", 15000);
        object.put("加班小时数",20.8);
        object.put("税后工资",12000);
        object.put("工龄",1.2);

        normalBatchService.associateEmpPayItems(batchCode,empGroupId,groupCode,empId,object);
    }

    @PostMapping("/updateEmpAgreement")
    public void updateEmpAgreement(){
        String batchCode = "5bb57dfe-1dcb-447e-9179-a4d4010085f5";
        String empGroupId = "19";
        String groupCode = "";
        String empId = "YYA14369";
        List<DBObject> list = new ArrayList<>();
        DBObject  object = new BasicDBObject();
        object.put("产品名称", "薪酬福利计算");
        object.put("金额",2220.8);
        object.put("频率","次/月");
        object.put("时间","2017-08-09");

        normalBatchService.associateEmpAgreements(batchCode,empGroupId,groupCode,empId,object);
    }
}
