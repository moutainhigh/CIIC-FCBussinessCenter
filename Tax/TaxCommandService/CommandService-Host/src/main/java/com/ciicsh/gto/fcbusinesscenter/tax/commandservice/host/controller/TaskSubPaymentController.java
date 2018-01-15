package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubPaymentDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubPaymentService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubPaymentPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.payment.RequestForSubPayment;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.payment.ResponseForSubPayment;
import com.ciicsh.gto.fcbusinesscenter.tax.util.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.DateTimeKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuantongqing on 2018/01/02
 * 缴纳详情返回对象
 */
@RestController
public class TaskSubPaymentController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(TaskSubPaymentController.class);

    @Autowired
    private TaskSubPaymentService taskSubPaymentService;

    /**
     * 条件查询缴纳子任务
     * @param taskSubPaymentDTO
     * @return
     */
    @PostMapping(value = "querySubPayment")
    public JsonResult querySubPayment(@RequestBody TaskSubPaymentDTO taskSubPaymentDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForSubPayment requestForSubPayment = new RequestForSubPayment();
            BeanUtils.copyProperties(taskSubPaymentDTO,requestForSubPayment);
            ResponseForSubPayment responseForSubPayment = taskSubPaymentService.querySubPayment(requestForSubPayment);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForSubPayment);
        } catch (Exception e) {
            logger.error("querySubPayment error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }


    /**
     * 批量完成缴纳子任务
     * @param taskSubPaymentDTO
     * @return
     */
    @PostMapping(value = "completeTaskSubPayment")
    public JsonResult completeTaskSubPayment(@RequestBody TaskSubPaymentDTO taskSubPaymentDTO){
        JsonResult jr = new JsonResult();
        try {
            RequestForSubPayment requestForSubPayment = new RequestForSubPayment();
            BeanUtils.copyProperties(taskSubPaymentDTO,requestForSubPayment);
            //修改人
            requestForSubPayment.setModifiedBy("adminTaskSubPayment");
            //任务状态:21:已提交/处理中，22:被退回，23:已完成，24:已失效
            requestForSubPayment.setStatus("23");
            taskSubPaymentService.completeTaskSubPayment(requestForSubPayment);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(true);
        } catch (Exception e) {
            logger.error("completeTaskSubPayment error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 批量退回缴纳子任务
     * @param taskSubPaymentDTO
     * @return
     */
    @PostMapping(value = "rejectTaskSubPayment")
    public JsonResult rejectTaskSubPayment(@RequestBody TaskSubPaymentDTO taskSubPaymentDTO){
        JsonResult jr = new JsonResult();
        try {
            RequestForSubPayment requestForSubPayment = new RequestForSubPayment();
            BeanUtils.copyProperties(taskSubPaymentDTO,requestForSubPayment);
            //修改人
            requestForSubPayment.setModifiedBy("adminTaskSubPayment");
            //任务状态:21:已提交/处理中，22:被退回，23:已完成，24:已失效
            requestForSubPayment.setStatus("22");
            taskSubPaymentService.rejectTaskSubPayment(requestForSubPayment);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(true);
        } catch (Exception e) {
            logger.error("rejectTaskSubPayment error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 根据缴纳子任务ID查询缴纳任务信息
     * @param subPaymentId
     * @return
     */
    @PostMapping(value = "/querySubPaymentById/{subPaymentId}")
    public JsonResult querySubPaymentById(@PathVariable Long subPaymentId){
        JsonResult jr = new JsonResult();
        try {
            TaskSubPaymentPO taskSubPaymentPO = taskSubPaymentService.querySubPaymentById(subPaymentId);
            TaskSubPaymentDTO taskSubPaymentDTO = new TaskSubPaymentDTO();
            BeanUtils.copyProperties(taskSubPaymentPO,taskSubPaymentDTO);
            //个税期间
            taskSubPaymentDTO.setPeriod(DateTimeKit.format(taskSubPaymentPO.getPeriod(),"YYYY-MM"));
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(taskSubPaymentDTO);
        } catch (Exception e) {
            logger.error("querySubPaymentById error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
             return jr;
        }
    }
}
