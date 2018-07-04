package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantWorkFlowEnums;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.WorkFlowTaskInfoService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.WorkFlowTaskInfoMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.WorkFlowTaskInfoPO;
import com.ciicsh.gto.sheetservice.api.dto.TaskCompleteMsgDTO;
import com.ciicsh.gto.sheetservice.api.dto.TaskCreateMsgDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 工作流任务日志表 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-09
 */
@Service
public class WorkFlowTaskInfoServiceImpl extends ServiceImpl<WorkFlowTaskInfoMapper, WorkFlowTaskInfoPO> implements WorkFlowTaskInfoService {
    @Autowired
    private WorkFlowTaskInfoMapper workFlowTaskInfoMapper;
    /**
     * 保存工作流日志
     * @author chenpb
     * @since 2018-06-27
     * @param taskCreateMsgDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMessage(TaskCreateMsgDTO taskCreateMsgDTO) {
        Map<String, Object> variables = taskCreateMsgDTO.getVariables();
        WorkFlowTaskInfoPO workFlowTaskInfoPO = BeanUtils.instantiate(WorkFlowTaskInfoPO.class);
        workFlowTaskInfoPO.setProcessDefinitionKey(taskCreateMsgDTO.getProcessDefinitionKey());
        workFlowTaskInfoPO.setWorkFlowProcessId(taskCreateMsgDTO.getProcessId());
        workFlowTaskInfoPO.setWorkFlowTaskId(taskCreateMsgDTO.getTaskId());
        workFlowTaskInfoPO.setTaskCode(taskCreateMsgDTO.getMissionId());
        workFlowTaskInfoPO.setTaskDealUserId(variables.get("taskDealUserId").toString());
        workFlowTaskInfoPO.setTaskDealUserName(variables.get("taskDealUserName").toString());
        workFlowTaskInfoPO.setTaskDealTime(new Date());
        workFlowTaskInfoPO.setTaskDealOperation(variables.get("taskDealOperation").toString());
        workFlowTaskInfoPO.setApprovedOpinion(variables.get("approvedOpinion").toString());
        workFlowTaskInfoPO.setCreatedBy(variables.get("taskDealUserId").toString());
        workFlowTaskInfoPO.setCreatedTime(new Date());
        workFlowTaskInfoPO.setWorkFlowTaskType(SalaryGrantWorkFlowEnums.Role.OPERATOR.getName());
        workFlowTaskInfoMapper.insert(workFlowTaskInfoPO);
    }

    /**
     * 任务完成
     * @author chenpb
     * @since 2018-06-27
     * @param taskCompleteMsgDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void taskComplete(TaskCompleteMsgDTO taskCompleteMsgDTO) {
        WorkFlowTaskInfoPO po = BeanUtils.instantiate(WorkFlowTaskInfoPO.class);
        Map<String, Object> variables = taskCompleteMsgDTO.getVariables();
        po.setWorkFlowTaskId(taskCompleteMsgDTO.getTaskId());
        po.setTaskDealUserId(variables.get("taskDealUserId").toString());
        po.setTaskDealUserName(variables.get("taskDealUserName").toString());
        po.setApprovedOpinion(variables.get("approvedOpinion").toString());
        workFlowTaskInfoMapper.updateByTaskId(po);
    }

    /**
     * taskId去重
     * @author chenpb
     * @since 2018-06-27
     * @param taskId
     * @return
     */
    @Override
    public Integer selectWfTaskInfoByTaskId(String taskId) {
        Wrapper<WorkFlowTaskInfoPO> wr = new EntityWrapper<>();
        wr.eq("work_flow_task_id",taskId);
        return  workFlowTaskInfoMapper.selectList(wr).size();
    }
}
