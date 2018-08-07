package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.salarymanagement.entity.po.PrCompareBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.Custom.PrCustomBatchDTO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrCompareBatchDTO;

/**
 * Created by bill on 17/12/11.
 */
public class BathTranslator {

    public static PrCustBatchPO toPrBatchPO(PrCustomBatchDTO PrCustomBatchDTO) {
        PrCustBatchPO custBatchPO = new PrCustBatchPO();

        custBatchPO.setAccountSetName(PrCustomBatchDTO.getAccountSetName());
        custBatchPO.setCode(PrCustomBatchDTO.getCode());
        custBatchPO.setEmpGroupName(PrCustomBatchDTO.getEmpGroupName());
        custBatchPO.setStatus(PrCustomBatchDTO.getStatus());
        custBatchPO.setPeriod(PrCustomBatchDTO.getPeriod());
        //custBatchPO.setManagementName(PrCustomBatchDTO.getManagementName());
        custBatchPO.setManagementId(PrCustomBatchDTO.getManagementId());

        return custBatchPO;
    }

    public static PrCompareBatchPO toPrCompareBatchPO(PrCompareBatchDTO prCompareBatchDTO) {
        PrCompareBatchPO prCompareBatchPO = new PrCompareBatchPO();
        prCompareBatchPO.setCode(prCompareBatchDTO.getCode());
        prCompareBatchPO.setPeriod(prCompareBatchDTO.getPeriod());
        prCompareBatchPO.setAccountSetCode(prCompareBatchDTO.getAccountSetCode());
        prCompareBatchPO.setAccountSetName(prCompareBatchDTO.getAccountSetName());
        prCompareBatchPO.setManagementId(prCompareBatchDTO.getManagementId());
        prCompareBatchPO.setManagementName(prCompareBatchDTO.getManagementName());
        prCompareBatchPO.setBeginDate(prCompareBatchDTO.getBeginDate());
        prCompareBatchPO.setEndDate(prCompareBatchDTO.getEndDate());
        prCompareBatchPO.setStatus(prCompareBatchDTO.getStatus());
        prCompareBatchPO.setBatchType(prCompareBatchDTO.getBatchType());
        prCompareBatchPO.setPageNum(prCompareBatchDTO.getPageNum());
        prCompareBatchPO.setPageSize(prCompareBatchDTO.getPageSize());
        return prCompareBatchPO;
    }
}
