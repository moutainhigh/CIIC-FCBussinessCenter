package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeCommandService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantSubTaskWorkFlowService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantWorkFlowService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantTaskHistoryMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantTaskHistoryPO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 薪资发放任务单子表工作流调用 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-06-07
 */
@Service
public class SalaryGrantSubTaskWorkFlowServiceImpl extends ServiceImpl<SalaryGrantSubTaskMapper, SalaryGrantSubTaskPO> implements SalaryGrantSubTaskWorkFlowService {
    @Autowired
    private SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;
    @Autowired
    private SalaryGrantTaskHistoryMapper salaryGrantTaskHistoryMapper;
    @Autowired
    private SalaryGrantEmployeeCommandService salaryGrantEmployeeCommandService;
    @Autowired
    private SalaryGrantWorkFlowService salaryGrantWorkFlowService;

    /**
     * 任务单流程--提交
     * @author chenpb
     * @date 2018-06-08
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitSubTask(SalaryGrantTaskBO bo) {
        SalaryGrantSubTaskPO subPo = BeanUtils.instantiate(SalaryGrantSubTaskPO.class);
        subPo.setSalaryGrantSubTaskId(bo.getTaskId());
        subPo = salaryGrantSubTaskMapper.selectById(subPo);
        if (!ObjectUtils.isEmpty(subPo)) {
            subPo.setOperatorUserId(bo.getUserId());
            subPo.setModifiedTime(new Date());
            subPo.setModifiedBy(bo.getUserId());
            if (StringUtils.isNotBlank(bo.getGrantDate())) {
                subPo.setGrantDate(bo.getGrantDate());
                subPo.setGrantTime(bo.getGrantTime());
            }
            subPo.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_APPROVAL);
            salaryGrantSubTaskMapper.updateById(subPo);
        }
    }

    /**
     * 任务单流程--审批通过
     * @author chenpb
     * @date 2018-06-08
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveSubTask(SalaryGrantTaskBO bo) {
        SalaryGrantSubTaskPO subPo = BeanUtils.instantiate(SalaryGrantSubTaskPO.class);
        subPo.setSalaryGrantSubTaskId(bo.getTaskId());
        subPo = salaryGrantSubTaskMapper.selectById(subPo);
        if (!ObjectUtils.isEmpty(subPo)) {
            subPo.setApproveUserId(bo.getUserId());
            subPo.setModifiedTime(new Date());
            subPo.setModifiedBy(bo.getUserId());
            subPo.setApprovedOpinion(bo.getApprovedOpinion());
            if (SalaryGrantBizConsts.GRANT_MODE_LOCAL.equals(subPo.getGrantMode())) {
                subPo.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_PASS);
            } else if (SalaryGrantBizConsts.GRANT_MODE_SUPPLIER.equals(subPo.getGrantMode())) {
                subPo.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_COMBINE_WAIT);
            }
            salaryGrantSubTaskMapper.updateById(subPo);
        }
    }

    /**
     * 任务单流程--退回
     * @author chenpb
     * @date 2018-06-08
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returnSubTask(SalaryGrantTaskBO bo) {
        taskProcessing(bo, SalaryGrantBizConsts.TASK_STATUS_REFUSE);
    }

    /**
     * 任务单流程--撤回
     * @author chenpb
     * @date 2018-06-08
     * @param bo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean retreatSubTask(SalaryGrantTaskBO bo) {
         return taskProcessing(bo, SalaryGrantBizConsts.TASK_STATUS_RETREAT);
    }

    private Boolean taskProcessing (SalaryGrantTaskBO bo, String status) {
        Boolean result = false;
        SalaryGrantSubTaskPO subPo = BeanUtils.instantiate(SalaryGrantSubTaskPO.class);
        SalaryGrantTaskHistoryPO historyPO = BeanUtils.instantiate(SalaryGrantTaskHistoryPO.class);
        subPo.setSalaryGrantSubTaskId(bo.getTaskId());
        subPo = salaryGrantSubTaskMapper.selectById(subPo);
        if (!ObjectUtils.isEmpty(subPo)) {
            subPo.setOperatorUserId(bo.getUserId());
            subPo.setModifiedTime(new Date());
            subPo.setModifiedBy(bo.getUserId());
            subPo.setApprovedOpinion(bo.getApprovedOpinion());
            subPo.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_DRAFT);
            BeanUtils.copyProperties(subPo, historyPO);
            assignValue(subPo, historyPO, status);
            salaryGrantTaskHistoryMapper.insert(historyPO);
            salaryGrantEmployeeCommandService.saveToHistory(historyPO.getSalaryGrantTaskHistoryId(), historyPO.getTaskCode(), historyPO.getTaskType());
            salaryGrantSubTaskMapper.updateById(subPo);
        }
        /** 子任务单撤回check*/
        if (SalaryGrantBizConsts.TASK_STATUS_RETREAT.equals(status)) {
            List<SalaryGrantTaskBO> allTask = salaryGrantSubTaskMapper.selectAllTaskBySubTaskCode(bo);
            List<SalaryGrantTaskBO> draftTask = allTask.parallelStream().filter(x -> SalaryGrantBizConsts.TASK_STATUS_DRAFT.equals(x.getTaskStatus())).collect(Collectors.toList());
            if (allTask.size() == draftTask.size()) {
                salaryGrantWorkFlowService.doRetreatTask(bo);
                result = true;
            }
        }
        return result;
    }

    private static void assignValue (SalaryGrantSubTaskPO subPo, SalaryGrantTaskHistoryPO hisPo, String status) {
        hisPo.setTaskStatus(status);
        hisPo.setTaskId(subPo.getSalaryGrantSubTaskId());
        hisPo.setTaskCode(subPo.getSalaryGrantSubTaskCode());
        hisPo.setMainTaskCode(subPo.getSalaryGrantMainTaskCode());
    }
}
