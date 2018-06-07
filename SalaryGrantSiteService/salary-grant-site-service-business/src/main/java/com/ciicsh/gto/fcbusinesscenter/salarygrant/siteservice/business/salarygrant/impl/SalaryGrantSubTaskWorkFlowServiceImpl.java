package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantSubTaskWorkFlowService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import org.springframework.stereotype.Service;

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
    /**
     * 任务单流程--提交（调用工作流引擎）
     * @return
     */
    @Override
    public Boolean doSubmitTask() {
        return true;
    }

    /**
     * 任务单流程--审批通过（调用工作流引擎）
     * @return
     */
    @Override
    public Boolean doApproveTask() {
        return true;
    }

    /**
     * 任务单流程--退回（调用工作流引擎）
     * @return
     */
    @Override
    public Boolean doReturnTask() {
        return true;
    }

    /**
     * 任务单流程--撤回（调用工作流引擎）
     * @return
     */
    @Override
    public Boolean doRetreatTask() {
        return true;
    }
}
