package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
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
    /**
     * 待提交
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param bo
     * @return
     */
    List<SalaryGrantTaskBO> submitList(Pagination page, SalaryGrantTaskBO bo);
    /**
     * 待审批
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param bo
     * @return
     */
    List<SalaryGrantTaskBO> approveList(Pagination page, SalaryGrantTaskBO bo);
    /**
     * 已处理任务单:1-审批中
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param bo
     * @return
     */
    List<SalaryGrantTaskBO> haveApprovedList(Pagination page, SalaryGrantTaskBO bo);
    /**
     * 已处理任务单:审批通过 2-审批通过、6-未支付、7-已支付 角色=操作员、审核员
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param bo
     * @return
     */
    List<SalaryGrantTaskBO> passList(Pagination page, SalaryGrantTaskBO bo);

    /**
     * 任务单编号查询任务单
     * @author chenpb
     * @since 2018-04-25
     * @param bo
     * @return
     */
    SalaryGrantTaskBO selectTaskByTaskCode(SalaryGrantTaskBO bo);

}