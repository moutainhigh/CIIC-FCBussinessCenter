package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForCalBatch;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatch;

/**
 * @author wuhua
 */
public interface CalculationBatchService {

    /**
     * 查询计算批次主信息
     * @param
     * @return
     */
    ResponseForCalBatch queryCalculationBatchs(RequestForCalBatch requestForCalBatch);


}

