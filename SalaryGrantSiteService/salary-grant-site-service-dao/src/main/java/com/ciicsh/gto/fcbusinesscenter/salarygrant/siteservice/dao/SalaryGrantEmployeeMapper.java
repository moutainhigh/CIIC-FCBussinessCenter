package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
  * 薪资发放雇员信息表 Mapper 接口
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-06
 */
@Component
public interface SalaryGrantEmployeeMapper extends BaseMapper<SalaryGrantEmployeePO> {
    /**
     * 查询主表的雇员信息
     * 主要根据主表任务单编号查询
     * @param page
     * @param salaryGrantEmployeeBO
     * @return Page<SalaryGrantEmployeeBO>
     */
    List<SalaryGrantEmployeeBO> selectBOList(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO);
}