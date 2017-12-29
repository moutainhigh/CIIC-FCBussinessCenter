package com.ciicsh.gto.fcsupportcenter.tax.commandservice.business;


import com.ciicsh.gto.fcsupportcenter.tax.entity.request.data.RequestForCalBatch;
import com.ciicsh.gto.fcsupportcenter.tax.entity.response.data.ResponseForCalBatch;

/**
 * @author wuhua
 */
public interface CalculationBatchService {

    /**
     * 条件计算批次主信息
     * @param
     * @return
     */
    ResponseForCalBatch queryCalculationBatchs(RequestForCalBatch requestForCalBatch);


}

