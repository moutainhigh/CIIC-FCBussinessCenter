package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.WorkflowService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskWorkflowMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskWorkflowPO;
import com.ciicsh.gto.sheetservice.api.SheetServiceProxy;
import com.ciicsh.gto.sheetservice.api.dto.Result;
import com.ciicsh.gto.sheetservice.api.dto.request.MissionRequestDTO;
import com.ciicsh.gto.sheetservice.api.dto.response.StartProcessResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    private SheetServiceProxy sheetServiceProxy;

    @Autowired
    private TaskWorkflowMapper taskWorkflowMapper;

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.MANDATORY)
    public boolean startProcess(String missionId, Process process, Map<String, Object> variables) {

        boolean flag = true;

        MissionRequestDTO missionRequestDTO = new MissionRequestDTO();
        missionRequestDTO.setMissionId(missionId);
        missionRequestDTO.setProcessDefinitionKey(process.toString());
        missionRequestDTO.setVariables(variables);
        try {
            Result<StartProcessResponseDTO> result = sheetServiceProxy.startProcess(missionRequestDTO);
            if(result!=null){
                TaskWorkflowPO taskWorkflowPO = new TaskWorkflowPO();
                taskWorkflowPO.setProcessDefinitionKey(process.toString());
                taskWorkflowPO.setProcessId(result.getObject().getProcessId());
                taskWorkflowPO.setMissionId(result.getObject().getMissionId());
                taskWorkflowMapper.insert(taskWorkflowPO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean completeTaskByMissionId(String missionId, Map<String, Object> variables) {
        boolean flag = true;
        return flag;
    }

    @Override
    public boolean completeTaskByTaskId(String taskId, Map<String, Object> variables) {
        boolean flag = true;
        return flag;
    }
}
