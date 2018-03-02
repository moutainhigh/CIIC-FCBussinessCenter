package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubMoneyDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubMoneyService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubMoneyPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money.RequestForSubMoney;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money.ResponseForSubMoney;
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
 * @author yuantongqing
 * on create 2018/1/8
 */
@RestController
public class TaskSubMoneyController extends BaseController {


    @Autowired
    private TaskSubMoneyService taskSubMoneyService;

    /**
     * 条件查询划款子任务
     *
     * @param taskSubMoneyDTO
     * @return
     */
    @PostMapping(value = "querySubMoney")
    public JsonResult querySubMoney(@RequestBody TaskSubMoneyDTO taskSubMoneyDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForSubMoney requestForSubMoney = new RequestForSubMoney();
            BeanUtils.copyProperties(taskSubMoneyDTO, requestForSubMoney);
            ResponseForSubMoney responseForSubMoney = taskSubMoneyService.querySubMoney(requestForSubMoney);
            jr.success(responseForSubMoney);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("paymentAccount", taskSubMoneyDTO.getPaymentAccount());
            tags.put("managerName", taskSubMoneyDTO.getManagerName());
            tags.put("statusType", taskSubMoneyDTO.getStatusType());
            //日志工具类返回
            logService.error(e, "TaskSubMoneyController.querySubMoney", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "03"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }


    /**
     * 批量完成划款子任务
     *
     * @param taskSubMoneyDTO
     * @return
     */
    @PostMapping(value = "completeTaskSubMoney")
    public JsonResult completeTaskSubMoney(@RequestBody TaskSubMoneyDTO taskSubMoneyDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForSubMoney requestForSubMoney = new RequestForSubMoney();
            BeanUtils.copyProperties(taskSubMoneyDTO, requestForSubMoney);
            // TODO 临时设置修改人
            //设置修改人
            requestForSubMoney.setModifiedBy("adminTaskSubMoney");
            //任务状态
            requestForSubMoney.setStatus("04");
            taskSubMoneyService.completeTaskSubMoney(requestForSubMoney);
            jr.success();
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subMoneyIds", taskSubMoneyDTO.getSubMoneyIds().toString());
            //日志工具类返回
            logService.error(e, "TaskSubMoneyController.completeTaskSubMoney", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "03"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 批量退回划款子任务
     *
     * @param taskSubMoneyDTO
     * @return
     */
    @PostMapping(value = "rejectTaskSubMoney")
    public JsonResult rejectTaskSubMoney(@RequestBody TaskSubMoneyDTO taskSubMoneyDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForSubMoney requestForSubMoney = new RequestForSubMoney();
            BeanUtils.copyProperties(taskSubMoneyDTO, requestForSubMoney);
            // TODO 临时设置修改人
            //修改人
            requestForSubMoney.setModifiedBy("adminTaskSubMoney");
            //任务状态
            requestForSubMoney.setStatus("03");
            taskSubMoneyService.rejectTaskSubMoney(requestForSubMoney);
            jr.success();
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subMoneyIds", taskSubMoneyDTO.getSubMoneyIds().toString());
            //日志工具类返回
            logService.error(e, "TaskSubMoneyController.rejectTaskSubMoney", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "03"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 根据划款子任务ID查询划款任务信息
     *
     * @param subMoneyId
     * @return
     */
    @PostMapping(value = "/querySubMoneyById/{subMoneyId}")
    public JsonResult querySubMoneyById(@PathVariable Long subMoneyId) {
        JsonResult jr = new JsonResult();
        try {
            TaskSubMoneyPO taskSubMoneyPO = taskSubMoneyService.querySubMoneyById(subMoneyId);
            TaskSubMoneyDTO taskSubMoneyDTO = new TaskSubMoneyDTO();
            BeanUtils.copyProperties(taskSubMoneyPO, taskSubMoneyDTO);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM");
            taskSubMoneyDTO.setPeriod(taskSubMoneyPO.getPeriod().format(formatter));
            jr.success(taskSubMoneyDTO);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subMoneyId", subMoneyId.toString());
            //日志工具类返回
            logService.error(e, "TaskSubMoneyController.querySubMoneyById", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "03"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

}
