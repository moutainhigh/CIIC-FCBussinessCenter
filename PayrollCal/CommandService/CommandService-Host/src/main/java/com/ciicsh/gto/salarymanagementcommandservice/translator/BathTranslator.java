package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.Custom.PrCustomBatchDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrEmpGroupDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.PeriodTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.util.BatchUtils;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

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
