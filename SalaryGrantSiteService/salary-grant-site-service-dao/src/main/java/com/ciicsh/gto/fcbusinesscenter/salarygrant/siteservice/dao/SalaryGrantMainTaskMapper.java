package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantMainTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
  * 薪资发放任务单主表 Mapper 接口
 * </p>
 *
 * @author gaoyang
 * @since 2018-01-22
 */
@Component
public interface SalaryGrantMainTaskMapper extends BaseMapper<SalaryGrantMainTaskPO> {
    List<SalaryGrantTaskBO> submitList(Pagination page, SalaryGrantTaskBO bo);
    List<SalaryGrantTaskBO> approveList(Pagination page, SalaryGrantTaskBO bo);
    List<SalaryGrantTaskBO> haveApprovedList(Pagination page, SalaryGrantTaskBO bo);
    List<SalaryGrantTaskBO> passList(Pagination page, SalaryGrantTaskBO bo);
    List<SalaryGrantTaskBO> rejectList(Pagination page, SalaryGrantTaskBO bo);
    List<SalaryGrantTaskBO> invalidList(Pagination page, SalaryGrantTaskBO bo);

    List<SalaryGrantMainTaskBO> querySalaryGrantMainTaskList(Pagination page, SalaryGrantMainTaskBO bo);

}