package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.entity.CancelClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantWorkFlowEnums;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.WorkFlowTaskInfoPO;
import com.ciicsh.gto.fcbusinesscenter.util.constants.EventName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.FCBizTransactionMongoOpt;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.logservice.client.LogClientService;
import com.ciicsh.gto.salarymanagementcommandservice.api.BatchProxy;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.BatchAuditDTO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.PayapplySalaryDTO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.SalaryBatchDTO;
import com.ciicsh.gto.sheetservice.api.SheetServiceProxy;
import com.ciicsh.gto.sheetservice.api.dto.Result;
import com.ciicsh.gto.sheetservice.api.dto.ResultCode;
import com.ciicsh.gto.sheetservice.api.dto.request.MissionRequestDTO;
import com.ciicsh.gto.sheetservice.api.dto.request.TaskRequestDTO;
import com.ciicsh.gto.sheetservice.api.dto.response.StartProcessResponseDTO;
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
import java.util.*;
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
public class SalaryGrantTaskQueryServiceImpl extends ServiceImpl<SalaryGrantMainTaskMapper, SalaryGrantMainTaskPO> implements SalaryGrantTaskQueryService {
    @Autowired
    LogClientService logClientService;
    @Autowired
    private BatchProxy batchProxy;
    @Autowired
    private SheetServiceProxy sheetServiceProxy;
    @Autowired
    private CommonService commonService;
    @Autowired
    private SalaryGrantMainTaskMapper salaryGrantMainTaskMapper;
    @Autowired
    private SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;
    @Autowired
    private SalaryGrantTaskHistoryMapper salaryGrantTaskHistoryMapper;
    @Autowired
    private SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    private WorkFlowTaskInfoMapper workFlowTaskInfoMapper;
    @Autowired
    private FCBizTransactionMongoOpt fcBizTransactionMongoOpt;
    @Autowired
    private SalaryGrantWorkFlowService salaryGrantWorkFlowService;
    @Autowired
    private SalaryGrantSubTaskWorkFlowService salaryGrantSubTaskWorkFlowService;
    @Autowired
    private SalaryGrantEmployeeCommandService salaryGrantEmployeeCommandService;

    /**
     * 刷新草稿状态任务单信息
     * @author chenpb
     * @since 2018-07-05
     * @param bo
     * @return
     */
    @Override
    public Page<SalaryGrantTaskBO> refreshDraftTask(SalaryGrantTaskBO bo) {
        Page<SalaryGrantTaskBO> page = new Page<>(bo.getCurrent(), bo.getSize());
        List<RefreshTaskBO> refreshList = salaryGrantMainTaskMapper.refreshList(bo);
        if (!refreshList.isEmpty()) {
            List<RefreshTaskBO> updateList = new ArrayList<>();
            refreshList.parallelStream().forEach(x -> x.getEmpList().parallelStream().forEach(y -> {
                SalaryGrantEmployeePO newPo = commonService.getEmployeeNewestInfo(y);
                boolean updateResult = salaryGrantEmployeeCommandService.compareAndUpdateEmployeeNewestInfo(y, newPo);
                if (updateResult) {
                    updateList.add(x);
                }
            }));
            if (!updateList.isEmpty()) {
                updateList.parallelStream().distinct().forEach(x -> {
                    if (SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(x.getTaskType())) {
                        SalaryGrantMainTaskPO mainPo = BeanUtils.instantiate(SalaryGrantMainTaskPO.class);
                        mainPo.setSalaryGrantMainTaskId(x.getTaskId());
                        mainPo.setRemark(SalaryGrantBizConsts.TASK_REMARK);
                        salaryGrantMainTaskMapper.updateById(mainPo);
                    } else {
                        SalaryGrantSubTaskPO subPo = BeanUtils.instantiate(SalaryGrantSubTaskPO.class);
                        subPo.setSalaryGrantSubTaskId(x.getTaskId());
                        subPo.setRemark(SalaryGrantBizConsts.TASK_REMARK);
                        salaryGrantSubTaskMapper.updateById(subPo);
                    }
                });
            }
        }
        page = this.queryTaskForSubmitPage(page, bo);
        formatGrantDate(page);
        return page;
    }

    /**
     * 查询薪资发放任务单列表
     * @author chenpb
     * @since 2018-04-25
     * @param bo
     * @return Page<SalaryGrantTaskBO>
     */
    @Override
    public Page<SalaryGrantTaskBO> salaryGrantList(SalaryGrantTaskBO bo) {
        Page<SalaryGrantTaskBO> paging = new Page<SalaryGrantTaskBO>(bo.getCurrent(), bo.getSize());
        if (SalaryGrantBizConsts.TASK_REFER.equals(bo.getTaskStatusEn())) {
            paging = this.queryTaskForSubmitPage(paging, bo);
        } else if (SalaryGrantBizConsts.TASK_PEND.equals(bo.getTaskStatusEn())) {
            paging = this.queryTaskForApprovePage(paging, bo);
        } else if (SalaryGrantBizConsts.TASK_APPROVAL.equals(bo.getTaskStatusEn())) {
            paging = this.queryTaskForHaveApprovedPage(paging, bo);
        } else if (SalaryGrantBizConsts.TASK_ADOPT.equals(bo.getTaskStatusEn())) {
            paging = this.queryTaskForPassPage(paging, bo);
        } else if (SalaryGrantBizConsts.TASK_REFUSE.equals(bo.getTaskStatusEn())) {
            paging = this.queryTaskForRejectPage(paging, bo);
        } else if (SalaryGrantBizConsts.TASK_CANCEL.equals(bo.getTaskStatusEn())) {
            paging = this.queryTaskForInvalidPage(paging, bo);
        }
        formatGrantDate(paging);
        return paging;
    }

    /**
     * 格式化信息发放日期
     * @author chenpb
     * @since 2018-07-10
     * @param paging
     */
    private void formatGrantDate (Page<SalaryGrantTaskBO> paging) {
        if (!ObjectUtils.isEmpty(paging.getRecords())) {
            paging.getRecords().stream().forEach(x -> {
                if (StringUtils.isNotBlank(x.getGrantDate())) {
                    String date = x.getGrantDate();
                    x.setGrantDate(date.substring(0,4) + '-' + date.substring(4,6) + '-' + date.substring(6,8));
                }
            });
        }
    }

    /**
     * 根据任务单编号查询任务单
     * @author chenpb
     * @since 2018-04-25
     * @param salaryGrantTaskBO
     * @return
     */
    @Override
    public SalaryGrantTaskBO selectTaskByTaskCode(SalaryGrantTaskBO salaryGrantTaskBO) {
        SalaryGrantTaskBO bo;
        //if (SalaryGrantBizConsts.TASK_STATUS_REFUSE.equals(salaryGrantTaskBO.getTaskStatus()) || SalaryGrantBizConsts.TASK_STATUS_CANCEL.equals(salaryGrantTaskBO.getTaskStatus())) {
        /** 根据查询一览数据查询条件修改，不保证业务正确性。 2018-07-18*/
        if (SalaryGrantBizConsts.TASK_STATUS_REFUSE.equals(salaryGrantTaskBO.getTaskStatus()) || SalaryGrantBizConsts.TASK_STATUS_CANCEL.equals(salaryGrantTaskBO.getTaskStatus()) || SalaryGrantBizConsts.TASK_STATUS_RETREAT.equals(salaryGrantTaskBO.getTaskStatus())
                || SalaryGrantBizConsts.TASK_STATUS_REJECT.equals(salaryGrantTaskBO.getTaskStatus()) || SalaryGrantBizConsts.TASK_STATUS_NULLIFY.equals(salaryGrantTaskBO.getTaskStatus())) {
            bo = salaryGrantTaskHistoryMapper.selectTaskByTaskId(salaryGrantTaskBO);
        } else if (SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.equals(salaryGrantTaskBO.getTaskType())) {
            bo = salaryGrantMainTaskMapper.selectTaskByTaskCode(salaryGrantTaskBO);
        } else {
            bo = salaryGrantSubTaskMapper.selectTaskByTaskCode(salaryGrantTaskBO);
        }
        if (!ObjectUtils.isEmpty(bo) && StringUtils.isNotBlank(bo.getGrantDate())) {
            String date = bo.getGrantDate();
            bo.setGrantDate(date.substring(0,4) + '-' + date.substring(4,6) + '-' + date.substring(6,8));
        }
        return bo;
    }

    /**
     * 根据任务单编号查询操作记录
     * @author chenpb
     * @since 2018-05-10
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
     * 待提交任务单：0-草稿 角色=操作员
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param salaryGrantTaskBO
     * @return
     */
    private Page<SalaryGrantTaskBO> queryTaskForSubmitPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantMainTaskMapper.submitList(page, salaryGrantTaskBO);
        return page.setRecords(list);
    }

    /**
     * 待审批任务单:1-审批中 角色=审核员
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param salaryGrantTaskBO
     * @return
     */
    private Page<SalaryGrantTaskBO> queryTaskForApprovePage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantMainTaskMapper.approveList(page, salaryGrantTaskBO);
        return page.setRecords(list);
    }

    /**
     * 已处理任务单:1-审批中 角色=操作员
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param salaryGrantTaskBO
     * @return
     */
    private Page<SalaryGrantTaskBO> queryTaskForHaveApprovedPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantMainTaskMapper.haveApprovedList(page, salaryGrantTaskBO);
        return page.setRecords(list);
    }

    /**
     * 已处理任务单:审批通过 2-审批通过、6-未支付、7-已支付 角色=操作员、审核员
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param salaryGrantTaskBO
     * @return
     */
    private Page<SalaryGrantTaskBO> queryTaskForPassPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantMainTaskMapper.passList(page, salaryGrantTaskBO);
        return page.setRecords(list);
    }

    /**
     * 已处理任务单:审批拒绝 3-审批拒绝、8-撤回、9-驳回 角色=操作员、审核员（查历史表）
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param salaryGrantTaskBO
     * @return
     */
    private Page<SalaryGrantTaskBO> queryTaskForRejectPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantTaskHistoryMapper.rejectList(page, salaryGrantTaskBO);
        return page.setRecords(list);
    }

    /**
     * 已处理任务单:已处理:4-失效 角色=操作员、审核员（查历史表）
     * @author chenpb
     * @since 2018-04-23
     * @param page
     * @param salaryGrantTaskBO
     * @return
     */
    private Page<SalaryGrantTaskBO> queryTaskForInvalidPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        List<SalaryGrantTaskBO> list = salaryGrantTaskHistoryMapper.invalidList(page, salaryGrantTaskBO);
        return page.setRecords(list);
    }

    /**
     * 根据主表任务单编号查询子表任务单
     * @author chenpb
     * @since 2018-05-10
     * @param salaryGrantTaskBO
     * @return
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
     * 抢占资源锁
     * @author chenpb
     * @since 2018-06-22
     * @param bo
     * @return
     */
    @Override
    public Integer lockMainTask(SalaryGrantTaskBO bo) {
        return salaryGrantMainTaskMapper.lockTask(bo);
    }

    /**
     * 抢占资源锁
     * @author chenpb
     * @since 2018-06-22
     * @param bo
     * @return
     */
    @Override
    public Integer lockSubTask(SalaryGrantTaskBO bo) {
        return salaryGrantSubTaskMapper.lockTask(bo);
    }

    /**
     * 提交
     * @author chenpb
     * @since 2018-06-08
     * @param flag
     * @param bo
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkFlowResultBO submit(Boolean flag, SalaryGrantTaskBO bo) throws Exception {
        WorkFlowResultBO workFlowResultBO = BeanUtils.instantiate(WorkFlowResultBO.class);
        workFlowResultBO.setBatchCode(bo.getBatchCode());
        workFlowResultBO.setTaskCode(bo.getTaskCode());
        workFlowResultBO.setGrantType(bo.getGrantType());
        workFlowResultBO.setTaskType(bo.getTaskType());
        workFlowResultBO.setResult(SalaryGrantWorkFlowEnums.TaskResult.HANDLE.getResult());
        if (flag && salaryGrantMainTaskMapper.lockTask(bo)> 0) {
            Integer result =  salaryGrantWorkFlowService.doSubmitTask(bo);
            workFlowResultBO.setResult(result);
        } else if (salaryGrantSubTaskMapper.lockTask(bo) > 0) {
            salaryGrantSubTaskWorkFlowService.submitSubTask(bo);
        } else {
            workFlowResultBO.setResult(SalaryGrantWorkFlowEnums.TaskResult.LOCK.getResult());
            workFlowResultBO.setMessage(SalaryGrantWorkFlowEnums.TaskResult.LOCK.getExtension());
            return workFlowResultBO;
        }
        if (SalaryGrantWorkFlowEnums.TaskResult.HANDLE.getResult().equals(workFlowResultBO.getResult())) {
            List<WorkFlowTaskInfoBO> list = workFlowTaskInfoMapper.operation(bo);
            if (list.isEmpty() || (list.size()>0 && StringUtils.isEmpty(list.get(0).getWorkFlowTaskId()))) {
                MissionRequestDTO missionRequestDTO = createMissionDto(SalaryGrantWorkFlowEnums.ActionType.ACTION_SUBMIT.getAction(), bo);
                Result<StartProcessResponseDTO> dto =  sheetServiceProxy.startProcess(missionRequestDTO);
                if (ResultCode.SUCCESS.code == dto.getCode()) {
                    WorkFlowTaskInfoPO po = createTaskInfo(bo);
                    po.setWorkFlowProcessId(dto.getObject().getProcessId());
                    //workFlowTaskInfoMapper.insert(po);
                } else {
                    workFlowResultBO.setResult(SalaryGrantWorkFlowEnums.TaskResult.EXCEPTION.getResult());
                    workFlowResultBO.setMessage(SalaryGrantWorkFlowEnums.TaskResult.EXCEPTION.getExtension());
                }
            } else {
                sheetServiceProxy.completeTask(createTaskRequestDTO(SalaryGrantWorkFlowEnums.ActionType.ACTION_SUBMIT.getAction(), SalaryGrantWorkFlowEnums.Role.OPERATOR.getName(), bo, list.get(0)));
            }
        }
        return workFlowResultBO;
    }

    /**
     * 审批通过
     * @author chenpb
     * @since 2018-06-08
     * @param flag
     * @param bo
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkFlowResultBO approvalPass(Boolean flag, SalaryGrantTaskBO bo) throws Exception {
        WorkFlowResultBO workFlowResultBO = BeanUtils.instantiate(WorkFlowResultBO.class);
        if (flag && salaryGrantMainTaskMapper.lockTask(bo)> 0) {
            salaryGrantWorkFlowService.doApproveTask(bo);
        } else if (salaryGrantSubTaskMapper.lockTask(bo) > 0) {
            salaryGrantSubTaskWorkFlowService.approveSubTask(bo);
        } else {
            workFlowResultBO.setResult(SalaryGrantWorkFlowEnums.TaskResult.LOCK.getResult());
            workFlowResultBO.setMessage(SalaryGrantWorkFlowEnums.TaskResult.LOCK.getExtension());
            return workFlowResultBO;
        }
        List<WorkFlowTaskInfoBO> list = workFlowTaskInfoMapper.operation(bo);
        if (!list.isEmpty()) {
            sheetServiceProxy.completeTask(createTaskRequestDTO(SalaryGrantWorkFlowEnums.ActionType.ACTION_APPROVAL.getAction(), SalaryGrantWorkFlowEnums.Role.AUDIT.getName(), bo, list.get(0)));
        }
        return workFlowResultBO;
    }

    /**
     * 审批退回
     * @author chenpb
     * @since 2018-06-08
     * @param flag
     * @param bo
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkFlowResultBO approvalReject(Boolean flag, SalaryGrantTaskBO bo) throws Exception {
        WorkFlowResultBO workFlowResultBO = BeanUtils.instantiate(WorkFlowResultBO.class);
        if (flag && salaryGrantMainTaskMapper.lockTask(bo)> 0) {
            salaryGrantWorkFlowService.doReturnTask(bo);
        } else if (salaryGrantSubTaskMapper.lockTask(bo) > 0) {
            salaryGrantSubTaskWorkFlowService.returnSubTask(bo);
        } else {
            workFlowResultBO.setResult(SalaryGrantWorkFlowEnums.TaskResult.LOCK.getResult());
            workFlowResultBO.setMessage(SalaryGrantWorkFlowEnums.TaskResult.LOCK.getExtension());
            return workFlowResultBO;
        }
        List<WorkFlowTaskInfoBO> list = workFlowTaskInfoMapper.operation(bo);
        if (!list.isEmpty()) {
            sheetServiceProxy.completeTask(createTaskRequestDTO(SalaryGrantWorkFlowEnums.ActionType.ACTION_REJECT.getAction(), SalaryGrantWorkFlowEnums.Role.AUDIT.getName(), bo, list.get(0)));
        }
        return workFlowResultBO;
    }

    private MissionRequestDTO createMissionDto(String action, SalaryGrantTaskBO bo) {
        MissionRequestDTO missionRequestDTO = BeanUtils.instantiate(MissionRequestDTO.class);
        Map variables = new HashMap<>();
        variables.put("action", action);
        variables.put("taskDealTime", new Date());
        variables.put("taskDealUserId", bo.getUserId());
        variables.put("taskDealUserName", bo.getUserName());
        variables.put("approvedOpinion", bo.getApprovedOpinion());
        variables.put("workFlowTaskType", SalaryGrantWorkFlowEnums.Role.OPERATOR.getName());
        missionRequestDTO.setVariables(variables);
        missionRequestDTO.setMissionId(bo.getTaskCode());
        missionRequestDTO.setProcessDefinitionKey(getDefinitionKey(bo.getTaskCode().substring(0, 3)));
        return missionRequestDTO;
    }

    private TaskRequestDTO createTaskRequestDTO(String action, String role, SalaryGrantTaskBO task, WorkFlowTaskInfoBO bo) {
        TaskRequestDTO taskRequestDTO = BeanUtils.instantiate(TaskRequestDTO.class);
        Map variables = new HashMap<>();
        variables.put("action", action);
        variables.put("taskDealTime", new Date());
        variables.put("taskDealUserId", task.getUserId());
        variables.put("taskDealUserName", task.getUserName());
        variables.put("approvedOpinion", task.getApprovedOpinion());
        variables.put("workFlowTaskType", role);
        taskRequestDTO.setVariables(variables);
        taskRequestDTO.setAssignee(task.getUserId());
        taskRequestDTO.setTaskId(bo.getWorkFlowTaskId());
        return taskRequestDTO;
    }

    private WorkFlowTaskInfoPO createTaskInfo(SalaryGrantTaskBO bo) {
        WorkFlowTaskInfoPO workFlowTaskInfoPO = BeanUtils.instantiate(WorkFlowTaskInfoPO.class);
        workFlowTaskInfoPO.setTaskCode(bo.getTaskCode());
        workFlowTaskInfoPO.setWorkFlowTaskType(SalaryGrantWorkFlowEnums.Role.OPERATOR.getName());
        workFlowTaskInfoPO.setTaskDealUserId(bo.getUserId());
        workFlowTaskInfoPO.setTaskDealUserName(bo.getUserName());
        workFlowTaskInfoPO.setTaskDealTime(new Date());
        workFlowTaskInfoPO.setTaskDealOperation(SalaryGrantWorkFlowEnums.ActionType.ACTION_SUBMIT.getAction());
        workFlowTaskInfoPO.setCreatedBy(bo.getUserId());
        workFlowTaskInfoPO.setCreatedTime(new Date());
        return workFlowTaskInfoPO;
    }

    private String getDefinitionKey(String type) {
        switch (type) {
            case "LTB":
                return SalaryGrantWorkFlowEnums.ProcessDefinitionKey.LTB.getKey();
            case "LTW":
                return SalaryGrantWorkFlowEnums.ProcessDefinitionKey.LTW.getKey();
            case "STA":
                return SalaryGrantWorkFlowEnums.ProcessDefinitionKey.STA.getKey();
            case "SPT":
                return SalaryGrantWorkFlowEnums.ProcessDefinitionKey.SPT.getKey();
            default:
                return SalaryGrantWorkFlowEnums.ProcessDefinitionKey.SGT.getKey();
        }
    }

    /**
     * 同步结算中心支付状态
     * @author chenpb
     * @since 2018-06-06
     * @param list
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncPayStatus(List<PayapplySalaryDTO> list) {
        if (!list.isEmpty()) {
            List<BatchAuditDTO> auditList = new ArrayList<>();
            String subTaskStr = getSubTaskCodes(list);
            salaryGrantSubTaskMapper.syncTaskInfo(subTaskStr, SalaryGrantBizConsts.TASK_STATUS_PAYMENT);
            List<String> batchCodes = getBatchCodes(list);
            batchCodes.forEach(x-> {
                SalaryGrantTaskBO bo = BeanUtils.instantiate(SalaryGrantTaskBO.class);
                bo.setBatchCode(x);
                List<SalaryGrantSubTaskPO> subTasks = salaryGrantSubTaskMapper.getSubListByBatchCode(bo);
                List<SalaryGrantSubTaskPO> payLists = subTasks.parallelStream().filter(y -> SalaryGrantBizConsts.TASK_STATUS_PAYMENT.equals(y.getTaskStatus())).collect(Collectors.toList());
                if (payLists.size() == subTasks.size()) {
                    salaryGrantMainTaskMapper.syncTaskInfo(payLists.get(0).getSalaryGrantMainTaskCode(), SalaryGrantBizConsts.TASK_STATUS_PAYMENT);
                    getUpdateMongoParams(payLists.get(0), auditList);
                }
            });
            if (!auditList.isEmpty()) {
                batchProxy.updateBatchListStatus(auditList.parallelStream().distinct().collect(Collectors.toList()));
            }
        }
    }

    /**
     * 同步参数
     * @author chenpb
     * @since 2018-06-28
     * @param po
     * @param auditList
     */
    private static void getUpdateMongoParams(SalaryGrantSubTaskPO po, List<BatchAuditDTO> auditList) {
        BatchAuditDTO dto = BeanUtils.instantiate(BatchAuditDTO.class);
        dto.setBatchCode(po.getBatchCode());
        dto.setBatchType(po.getGrantType());
        dto.setModifyBy(SalaryGrantBizConsts.SYSTEM_EN);
        dto.setStatus(8);
        auditList.add(dto);
    }

    /**
     * 获取BatchCode列表
     * @author chenpb
     * @since 2018-06-28
     * @param list
     * @return
     */
    private static List<String> getBatchCodes(List<PayapplySalaryDTO> list) {
        List<String> batchCodes = list.parallelStream().map(x -> x.getBatchCode()).distinct().collect(Collectors.toList());
        return batchCodes;
    }

    /**
     * 获取subTaskCode列表
     * @author chenpb
     * @since 2018-06-06
     * @param list
     * @return
     */
    private static String getSubTaskCodes(List<PayapplySalaryDTO> list) {
        if (list.isEmpty()) {
            return "";
        }
        List<String> taskCodes = list.parallelStream().map(x -> "'" + x.getSequenceNo() + "'").distinct().collect(Collectors.toList());
        return String.join(",", taskCodes);
    }

    /**
     * 获取mainTaskCode列表
     * @author chenpb
     * @since 2018-06-06
     * @param list
     * @return
     */
    private static List<String> getMainTaskCodes(List<SalaryGrantSubTaskPO> list) {
        List<String> taskCodes = list.parallelStream().map(x -> x.getSalaryGrantMainTaskCode()).distinct().collect(Collectors.toList());
        return taskCodes;
    }

    /**
     * 取消关账
     * @author chenpb
     * @since 2018-06-07
     * @param msg
     * @throws Exception
     */
    @Override
    public void cancelClosing(CancelClosingMsg msg) throws Exception {
        SalaryGrantTaskBO bo = salaryGrantMainTaskMapper.selectByTBatchInfo(msg.getBatchCode(), msg.getBatchType());
        if (!ObjectUtils.isEmpty(bo) && Long.valueOf(msg.getVersion()).compareTo(bo.getBatchVersion()) != -1) {
            bo.setBatchVersion(msg.getVersion());
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("取消关账").setTitle("关账业务处理").setContent(JSON.toJSONString(bo)));
            salaryGrantWorkFlowService.doCancelTask(bo);
        }
    }

    /**
     * 每天晚上8点执行任务
     */
//    @Scheduled(cron = "0 0 20 * * ?")
    //每3分钟执行一次
//    @Scheduled(cron = "0 0/3 * * * ?")
//    @Transactional(rollbackFor = Exception.class)
    @Override
    public void queryForPayment() {
//        System.out.println("薪资发放定时任务 启动时间: " + LocalDateTime.now());

        //获取次日日期
        String grantDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        //查询薪资发放任务单子表记录列表
        List<SalaryGrantTaskPaymentBO> waitForPaymentTaskList = salaryGrantSubTaskMapper.queryWaitForPaymentTaskList(grantDate);
        if (!CollectionUtils.isEmpty(waitForPaymentTaskList)) {
            waitForPaymentTaskList.forEach(salaryGrantTaskPaymentBO -> {
                //任务单子表编号
                String taskCode = salaryGrantTaskPaymentBO.getTaskCode();
                //任务单主表编号
                String mainTaskCode = salaryGrantTaskPaymentBO.getMainTaskCode();

                //根据任务单编号查询雇员信息，查询语句添加过滤条件去掉暂缓雇员（手动+自动）和工资为0的雇员 update by gy on 2018-07-26
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
                // 如果查询任务单中雇员个数为0则不同步数据到结算中心 update by gy on 2018-07-26
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

                        //查询任务单主表记录
                        EntityWrapper<SalaryGrantMainTaskPO> mainTaskPOEntityWrapper1 = new EntityWrapper<>();
                        mainTaskPOEntityWrapper1.where("salary_grant_main_task_code = {0} and is_active = 1", mainTaskCode);
                        List<SalaryGrantMainTaskPO> mainTaskPOList = salaryGrantMainTaskMapper.selectList(mainTaskPOEntityWrapper1);
                        if (!CollectionUtils.isEmpty(mainTaskPOList)) {
                            mainTaskPOList.stream().forEach(salaryGrantMainTaskPO1 -> {
                                //更新mongodb中标记为1
                                fcBizTransactionMongoOpt.commitEvent(salaryGrantMainTaskPO1.getBatchCode(), salaryGrantMainTaskPO1.getGrantType(), EventName.FC_GRANT_EVENT, 1);
                            });
                        }
                    }
                }
            });
        }

//        System.out.println("薪资发放定时任务 完成时间: " + LocalDateTime.now());
    }
}
