package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupPO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by jiangtianning on 2017/10/25.
 */
public interface EmployeeGroupService{

    /**
     * 获取雇员组名称列表
     * @param managementId 管理方ID
     * @return 雇员组名称列表
     */
    List<KeyValuePO> getEmployeeGroupNames(String managementId);

    /**
     * 新增雇员组
     * @param prEmpGroupPO 雇员组实体
     * @return 是否新增成功
     */
    Integer addEmployeeGroup(PrEmpGroupPO prEmpGroupPO);


    /**
     * 删除雇员组
     * @param id 雇员组ID
     * @return 是否删除成功
     */
    Integer deleteEmployeeGroup(Integer id);

    /**
     * 获取雇员组
     * @param id 雇员组ID
     * @param empGroupCode 雇员组Code
     * @param empGroupName 雇员组名称
     * @return 雇员组
     */
    PrEmpGroupPO getEmployeeGroup(Integer id,String empGroupCode,String empGroupName);


    /**
     * 获取雇员组列表
     * @param prEmpGroupPO 雇员组实体
     * @param pageNum 页码
     * @return 雇员组列表
     */
    PageInfo<PrEmpGroupPO> getEmployeeGroups(PrEmpGroupPO prEmpGroupPO, Integer pageNum,Integer pageSize);


    /**
     * 修改雇员组
     * @param prEmpGroupPO 雇员组实体
     * @return 是否修改成功
     */
    Integer editEmployeeGroup(PrEmpGroupPO prEmpGroupPO);

    /**
     * 批量删除
     * @param ids 雇员组ID集合
     * @return
     */
    Boolean batchDelete(List<String> ids);
}
