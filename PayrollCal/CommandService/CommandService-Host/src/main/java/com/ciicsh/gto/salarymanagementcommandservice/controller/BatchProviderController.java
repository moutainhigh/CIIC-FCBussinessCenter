package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.bo.BatchCompareEmpBO;
import com.ciicsh.gto.salarymanagement.entity.po.*;
import com.ciicsh.gto.salarymanagementcommandservice.api.BatchProxy;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.*;
import com.ciicsh.gto.salarymanagementcommandservice.api.page.Pagination;
import com.ciicsh.gto.salarymanagement.entity.dto.BatchCompareRequestDTO;
import com.ciicsh.gto.salarymanagement.entity.dto.BatchPayrollSchemaDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagementcommandservice.service.BatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrAdjustBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrBackTrackingBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrNormalBatchService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
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

    @Override
    public int updateAdvanceBatch(@RequestBody AdvanceBatchDTO advanceBatchDTO) {
        List<String> batchCodes = Arrays.asList(advanceBatchDTO.getBatchCodes().split(","));
        int advance = advanceBatchDTO.getAdvance();
        String modifiedBy = advanceBatchDTO.getModifiedBy();
        int rowAffected = 0;
        if(advanceBatchDTO.getBatchType() == BatchTypeEnum.NORMAL.getValue()) {
            rowAffected = normalBatchService.updateHasAdvance(batchCodes, advance, modifiedBy);
        }else if(advanceBatchDTO.getBatchType() == BatchTypeEnum.ADJUST.getValue()){
            rowAffected = adjustBatchService.updateHasAdvance(batchCodes, advance, modifiedBy);
        }else if(advanceBatchDTO.getBatchType() == BatchTypeEnum.BACK.getValue()){
            rowAffected = backTrackingBatchService.updateHasAdvance(batchCodes, advance, modifiedBy);
        }
        return rowAffected;
    }

    @Override
    public int updateHasMoneyBatch(@RequestBody MoneyBatchDTO moneyBatchDTO) {
        List<String> batchCodes = Arrays.asList(moneyBatchDTO.getBatchCodes().split(","));
        boolean hasMoney = moneyBatchDTO.isHasMoney();
        String modifiedBy = moneyBatchDTO.getModifiedBy();
        int rowAffected = 0;
        if(moneyBatchDTO.getBatchType() == BatchTypeEnum.NORMAL.getValue()) {
            rowAffected = normalBatchService.updateHasMoney(batchCodes, hasMoney, modifiedBy);
        }else  if(moneyBatchDTO.getBatchType() == BatchTypeEnum.ADJUST.getValue()) {
            rowAffected = adjustBatchService.updateHasMoney(batchCodes, hasMoney, modifiedBy);
        }else  if(moneyBatchDTO.getBatchType() == BatchTypeEnum.BACK.getValue()) {
            rowAffected = backTrackingBatchService.updateHasMoney(batchCodes, hasMoney, modifiedBy);
        }
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
            batchDTO.setActualPeriod(normalBatchPO.getActualPeriod());

        } else if (batchType == BatchTypeEnum.ADJUST.getValue()) {

            PrAdjustBatchPO adjustBatchPO = adjustBatchService.getAdjustBatchPO(batchCode);
            batchDTO.setHasMoney(adjustBatchPO.getHasMoney());
            batchDTO.setHasAdvance(adjustBatchPO.getHasAdvance());
            batchDTO.setStatus(adjustBatchPO.getStatus());
            batchDTO.setPeriod(adjustBatchPO.getPeriod());

            normalBatchPO = normalBatchService.getBatchByCode(adjustBatchPO.getRootBatchCode());
        } else {

            PrBackTrackingBatchPO backTrackingBatchPO = backTrackingBatchService.getPrBackTrackingBatchPO(batchCode);
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
            rowAffected = normalBatchService.auditBatch(batchAuditDTO.getBatchCode(), batchAuditDTO.getComments(), batchAuditDTO.getStatus(), batchAuditDTO.getModifyBy(), batchAuditDTO.getAdvancePeriod(), batchAuditDTO.getResult());
        }else if(batchAuditDTO.getBatchType() == BatchTypeEnum.ADJUST.getValue()) {
            rowAffected = adjustBatchService.auditBatch(batchAuditDTO.getBatchCode(), batchAuditDTO.getComments(), batchAuditDTO.getStatus(), batchAuditDTO.getModifyBy(),batchAuditDTO.getAdvancePeriod(), batchAuditDTO.getResult());
        }else {
            rowAffected = backTrackingBatchService.auditBatch(batchAuditDTO.getBatchCode(), batchAuditDTO.getComments(), batchAuditDTO.getStatus(), batchAuditDTO.getModifyBy(), batchAuditDTO.getAdvancePeriod(), batchAuditDTO.getResult());
        }
        return rowAffected;
    }


    @Override
    public int updateBatchListStatus(@RequestBody List<BatchAuditDTO> batchAuditDTOs) {
        int rowAffected = 0;
        for (BatchAuditDTO batchAuditDTO: batchAuditDTOs) {
            rowAffected += updateBatchStatus(batchAuditDTO);
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

    @Override
    public int updateAdvancedBatch(@RequestBody AdvanceBatchInfoDTO advanceBatchInfoDTO) {

        AdvanceBatchInfoPO advanceBatchInfoPO = new AdvanceBatchInfoPO();
        advanceBatchInfoPO.setAdvancePeriod(advanceBatchInfoDTO.getAdvancePeriod());
        advanceBatchInfoPO.setBatchType(advanceBatchInfoDTO.getBatchType());
        advanceBatchInfoPO.setCode(advanceBatchInfoDTO.getCode());
        advanceBatchInfoPO.setHasAdvance(advanceBatchInfoDTO.getHasAdvance());
        advanceBatchInfoPO.setModifyBy(!StringUtils.isNotEmpty(advanceBatchInfoDTO.getModifyBy())? "system" : advanceBatchInfoDTO.getModifyBy()) ;
        return batchService.updateAdvancedBatch(advanceBatchInfoPO);
    }

    @PostMapping("/compareBatch")
    public JsonResult compareBatch(@RequestBody BatchCompareRequestDTO obj) {

        //TODO Excel对比
//        ObjectMapper
        List<BatchCompareEmpBO> batchCompareEmpBOList = batchService.compareBatch(obj.getSrc(), obj.getSrcBatchType()
                , obj.getTgt(), obj.getTgtBatchType()
                , Arrays.asList(obj.getCompareKeysStr().split(","))
                , obj.getMapping());

        return JsonResult.success(batchCompareEmpBOList);
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
