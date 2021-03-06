package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.EmpGroupOptPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
  * 配置表，雇员组表 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Mapper
@Component
public interface PrEmpGroupMapper extends BaseMapper<PrEmpGroupPO> {
    /**
     * 获取雇员组名称列表
     * @param managementId 管理方ID
     * @return 雇员组名称列表
     */
    List<KeyValuePO> getEmployeeGroupNames(@Param("managementId") String managementId);

    /**
     * 获取雇员组列表
     * @param empGroupPO 雇员组实体
     * @return 雇员组列表
     */
    List<PrEmpGroupPO> getEmployeeGroups(PrEmpGroupPO empGroupPO);


    /**
     * 是否已经存在雇员组
     * @param optPO
     * @return 返回值大于0表示记录已经存在，返回小于或者等于0表示记录不存在
     */
    Integer isExistEmpGroup(EmpGroupOptPO optPO);

    /**
     * 根据雇员组Code查询雇员组
     * @param empGroupCode 雇员组Code
     * @return 返回雇员组
     */
    PrEmpGroupPO selectEmployeeGroupByCode(@Param("empGroupCode") String empGroupCode);
}