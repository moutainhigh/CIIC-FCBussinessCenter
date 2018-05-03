package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.salarymanagementcommandservice.api.dto.Custom.PrCustomBatchDTO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;

/**
 * Created by bill on 17/12/11.
 */
public class BathTranslator {

    public static PrCustBatchPO toPrBatchPO(PrCustomBatchDTO PrCustomBatchDTO){
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
}
