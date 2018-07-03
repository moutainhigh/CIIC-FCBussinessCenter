package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.FinanceEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeePaymentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantFinanceBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
  * 薪资发放雇员信息表 Mapper 接口
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-06
 */
@Component
public interface SalaryGrantEmployeeMapper extends BaseMapper<SalaryGrantEmployeePO> {
    /**
     * 查询主表的雇员信息
     * 主要根据主表任务单编号查询
     * @param page
     * @param salaryGrantEmployeeBO
     * @return Page<SalaryGrantEmployeeBO>
     */
    List<SalaryGrantEmployeeBO> selectBOList(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO);

    /**
     * 根据任务单编号查询雇员信息
     *
     * @param taskCode
     * @return
     */
    List<SalaryGrantEmployeePaymentBO> queryWaitForPaymentEmpList(String taskCode);

    /**
     * 根据任务单编号，任务单类型，雇员编号，发放状态暂缓雇员
     * @author chenpb
     * @since 2018-05-23
     * @param bo
     * @return
     */
    Integer deferEmployee(SalaryGrantEmployeeBO bo);

    /**
     * 查询财务数据
     * @author chenpb
     * @since 2018-05-25
     * @param taskCode
     * @return
     */
    List<FinanceEmployeeBO> selectEmpForFinance(@Param("taskCode") String taskCode);

    /**
     * 财务报表数据
     * @author chenpb
     * @since 2018-05-29
     * @param taskCode
     * @return
     */
    SalaryGrantFinanceBO selectFinanceData(@Param("taskCode") String taskCode);

    /**
     * 查询主表的雇员信息
     * 根据主表任务单编号及发放方式查询
     * @param salaryGrantEmployeeBO
     * @return List<SalaryGrantEmployeeBO>
     */
    List<SalaryGrantEmployeeBO> selectEmpInfoByGrantMode(SalaryGrantEmployeeBO salaryGrantEmployeeBO);
}