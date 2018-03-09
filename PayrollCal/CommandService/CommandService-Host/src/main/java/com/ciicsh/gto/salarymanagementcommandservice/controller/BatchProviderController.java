package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.BatchProxy;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrAdjustBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrBackTrackingBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrNormalBatchService;
import com.google.gson.Gson;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by bill on 18/1/13.
 */
@RestController
public class BatchProviderController implements BatchProxy {

    @Autowired
    private PrNormalBatchService normalBatchService;

    @Autowired
    private PrBackTrackingBatchService backTrackingBatchService;

    @Autowired
    private PrAdjustBatchService adjustBatchService;

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @Autowired
    private AdjustBatchMongoOpt adjustBatchMongoOpt;

    @Autowired
    private BackTraceBatchMongoOpt backTraceBatchMongoOpt;

    //@Autowired
    //public KieSession kieSession;

    @Override
    public String getBatchInfoByCode(String batchCode, int batchType) {

        //kieSession.fireAllRules();

        Gson gson = new Gson();
        List<DBObject> batchResult = null;
        if(batchType == BatchTypeEnum.NORMAL.getValue()){
            batchResult = normalBatchMongoOpt.list(Criteria.where("batch_code").is(batchCode));
            return gson.toJson(batchResult);

        }else if(batchType == BatchTypeEnum.ADJUST.getValue()){
            batchResult = adjustBatchMongoOpt.list(Criteria.where("batch_code").is(batchCode));
            return gson.toJson(batchResult);
        }
        else {
            batchResult = backTraceBatchMongoOpt.list(Criteria.where("batch_code").is(batchCode));
            return gson.toJson(batchResult);
        }

    }

    public String getBatchListByCodes(List<String> batchCodes, int batchType) {
        Gson gson = new Gson();
        List<DBObject> batchResult = null;
        if(batchType == BatchTypeEnum.NORMAL.getValue()){
            batchResult = normalBatchMongoOpt.list(Criteria.where("batch_code").in(Arrays.asList(batchCodes)));
            return gson.toJson(batchResult);

        }else if(batchType == BatchTypeEnum.ADJUST.getValue()){
            batchResult = adjustBatchMongoOpt.list(Criteria.where("batch_code").in(Arrays.asList(batchCodes)));
            return gson.toJson(batchResult);
        }
        else {
            batchResult = backTraceBatchMongoOpt.list(Criteria.where("batch_code").in(Arrays.asList(batchCodes)));
            return gson.toJson(batchResult);
        }
    }

    public int updateAdvanceBatch(String batchCode, Boolean hasAdvance, int batchType) {
        int rowAffected = 0;
        if(batchType == BatchTypeEnum.NORMAL.getValue()){
            normalBatchService.updateHasAdvance(batchCode,hasAdvance,"sys"); //TODO
            rowAffected = normalBatchMongoOpt.update(Criteria.where("batchCode").is(batchCode),"has_advance",hasAdvance);
        }else if(batchType == BatchTypeEnum.ADJUST.getValue()){
            backTrackingBatchService.updateHasAdvance(batchCode,hasAdvance,"sys"); //TODO
            rowAffected = adjustBatchMongoOpt.update(Criteria.where("batchCode").is(batchCode),"has_advance",hasAdvance);
        }
        else {
            adjustBatchService.updateHasAdvance(batchCode,hasAdvance,"sys");
            rowAffected = backTraceBatchMongoOpt.update(Criteria.where("batchCode").is(batchCode),"has_advance",hasAdvance);
        }
        return rowAffected;
    }

    public int updateHasMoneyBatch(String batchCode, Boolean hasMoney, int batchType) {
        int rowAffected = 0;
        if (batchType == BatchTypeEnum.NORMAL.getValue()) {
            normalBatchService.updateHasMoneny(batchCode, false, "sys");
            rowAffected = normalBatchMongoOpt.update(Criteria.where("batchCode").is(batchCode), "has_money", hasMoney);
        } else if (batchType == BatchTypeEnum.ADJUST.getValue()) {
            adjustBatchService.updateHasMoneny(batchCode, false, "sys");
            rowAffected = adjustBatchMongoOpt.update(Criteria.where("batchCode").is(batchCode), "has_money", hasMoney);
        } else {
            backTrackingBatchService.updateHasMoneny(batchCode, false, "sys");
            rowAffected = backTraceBatchMongoOpt.update(Criteria.where("batchCode").is(batchCode), "has_money", hasMoney);
        }
        return rowAffected;
    }

    @Override
    public List<String> getBatchListByManagementId(String managementId) {
        if (StringUtils.isEmpty(managementId)) {
            return null;
        }
        return normalBatchService.getAllBatchIdsByManagementId(managementId);
    }

}
