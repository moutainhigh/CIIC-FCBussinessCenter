package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gt1.common.auth.ManagementInfo;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubPaymentDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubPaymentService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubPaymentPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.payment.RequestForSubPayment;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.payment.ResponseForSubPayment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

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

        RequestForSubPayment requestForSubPayment = new RequestForSubPayment();
        BeanUtils.copyProperties(taskSubPaymentDTO, requestForSubPayment);
        Optional.ofNullable(UserContext.getManagementInfoLists()).ifPresent(managementInfo -> {
            //设置request请求管理方名称数组
            requestForSubPayment.setManagerNames(managementInfo.stream().map(ManagementInfo::getManagementName).collect(Collectors.toList()).stream().toArray(String[]::new));
        });
        ResponseForSubPayment responseForSubPayment = taskSubPaymentService.querySubPayment(requestForSubPayment);
        jr.fill(responseForSubPayment);

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

        RequestForSubPayment requestForSubPayment = new RequestForSubPayment();
        BeanUtils.copyProperties(taskSubPaymentDTO, requestForSubPayment);
        //任务状态:
        requestForSubPayment.setStatus("04");
        taskSubPaymentService.completeTaskSubPayment(requestForSubPayment);

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

        RequestForSubPayment requestForSubPayment = new RequestForSubPayment();
        BeanUtils.copyProperties(taskSubPaymentDTO, requestForSubPayment);
        //任务状态
        requestForSubPayment.setStatus("03");
        taskSubPaymentService.rejectTaskSubPayment(requestForSubPayment);

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

        TaskSubPaymentPO taskSubPaymentPO = taskSubPaymentService.querySubPaymentById(subPaymentId);
        TaskSubPaymentDTO taskSubPaymentDTO = new TaskSubPaymentDTO();
        BeanUtils.copyProperties(taskSubPaymentPO, taskSubPaymentDTO);
        //个税期间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM");
        taskSubPaymentDTO.setPeriod(taskSubPaymentPO.getPeriod().format(formatter));
        jr.fill(taskSubPaymentDTO);

        return jr;
    }
}
