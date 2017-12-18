package com.ciicsh.gto.fcsupportcenter.tax.queryservice.host.controller;


import com.ciicsh.gto.fcsupportcenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.request.RequestForProof;
import com.ciicsh.gto.fcsupportcenter.tax.entity.response.voucher.ResponseForSubProof;
import com.ciicsh.gto.fcsupportcenter.tax.queryservice.api.dto.TaskSubProofDTO;
import com.ciicsh.gto.fcsupportcenter.tax.queryservice.business.TaskSubProofService;
import com.ciicsh.gto.fcsupportcenter.tax.util.json.JsonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuantongqing on 2017/12/12
 */
@RestController
public class TaskSubProofController {

    @Autowired
    private TaskSubProofService taskSubProofService;

    /**
     * 根据主任务ID查询子任务
     * @param taskMainProofId
     * @return
     */
    @RequestMapping(value = "/queryTaskSubProofByMainId/{taskMainProofId}")
    public JsonResult queryTaskSubProofByMainId(@PathVariable Long taskMainProofId){
        JsonResult jr = new JsonResult();
        try {
            List<TaskSubProofDTO> taskSubProofDTOLists = new ArrayList<>();
            List<TaskSubProofPO> taskSubProofPOLists = taskSubProofService.queryTaskSubProofByMainId(taskMainProofId);
            for (TaskSubProofPO taskSubProofPO:taskSubProofPOLists){
                TaskSubProofDTO taskSubProofDTO = new TaskSubProofDTO();
                BeanUtils.copyProperties(taskSubProofPO,taskSubProofDTO);
                taskSubProofDTOLists.add(taskSubProofDTO);
            }
            jr.setErrorcode("200");
            jr.setErrormsg("success");
            jr.setData(taskSubProofDTOLists);
        } catch (BeansException e) {
            e.printStackTrace();
            jr.setErrorcode("500");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    @RequestMapping(value = "/queryTaskSubProofByRes")
    public JsonResult queryTaskSubProofByRes(@RequestBody TaskSubProofDTO taskSubProofDTO){
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskSubProofDTO, requestForProof);
            //其中管理方名称
            ResponseForSubProof responseForSubProof = taskSubProofService.queryTaskSubProofByRes(requestForProof);
            jr.setErrorcode("200");
            jr.setErrormsg("success");
            jr.setData(responseForSubProof);
        } catch (BeansException e) {
            e.printStackTrace();
            jr.setErrorcode("500");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }

    }

}
