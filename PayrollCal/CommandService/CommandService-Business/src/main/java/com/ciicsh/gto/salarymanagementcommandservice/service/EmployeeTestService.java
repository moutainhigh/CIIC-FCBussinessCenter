package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeeTestPO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by houwanhua on 2017/12/8.
 */
public interface EmployeeTestService {
    /**
     * 新增雇员
     * @param employeeTestPO 雇员实体
     * @return 是否新增成功
     */
    Integer addEmployeeTest(PrEmployeeTestPO employeeTestPO);

    /**
     * 获取雇员列表
     * @param employeeTestPO 雇员实体
     * @param pageNum 页码
     * @return 雇员列表
     */
    PageInfo<PrEmployeeTestPO> getEmployees(PrEmployeeTestPO employeeTestPO, Integer pageNum, Integer pageSize);

}
