package com.ciicsh.gto.fcsupportcenter.tax.queryservice.host.controller;

import com.ciicsh.gto.fcsupportcenter.tax.entity.po.TaskMainProofPO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.request.RequestForProof;
import com.ciicsh.gto.fcsupportcenter.tax.entity.response.voucher.ResponseForMainProof;
import com.ciicsh.gto.fcsupportcenter.tax.queryservice.api.dto.TaskMainProofDTO;
import com.ciicsh.gto.fcsupportcenter.tax.queryservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcsupportcenter.tax.queryservice.business.TaskMainProofService;
import com.ciicsh.gto.fcsupportcenter.tax.util.json.JsonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yuantongqing
 */
@RestController
public class TaskMainProofController {

    @Autowired
    public TaskMainProofService taskMainProofService;

    /**
     * 条件查询完税凭证主任务
     * @param taskMainProofDTO
     * @return
     */
    @RequestMapping(value = "/queryTaskMainProofByRes")
    public JsonResult queryTaskMainProofByRes(@RequestBody TaskMainProofDTO taskMainProofDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskMainProofDTO, requestForProof);
            ResponseForMainProof responseForMainProof = taskMainProofService.queryTaskMainProofByRes(requestForProof);
            jr.setErrorcode("200");
            jr.setErrormsg("success");
            jr.setData(responseForMainProof);
        } catch (BeansException e) {
            e.printStackTrace();
            jr.setErrorcode("500");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 新建任务
     * @param taskProofDTO
     * @return
     */
    @RequestMapping(value = "/addTaskProof")
    public JsonResult addTaskProof(@RequestBody TaskProofDTO taskProofDTO){
        //创建人
        taskProofDTO.setCreatedBy("admin");
        JsonResult jr = new JsonResult();
        try {
            //完税凭证主任务
            TaskMainProofPO taskMainProofPO = null;
            //完税凭证子任务
            TaskSubProofPO taskSubProofPO = null;
            String dateTimeStr = new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date() );
            Boolean flag = true;
            if(taskProofDTO.getManagerName() != null && !"".equals(taskProofDTO.getManagerName())){
                taskMainProofPO = new TaskMainProofPO();
                BeanUtils.copyProperties(taskProofDTO, taskMainProofPO);
                taskMainProofPO.setTaskNo("TAX"+dateTimeStr);
                taskMainProofPO.setStatus("00");
                taskMainProofPO.setModifiedBy("zhangsan");
            }
            dateTimeStr = new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date() );
            if(taskProofDTO.getDeclareAccount() != null && !"".equals(taskProofDTO.getDeclareAccount())){
                taskSubProofPO = new TaskSubProofPO();
                BeanUtils.copyProperties(taskProofDTO, taskSubProofPO);
                taskSubProofPO.setTaskNo("TAX"+dateTimeStr);
                taskSubProofPO.setStatus("00");
                taskSubProofPO.setModifiedBy("zhangsan");
            }
            flag = taskMainProofService.addTaskProof(taskMainProofPO,taskSubProofPO);
            jr.setErrorcode("200");
            jr.setErrormsg("success");
            jr.setData(flag);
        } catch (BeansException e) {
            e.printStackTrace();
            jr.setErrorcode("500");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 修改（即：提交）完税凭证状态
     * @param taskProofDTO
     * @return
     */
    @RequestMapping(value = "/updateTaskProof")
    public JsonResult updateTaskProof(@RequestBody TaskProofDTO taskProofDTO){
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            requestForProof.setModifiedBy("adminMain");
            Boolean flag = taskMainProofService.updateTaskProofByRes(requestForProof);
            jr.setErrorcode("200");
            jr.setErrormsg("success");
            jr.setData(flag);
        } catch (Exception e) {
            e.printStackTrace();
            jr.setErrorcode("500");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 将完税凭证任务置为失效
     * @param taskProofDTO
     * @return
     */
    @RequestMapping(value = "/invalidTaskProof")
    public JsonResult invalidTaskProof(@RequestBody TaskProofDTO taskProofDTO){
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            Boolean flag = taskMainProofService.invalidTaskProof(requestForProof);
            jr.setErrorcode("200");
            jr.setErrormsg("success");
            jr.setData(flag);
        } catch (Exception e) {
            e.printStackTrace();
            jr.setErrorcode("500");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }


}
