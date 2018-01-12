package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskMainProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainProofService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForMainProof;
import com.ciicsh.gto.fcbusinesscenter.tax.util.json.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yuantongqing
 */
@RestController
@RequestMapping("/tax")
public class TaskMainProofController {

    private static final Logger logger = LoggerFactory.getLogger(TaskMainProofController.class);

    @Autowired
    public TaskMainProofService taskMainProofService;

    /**
     * 条件查询完税凭证主任务
     *
     * @param taskMainProofDTO
     * @return
     */
    @PostMapping(value = "/queryTaskMainProofByRes")
    public JsonResult queryTaskMainProofByRes(@RequestBody TaskMainProofDTO taskMainProofDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskMainProofDTO, requestForProof);
            ResponseForMainProof responseForMainProof = taskMainProofService.queryTaskMainProofByRes(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForMainProof);
        } catch (BeansException e) {
            logger.error("queryTaskMainProofByRes error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 新建任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/addTaskProof")
    public JsonResult addTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        //设置创建人
        taskProofDTO.setCreatedBy("admin");
        JsonResult jr = new JsonResult();
        try {
            //完税凭证主任务
            TaskMainProofPO taskMainProofPO = null;
            //完税凭证子任务
            TaskSubProofPO taskSubProofPO = null;
            String dateTimeStr = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            if (taskProofDTO.getManagerName() != null && !"".equals(taskProofDTO.getManagerName())) {
                taskMainProofPO = new TaskMainProofPO();
                BeanUtils.copyProperties(taskProofDTO, taskMainProofPO);
                //设置任务编号
                taskMainProofPO.setTaskNo("TAX" + dateTimeStr);
                //设置任务状态为草稿状态
                taskMainProofPO.setStatus("00");
                //设置修改人
                taskMainProofPO.setModifiedBy("zhangsan");
            }
            dateTimeStr = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            if (taskProofDTO.getDeclareAccount() != null && !"".equals(taskProofDTO.getDeclareAccount())) {
                taskSubProofPO = new TaskSubProofPO();
                BeanUtils.copyProperties(taskProofDTO, taskSubProofPO);
                //设置任务编号
                taskSubProofPO.setTaskNo("TAX" + dateTimeStr);
                //设置任务状态为草稿状态
                taskSubProofPO.setStatus("00");
                //设置修改
                taskSubProofPO.setModifiedBy("zhangsan");
                //设置任务类型
                taskSubProofPO.setTaskType("02");
            }
            taskMainProofService.addTaskProof(taskMainProofPO, taskSubProofPO);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(true);
        } catch (BeansException e) {
            logger.error("addTaskProof error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 修改（即：提交）完税凭证状态
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/updateTaskProof")
    public JsonResult updateTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            //设置修改人
            requestForProof.setModifiedBy("adminMain");
            taskMainProofService.updateTaskProofByRes(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(true);
        } catch (Exception e) {
            logger.error("updateTaskProof error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 将完税凭证任务置为失效
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/invalidTaskProof")
    public JsonResult invalidTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        taskProofDTO.setModifiedBy("admin");
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            taskMainProofService.invalidTaskProof(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(true);
        } catch (Exception e) {
            logger.error("invalidTaskProof error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

}
