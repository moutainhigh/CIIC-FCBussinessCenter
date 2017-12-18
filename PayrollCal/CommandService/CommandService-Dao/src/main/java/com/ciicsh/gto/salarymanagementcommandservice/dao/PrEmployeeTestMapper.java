package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeeTestPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
  * 雇员主数据册数 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-08
 */
@Mapper
@Component
public interface PrEmployeeTestMapper extends BaseMapper<PrEmployeeTestPO> {

    /**
     * 获取雇员列表
     * @param employeeTestPO 雇员实体
     * @return 雇员列表
     */
    List<PrEmployeeTestPO> getEmployees(PrEmployeeTestPO employeeTestPO);

}