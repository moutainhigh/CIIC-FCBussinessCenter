package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantTaskQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeePaymentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskPaymentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.WorkFlowTaskInfoBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import com.ciicsh.gto.fcbusinesscenter.util.constants.EventName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.FCBizTransactionMongoOpt;
import com.ciicsh.gto.salarymanagementcommandservice.api.BatchProxy;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.Custom.BatchAuditDTO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.SalaryBatchDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 薪资发放任务单查询 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-04-17
 */
@Service
public class SalaryGrantTaskQueryServiceImpl implements SalaryGrantTaskQueryService {
    @Autowired
    CommonService commonService;
    @Autowired
    SalaryGrantMainTaskMapper salaryGrantMainTaskMapper;
    @Autowired
    SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;
    @Autowired
    SalaryGrantTaskHistoryMapper salaryGrantTaskHistoryMapper;
    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    WorkFlowTaskInfoMapper workFlowTaskInfoMapper;
    @Autowired
    BatchProxy batchProxy;
    @Autowired
    FCBizTransactionMongoOpt fcBizTransactionMongoOpt;

    /**
     * 查询薪资发放任务单列表
     *
     * @param bo
     * @return Page<SalaryGrantTaskBO>
     * @author chenpb
     * @since 2018-04-25
     */
    @Override
    public Page<SalaryGrantTaskBO> salaryGrantList(SalaryGrantTaskBO bo) {
        Page<SalaryGrantTaskBO> page = null;
        Page<SalaryGrantTaskBO> paging = new Page<SalaryGrantTaskBO>(bo.getCurrent(), bo.getSize());
        if (SalaryGrantBizConsts.TASK_REFER.equals(bo.getTaskStatusEn())) {
            page = this.queryTaskForSubmitPage(paging, bo);
        } else if (SalaryGrantBizConsts.TASK_PEND.equals(bo.getTaskStatusEn())) {
            page = this.queryTaskForApprovePage(paging, bo);
        } else if (SalaryGrantBizConsts.TASK_APPROVAL.equals(bo.getTaskStatusEn())) {
            page = this.queryTaskForHaveApprovedPage(paging, bo);
        } else if (SalaryGrantBizConsts.TASK_ADOPT.equals(bo.getTaskStatusEn())) {
            page = this.queryTaskForPassPage(paging, bo);
        } else if (SalaryGrantBizConsts.TASK_REFUSE.equals(bo.getTaskStatusEn())) {
            page = this.queryTaskForRejectPage(paging, bo);
        } else if (SalaryGrantBizConsts.TASK_CANCEL.equals(bo.getTaskStatusEn())) {
            page = this.queryTaskForInvalidPage(paging, bo);
        }
        return page;
    }

    /**
     * @param salaryGrantTaskBO
     * @return
     * @description 根据任务单编号查询任务单
     * @author chenpb
     * @since 2018-04-25
     */
    @Override
    public SalaryGrantTaskBO selectTaskByTaskCode(SalaryGrantTaskBO salaryGrantTaskBO) {
        SalaryGrantTaskBO bo;
        if (SalaryGrantBizConsts.TASK_STATUS_REFUSE.equals(salaryGrantTaskBO.getTaskStatus()) || SalaryGrantBizConsts.TASK_STATUS_CANCEL.equals(salaryGrantTaskBO.getTaskStatus())) {
            bo = salaryGrantTaskHistoryMapper.selectTaskByTaskId(salaryGrantTaskBO);
        } else if (SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(salaryGrantTaskBO.getTaskType())) {
            bo = salaryGrantMainTaskMapper.selectTaskByTaskCode(salaryGrantTaskBO);
        } else {
            bo = salaryGrantSubTaskMapper.selectTaskByTaskCode(salaryGrantTaskBO);
        }
        return bo;
    }

    /**
     * 根据任务单编号查询操作记录
     *
     * @param bo
     * @return
     */
    @Override
    public Page<WorkFlowTaskInfoBO> operation(SalaryGrantTaskBO bo) {
        Page<WorkFlowTaskInfoBO> page = new Page<WorkFlowTaskInfoBO>(bo.getCurrent(), bo.getSize());
        List<WorkFlowTaskInfoBO> list = workFlowTaskInfoMapper.operation(page, bo);
        return page.setRecords(list);
    }

    /**
     * @param page
     * @param salaryGrantTaskBO
     * @return
     * @description 待提交任务单：0-草稿 角色=操作员
     * @author chenpb
     * @since 2018-04-23
     */
    private Page<SalaryGrantTaskBO> queryTaskForSubmitPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantMainTaskMapper.submitList(page, salaryGrantTaskBO);
        page.setRecords(list);
        return page;
    }

    /**
     * @param page
     * @param salaryGrantTaskBO
     * @return
     * @description 待审批任务单:1-审批中 角色=审核员
     * @author chenpb
     * @since 2018-04-23
     */
    private Page<SalaryGrantTaskBO> queryTaskForApprovePage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantMainTaskMapper.approveList(page, salaryGrantTaskBO);
        page.setRecords(list);
        return page;
    }

    /**
     * @param page
     * @param salaryGrantTaskBO
     * @return
     * @description 已处理任务单:1-审批中 角色=操作员
     * @author chenpb
     * @since 2018-04-23
     */
    private Page<SalaryGrantTaskBO> queryTaskForHaveApprovedPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantMainTaskMapper.haveApprovedList(page, salaryGrantTaskBO);
        page.setRecords(list);
        return page;
    }

    /**
     * @param page
     * @param salaryGrantTaskBO
     * @return
     * @description 已处理任务单:审批通过 2-审批通过、6-未支付、7-已支付 角色=操作员、审核员
     * @author chenpb
     * @since 2018-04-23
     */
    private Page<SalaryGrantTaskBO> queryTaskForPassPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantMainTaskMapper.passList(page, salaryGrantTaskBO);
        page.setRecords(list);
        return page;
    }

    /**
     * @param page
     * @param salaryGrantTaskBO
     * @return
     * @description 已处理任务单:审批拒绝 3-审批拒绝、8-撤回、9-驳回 角色=操作员、审核员（查历史表）
     * @author chenpb
     * @since 2018-04-23
     */
    private Page<SalaryGrantTaskBO> queryTaskForRejectPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantTaskHistoryMapper.rejectList(page, salaryGrantTaskBO);
        page.setRecords(list);
        return page;
    }

    /**
     * @param page
     * @param salaryGrantTaskBO
     * @return
     * @description 已处理任务单:已处理:4-失效 角色=操作员、审核员（查历史表）
     * @author chenpb
     * @since 2018-04-23
     */
    private Page<SalaryGrantTaskBO> queryTaskForInvalidPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantTaskHistoryMapper.invalidList(page, salaryGrantTaskBO);
        page.setRecords(list);
        return page;
    }

    /**
     * @param salaryGrantTaskBO
     * @return
     * @description 根据主表任务单编号查询子表任务单
     * @author chenpb
     * @since 2018-05-10
     */
    @Override
    public List<SalaryGrantTaskBO> querySubTask(SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantSubTaskMapper.subTaskList(salaryGrantTaskBO);
        if (!list.isEmpty()) {
            list.parallelStream().forEach(x -> {
                if (StringUtils.isNotBlank(x.getGrantMode())) {
                    x.setGrantModeName(commonService.getNameByValue("sgGrantMode", x.getGrantMode()));
                }
                if (StringUtils.isNotBlank(x.getTaskStatus())) {
                    x.setTaskStatusName(commonService.getNameByValue("sgsTaskStatus", x.getTaskStatus()));
                }
            });
        }
        return list;
    }

    /**
     * 同步结算中心支付状态
     * @author chenpb
     * @since 2018-06-05
     * @param taskCode
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean syncPayStatus(String taskCode) {
        SalaryGrantSubTaskPO subTask = BeanUtils.instantiate(SalaryGrantSubTaskPO.class);
        subTask.setSalaryGrantSubTaskCode(taskCode);
        subTask.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_PAYMENT);
        subTask = salaryGrantSubTaskMapper.syncTaskInfo(subTask);
        salaryGrantMainTaskMapper.syncTaskInfo(subTask.getSalaryGrantMainTaskCode(), SalaryGrantBizConsts.TASK_STATUS_PAYMENT);
        batchProxy.updateBatchStatus(getBatchAuditDTO(subTask));
        fcBizTransactionMongoOpt.commitEvent(subTask.getBatchCode(), subTask.getGrantType(), EventName.FC_GRANT_EVENT, 1);
        return true;
    }

    /**
     * 获取批次信息
     * @author chenpb
     * @since 2018-06-05
     * @param po
     * @return
     */
    private BatchAuditDTO getBatchAuditDTO(SalaryGrantSubTaskPO po) {
        BatchAuditDTO batchAuditDTO = BeanUtils.instantiate(BatchAuditDTO.class);
        batchAuditDTO.setBatchCode(po.getBatchCode());
        batchAuditDTO.setBatchType(po.getGrantType());
        batchAuditDTO.setModifyBy(SalaryGrantBizConsts.SYSTEM_EN);
        batchAuditDTO.setStatus(8);
        return batchAuditDTO;
    }
    /**
     * 每天晚上8点执行任务
     *
     */
    @Scheduled(cron = "0 0 20 * * ?")
    //每30秒执行一次
//    @Scheduled(cron = "*/30 * * * * ?")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void queryForPayment() {
//        System.out.println("薪资发放定时任务 启动");

        //获取次日日期
        String grantDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        //查询薪资发放任务单子表记录列表
        List<SalaryGrantTaskPaymentBO> waitForPaymentTaskList = salaryGrantSubTaskMapper.queryWaitForPaymentTaskList(grantDate);
        if (!CollectionUtils.isEmpty(waitForPaymentTaskList)) {
            int taskPaymentBOListSize = waitForPaymentTaskList.size();
            for (int i = 0; i < taskPaymentBOListSize; i++) {
                SalaryGrantTaskPaymentBO salaryGrantTaskPaymentBO = waitForPaymentTaskList.get(i);
                //任务单子表编号
                String taskCode = salaryGrantTaskPaymentBO.getTaskCode();
                //任务单主表编号
                String mainTaskCode = salaryGrantTaskPaymentBO.getMainTaskCode();

                //根据任务单编号查询雇员信息
                List<SalaryGrantEmployeePaymentBO> waitForPaymentEmpList = salaryGrantEmployeeMapper.queryWaitForPaymentEmpList(taskCode);

                //设置任务单记录字段
                //正常发放雇员的实发工资之和
                BigDecimal payAmount = null;
                //正常发放雇员的雇员数量
                Integer payEmployeeCount = null;
                //正常发放雇员的实发工资之和雇员列表
                List<SalaryGrantEmployeePaymentBO> payAmountPaymentBOList;
                //正常发放雇员的雇员数量雇员列表
                List<SalaryGrantEmployeePaymentBO> payEmployeeCountPaymentBOList;
                if (!CollectionUtils.isEmpty(waitForPaymentEmpList)) {
                    //汇总金额去掉 sg_salary_grant_employee.grant_status暂缓（1-自动、2-手动）的雇员发放金额
                    payAmountPaymentBOList = waitForPaymentEmpList.stream().filter(paymentBO ->
                            !ObjectUtils.isEmpty(paymentBO.getGrantStatus()) && paymentBO.getGrantStatus() != 1 && paymentBO.getGrantStatus() != 2
                    ).collect(Collectors.toList());
                    //需要发放的雇员数量，去掉暂缓雇员grant_status（1-自动、2-手动）和 发放金额payment_amount为0的雇员
                    payEmployeeCountPaymentBOList = waitForPaymentEmpList.stream().filter(paymentBO ->
                            (!ObjectUtils.isEmpty(paymentBO.getGrantStatus()) && paymentBO.getGrantStatus() != 1 && paymentBO.getGrantStatus() != 2) ||
                                    new BigDecimal(0).compareTo(paymentBO.getPaymentAmount()) != 0
                    ).collect(Collectors.toList());

                    if (!CollectionUtils.isEmpty(payAmountPaymentBOList)) {
                        payAmount = payAmountPaymentBOList.stream().map(SalaryGrantEmployeePaymentBO::getPaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    }

                    payEmployeeCount = payEmployeeCountPaymentBOList.size();
                }

                //正常发放雇员的实发工资之和
                salaryGrantTaskPaymentBO.setPayAmount(payAmount);
                //正常发放雇员的雇员数量
                salaryGrantTaskPaymentBO.setPayEmployeeCount(payEmployeeCount);

                //调用结算中心发放工资清单接口
                SalaryBatchDTO salaryBatchDTO = commonService.saveSalaryBatchData(salaryGrantTaskPaymentBO, waitForPaymentEmpList);
                if (!ObjectUtils.isEmpty(salaryBatchDTO)) {
                    //更新任务单子表状态
                    //更新字段
                    SalaryGrantSubTaskPO salaryGrantSubTaskPO = new SalaryGrantSubTaskPO();
                    salaryGrantSubTaskPO.setTaskStatus("6");
                    //更新条件
                    EntityWrapper<SalaryGrantSubTaskPO> subTaskPOEntityWrapper = new EntityWrapper<>();
                    subTaskPOEntityWrapper.where("salary_grant_sub_task_code = {0} and is_active = 1", taskCode);
                    salaryGrantSubTaskMapper.update(salaryGrantSubTaskPO, subTaskPOEntityWrapper);

                    //更新任务单主表状态
                    //更新字段
                    SalaryGrantMainTaskPO salaryGrantMainTaskPO = new SalaryGrantMainTaskPO();
                    salaryGrantMainTaskPO.setTaskStatus("6");
                    //更新条件
                    EntityWrapper<SalaryGrantMainTaskPO> mainTaskPOEntityWrapper = new EntityWrapper<>();
                    mainTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", mainTaskCode);
                    salaryGrantMainTaskMapper.update(salaryGrantMainTaskPO, mainTaskPOEntityWrapper);
                }
            }
        }

//        System.out.println("薪资发放定时任务 完成");
    }
}
