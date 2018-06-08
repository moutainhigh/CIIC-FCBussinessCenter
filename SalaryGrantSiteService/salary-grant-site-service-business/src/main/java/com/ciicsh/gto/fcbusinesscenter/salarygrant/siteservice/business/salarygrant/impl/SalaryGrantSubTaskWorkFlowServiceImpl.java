package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeCommandService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantSubTaskWorkFlowService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantTaskHistoryMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantTaskHistoryPO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
    private SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    private SalaryGrantEmployeeCommandService salaryGrantEmployeeCommandService;

    /**
     * 任务单流程--提交（调用工作流引擎）
     * @return
     */
    @Override
    public void submitSubTask(SalaryGrantTaskBO bo) {
    }

    /**
     * 任务单流程--审批通过（调用工作流引擎）
     * @return
     */
    @Override
    public void approveSubTask(SalaryGrantTaskBO bo) {
    }

    /**
     * 任务单流程--退回（调用工作流引擎）
     * @return
     */
    @Override
    public void returnSubTask(SalaryGrantTaskBO bo) {
    }

    /**
     * 任务单流程--撤回（调用工作流引擎）
     * @author chenpb
     * @date 2018-06-08
     * @param bo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void retreatSubTask(SalaryGrantTaskBO bo) {
        SalaryGrantSubTaskPO po = BeanUtils.instantiate(SalaryGrantSubTaskPO.class);
        SalaryGrantTaskHistoryPO historyPO = BeanUtils.instantiate(SalaryGrantTaskHistoryPO.class);
        po.setSalaryGrantSubTaskId(bo.getTaskId());
        po = salaryGrantSubTaskMapper.selectById(po);
        po.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_DRAFT);
        po.setModifiedBy(bo.getUserId());
        po.setModifiedTime(new Date());
        salaryGrantSubTaskMapper.updateById(po);
        BeanUtils.copyProperties(po, historyPO);
        assignValue(po,historyPO);
        salaryGrantTaskHistoryMapper.insert(historyPO);
        salaryGrantEmployeeCommandService.saveToHistory(historyPO.getSalaryGrantTaskHistoryId(), historyPO.getTaskCode(), historyPO.getTaskType());
    }

    private static void assignValue (SalaryGrantSubTaskPO subPo, SalaryGrantTaskHistoryPO hisPO) {
        hisPO.setTaskId(subPo.getSalaryGrantSubTaskId());
        hisPO.setTaskCode(subPo.getSalaryGrantSubTaskCode());
        hisPO.setMainTaskCode(subPo.getSalaryGrantMainTaskCode());
        hisPO.setTaskStatus(SalaryGrantBizConsts.TASK_STATUS_RETREAT);
    }
}
