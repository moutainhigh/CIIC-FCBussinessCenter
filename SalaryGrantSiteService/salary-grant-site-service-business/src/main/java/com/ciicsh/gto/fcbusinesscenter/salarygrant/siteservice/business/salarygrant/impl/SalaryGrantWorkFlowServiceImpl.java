package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.ResultGenerator;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantTaskMissionRequestDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantTaskRequestDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantWorkFlowService;
import com.ciicsh.gto.sheetservice.api.SheetServiceProxy;
import com.ciicsh.gto.sheetservice.api.dto.Result;
import com.ciicsh.gto.sheetservice.api.dto.request.MissionRequestDTO;
import com.ciicsh.gto.sheetservice.api.dto.request.TaskRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 薪资发放工作流调用 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-28
 */
@Service
public class SalaryGrantWorkFlowServiceImpl implements SalaryGrantWorkFlowService {
    @Autowired
    private SheetServiceProxy sheetServiceProxy;

    @Override
    public Map startSalaryGrantTaskProcess(SalaryGrantTaskMissionRequestDTO salaryGrantTaskMissionRequestDTO) {
        Map<String,String> startProcessResponseMap = null;
        MissionRequestDTO missionRequestDTO = new MissionRequestDTO();
        missionRequestDTO.setMissionId(salaryGrantTaskMissionRequestDTO.getMissionId());
        missionRequestDTO.setProcessDefinitionKey(salaryGrantTaskMissionRequestDTO.getProcessDefinitionKey());
        missionRequestDTO.setVariables(salaryGrantTaskMissionRequestDTO.getVariables());
        try{
            Result restResult = sheetServiceProxy.startProcess(missionRequestDTO);
            startProcessResponseMap = (Map<String,String>)restResult.getObject();
            com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Result returnResult = ResultGenerator.genSuccessResult(true);
        }catch (Exception e) {
            ResultGenerator.genServerFailResult();
        }
        return startProcessResponseMap;
    }

    @Override
    public String getProcessId(Map startProcessResponseMap) {
        String processId = null;
        //工作流返回的流程编号
        if(startProcessResponseMap != null){
            processId = (String)startProcessResponseMap.get("processId");
        }
        return processId;
    }

    @Override
    public Map completeSalaryGrantTask(SalaryGrantTaskRequestDTO salaryGrantTaskRequestDTO) {
        Map<String,String> completeTaskResponseMap = null;
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO();
        taskRequestDTO.setTaskId(salaryGrantTaskRequestDTO.getTaskId());
        taskRequestDTO.setAssignee(salaryGrantTaskRequestDTO.getAssignee());
        taskRequestDTO.setVariables(salaryGrantTaskRequestDTO.getVariables());
        try {
            Result restResult = sheetServiceProxy.completeTask(taskRequestDTO);
            completeTaskResponseMap = (Map<String,String>)restResult.getObject();
            com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Result returnResult = ResultGenerator.genSuccessResult(true);
        } catch (Exception e) {
            ResultGenerator.genServerFailResult();
        }
        return completeTaskResponseMap;
    }
}
