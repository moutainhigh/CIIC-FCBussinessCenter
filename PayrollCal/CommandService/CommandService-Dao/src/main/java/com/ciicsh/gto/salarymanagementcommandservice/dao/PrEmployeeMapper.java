package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.EmployeeExtensionPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
  * 配置表，雇员个人基本信息表。 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Mapper
@Component
public interface PrEmployeeMapper extends BaseMapper<PrEmployeePO> {

    /**
     * 获取雇员组雇员列表
     * @param empGroupCode 雇员组Code
     * @return 雇员组雇员列表
     */
    List<EmployeeExtensionPO> getEmployees(@Param("empGroupCode") String empGroupCode,@Param("empCode") String empCode,@Param("empName") String empName);

    /**
     * 判断雇员是否已经存在
     * @param empId 雇员ID
     * @return 返回值大于0表示记录已经存在，返回小于或者等于0表示记录不存在
     */
    Integer isExistEmployee(@Param("empId") String empId);

}