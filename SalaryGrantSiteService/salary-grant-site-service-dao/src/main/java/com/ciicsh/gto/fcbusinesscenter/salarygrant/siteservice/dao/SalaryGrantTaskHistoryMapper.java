package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantTaskHistoryPO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
  * 薪资发放任务单历史表 Mapper 接口
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-06
 */
@Component
public interface SalaryGrantTaskHistoryMapper extends BaseMapper<SalaryGrantTaskHistoryPO> {
    /**
     * 查询供应商任务单列表
     * 已处理:审批拒绝 3-审批拒绝、8-撤回 角色=操作员、审核员（查历史表）
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    List<SalaryGrantTaskBO> querySupplierSubTaskForRejectPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 已处理任务单:审批拒绝 3-审批拒绝、8-撤回、9-驳回 角色=操作员、审核员
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param bo
     * @return
     */
    List<SalaryGrantTaskBO> rejectList(Pagination page, SalaryGrantTaskBO bo);
    /**
     * 已处理任务单:4-失效 角色=操作员、审核员
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param bo
     * @return
     */
    List<SalaryGrantTaskBO> invalidList(Pagination page, SalaryGrantTaskBO bo);

    /**
     * 任务单编号查询任务单
     * @author chenpb
     * @since 2018-04-25
     * @param bo
     * @return
     */
    SalaryGrantTaskBO selectTaskByTaskCode(SalaryGrantTaskBO bo);
}