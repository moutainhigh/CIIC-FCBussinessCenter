package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagementcommandservice.api.BatchProxy;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.AdvanceBatchDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.Custom.BatchAuditDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.MoneyBatchDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrBatchDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrNormalBatchDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.page.Pagination;
import com.ciicsh.gto.salarymanagement.entity.dto.BatchCompareRequestDTO;
import com.ciicsh.gto.salarymanagement.entity.dto.BatchPayrollSchemaDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrAdjustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrBackTrackingBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.BatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrAdjustBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrBackTrackingBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrNormalBatchService;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.mongodb.DBObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @Autowired
    private BatchService batchService;

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
    public Pagination<PrNormalBatchDTO> getBatchListByManagementId(@RequestParam(value = "managementId", required = false) String managementId,
                                                                   @RequestParam(value = "batchCode", required = false) String batchCode,
                                                                   @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                                   @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        /*if (StringUtils.isEmpty(managementId)) {
            return null;
        }*/
        PageInfo<PrNormalBatchPO> pageInfo = normalBatchService.getAllBatchesByManagementId(managementId, batchCode, pageNum, pageSize);
        List<PrNormalBatchDTO> resultList = JSON.parseObject(JSON.toJSONString(pageInfo.getList())
                , new TypeReference<List<PrNormalBatchDTO>>(){});
        Pagination<PrNormalBatchDTO> resultPage = new Pagination<>();
        BeanUtils.copyProperties(pageInfo, resultPage, "list");
        resultPage.setList(resultList);

        return resultPage;
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
        } catch (Exception ex) {
            result.setSuccess(false);
        }

        return result;
    }

    @PostMapping("/compareBatch")
    public JsonResult compareBatch(@RequestBody BatchCompareRequestDTO obj) {

        //TODO
//        ObjectMapper
        batchService.compareBatch(obj.getSrc(), obj.getSrcBatchType()
                , obj.getTgt(), obj.getTgtBatchType()
                , Arrays.asList(obj.getCompareKeysStr().split(","))
                , obj.getMapping());

        return null;
    }

    @GetMapping("/batchPayrollSchema")
    public JsonResult getBatchPayrollSchemas(@RequestParam String batchCodesStr) {

        String[] batchCodes = batchCodesStr.split(",");
        List<BatchPayrollSchemaDTO> batchPayrollSchemaDTOList = new ArrayList<>();
        Arrays.stream(batchCodes).map(String::trim).forEach(i -> {
            List<PrPayrollItemPO> items = normalBatchService.getBatchPayrollSchema(i);
            BatchPayrollSchemaDTO batchPayrollSchemaDTO = new BatchPayrollSchemaDTO();
            batchPayrollSchemaDTO.setBatchCode(i);
            List<BatchPayrollSchemaDTO.PayItemCodeNameObj> itemList = new ArrayList<>();
            if (items != null) {
                items.forEach(j -> {
                    BatchPayrollSchemaDTO.PayItemCodeNameObj itemCodeNameObj
                            = batchPayrollSchemaDTO.new PayItemCodeNameObj(j.getItemCode(), j.getItemName());
                    itemList.add(itemCodeNameObj);
                });
                batchPayrollSchemaDTO.setItemList(itemList);
            }
            batchPayrollSchemaDTOList.add(batchPayrollSchemaDTO);
        });

        return JsonResult.success(batchPayrollSchemaDTOList);
    }

}
