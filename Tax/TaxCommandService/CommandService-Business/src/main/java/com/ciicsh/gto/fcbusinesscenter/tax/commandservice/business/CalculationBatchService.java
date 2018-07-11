package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForCalBatch;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForEmployees;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatch;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatchDetail;

import java.util.List;

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

    /**
     * 查询批次明细
     * @param requestForEmployees
     * @return
     */
    public ResponseForCalBatchDetail queryCalculationBatchDetails(RequestForEmployees requestForEmployees);

    /**
     * 创建任务
     * @param requestForMainTask
     * @return
     */
    public void createMainTask(RequestForTaskMain requestForMainTask);

    /**
     * 根据批次号查询批次信息
     * @param batchNo
     * @return
     */
    CalculationBatchPO queryCalculationBatchPOByBatchNo(String batchNo);

    /**
     * 根据批次号查询批次信息集合
     * @param batchNos
     * @return
     */
    List<CalculationBatchPO> queryCalculationBatchPOByBatchNos(String[] batchNos);
}

