package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.BatchProxy;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.AdvanceBatchDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.Custom.BatchAuditDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.MoneyBatchDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrBatchDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrNormalBatchDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrAdjustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrBackTrackingBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrAdjustBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrBackTrackingBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrNormalBatchService;
import com.google.gson.Gson;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Override
    public int updateAdvanceBatch(@RequestBody AdvanceBatchDTO advanceBatchDTO) {
        List<String> batchCodes = Arrays.asList(advanceBatchDTO.getBatchCodes().split(","));
        boolean hasAdvance = advanceBatchDTO.isHasAdvance();
        String modifiedBy = advanceBatchDTO.getModifiedBy();
        int rowAffected = normalBatchService.updateHasAdvance(batchCodes,hasAdvance,modifiedBy);
        return rowAffected;
    }

    @Override
    public int updateHasMoneyBatch(@RequestBody MoneyBatchDTO moneyBatchDTO) {
        List<String> batchCodes = Arrays.asList(moneyBatchDTO.getBatchCodes().split(","));
        boolean hasMoney = moneyBatchDTO.isHasMoney();
        String modifiedBy = moneyBatchDTO.getModifiedBy();
        int rowAffected = normalBatchService.updateHasMoneny(batchCodes,hasMoney,modifiedBy);
        return rowAffected;
    }

    @Override
    public List<PrNormalBatchDTO> getBatchListByManagementId(@RequestParam(value = "managementId", required = false) String managementId) {
        /*if (StringUtils.isEmpty(managementId)) {
            return null;
        }*/
        List<PrNormalBatchPO> batchList = normalBatchService.getAllBatchesByManagementId(managementId);
        List<PrNormalBatchDTO> resultList = JSON.parseObject(JSON.toJSONString(batchList)
                , new TypeReference<List<PrNormalBatchDTO>>(){});
        return resultList;
    }

    @Override
    public PrBatchDTO getBatchInfo(@RequestParam("batchCode") String batchCode, @RequestParam("batchType") int batchType) {
        PrNormalBatchPO normalBatchPO = null;
        PrBatchDTO batchDTO = new PrBatchDTO();

        if (batchType == BatchTypeEnum.NORMAL.getValue()) {
            normalBatchPO = normalBatchService.getBatchByCode(batchCode);
            batchDTO.setHasMoney(normalBatchPO.getHasMoney());
            batchDTO.setHasAdvance(normalBatchPO.getHasAdvance());
            batchDTO.setStatus(normalBatchPO.getStatus());
            batchDTO.setActualPeriod(normalBatchPO.getPeriod());


        } else if (batchType == BatchTypeEnum.ADJUST.getValue()) {
            PrAdjustBatchPO adjustBatchPO = new PrAdjustBatchPO();
            adjustBatchPO.setAdjustBatchCode(batchCode);
            adjustBatchPO = adjustBatchService.getAdjustBatchPO(adjustBatchPO);
            batchDTO.setHasMoney(adjustBatchPO.getHasMoney());
            batchDTO.setHasAdvance(adjustBatchPO.getHasAdvance());
            batchDTO.setStatus(adjustBatchPO.getStatus());
            batchDTO.setPeriod(adjustBatchPO.getPeriod());

            normalBatchPO = normalBatchService.getBatchByCode(adjustBatchPO.getRootBatchCode());
        } else {
            PrBackTrackingBatchPO backTrackingBatchPO = new PrBackTrackingBatchPO();
            backTrackingBatchPO.setBackTrackingBatchCode(batchCode);
            backTrackingBatchPO = backTrackingBatchService.getPrBackTrackingBatchPO(backTrackingBatchPO);
            batchDTO.setHasMoney(backTrackingBatchPO.getHasMoney());
            batchDTO.setHasAdvance(backTrackingBatchPO.getHasAdvance());
            batchDTO.setStatus(backTrackingBatchPO.getStatus());
            batchDTO.setPeriod(backTrackingBatchPO.getPeriod());

            normalBatchPO = normalBatchService.getBatchByCode(backTrackingBatchPO.getRootBatchCode());
        }
        batchDTO.setAccountSetCode(normalBatchPO.getAccountSetCode());
        batchDTO.setCode(normalBatchPO.getCode());
        batchDTO.setManagementId(normalBatchPO.getManagementId());
        batchDTO.setManagementName(normalBatchPO.getManagementName());
        batchDTO.setStartDate(normalBatchPO.getStartDate());
        batchDTO.setEndData(normalBatchPO.getEndDate());

        return batchDTO;
    }

    @Override
    public int updateBatchStatus(@RequestBody BatchAuditDTO batchAuditDTO) {
        int rowAffected = 0;
        if(batchAuditDTO.getBatchType() == BatchTypeEnum.NORMAL.getValue()) {
            rowAffected = normalBatchService.auditBatch(batchAuditDTO.getBatchCode(), batchAuditDTO.getComments(), batchAuditDTO.getStatus(), batchAuditDTO.getModifyBy(), batchAuditDTO.getResult());
        }else if(batchAuditDTO.getBatchType() == BatchTypeEnum.ADJUST.getValue()) {
            rowAffected = adjustBatchService.auditBatch(batchAuditDTO.getBatchCode(), batchAuditDTO.getComments(), batchAuditDTO.getStatus(), batchAuditDTO.getModifyBy(), batchAuditDTO.getResult());
        }else {
            rowAffected = backTrackingBatchService.auditBatch(batchAuditDTO.getBatchCode(), batchAuditDTO.getComments(), batchAuditDTO.getStatus(), batchAuditDTO.getModifyBy(), batchAuditDTO.getResult());
        }
        return rowAffected;
    }

    @Override
    public JsonResult<List<String>> getBatchIdListByManagementId(@RequestParam("mgrId") String mgrId) {
        JsonResult<List<String>> result = new JsonResult<>();

        try {
            List<String> list = normalBatchService.getBatchIdListByManagementId(mgrId);
            result.setData(list);
            result.setSuccess(true);
        }catch (Exception ex){
            result.setSuccess(false);
        }

        return result;
    }

}
