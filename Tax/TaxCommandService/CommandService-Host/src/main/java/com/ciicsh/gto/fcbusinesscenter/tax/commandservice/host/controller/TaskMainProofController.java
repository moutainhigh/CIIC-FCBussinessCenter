package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gt1.common.auth.ManagementInfo;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskMainProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.proxy.TaskMainProofProxy;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainProofService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.TaskNoService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForMainProof;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yuantongqing
 */
@RestController
public class TaskMainProofController extends BaseController implements TaskMainProofProxy {

    @Autowired
    public TaskMainProofService taskMainProofService;

    @Autowired
    public TaskNoService taskNoService;

    /**
     * 条件查询完税凭证主任务
     *
     * @param taskMainProofDTO
     * @return
     */
    @Override
    public JsonResult<ResponseForMainProof> queryTaskMainProofByRes(@RequestBody TaskMainProofDTO taskMainProofDTO) {
        JsonResult<ResponseForMainProof> jr = new JsonResult<>();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskMainProofDTO, requestForProof);
            Optional.ofNullable(UserContext.getManagementInfo()).ifPresent(managementInfo -> {
                //设置request请求管理方名称数组
                requestForProof.setManagerNames(managementInfo.stream().map(ManagementInfo::getManagementName).collect(Collectors.toList()).stream().toArray(String[]::new));
            });
            ResponseForMainProof responseForMainProof = taskMainProofService.queryTaskMainProofByRes(requestForProof);
            jr.fill(responseForMainProof);
        } catch (Exception e) {
            //标签
            Map<String, String> tags = new HashMap<>(16);
            tags.put("managerName", taskMainProofDTO.getManagerName());
            tags.put("submitTimeStart", taskMainProofDTO.getSubmitTimeStart());
            tags.put("submitTimeEnd", taskMainProofDTO.getSubmitTimeEnd());
            tags.put("currentNum", taskMainProofDTO.getCurrentNum() + "");
            tags.put("pageSize", taskMainProofDTO.getPageSize() + "");
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainProofController.queryTaskMainProofByRes", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            //错误信息返回
            jr.error();
        }
        return jr;
    }

    /**
     * 新建任务
     *
     * @param taskProofDTO
     * @return
     */
    @Override
    public JsonResult<Boolean> addTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            //完税凭证主任务
            TaskMainProofPO taskMainProofPO = null;
            //完税凭证子任务
            TaskSubProofPO taskSubProofPO = null;
            if (taskProofDTO.getManagerNo() != null && !"".equals(taskProofDTO.getManagerNo())) {
                taskMainProofPO = new TaskMainProofPO();
                BeanUtils.copyProperties(taskProofDTO, taskMainProofPO);
                //设置任务编号
                taskMainProofPO.setTaskNo(taskNoService.getTaskNo(TaskNoService.TASK_MAIN_PROOF));
                //设置任务状态为草稿状态
                taskMainProofPO.setStatus("00");
            }
            if (taskProofDTO.getDeclareAccount() != null && !"".equals(taskProofDTO.getDeclareAccount())) {
                taskSubProofPO = new TaskSubProofPO();
                BeanUtils.copyProperties(taskProofDTO, taskSubProofPO);
                //设置任务编号
                taskSubProofPO.setTaskNo(taskNoService.getTaskNo(TaskNoService.TASK_SUB_PROOF));
                //设置任务状态为草稿状态
                taskSubProofPO.setStatus("00");
                //设置任务类型
                taskSubProofPO.setTaskType("02");
            }
            taskMainProofService.addTaskProof(taskMainProofPO, taskSubProofPO);
            ////jr.fill(true);
        } catch (Exception e) {
            //标签
            Map<String, String> tags = new HashMap<>(16);
            tags.put("managerNo", taskProofDTO.getManagerNo());
            tags.put("managerName", taskProofDTO.getManagerName());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainProofController.addTaskProof", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            //错误信息返回
            jr.error();
        }
        return jr;
    }

    /**
     * 修改（即：提交）完税凭证状态
     *
     * @param taskProofDTO
     * @return
     */
    @Override
    @PostMapping(value = "/updateTaskProof")
    public JsonResult<Boolean> updateTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            taskMainProofService.updateTaskProofByRes(requestForProof);
            ////jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("mainProofIds", taskProofDTO.getMainProofIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainProofController.updateTaskProof", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 将完税凭证任务置为失效
     *
     * @param taskProofDTO
     * @return
     */
    @Override
    @PostMapping(value = "/invalidTaskProof")
    public JsonResult<Boolean> invalidTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            taskMainProofService.invalidTaskProof(requestForProof);
            ////jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("mainProofIds", taskProofDTO.getMainProofIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainProofController.invalidTaskProof", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

}
