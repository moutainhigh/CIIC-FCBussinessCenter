package com.ciicsh.gto.fcsupportcenter.tax.commandservice.host.controller;


import com.ciicsh.gto.fcsupportcenter.tax.commandservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcsupportcenter.tax.commandservice.api.dto.TaskSubProofDTO;
import com.ciicsh.gto.fcsupportcenter.tax.commandservice.business.TaskSubProofService;
import com.ciicsh.gto.fcsupportcenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.request.RequestForProof;
import com.ciicsh.gto.fcsupportcenter.tax.entity.response.voucher.ResponseForSubProof;
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
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(taskSubProofDTOLists);
        } catch (BeansException e) {
            e.printStackTrace();
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 查询子任务信息
     * @param taskSubProofDTO
     * @return
     */
    @RequestMapping(value = "/queryTaskSubProofByRes")
    public JsonResult queryTaskSubProofByRes(@RequestBody TaskSubProofDTO taskSubProofDTO){
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskSubProofDTO, requestForProof);
            //其中管理方名称
            ResponseForSubProof responseForSubProof = taskSubProofService.queryTaskSubProofByRes(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForSubProof);
        } catch (BeansException e) {
            e.printStackTrace();
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }

    }

    /**
     * 根据子任务ID复制其关联数据
     * @param taskSubProofId
     * @return
     */
    @RequestMapping(value = "/copyProofInfoBySubId/{taskSubProofId}")
    public JsonResult copyProofInfoBySubId(@PathVariable Long taskSubProofId){
        JsonResult jr = new JsonResult();
        try {
            Boolean flag = taskSubProofService.copyProofInfoBySubId(taskSubProofId);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(flag);
        } catch (BeansException e) {
            e.printStackTrace();
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 多表查询完税凭证子任务
     * @param taskProofDTO
     * @return
     */
    @RequestMapping(value = "/querySubProofInfoByTaskType")
    public JsonResult querySubProofInfoByTaskType(@RequestBody TaskProofDTO taskProofDTO){
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO,requestForProof);
            ResponseForSubProof responseForSubProof = taskSubProofService.querySubProofInfoByTaskType(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForSubProof);
        } catch (Exception e) {
            e.printStackTrace();
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 合并完税凭证子任务
     * @param taskProofDTO
     * @return
     */
    @RequestMapping(value = "/combineSubTaskProof")
    public JsonResult combineTaskProof(@RequestBody TaskProofDTO taskProofDTO){
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            requestForProof.setModifiedBy("adminMain");
            Boolean flag = taskSubProofService.combineTaskProofByRes(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(flag);
        } catch (Exception e) {
            e.printStackTrace();
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 拆分合并的任务
     * @param taskProofDTO
     * @return
     */
    @RequestMapping(value = "/splitSubTaskProof")
    public JsonResult splitTaskProof(@RequestBody TaskProofDTO taskProofDTO){
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            requestForProof.setModifiedBy("adminMain");
            Boolean flag = taskSubProofService.splitTaskProofByRes(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(flag);
        } catch (Exception e) {
            e.printStackTrace();
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 批量完成完税凭证子任务
     * @param taskProofDTO
     * @return
     */
    @RequestMapping(value = "/completeSubTaskProof")
    public JsonResult completeTaskProof(@RequestBody TaskProofDTO taskProofDTO){
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            requestForProof.setModifiedBy("adminMain");
            requestForProof.setStatus("03");
            Boolean flag = taskSubProofService.completeTaskProofByRes(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(flag);
        } catch (Exception e) {
            e.printStackTrace();
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 批量退回完税凭证子任务
     * @param taskProofDTO
     * @return
     */
    @RequestMapping(value = "/rejectSubTaskProof")
    public JsonResult rejectTaskProof(@RequestBody TaskProofDTO taskProofDTO){
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            requestForProof.setModifiedBy("adminMain");
            requestForProof.setStatus("02");
            Boolean flag = taskSubProofService.rejectTaskProofByRes(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(flag);
        } catch (Exception e) {
            e.printStackTrace();
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 批量失效完税凭证子任务
     * @param taskProofDTO
     * @return
     */
    @RequestMapping(value = "/invalidSubTaskProof")
    public JsonResult invalidTaskProof(@RequestBody TaskProofDTO taskProofDTO){
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            requestForProof.setModifiedBy("adminMain");
            requestForProof.setStatus("04");
            Boolean flag = taskSubProofService.invalidTaskProofByRes(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(flag);
        } catch (Exception e) {
            e.printStackTrace();
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

}
