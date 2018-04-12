package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubSupplierDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubSupplierDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubSupplierService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.intercept.LoginInfoHolder;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubSupplierPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.support.RequestForTaskSubSupplier;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.support.ResponseForTaskSubSupplier;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.identityservice.api.dto.response.UserInfoResponseDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuantongqing  on 2018/02/09
 */
@RestController
public class TaskSubSupplierController extends BaseController {

    @Autowired
    private TaskSubSupplierService taskSubSupplierService;

    @Autowired
    private TaskSubSupplierDetailService taskSubSupplierDetailService;

    /**
     * 查询供应商子任务列表
     *
     * @param taskSubSupplierDTO
     * @return
     */
    @PostMapping(value = "/queryTaskSubSupplier")
    public JsonResult<ResponseForTaskSubSupplier> queryTaskSubSupplier(@RequestBody TaskSubSupplierDTO taskSubSupplierDTO) {
        JsonResult<ResponseForTaskSubSupplier> jr = new JsonResult<>();
        try {
            RequestForTaskSubSupplier requestForTaskSubSupplier = new RequestForTaskSubSupplier();
            BeanUtils.copyProperties(taskSubSupplierDTO, requestForTaskSubSupplier);
            ResponseForTaskSubSupplier responseForTaskSubSupplier = taskSubSupplierService.queryTaskSubSupplier(requestForTaskSubSupplier);
            jr.fill(responseForTaskSubSupplier);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("declareAccount", taskSubSupplierDTO.getDeclareAccount());
            tags.put("managerName", taskSubSupplierDTO.getManagerName());
            tags.put("period", taskSubSupplierDTO.getPeriod());
            tags.put("periodType", taskSubSupplierDTO.getPeriodType());
            tags.put("supportName", taskSubSupplierDTO.getSupportName());
            tags.put("statusType", taskSubSupplierDTO.getStatusType());
            tags.put("accountType", taskSubSupplierDTO.getAccountType());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubSupplierController.queryTaskSubSupplier", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "07"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }


    /**
     * 根据供应商子任务ID查询供应商信息
     *
     * @param subSupplierId
     * @return
     */
    @PostMapping(value = "/querySupplierDetailsById/{subSupplierId}")
    public JsonResult<TaskSubSupplierPO> querySupplierDetailsById(@PathVariable(value = "subSupplierId") Long subSupplierId) {
        JsonResult<TaskSubSupplierPO> jr = new JsonResult();
        try {
            //根据供应商子任务ID查询供应商信息
            TaskSubSupplierPO taskSubSupplierPO = taskSubSupplierService.querySupplierDetailsById(subSupplierId);
            jr.fill(taskSubSupplierPO);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subSupplierId", subSupplierId.toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubSupplierController.querySupplierDetailsById", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "07"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 合并全国委托供应商任务
     *
     * @param taskSubSupplierDTO
     * @return
     */
    @PostMapping(value = "/mergeTaskSubSuppliers")
    public JsonResult<Boolean> mergeTaskSubSuppliers(@RequestBody TaskSubSupplierDTO taskSubSupplierDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForTaskSubSupplier requestForTaskSubSupplier = new RequestForTaskSubSupplier();
            BeanUtils.copyProperties(taskSubSupplierDTO, requestForTaskSubSupplier);
            //修改人
            UserInfoResponseDTO userInfoResponseDTO = LoginInfoHolder.get().getResult().getObject();
            requestForTaskSubSupplier.setModifiedBy(userInfoResponseDTO.getLoginName());
            taskSubSupplierService.mergeTaskSubSuppliers(requestForTaskSubSupplier);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subSupplierIds", taskSubSupplierDTO.getSubSupplierIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubSupplierController.mergeTaskSubSuppliers", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "07"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 拆分供应商子任务
     *
     * @param taskSubSupplierDTO
     * @return
     */
    @PostMapping(value = "splitSubSupplier")
    public JsonResult<Boolean> splitSubSupplier(@RequestBody TaskSubSupplierDTO taskSubSupplierDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForTaskSubSupplier requestForTaskSubSupplier = new RequestForTaskSubSupplier();
            BeanUtils.copyProperties(taskSubSupplierDTO, requestForTaskSubSupplier);
            //修改人
            UserInfoResponseDTO userInfoResponseDTO = LoginInfoHolder.get().getResult().getObject();
            requestForTaskSubSupplier.setModifiedBy(userInfoResponseDTO.getLoginName());
            taskSubSupplierService.splitSubSupplier(requestForTaskSubSupplier, "only");
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("id", taskSubSupplierDTO.getId().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubSupplierController.splitSubDeclare", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "07"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 根据ID查询合并之前的供应商子任务
     *
     * @param mergeId
     * @return
     */
    @PostMapping(value = "/queryTaskSubSupplierByMergeId/{mergeId}")
    public JsonResult<List<TaskSubSupplierDTO>> queryTaskSubSupplierByMergeId(@PathVariable(value = "mergeId") Long mergeId) {
        JsonResult<List<TaskSubSupplierDTO>> jr = new JsonResult<>();
        try {
            List<TaskSubSupplierDTO> taskSubSupplierDTOList = new ArrayList<>();
            List<TaskSubSupplierPO> taskSubSupplierPOList = taskSubSupplierService.queryTaskSubSupplierByMergeId(mergeId);
            for (TaskSubSupplierPO taskSubSupplierPO : taskSubSupplierPOList) {
                TaskSubSupplierDTO taskSubSupplierDTO = new TaskSubSupplierDTO();
                BeanUtils.copyProperties(taskSubSupplierPO, taskSubSupplierDTO);
                if (taskSubSupplierPO.getPeriod() != null) {
                    taskSubSupplierDTO.setPeriod(DateTimeFormatter.ofPattern("yyyy-MM").format(taskSubSupplierPO.getPeriod()));
                }
                taskSubSupplierDTOList.add(taskSubSupplierDTO);
            }
            jr.fill(taskSubSupplierDTOList);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("mergeId", mergeId.toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubSupplierController.queryTaskSubSupplierByMergeId", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "07"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 批量完成供应商任务
     *
     * @param taskSubSupplierDTO
     * @return
     */
    @PostMapping(value = "/completeTaskSuppliers")
    public JsonResult<Boolean> completeTaskSuppliers(@RequestBody TaskSubSupplierDTO taskSubSupplierDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            int count = 0;
            if(taskSubSupplierDTO.getSubHasCombinedSupplierIds().length > 0){
                //根据有合并明细的供应商ID查询未确认的数目
                count = taskSubSupplierDetailService.selectCount(taskSubSupplierDTO.getSubHasCombinedSupplierIds());
            }
            if (count > 0) {
                jr.fill(JsonResult.ReturnCode.SU_ER01);
            }else{
                RequestForTaskSubSupplier requestForTaskSubSupplier = new RequestForTaskSubSupplier();
                BeanUtils.copyProperties(taskSubSupplierDTO, requestForTaskSubSupplier);
                //修改人
                UserInfoResponseDTO userInfoResponseDTO = LoginInfoHolder.get().getResult().getObject();
                requestForTaskSubSupplier.setModifiedBy(userInfoResponseDTO.getLoginName());
                //任务状态
                requestForTaskSubSupplier.setStatus("04");
                taskSubSupplierService.completeTaskSubSupplier(requestForTaskSubSupplier);
            }
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subSupplierIds", taskSubSupplierDTO.getSubSupplierIds().toString());
            tags.put("subHasCombinedSupplierIds", taskSubSupplierDTO.getSubHasCombinedSupplierIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubSupplierController.completeTaskSuppliers", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "07"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }


    /**
     * 批量退回供应商任务
     *
     * @param taskSubSupplierDTO
     * @return
     */
    @PostMapping(value = "/rejectTaskSuppliers")
    public JsonResult<Boolean> rejectTaskSuppliers(@RequestBody TaskSubSupplierDTO taskSubSupplierDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForTaskSubSupplier requestForTaskSubSupplier = new RequestForTaskSubSupplier();
            BeanUtils.copyProperties(taskSubSupplierDTO, requestForTaskSubSupplier);
            //任务状态
            requestForTaskSubSupplier.setStatus("03");
            taskSubSupplierService.rejectTaskSuppliers(requestForTaskSubSupplier);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subSupplierIds", taskSubSupplierDTO.getSubSupplierIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubSupplierController.rejectTaskSuppliers", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "07"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }
}
