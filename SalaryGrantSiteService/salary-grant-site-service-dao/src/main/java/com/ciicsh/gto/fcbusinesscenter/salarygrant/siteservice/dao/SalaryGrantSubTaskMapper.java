package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.FinanceTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskPaymentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
  * 薪资发放任务单子表 Mapper 接口
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-04-24
 */
@Component
public interface SalaryGrantSubTaskMapper extends BaseMapper<SalaryGrantSubTaskPO> {
    /**
     * 查询供应商任务单列表
     * 待提交：0-草稿 角色=操作员
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    List<SalaryGrantTaskBO> querySupplierSubTaskForSubmitPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询供应商任务单列表
     * 待审批:1-审批中 角色=审核员
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    List<SalaryGrantTaskBO> querySupplierSubTaskForApprovePage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询供应商任务单列表
     * 已处理:1-审批中 角色=操作员
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    List<SalaryGrantTaskBO> querySupplierSubTaskForHaveApprovedPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询供应商任务单列表
     * 已处理:审批通过 2-审批通过、10-待合并、11-已合并 角色=操作员、审核员
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    List<SalaryGrantTaskBO> querySupplierSubTaskForPassPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询薪资发放报盘任务单列表
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    List<SalaryGrantTaskBO> queryOfferDocumentTaskPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询薪资发放日任务单数据
     *
     * @param grantDate 薪资发放次日日期
     * @return
     */
    List<SalaryGrantTaskPaymentBO> queryWaitForPaymentTaskList(String grantDate);

    /**
     * 根据主表任务单编号查询子表任务单
     * @author chenpb
     * @since 2018-04-23
     * @param bo
     * @return
     */
    List<SalaryGrantTaskBO> subTaskList (SalaryGrantTaskBO bo);

    /**
     * 任务单编号查询任务单
     * @author chenpb
     * @since 2018-04-25
     * @param bo
     * @return
     */
    SalaryGrantTaskBO selectTaskByTaskCode(SalaryGrantTaskBO bo);

    /**
     * 根据任务单编号列表查询任务单一览
     * @author chenpb
     * @since 2018-06-06
     * @param taskCodes
     * @return
     */
    List<SalaryGrantSubTaskPO> selectListByTaskCodes (@Param("taskCodes") String taskCodes);

    /**
     * 查询财务数据
     * @author chenpb
     * @since 2018-05-25
     * @param taskCode
     * @return
     */
    FinanceTaskBO selectTaskForFinance(@Param("taskCode") String taskCode);

    /**
     * 同步结算中心薪资发放信息
     * @author chenpb
     * @since 2018-06-05
     * @param taskCodes
     * @param taskStatus
     * @return
     */
    Integer syncTaskInfo(@Param("taskCodes") String taskCodes, @Param("taskStatus") String taskStatus);

}