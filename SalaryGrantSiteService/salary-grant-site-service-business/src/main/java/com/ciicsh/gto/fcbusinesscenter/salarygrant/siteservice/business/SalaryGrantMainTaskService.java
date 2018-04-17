package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.PayrollBatchDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantMainTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 薪资发放任务单主表 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-01-22
 */
public interface SalaryGrantMainTaskService extends IService<SalaryGrantMainTaskPO> {

    /**
     *  根据计算批次编号判断薪资发放任务单主表是否已存在
     * @param batchParam
     * @return Boolean
     */
    Boolean isExistSalaryGrantMainTask(Map batchParam);

    // 1、接收计算引擎消息，获取计算批次号，根据计算批次号查询批次信息、批次计算结果数据及雇员服务协议信息。（调用计算批次接口--数据表结构待定）
    // 后台处理薪资发放任务单入口方法，调用多个子方法包括（获取批次业务表信息、生成entity_id、调用工作流引擎回写任务单工作流的流程编号、创建薪资发放任务单、创建薪资发放雇员信息表-带入任务单编号entity_id）
    /**
     *  调用工作流引擎，获取工作流的流程编号。后台开始处理薪资发放任务单入口方法。内部调用listPayrollCalcResult方法
     * @param batchParam 计算批次号batchCode
     * @return
     */
    void createSalaryGrantMainTask(Map batchParam);

    /**
     *  当批次已进入业务处理，其他模块可能会对批次要求进行重算或作废处理，薪资发放模块要对批次任务做相应的处理。
     * @param batchParam 计算批次号batchCode
     * @return
     */
    void modifySalaryGrantMainTask(Map batchParam);

    /**
     *  根据计算批次号查询批次业务表的批次数据信息，包括计算结果数据及雇员服务协议信息。
     * @param batchParam 计算批次号batchCode
     * @return List 对应批次的雇员信息列表
     */
    List listPayrollCalcResult(Map batchParam);

    /**
     *  修改薪资发放雇员信息（发放状态-暂缓、是否有效）
     * @param salaryGrantEmployeePO
     * @return Integer
     */
    Integer updateSalaryGrantEmployee(SalaryGrantEmployeePO salaryGrantEmployeePO);

    /**
     *  查询计算批次：批次状态（调用计算批次接口）
     * @param paramMap
     * @return PayrollBatchDTO
     */
    PayrollBatchDTO getBatchInfo(Map paramMap);

	/**
	 * 查询薪资发放任务单主表列表
     * 根据查询条件及任务单状态，待提交列表查询草稿状态的任务单；
     * 已处理-审批中列表查询审批中状态的任务单；
     * 已处理-审批通过列表查询审批通过、待支付、未支付、已支付状态的任务单；
     * 已处理-审批拒绝列表查询审批拒绝状态的任务单，查询历史表；
     * 已处理-审批失效列表查询失效状态的任务单，查询历史表。
	 * @param page
	 * @param salaryGrantMainTaskBO
	 * @return Page<SalaryGrantMainTaskBO>
	 */
	Page<SalaryGrantMainTaskBO> querySalaryGrantMainTaskPage(Page<SalaryGrantMainTaskBO> page, SalaryGrantMainTaskBO salaryGrantMainTaskBO);

    /**
     * 查询薪资发放任务单列表
     * 待提交：0-草稿 角色=操作员
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> queryTaskForSubmitPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询薪资发放任务单列表
     * 待审批:1-审批中 角色=审核员
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> queryTaskForApprovePage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询薪资发放任务单列表
     * 已处理:1-审批中 角色=操作员
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> queryTaskForHaveApprovedPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询薪资发放任务单列表
     * 已处理:审批通过 2-审批通过、6-未支付、7-已支付 角色=操作员、审核员
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> queryTaskForPassPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询薪资发放任务单列表
     * 已处理:审批拒绝 3-审批拒绝、8-撤回、9-驳回 角色=操作员、审核员（查历史表）
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> queryTaskForRejectPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询薪资发放任务单列表
     * 已处理:4-失效 角色=操作员、审核员（查历史表）
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> queryTaskForInvalidPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询主表的雇员信息
     * 主要根据主表任务单编号查询
     * @param page
     * @param salaryGrantEmployeeBO
     * @return Page<SalaryGrantEmployeeBO>
     */
    Page<SalaryGrantEmployeeBO> queryEmployeeForMainTask(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO);

    /**
     * 查询子表的雇员信息
     * 主要根据子表任务单编号查询
     * @param page
     * @param salaryGrantEmployeeBO
     * @return Page<SalaryGrantEmployeeBO>
     */
    Page<SalaryGrantEmployeeBO> queryEmployeeForSubTask(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO);

    /**
     * 根据主表查询子表任务单列表
     * 主要根据主表任务单编号查询
     * @param page
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> querySubTaskPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 修改任务单信息（薪资发放日、上下午标识、审批意见、失效原因等）
     * @param salaryGrantMainTaskPO
     * @return Integer
     */
    Integer updateSalaryGrantMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO);

    /**
     * 查询任务单主表明细信息
     * @param queryCondition
     * @return SalaryGrantMainTaskPO
     */
    SalaryGrantMainTaskPO getSalaryGrantMainTaskPO(SalaryGrantMainTaskPO queryCondition);

    /**
     * 查询子任务单明细信息
     * @param queryCondition
     * @return SalaryGrantSubTaskPO
     */
    SalaryGrantSubTaskPO getSalaryGrantSubTaskPO(SalaryGrantSubTaskPO queryCondition);

    /**
     * 修改子任务单信息（任务单状态、是否生效）
     * @param salaryGrantSubTaskPO
     * @return Integer
     */
    Integer updateSalaryGrantSubTask(SalaryGrantSubTaskPO salaryGrantSubTaskPO);

    /**
     * 任务单主表流程--提交
     * 提交任务单主表，拆分任务单为多个子表，根据多个拆分规则。按发放方式、发放账户拆分出客户自行的子任务单，新建薪资发放任务单子表sg_salarygrant_sub_task
     * 提交按钮后台业务逻辑：查询薪资发放日是否为空、查询批次对应的账单是否已核销/已代垫、如果是外区是否要发起代垫流程
     * @param salaryGrantMainTaskPO
     * @return Boolean
     */
    Boolean toSubmitMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO);

    /**
     * 任务单主表流程--审批通过
     * 审批通过时，把发放方式为0-中智大库、1-中智代发（委托机构）的两种雇员数据进行归类建立子表，并按照发放账户进行拆分。
     * @param salaryGrantMainTaskPO
     * @return Boolean
     */
    Boolean toApproveMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO);

    /**
     * 任务单主表流程--退回
     *
     * @param salaryGrantMainTaskPO
     * @return Boolean
     */
    Boolean toReturnMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO);

    /**
     * 任务单主表流程--撤回
     * 如果主表驳回，主表和子表都修改状态为草稿，同时记录数据到历史表状态为撤回/驳回。如果子表驳回，对应子表修改状态为草稿，同时记录数据到历史表状态为撤回/驳回。
     * @param salaryGrantMainTaskPO
     * @return Boolean
     */
    Boolean toRetreatMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO);

    /**
     * 任务单主表流程--驳回
     * 前端调用，处理业务逻辑
     * @param salaryGrantMainTaskPO
     * @return Boolean
     */
    Boolean toRejectTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO);

    /**
     * 任务单流程--失效
     * 调用消息，把所有和该计算批次的相关业务模块进行通知，把计算批次的状态进行修改。
     * @param salaryGrantMainTaskPO
     * @return
     */
    void toNotifyOtherCenterInvalidTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO);

    /**
     * 任务单流程--失效
     * 把薪资发放任务单，包括主表和子表，状态修改为失效，记录失效原因，记入历史表，主表修改为无效。
     * @param salaryGrantMainTaskPO
     * @return Boolean
     */
    Boolean toInvalidTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO);

    /**
     * 任务单子表流程--提交
     *
     * @param salaryGrantSubTaskPO
     * @return Boolean
     */
    Boolean toSubmitSubTask(SalaryGrantSubTaskPO salaryGrantSubTaskPO);

    /**
     * 任务单子表流程--审批通过
     * 审批通过时，把发放方式为0-中智大库、1-中智代发（委托机构）的两种雇员数据进行归类建立子表，并按照发放账户进行拆分。
     * @param salaryGrantSubTaskPO
     * @return Boolean
     */
    Boolean toApproveSubTask(SalaryGrantSubTaskPO salaryGrantSubTaskPO);

    /**
     * 任务单子表流程--退回
     *
     * @param salaryGrantSubTaskPO
     * @return Boolean
     */
    Boolean toReturnSubTask(SalaryGrantSubTaskPO salaryGrantSubTaskPO);

    /**
     * 任务单子表流程--撤回
     * 如果主表驳回，主表和子表都修改状态为草稿，同时记录数据到历史表状态为撤回/驳回。如果子表驳回，对应子表修改状态为草稿，同时记录数据到历史表状态为撤回/驳回。
     * @param salaryGrantSubTaskPO
     * @return Boolean
     */
    Boolean toRetreatSubTask(SalaryGrantSubTaskPO salaryGrantSubTaskPO);

    /**
     * 暂缓雇员操作
     * 暂缓单个雇员信息，修改雇员的发放状态为手工暂缓。
     * @param salaryGrantEmployeePO
     * @return Boolean
     */
    Boolean toReprieveEmployee(SalaryGrantEmployeePO salaryGrantEmployeePO);

    /**
     * 恢复雇员操作
     * 恢复单个雇员信息，修改雇员的发放状态为正常。
     * @param salaryGrantEmployeePO
     * @return Boolean
     */
    Boolean toRecoverEmployee(SalaryGrantEmployeePO salaryGrantEmployeePO);

    /**
     * 批量暂缓雇员操作
     * 循环遍历雇员信息，调用单个暂缓雇员操作
     * @param salaryGrantEmployeePOList
     * @return Boolean
     */
    Boolean toBatchReprieveEmployee(List<SalaryGrantEmployeePO> salaryGrantEmployeePOList);

    /**
     * 批量恢复雇员操作
     * 循环遍历雇员信息，调用单个恢复雇员操作
     * @param salaryGrantEmployeePOList
     * @return Boolean
     */
    Boolean toBatchRecoverEmployee(List<SalaryGrantEmployeePO> salaryGrantEmployeePOList);

    /**
     * 调整信息-对于发放类型是调整/回溯发放。
     * 查询调整/回溯批次、基于的计算批次，查询2条计算结果数据，再进行数据合并生成第三条合并数据。（调用计算批次接口，查询第3个方法）
     * @param salaryGrantEmployeePO
     * @return List
     */
    List listAdjustCalcInfo(SalaryGrantEmployeePO salaryGrantEmployeePO);

    /**
     * 生成工资清单--业务明细
     * 调用结算中心接口，明确插入的数据结构。2018-02-05后提供接口
     * @param salaryGrantMainTaskCode
     * @return Boolean
     */
    Boolean toCreatePayrollForBusiness(String salaryGrantMainTaskCode);

    /**
     * 生成工资清单--财务明细
     * 调用结算中心接口，明确插入的数据结构，被定时任务调用。2018-02-05后提供接口
     * @param salaryGrantMainTaskCode
     * @return Boolean
     */
    Boolean toCreatePayrollForFinance(String salaryGrantMainTaskCode);

    /**
     * 工资清单--预览
     * @param reviewInfo
     * @return
     */
    void toPreviewForPayroll(List reviewInfo);

    /**
     * 工资清单--打印
     * @param printInfo
     * @return
     */
    void toPrintForPayroll(List printInfo);

    /**
     * 导出雇员信息
     * @param salaryGrantMainTaskCode
     * @return
     */
    void toExportEmployee(String salaryGrantMainTaskCode);

}
