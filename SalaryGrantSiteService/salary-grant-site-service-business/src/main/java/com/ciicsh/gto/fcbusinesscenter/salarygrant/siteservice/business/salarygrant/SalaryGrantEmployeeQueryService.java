package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.CalcResultItemBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.EmpCalcResultBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;

import java.util.List;
import java.util.Map;

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
     * @param page
     * @param salaryGrantEmployeeBO
     * @return
     */
    Page<SalaryGrantEmployeeBO> queryEmployeeTask(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO);

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
     * 查询发放变化的雇员信息
     *
     * @param page
     * @param salaryGrantEmployeeBO
     * @return Page<SalaryGrantEmployeeBO>
     */
    Page<SalaryGrantEmployeeBO> queryEmployeeInfoChanged(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO);

    /**
     * 调整信息-对于发放类型是调整/回溯发放。
     * 查询调整/回溯批次、基于的计算批次，查询2条计算结果数据，再进行数据合并生成第三条合并数据。（调用计算批次接口，查询第3个方法）
     * @param salaryGrantEmployeePO
     * @return List
     */
    List listAdjustCalcInfo(SalaryGrantEmployeePO salaryGrantEmployeePO);

    /**
     * 查询雇员薪资项列表
     *
     * @param batchParam
     * @return
     */
    List<CalcResultItemBO> getSalaryCalcResultItemsList(Map batchParam);

    /**
     * 查询计算批次结果的雇员信息数据
     *
     * @param batchCode 计算批次号
     * @param grantType 发放类型
     * @return
     */
    List<EmpCalcResultBO> getEmpCalcResultItemsList(String batchCode, int grantType);

    /**
     * 发放金额更新到计算批次的对应薪资项中
     *
     * @param checkedItemsList 选定薪资项列表
     * @param salaryGrantEmployeeBO
     * @return
     */
    List<EmpCalcResultBO> getEmployeeForBizList(List<CalcResultItemBO> checkedItemsList, SalaryGrantEmployeeBO salaryGrantEmployeeBO);

    /**
     * 查询任务单对应雇员历史数据
     *
     * @param task_his_id
     * @return
     */
    Page<SalaryGrantEmployeeBO> queryEmpHisInfo(long task_his_id, Integer pageNum, Integer pageSize);
}
