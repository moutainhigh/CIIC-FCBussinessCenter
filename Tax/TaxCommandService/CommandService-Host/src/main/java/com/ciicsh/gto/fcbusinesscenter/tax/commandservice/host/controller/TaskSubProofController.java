package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;


import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubProofService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubProofDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.json.JsonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuantongqing on 2017/12/12
 */
@RestController
@RequestMapping("/tax")
public class TaskSubProofController {

    private static final Logger logger = LoggerFactory.getLogger(TaskSubProofController.class);

    @Autowired
    private TaskSubProofService taskSubProofService;

    /**
     * 根据主任务ID查询子任务
     *
     * @param taskMainProofId
     * @return
     */
    @PostMapping(value = "/queryTaskSubProofByMainId/{taskMainProofId}")
    public JsonResult queryTaskSubProofByMainId(@PathVariable Long taskMainProofId) {
        JsonResult jr = new JsonResult();
        try {
            List<TaskSubProofDTO> taskSubProofDTOLists = new ArrayList<>();
            List<TaskSubProofPO> taskSubProofPOLists = taskSubProofService.queryTaskSubProofByMainId(taskMainProofId);
            for (TaskSubProofPO taskSubProofPO : taskSubProofPOLists) {
                TaskSubProofDTO taskSubProofDTO = new TaskSubProofDTO();
                BeanUtils.copyProperties(taskSubProofPO, taskSubProofDTO);
                taskSubProofDTOLists.add(taskSubProofDTO);
            }
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(taskSubProofDTOLists);
        } catch (BeansException e) {
            logger.error("queryTaskSubProofByMainId error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 查询子任务信息
     *
     * @param taskSubProofDTO
     * @return
     */
    @PostMapping(value = "/queryTaskSubProofByRes")
    public JsonResult queryTaskSubProofByRes(@RequestBody TaskSubProofDTO taskSubProofDTO) {
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
            logger.error("queryTaskSubProofByRes error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }

    }

    /**
     * 根据子任务ID复制其关联数据
     *
     * @param taskSubProofId
     * @return
     */
    @PostMapping(value = "/copyProofInfoBySubId/{taskSubProofId}")
    public JsonResult copyProofInfoBySubId(@PathVariable Long taskSubProofId) {
        JsonResult jr = new JsonResult();
        try {
            Boolean flag = taskSubProofService.copyProofInfoBySubId(taskSubProofId);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(flag);
        } catch (BeansException e) {
            logger.error("copyProofInfoBySubId error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 多表查询完税凭证子任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/querySubProofInfoByTaskType")
    public JsonResult querySubProofInfoByTaskType(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            ResponseForSubProof responseForSubProof = taskSubProofService.querySubProofInfoByTaskType(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForSubProof);
        } catch (Exception e) {
            logger.error("querySubProofInfoByTaskType error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 合并完税凭证子任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/combineSubTaskProof")
    public JsonResult combineTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
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
            logger.error("combineTaskProof error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 拆分合并的任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/splitSubTaskProof")
    public JsonResult splitTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
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
            logger.error("splitTaskProof error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 批量完成完税凭证子任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/completeSubTaskProof")
    public JsonResult completeTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
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
            logger.error("completeTaskProof error:" + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 批量退回完税凭证子任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/rejectSubTaskProof")
    public JsonResult rejectTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
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
            logger.error("rejectTaskProof error:" + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 批量失效完税凭证子任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/invalidSubTaskProof")
    public JsonResult invalidTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
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
            logger.error("invalidTaskProof error:" + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }


    /**
     * 根据子任务ID查询子任务详细信息
     *
     * @param subProofId
     * @return
     */
    @PostMapping(value = "/queryApplyDetailsBySubId/{subProofId}")
    public JsonResult queryApplyDetailsBySubId(@PathVariable Long subProofId) {
        JsonResult jr = new JsonResult();
        try {
            TaskSubProofBO taskSubProofBO = taskSubProofService.queryApplyDetailsBySubId(subProofId);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(taskSubProofBO);
        } catch (BeansException e) {
            logger.error("queryApplyDetailsBySubId error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 根据子任务ID分页查询完税凭证子任务申请明细
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/queryTaskSubProofDetailBySubId")
    public JsonResult queryTaskSubProofDetailBySubId(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            ResponseForSubProofDetail responseForSubProofDetail = taskSubProofService.queryTaskSubProofDetail(requestForProof);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForSubProofDetail);
        } catch (Exception e) {
            logger.error("queryTaskSubProofDetailBySubId error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

}
