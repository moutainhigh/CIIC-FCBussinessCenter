package com.ciicsh.caldispatchjob.compute.service;

import com.ciicsh.gto.fcsupportcenter.util.mongo.EmpAgreementMongoOpt;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bill on 17/12/19.
 */
@Service
public class EmpAgreementServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(EmpAgreementServiceImpl.class);

    @Autowired
    private EmpAgreementMongoOpt empAgreementMongoOpt;

    public void batchInsertServiceAgreement(List<String> empIds){

        empAgreementMongoOpt.createIndex();

        List<DBObject> list = new ArrayList<>();

        //TODO 获取 雇员服务协议 和 个税期间 mock data
        List<DBObject> list1 = new ArrayList<>();
        for (int i=0; i< 3; i++) {
            DBObject object = new BasicDBObject();
            object.put("product_name", "产品名称" + String.valueOf(i));
            object.put("money", 1000.00+i);
            object.put("rate", "次/月");
            object.put("date", "2017/12/09");
            list1.add(object);
        }
        //end

        empIds.forEach(id -> {
            DBObject dbObject = new BasicDBObject();
            dbObject.put("emp_id",id);
            dbObject.put("tax_period","2017/09/11"); //雇员个税期间
            dbObject.put("emp_agreement", list1);
            list.add(dbObject);
        });

        try {
            int rowAffected = empAgreementMongoOpt.batchInsert(list);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

}
