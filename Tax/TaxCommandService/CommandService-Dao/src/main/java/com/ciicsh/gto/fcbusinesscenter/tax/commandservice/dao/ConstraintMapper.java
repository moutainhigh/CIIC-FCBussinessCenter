package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao;

import org.apache.ibatis.annotations.Param;

/**
 * 约束mapper
 * @author wuhua
 */
public interface ConstraintMapper{
    //批次是否为'取消关账'
    int selectNumsForBatch(@Param("batchIds")String[] batchIds);

    //主任务相关的批次是否为'取消关账'
    int selectNumsForTaskMain(@Param("taskIds")String[] taskIds);

    //申报任务相关的批次是否为'取消关账'
    int selectNumsForTaskDeclare(@Param("taskIds")String[] taskIds);

    //划款任务相关的批次是否为'取消关账'
    int selectNumsForTaskMoney(@Param("taskIds")String[] taskIds);

    //缴纳任务相关的批次是否为'取消关账'
    int selectNumsForTaskPayment(@Param("taskIds")String[] taskIds);

    //供应商处理任务相关的批次是否为'取消关账'
    int selectNumsForTaskSupplier(@Param("taskIds")String[] taskIds);

}