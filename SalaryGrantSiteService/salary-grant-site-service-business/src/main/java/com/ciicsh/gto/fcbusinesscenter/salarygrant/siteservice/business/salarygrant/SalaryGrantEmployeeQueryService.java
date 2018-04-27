package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;

import java.util.List;

/**
 * <p>
 * 薪资发放雇员信息查询 服务类
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-04-23
 */
public interface SalaryGrantEmployeeQueryService extends IService<SalaryGrantEmployeePO> {

    /**
     * 查询雇员信息
     * 根据taskType判断查询主表或者子表的雇员信息
     *
     * @param taskType 1-查询主表的雇员信息;2-查询子表的雇员信息
     * @param page
     * @param salaryGrantEmployeeBO
     * @return
     */
    Page<SalaryGrantEmployeeBO> queryEmployeeTask(int taskType, Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO);

    /**
     * 查询主表的雇员信息
     * 主要根据主表任务单编号查询
     * @param page
     * @param salaryGrantEmployeeBO
     * @return Page<SalaryGrantEmployeeBO>
     */
    Page<SalaryGrantEmployeeBO> queryEmployeeForMainTask(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO);

    /**
     * 查询子表的雇员信息
     * 主要根据子表任务单编号查询
     * @param page
     * @param salaryGrantEmployeeBO
     * @return Page<SalaryGrantEmployeeBO>
     */
    Page<SalaryGrantEmployeeBO> queryEmployeeForSubTask(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO);

    /**
     * 调整信息-对于发放类型是调整/回溯发放。
     * 查询调整/回溯批次、基于的计算批次，查询2条计算结果数据，再进行数据合并生成第三条合并数据。（调用计算批次接口，查询第3个方法）
     * @param salaryGrantEmployeePO
     * @return List
     */
    List listAdjustCalcInfo(SalaryGrantEmployeePO salaryGrantEmployeePO);
}
