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
