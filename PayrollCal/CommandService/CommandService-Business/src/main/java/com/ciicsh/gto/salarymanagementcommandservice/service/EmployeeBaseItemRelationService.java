package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeeBaseItemRelationPO;

/**
 * Created by houwanhua on 2017/12/11.
 */
public interface EmployeeBaseItemRelationService {

    /**
     * 新增雇员全局基类关系
     * @param employeeBaseItemRelationPO
     * @return 是否新增成功
     */
    Integer addEmployeeBaseItemRelation(PrEmployeeBaseItemRelationPO employeeBaseItemRelationPO);
}
