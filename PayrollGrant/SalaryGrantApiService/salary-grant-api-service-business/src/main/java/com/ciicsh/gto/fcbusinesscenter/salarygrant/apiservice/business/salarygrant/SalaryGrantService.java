package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant;


import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantTaskPO;

import java.util.List;

/**
 * <p>
 * 薪资发放 服务类
 * </p>
 *
 * @author chenpb
 * @since 2018-02-27
 */
public interface SalaryGrantService extends IService<SalaryGrantTaskPO> {
    /**
     * 根据批次号查相关任务单
     * @author chenpb
     * @date 2018-04-18
     * @param bo
     * @return
     */
    List<SalaryGrantTaskBO> getTask(SalaryGrantTaskBO bo);

    /**
     *  根据主表任务单编号查询薪资发放任务单子表
     * @author gaoyang
     * @date 2018-05-18
     * @param subTaskBO
     * @return List<SalaryGrantSubTaskBO>
     */
    List<SalaryGrantSubTaskBO> getSubTask(SalaryGrantSubTaskBO subTaskBO);

    /**
     * 修改雇员发放状态
     * @author chenpb
     * @date 2018-06-19
     * @param bo
     * @return
     */
    ReprieveEmployeeBO updateForReprieveEmployee(ReprieveEmployeeBO bo);

    /**
     *  根据退票雇员信息创建薪资发放任务单
     *  @author gaoyang
     *  @date 2018-05-23
     * @param employeeRefundList
     * @return Boolean
     */
    ResponseRefundBO toCreateRefundTask(List<SalaryGrantEmployeeRefundBO> employeeRefundList, String batchCode);

    /**
     *  根据任务单信息进行驳回处理
     *  @author gaoyang
     *  @date 2018-05-23
     * @param salaryGrantTaskBO
     * @return Boolean
     */
    Boolean toRejectTask(SalaryGrantTaskBO salaryGrantTaskBO);
}
