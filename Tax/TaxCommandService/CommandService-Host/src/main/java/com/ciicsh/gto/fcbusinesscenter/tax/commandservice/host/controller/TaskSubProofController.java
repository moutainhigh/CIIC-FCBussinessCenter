package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;


import com.ciicsh.gt1.common.auth.ManagementInfo;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.proxy.TaskSubProofProxy;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.ExportFileService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubProofService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubProofDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.identityservice.api.dto.response.UserInfoResponseDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yuantongqing on 2017/12/12
 */
@RestController
public class TaskSubProofController extends BaseController implements TaskSubProofProxy {

    @Autowired
    private TaskSubProofService taskSubProofService;

    @Autowired
    private ExportFileService exportFileService;

    /**
     * 根据主任务ID查询子任务
     *
     * @param taskMainProofId
     * @return
     */
    @Override
    public JsonResult<List<TaskSubProofDTO>> queryTaskSubProofByMainId(@PathVariable(value = "taskMainProofId") Long taskMainProofId) {
        JsonResult<List<TaskSubProofDTO>> jr = new JsonResult<>();
        try {
            List<TaskSubProofDTO> taskSubProofDTOLists = new ArrayList<>();
            List<TaskSubProofPO> taskSubProofPOLists = taskSubProofService.queryTaskSubProofByMainId(taskMainProofId);
            for (TaskSubProofPO taskSubProofPO : taskSubProofPOLists) {
                TaskSubProofDTO taskSubProofDTO = new TaskSubProofDTO();
                BeanUtils.copyProperties(taskSubProofPO, taskSubProofDTO);
                taskSubProofDTO.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, taskSubProofDTO.getStatus()));
                taskSubProofDTOLists.add(taskSubProofDTO);
            }
            jr.fill(taskSubProofDTOLists);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("mainProofIds", taskMainProofId.toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubProofController.queryTaskSubProofByMainId", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 查询子任务信息
     *
     * @param taskSubProofDTO
     * @return
     */
    @Override
    public JsonResult<ResponseForSubProof> queryTaskSubProofByRes(@RequestBody TaskSubProofDTO taskSubProofDTO) {
        JsonResult<ResponseForSubProof> jr = new JsonResult<>();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskSubProofDTO, requestForProof);
            //其中管理方名称
            ResponseForSubProof responseForSubProof = taskSubProofService.queryTaskSubProofByRes(requestForProof);
            jr.fill(responseForSubProof);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("id", taskSubProofDTO.getId().toString());
            tags.put("declareAccount", taskSubProofDTO.getDeclareAccount());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubProofController.queryTaskSubProofByRes", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 根据子任务ID复制其关联数据
     *
     * @param taskSubProofId
     * @return
     */
    @Override
    public JsonResult<Boolean> copyProofInfoBySubId(@PathVariable(value = "taskSubProofId") Long taskSubProofId) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            //登录信息
            UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
            taskSubProofService.copyProofInfoBySubId(taskSubProofId, userInfoResponseDTO.getLoginName());
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskSubProofId", taskSubProofId.toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubProofController.copyProofInfoBySubId", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 多表查询完税凭证子任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/querySubProofInfoByTaskType")
    public JsonResult<ResponseForSubProof> querySubProofInfoByTaskType(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<ResponseForSubProof> jr = new JsonResult<>();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            Optional.ofNullable(UserContext.getManagementInfoLists()).ifPresent(managementInfo -> {
                //设置request请求管理方名称数组
                requestForProof.setManagerNames(managementInfo.stream().map(ManagementInfo::getManagementName).collect(Collectors.toList()).stream().toArray(String[]::new));
            });
            ResponseForSubProof responseForSubProof = taskSubProofService.querySubProofInfoByTaskType(requestForProof);
            jr.fill(responseForSubProof);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("managerName", taskProofDTO.getManagerName());
            tags.put("declareAccount", taskProofDTO.getDeclareAccount());
            tags.put("status", taskProofDTO.getStatus());
            tags.put("period", taskProofDTO.getPeriod());
            tags.put("taskType", taskProofDTO.getTaskType());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubProofController.querySubProofInfoByTaskType", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 合并完税凭证子任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/combineSubTaskProof")
    public JsonResult<Boolean> combineTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            taskSubProofService.combineTaskProofByRes(requestForProof);
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subProofIds", taskProofDTO.getManagerName());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubProofController.combineTaskProof", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 拆分合并的任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/splitSubTaskProof")
    public JsonResult<Boolean> splitTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            taskSubProofService.splitTaskProofByRes(requestForProof);
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("id", taskProofDTO.getId().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubProofController.splitTaskProof", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 批量完成完税凭证子任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/completeSubTaskProof")
    public JsonResult<Boolean> completeTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            //修改人
            UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
            requestForProof.setModifiedBy(userInfoResponseDTO.getLoginName());
            //任务状态：00:草稿，01:已提交/处理中，02:通过，03:退回，04:已完成，05:已失效
            requestForProof.setStatus("04");
            taskSubProofService.completeTaskProofByRes(requestForProof);
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subProofIds", taskProofDTO.getSubProofIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubProofController.completeTaskProof", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 批量退回完税凭证子任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/rejectSubTaskProof")
    public JsonResult<Boolean> rejectTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            //修改人
            UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
            requestForProof.setModifiedBy(userInfoResponseDTO.getLoginName());
            //任务状态：00:草稿，01:已提交/处理中，02:通过,03:被退回，04:已完成，05:已失效
            requestForProof.setStatus("03");
            taskSubProofService.rejectTaskProofByRes(requestForProof);
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subProofIds", taskProofDTO.getSubProofIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubProofController.rejectTaskProof", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 批量失效完税凭证子任务
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/invalidSubTaskProof")
    public JsonResult<Boolean> invalidTaskProof(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            //修改人
            UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
            requestForProof.setModifiedBy(userInfoResponseDTO.getLoginName());
            //任务状态：00:草稿，01:已提交/处理中，02:通过,03:被退回，04:已完成，05:已失效
            requestForProof.setStatus("05");
            taskSubProofService.invalidTaskProofByRes(requestForProof);
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subProofIds", taskProofDTO.getSubProofIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubProofController.invalidTaskProof", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }


    /**
     * 根据子任务ID查询子任务详细信息
     *
     * @param subProofId
     * @return
     */
    @PostMapping(value = "/queryApplyDetailsBySubId/{subProofId}")
    public JsonResult<TaskSubProofBO> queryApplyDetailsBySubId(@PathVariable(value = "subProofId") Long subProofId) {
        JsonResult<TaskSubProofBO> jr = new JsonResult<>();
        try {
            TaskSubProofBO taskSubProofBO = taskSubProofService.queryApplyDetailsBySubId(subProofId);
            jr.fill(taskSubProofBO);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subProofId", subProofId.toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubProofController.queryApplyDetailsBySubId", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 根据子任务ID分页查询完税凭证子任务申请明细
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/queryTaskSubProofDetailBySubId")
    public JsonResult<ResponseForSubProofDetail> queryTaskSubProofDetailBySubId(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<ResponseForSubProofDetail> jr = new JsonResult<>();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            ResponseForSubProofDetail responseForSubProofDetail = taskSubProofService.queryTaskSubProofDetail(requestForProof);
            jr.fill(responseForSubProofDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("id", taskProofDTO.getId().toString());
            tags.put("employeeNo", taskProofDTO.getEmployeeNo());
            tags.put("employeeName", taskProofDTO.getEmployeeName());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubProofController.queryTaskSubProofDetailBySubId", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 根据任务ID,导出完税凭证申请单
     *
     * @param subProofId
     */
    @RequestMapping(value = "/exportSubTaskProof/{subProofId}", method = RequestMethod.GET)
    public void exportSubTaskProof(@PathVariable(value = "subProofId") Long subProofId, HttpServletResponse response) {

        String fileName = "完税凭证申请.xls";
        try {
            //导出excel
            exportExcel(response, this.exportFileService.exportForProof(subProofId), fileName);
        } catch (Exception e) {
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubProofController.exportSubTaskProof", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, null);
        }
    }


}
