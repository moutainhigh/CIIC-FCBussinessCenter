package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubPaymentDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubPaymentService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubPaymentPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.payment.RequestForSubPayment;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.payment.ResponseForSubPayment;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yuantongqing on 2018/01/02
 * 缴纳详情返回对象
 */
@RestController
public class TaskSubPaymentController extends BaseController {

    @Autowired
    private TaskSubPaymentService taskSubPaymentService;

    /**
     * 条件查询缴纳子任务
     *
     * @param taskSubPaymentDTO
     * @return
     */
    @PostMapping(value = "querySubPayment")
    public JsonResult<ResponseForSubPayment> querySubPayment(@RequestBody TaskSubPaymentDTO taskSubPaymentDTO) {
        JsonResult<ResponseForSubPayment> jr = new JsonResult<>();
        try {
            RequestForSubPayment requestForSubPayment = new RequestForSubPayment();
            BeanUtils.copyProperties(taskSubPaymentDTO, requestForSubPayment);
            ResponseForSubPayment responseForSubPayment = taskSubPaymentService.querySubPayment(requestForSubPayment);
            jr.fill(responseForSubPayment);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("paymentAccount", taskSubPaymentDTO.getPaymentAccount());
            tags.put("managerName", taskSubPaymentDTO.getManagerName());
            tags.put("statusType", taskSubPaymentDTO.getStatusType());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubPaymentController.querySubPayment", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "04"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }


    /**
     * 批量完成缴纳子任务
     *
     * @param taskSubPaymentDTO
     * @return
     */
    @PostMapping(value = "completeTaskSubPayment")
    public JsonResult<Boolean> completeTaskSubPayment(@RequestBody TaskSubPaymentDTO taskSubPaymentDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForSubPayment requestForSubPayment = new RequestForSubPayment();
            BeanUtils.copyProperties(taskSubPaymentDTO, requestForSubPayment);
            //任务状态:
            requestForSubPayment.setStatus("04");
            taskSubPaymentService.completeTaskSubPayment(requestForSubPayment);
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subPaymentIds", taskSubPaymentDTO.getSubPaymentIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubPaymentController.completeTaskSubPayment", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "04"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 批量退回缴纳子任务
     *
     * @param taskSubPaymentDTO
     * @return
     */
    @PostMapping(value = "rejectTaskSubPayment")
    public JsonResult<Boolean> rejectTaskSubPayment(@RequestBody TaskSubPaymentDTO taskSubPaymentDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForSubPayment requestForSubPayment = new RequestForSubPayment();
            BeanUtils.copyProperties(taskSubPaymentDTO, requestForSubPayment);
            //任务状态
            requestForSubPayment.setStatus("03");
            taskSubPaymentService.rejectTaskSubPayment(requestForSubPayment);
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subPaymentIds", taskSubPaymentDTO.getSubPaymentIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubPaymentController.rejectTaskSubPayment", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "04"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 根据缴纳子任务ID查询缴纳任务信息
     *
     * @param subPaymentId
     * @return
     */
    @PostMapping(value = "/querySubPaymentById/{subPaymentId}")
    public JsonResult<TaskSubPaymentDTO> querySubPaymentById(@PathVariable Long subPaymentId) {
        JsonResult<TaskSubPaymentDTO> jr = new JsonResult<>();
        try {
            TaskSubPaymentPO taskSubPaymentPO = taskSubPaymentService.querySubPaymentById(subPaymentId);
            TaskSubPaymentDTO taskSubPaymentDTO = new TaskSubPaymentDTO();
            BeanUtils.copyProperties(taskSubPaymentPO, taskSubPaymentDTO);
            //个税期间
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM");
            taskSubPaymentDTO.setPeriod(taskSubPaymentPO.getPeriod().format(formatter));
            jr.fill(taskSubPaymentDTO);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subPaymentId", subPaymentId.toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubPaymentController.querySubPaymentById", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "04"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }
}
