package com.ciicsh.caldispatchjob.compute.service;

import com.ciicsh.gto.fcsupportcenter.util.mongo.EmpGroupMongoOpt;
import com.ciicsh.gto.fcsupportcenter.util.mongo.NormalBatchMongoOpt;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bill on 17/12/19.
 */
@Service
public class NormalBatchServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(NormalBatchServiceImpl.class);

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @Autowired
    private EmpGroupMongoOpt empGroupMongoOpt;

    public void batchInsertNormalBatch(String batchCode, String empGroupId){

        List<DBObject> employees = empGroupMongoOpt.list(Criteria.where("emp_group_code").is(empGroupId));

        /*DBObject dbObject = new BasicDBObject();
        dbObject.put("batch_code",batchCode);
        dbObject.put("emp_group_id",empGroupId);

        dbObject.put("employees", employees);

        mongoTemplate.insert(dbObject,PR_NORMAL_BATCH);*/


        employees.forEach( emp -> {
            emp.put("batch_code",batchCode);
        });
        try {
            normalBatchMongoOpt.batchInsert(employees);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    public void associateEmpPayItems(String batchCode, String empGroupId, String groupCode, String empId,DBObject payItems){
        normalBatchMongoOpt.associateEmpPayItems(batchCode,empGroupId,groupCode,empId,payItems);
    }

    public void associateEmpAgreements(String batchCode, String empGroupId, String groupCode, String empId,DBObject agreements){
        normalBatchMongoOpt.associateEmpAgreements(batchCode,empGroupId,groupCode,empId,agreements);
    }

}
