package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantEmployeePO;

/**
 * <p>
 * 薪资发放任务单批量处理 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-06-29
 */
public interface SalaryGrantEmployeeService extends IService<SalaryGrantEmployeePO> {

    /**
     * 查询雇员信息
     * 根据taskType判断查询主表或者子表的雇员信息
     *
     * @param page
     * @param salaryGrantEmployeeBO
     * @return
     */
    Page<SalaryGrantEmployeeBO> queryEmployeeTask(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO);
}
