package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForCalBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForBatchDetail;

import java.util.List;

/**
 * @author yuantongqing on 2017/12/19
 */
public interface CalculationBatchDetailService {

    /**
     * 查询申报记录信息
     * @param requestForProof
     * @return
     */
    ResponseForBatchDetail queryCalculationBatchDetail(RequestForProof requestForProof);

    /**
     * 查询计算批次明细信息
     * @param
     * @return
     */
//    ResponseForCalBatchDetail queryCalculationBatchDetails(RequestForEmployees requestForEmployees);

    /**
     * 条件查询计算批次明细
     * @param requestForCalBatchDetail
     * @return
     */
    ResponseForCalBatchDetail queryTaxBatchDetailByRes(RequestForCalBatchDetail requestForCalBatchDetail);

    /**
     * 批量恢复计算批次明细
     * @param ids
     */
    void recoveryCalculationBatchDetail(String[] ids);

    /**
     * 批量暂缓计算批次明细
     * @param ids
     */
    void batchPostponeCalBatchDetail(String[] ids);

    /**
     * 判断所选详情是否有取消关账状态的数据是否已经创建任务
     * @param ids
     * @return
     */
    int checkTaskByDetailIds(String[] ids);

    /**
     * 根据批次ID查询批次明细列表
     * @param batchId
     * @return
     */
    List<CalculationBatchDetailPO> queryCalculationBatchDetailByBatchId(Long batchId);

    /**
     * 根据批次ID数组查询批次明细列表(不含暂缓)
     * @param batchIds
     * @return
     */
    List<CalculationBatchDetailPO> queryCalculationBatchDetailByBatchNos(List<Long> batchIds);
}
