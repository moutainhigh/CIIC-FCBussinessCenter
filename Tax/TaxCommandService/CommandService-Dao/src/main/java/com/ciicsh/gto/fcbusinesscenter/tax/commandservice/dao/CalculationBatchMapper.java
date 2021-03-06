package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.CalculationBatchDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wuhua
 */
public interface CalculationBatchMapper extends BaseMapper<CalculationBatchPO> {

    /**
     * 查询计算批次列表
     * @param calculationBatchBO
     * @return
     *//*
    List<CalculationBatchBO> queryCalculationBatchsByCondition(CalculationBatchBO calculationBatchBO);*/
    /**
     * 查询计算批次列表
     * @param page
     * @return
     */
//    List<CalculationBatchPO> queryCalculationBatchs(Pagination page,Map<String,String> params);
    /**
     * 查询计算批次明细列表
     * @param page
     * @param params
     * @return
     */
    List<CalculationBatchDetailBO> queryCalculationBatchDetails(Pagination page,Map<String,Object> params);
    /**
     * 查询计算批次明细列表
     * @param batchIds
     * @return
     */
    List<CalculationBatchDetailBO> queryCalculationBatchDetailsByBatchIds(@Param("batchIds")Long[] batchIds);
    /**
     * 根据批次id查询对应的任务集合
     * @param calBatchId
     * @return
     */
    List<TaskMainPO> queryTaskMainsByCalBatch(@Param("calBatchId")Long calBatchId);
    /**
     * 根据批次id删除明细信息
     * @param calBatchId
     * @return
     */
    void cancelDetailsByCalBatchId(@Param("calBatchId")Long calBatchId);
    /**
     * 根据批次id删除个税信息
     * @param calBatchId
     * @return
     */
    void cancelInfosByCalBatchId(@Param("calBatchId")Long calBatchId);
    /**
     * 根据批次id删除服务信息
     * @param calBatchId
     * @return
     */
    void cancelServicesByCalBatchId(@Param("calBatchId")Long calBatchId);

    /**
     * 根据主任务ID集合查询对应批次ID集合
     * @param batchIds
     * @return
     */
    List<CalculationBatchPO> queryBatchIdsByTaskMainIds(@Param("batchIds")List<Long> batchIds);

}