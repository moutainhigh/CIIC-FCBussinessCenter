package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.po.EmployeeExtensionPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeeTestPO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by jiangtianning on 2017/10/31.
 */
public interface EmployeeService {
    /**
     * 新增雇员
     * @param employeePO 雇员实体
     * @return 是否新增成功
     */
    Integer addEmployee(PrEmployeePO employeePO);
    /**
     * 添加雇员组雇员
     * @param employeeTestPOS 雇员主表数据
     * @return 是否增加成功
     */
    Boolean addEmployees(List<PrEmployeeTestPO> employeeTestPOS, String empGroupCode);

    /**
     * 获取雇员组雇员列表
     * @param empGroupCode 雇员组Code
     * @return 雇员组雇员列表
     */
    PageInfo<EmployeeExtensionPO> getEmployees(String empGroupCode, Integer pageNum, Integer pageSize);


    /**
     * 批量删除
     * @param ids 雇员和雇员组关系ID
     * @return
     */
    Integer batchDelete(List<String> ids, List<String> employeeIds, String empGroupCode);
}