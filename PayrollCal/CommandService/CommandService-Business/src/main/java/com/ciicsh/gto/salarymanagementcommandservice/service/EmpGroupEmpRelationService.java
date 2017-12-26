package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupEmpRelationPO;

import java.util.List;

/**
 * Created by houwanhua on 2017/12/11.
 */
public interface EmpGroupEmpRelationService {
    /**
     * 新增雇员组雇员关系
     * @param empGroupEmpRelationPO 雇员组雇员关系实体
     * @return 是否新增成功
     */
    Integer addEmpGroupEmpRelation(PrEmpGroupEmpRelationPO empGroupEmpRelationPO);

    /**
     * 判断雇员组雇员关系是否已经存在
     * @param empGroupCode 雇员组Code
     * @param empId 雇员ID
     * @return 返回值大于0表示记录已经存在，返回小于或者等于0表示记录不存在
     */
    Integer isExistEmpGroupEmpRelation(String empGroupCode, String empId);
}
