package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.WorkflowService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskWorkflowHistoryMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskWorkflowMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskWorkflowHistoryPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskWorkflowPO;
import com.ciicsh.gto.identityservice.api.dto.response.UserInfoResponseDTO;
import com.ciicsh.gto.sheetservice.api.SheetServiceProxy;
import com.ciicsh.gto.sheetservice.api.dto.Result;
import com.ciicsh.gto.sheetservice.api.dto.request.MissionRequestDTO;
import com.ciicsh.gto.sheetservice.api.dto.request.TaskRequestDTO;
import com.ciicsh.gto.sheetservice.api.dto.response.StartProcessResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    private SheetServiceProxy sheetServiceProxy;

    @Autowired
    private TaskWorkflowMapper taskWorkflowMapper;

    @Autowired
    private TaskWorkflowHistoryMapper taskWorkflowHistoryMapper;

    //启动工作流
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.MANDATORY)
    public boolean startProcess(String missionId, String processDef, Map<String, Object> variables) {

        boolean flag = true;

        MissionRequestDTO missionRequestDTO = new MissionRequestDTO();
        missionRequestDTO.setMissionId(missionId);
        missionRequestDTO.setProcessDefinitionKey(processDef);
        missionRequestDTO.setVariables(variables);
        try {
            Result<StartProcessResponseDTO> result = sheetServiceProxy.startProcess(missionRequestDTO);
            if(result!=null){
                TaskWorkflowHistoryPO taskWorkflowHistoryPO = new TaskWorkflowHistoryPO();
                taskWorkflowHistoryPO.setProcessDefinitionKey(processDef);//流程定义key
                taskWorkflowHistoryPO.setProcessId(result.getObject().getProcessId());//流程id
                taskWorkflowHistoryPO.setMissionId(result.getObject().getMissionId());//业务编号
                taskWorkflowHistoryPO.setOperationType(operationType.startProcess.toString());//处理类型
                taskWorkflowHistoryPO.setOwnerCode(UserContext.getUser()==null?null:UserContext.getUser().getLoginName());//操作人登录名
                taskWorkflowHistoryPO.setOwnerName(UserContext.getUser()==null?null:UserContext.getUser().getDisplayName());//操作人显示名
                taskWorkflowHistoryPO.setVariables(variables==null?null:variables.toString());//参数
                taskWorkflowHistoryMapper.insert(taskWorkflowHistoryPO);//记录流程创建信息
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }



    //创建任务
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTask(String taskId,String taskType,String missionId,String processId,String processDefinitionKey,String assumer,String assumeType){
        //工作流表
        TaskWorkflowPO taskWorkflowPO = new TaskWorkflowPO();
        taskWorkflowPO.setMissionId(missionId);
        taskWorkflowPO.setProcessDefinitionKey(processDefinitionKey);
        taskWorkflowPO.setProcessId(processId);
        taskWorkflowPO.setTaskId(taskId);
        taskWorkflowPO.setTaskType(taskType);
        taskWorkflowPO.setAssumerCode(assumer);
        taskWorkflowPO.setAssumerType(assumeType);
        taskWorkflowMapper.insert(taskWorkflowPO);
        //工作流执行历史表
        TaskWorkflowHistoryPO taskWorkflowHistoryPO = new TaskWorkflowHistoryPO();
        taskWorkflowHistoryPO.setProcessDefinitionKey(processDefinitionKey);
        taskWorkflowHistoryPO.setProcessId(processId);
        taskWorkflowHistoryPO.setMissionId(missionId);
        taskWorkflowHistoryPO.setTaskType(taskType);
        taskWorkflowHistoryPO.setOperationType(operationType.createTask.toString());
        taskWorkflowHistoryPO.setTaskId(taskId);
        taskWorkflowHistoryMapper.insert(taskWorkflowHistoryPO);
    }

    //完成任务
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeTask(String missionId , Map<String, Object> variables){


        EntityWrapper wrapperForFlow = new EntityWrapper();
        wrapperForFlow.eq("mission_id",missionId);
        List<TaskWorkflowPO> ListForFlow = taskWorkflowMapper.selectList(wrapperForFlow);
        TaskWorkflowPO taskWorkflowPO = ListForFlow.get(0);
        String taskId = taskWorkflowPO.getTaskId();

        //登录信息
        UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO();
        taskRequestDTO.setTaskId(taskId);
        taskRequestDTO.setAssignee(userInfoResponseDTO==null?null:userInfoResponseDTO.getLoginName());
        taskRequestDTO.setVariables(variables);

        try {
            Result<Boolean> res = sheetServiceProxy.completeTask(taskRequestDTO);
            //完成任务：1.删除工作流表记录，2：更新工作流历史表记录
            if(res!=null && res.getObject()!=null && res.getObject()==true){
                //1
                EntityWrapper wrapper = new EntityWrapper();
                wrapper.eq("task_id",taskId);
                taskWorkflowMapper.delete(wrapper);

                //2
                TaskWorkflowHistoryPO taskWorkflowHistoryPO = new TaskWorkflowHistoryPO();
                taskWorkflowHistoryPO.setOperationType(operationType.completeTask.toString());
                taskWorkflowHistoryPO.setOwnerCode(userInfoResponseDTO==null?null:userInfoResponseDTO.getLoginName());
                taskWorkflowHistoryPO.setOwnerName(userInfoResponseDTO==null?null:userInfoResponseDTO.getDisplayName());
                taskWorkflowHistoryPO.setVariables(variables==null?null:variables.toString());
                EntityWrapper wrapper2 = new EntityWrapper();
                wrapper2.eq("task_id",taskId);
                taskWorkflowHistoryMapper.update(taskWorkflowHistoryPO,wrapper2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
