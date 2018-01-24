package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubMoneyDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubMoneyService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubMoneyPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money.RequestForSubMoney;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money.ResponseForSubMoney;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
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
 * @author yuantongqing
 * on create 2018/1/8
 */
@RestController
public class TaskSubMoneyController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TaskSubMoneyController.class);

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
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForSubMoney);
        } catch (Exception e) {
            logger.error("querySubMoney error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
            requestForSubMoney.setStatus("03");
            taskSubMoneyService.completeTaskSubMoney(requestForSubMoney);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(true);
        } catch (Exception e) {
            logger.error("completeTaskSubMoney error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
            requestForSubMoney.setStatus("02");
            taskSubMoneyService.rejectTaskSubMoney(requestForSubMoney);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(true);
        } catch (Exception e) {
            logger.error("rejectTaskSubMoney error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
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
            taskSubMoneyDTO.setPeriod(DateTimeKit.format(taskSubMoneyPO.getPeriod(), "YYYY-MM"));
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(taskSubMoneyDTO);
        } catch (Exception e) {
            logger.error("querySubMoneyById error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        }
        return jr;
    }

}
