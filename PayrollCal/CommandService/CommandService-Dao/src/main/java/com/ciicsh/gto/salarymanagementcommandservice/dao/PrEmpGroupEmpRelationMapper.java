package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupEmpRelationPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
  * 配置表，雇员组和雇员关系表 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Mapper
@Component
public interface PrEmpGroupEmpRelationMapper extends BaseMapper<PrEmpGroupEmpRelationPO> {
    /**
     * 判断雇员组雇员关系是否已经存在
     * @param empGroupCode 雇员组编号
     * @param employeeId 雇员ID
     * @return 返回值大于0表示记录已经存在，返回小于或者等于0表示记录不存在
     */
    Integer isExistEmpGroupEmpRelation(@Param("empGroupCode") String empGroupCode, @Param("employeeId") String employeeId);

    /**
     * 根据雇员组Code删除雇员组和雇员关系表数据
     * @param empGroupCodes
     * @return
     */
    Integer delByEmpGroupCodes(List<String> empGroupCodes);
}