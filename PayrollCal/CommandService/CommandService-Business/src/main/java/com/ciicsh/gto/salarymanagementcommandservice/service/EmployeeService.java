package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.po.EmployeeExtensionPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeePO;
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
     *
     * @param empIds
     * @param empGroupCode
     * @return
     */
    Boolean addEmployees(List<String> empIds, String empGroupCode);

    /**
     * 获取雇员组雇员列表
     * @param empGroupCode 雇员组Code
     * @return 雇员组雇员列表
     */
    PageInfo<PrEmployeePO> getEmployees(String empGroupCode, Integer pageNum, Integer pageSize);


    List<PrEmployeePO> getAllEmployees(String empGroupCode);


    /**
     * 批量删除
     * @param
     * @return
     */
    Integer batchDelete(List<String> employeeIds, String empGroupCode);

    /**
     * 检查雇员在该雇员组是否存在
     * @param empGroupCode
     * @return
     */
    int hasEmployees(String empGroupCode);

    /**
     * 更新插入本地雇员表雇员
     * @param employeePO
     * @return
     */
    int upsertEmployee(PrEmployeePO employeePO);

}