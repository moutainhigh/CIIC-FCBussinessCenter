package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gt1.common.auth.ManagementInfo;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.ManagerDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskMainDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskMainDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubsDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.*;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskMainDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMainDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wuhua
 */
@RestController
public class TaskMainController extends BaseController {


    @Autowired
    public TaskMainService taskMainService;

    @Autowired
    private TaskSubDeclareServiceImpl taskSubDeclareService;

    @Autowired
    private TaskSubMoneyServiceImpl taskSubMoneyService;

    @Autowired
    private TaskSubPaymentServiceImpl taskSubPaymentService;

    @Autowired
    private TaskSubSupplierServiceImpl taskSubSupplierService;

    @Autowired
    public TaskMainDetailServiceImpl taskMainDetailService;

    /**
     * 查询主任务列表
     *
     * @param taskMainDTO
     * @return
     */
    @RequestMapping(value = "/queryTaskMains")
    public JsonResult<ResponseForTaskMain> queryTaskMains(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult<ResponseForTaskMain> jr = new JsonResult<>();

        try {
            RequestForTaskMain requestForTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForTaskMain);
            Optional.ofNullable(UserContext.getManagementInfoLists()).ifPresent(managementInfo -> {
                //设置request请求管理方名称数组
                requestForTaskMain.setManagerNames(managementInfo.stream().map(ManagementInfo::getManagementName).collect(Collectors.toList()).stream().toArray(String[]::new));
            });
            ResponseForTaskMain responseForTaskMain = taskMainService.queryTaskMains(requestForTaskMain);
            jr.fill(responseForTaskMain);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("managerName", taskMainDTO.getManagerName());
            tags.put("batchNo", taskMainDTO.getBatchNo());
            tags.put("tabsName", taskMainDTO.getTabsName());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainController.queryTaskMains", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }

    /**
     * 查询主任务列表
     *
     * @param taskMainDTO
     * @return
     */
    @RequestMapping(value = "/queryTaskMainsForDraft")
    public JsonResult<ResponseForTaskMain> queryTaskMainsForDraft(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult<ResponseForTaskMain> jr = new JsonResult<>();

        try {
            RequestForTaskMain requestForTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForTaskMain);
            Optional.ofNullable(UserContext.getManagementInfoLists()).ifPresent(managementInfo -> {
                //设置request请求管理方名称数组
                requestForTaskMain.setManagerNames(managementInfo.stream().map(ManagementInfo::getManagementName).collect(Collectors.toList()).stream().toArray(String[]::new));
            });
            ResponseForTaskMain responseForTaskMain = taskMainService.queryTaskMainsForDraft(requestForTaskMain);
            jr.fill(responseForTaskMain);

        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("managerName", taskMainDTO.getManagerName());
            tags.put("batchNo", taskMainDTO.getBatchNo());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainController.queryTaskMainsForDraft", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }

    /**
     * 查询主任务列表
     *
     * @param taskMainDTO
     * @return
     */
    @RequestMapping(value = "/queryTaskMainsForCheck")
    public JsonResult<ResponseForTaskMain> queryTaskMainsForCheck(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult<ResponseForTaskMain> jr = new JsonResult<>();

        try {
            RequestForTaskMain requestForTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForTaskMain);
            Optional.ofNullable(UserContext.getManagementInfoLists()).ifPresent(managementInfo -> {
                //设置request请求管理方名称数组
                requestForTaskMain.setManagerNames(managementInfo.stream().map(ManagementInfo::getManagementName).collect(Collectors.toList()).stream().toArray(String[]::new));
            });
            ResponseForTaskMain responseForTaskMain = taskMainService.queryTaskMainsForCheck(requestForTaskMain);
            jr.fill(responseForTaskMain);

        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("managerName", taskMainDTO.getManagerName());
            tags.put("batchNo", taskMainDTO.getBatchNo());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainController.queryTaskMainsForCheck", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }

    /**
     * 查询子任务集
     *
     * @param taskSubsDTO
     * @return
     */
    @RequestMapping(value = "/queryTaskSubs")
    public JsonResult<Map<String, List>> queryTaskSubs(@RequestBody TaskSubsDTO taskSubsDTO) {

        JsonResult<Map<String, List>> jr = new JsonResult<>();

        try {

            Map<String, List> map = new HashMap<>();

            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("task_main_id", taskSubsDTO.getTaskMainId());
            map.put("sb", taskSubDeclareService.selectByMap(columnMap));//申报子任务
            map.put("hk", taskSubMoneyService.selectByMap(columnMap));//划款子任务
            map.put("jn", taskSubPaymentService.selectByMap(columnMap));//缴纳子任务
            map.put("su", taskSubSupplierService.selectByMap(columnMap));//供应商处理子任务
            jr.fill(map);

        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainId", taskSubsDTO.getTaskMainId().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainController.queryTaskSubs", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }

    /**
     * 提交主任务
     *
     * @param taskMainDTO
     * @return
     */
    @RequestMapping(value = "/submitMainTask")
    public JsonResult<Boolean> submitMainTask(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();
        try {

            //子任务与主任务的状态是否一致
            boolean flag = this.taskMainService.isStatusSame(taskMainDTO.getTaskMainIds(),taskMainDTO.getStatus());
            if(flag){
                EntityWrapper wrapper = new EntityWrapper();
                wrapper.andNew("is_combine_confirmed = {0}",false);
                wrapper.andNew("is_combined = {0}",true);
                wrapper.in("task_main_id",taskMainDTO.getTaskMainIds());
                int count = taskMainDetailService.selectCount(wrapper);
                if(count>0){

                    jr.fill(JsonResult.ReturnCode.TM_ER01);
                }else{
                    RequestForTaskMain requestForMainTaskMain = new RequestForTaskMain();
                    BeanUtils.copyProperties(taskMainDTO, requestForMainTaskMain);
                    taskMainService.submitTaskMains(requestForMainTaskMain);
                }
            }else{
                jr.fill(JsonResult.ReturnCode.TM_ER02);
            }
//            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainIds", taskMainDTO.getTaskMainIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainController.submitMainTask", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }

    /**
     * 审批通过主任务
     *
     * @param taskMainDTO
     * @return
     */
    @RequestMapping(value = "/passMainTask")
    public JsonResult<Boolean> passMainTask(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForTaskMain requestForMainTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForMainTaskMain);
            taskMainService.passTaskMains(requestForMainTaskMain);
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainIds", taskMainDTO.getTaskMainIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainController.passMainTask", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }
    /**
     * 失效主任务
     * @param taskMainDTO
     * @return
     */
    @RequestMapping(value = "/invalidMainTask")
    public JsonResult<Boolean> invalidMainTask(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();
        try {

            //子任务与主任务的状态是否一致
            boolean flag = this.taskMainService.isStatusSame(taskMainDTO.getTaskMainIds(),taskMainDTO.getStatus());
            if(flag){
                RequestForTaskMain requestForMainTaskMain = new RequestForTaskMain();
                BeanUtils.copyProperties(taskMainDTO, requestForMainTaskMain);
                taskMainService.invalidTaskMains(requestForMainTaskMain);
            }else{
                jr.fill(JsonResult.ReturnCode.TM_ER03);
            }
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainIds", taskMainDTO.getTaskMainIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainController.invalidMainTask", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }
    /**
     * 退回主任务
     * @param taskMainDTO
     * @return
     */
    @RequestMapping(value = "/rejectMainTask")
    public JsonResult<Boolean> rejectMainTask(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForTaskMain requestForMainTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForMainTaskMain);
            taskMainService.rejectTaskMains(requestForMainTaskMain);
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainIds", taskMainDTO.getTaskMainIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainController.rejectMainTask", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }
    /**
     * 查询主任务详情列表
     * @param taskMainDTO
     * @return
     */
    @RequestMapping(value = "/queryTaskMainDetails")
    public JsonResult<ResponseForTaskMainDetail> queryTaskMainDetails(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult<ResponseForTaskMainDetail> jr = new JsonResult<>();
        try {
            RequestForTaskMain requestForTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForTaskMain);
            ResponseForTaskMainDetail<TaskMainDetailBO> responseForTaskMainDetail = taskMainService.queryTaskMainDetails(requestForTaskMain);
            jr.fill(responseForTaskMainDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainId", taskMainDTO.getTaskMainId().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainController.queryTaskMainDetails", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }

    /**
     * 查询主任务合并的明细列表
     * @param taskMainDTO
     * @return
     */
    @RequestMapping(value = "/queryTaskMainDetailsByCombined")
    public JsonResult<ResponseForTaskMainDetail> queryTaskMainDetailsByCombined(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult<ResponseForTaskMainDetail> jr = new JsonResult<>();
        try {
            RequestForTaskMain requestForTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForTaskMain);
            requestForTaskMain.setIsCombined(true);
            ResponseForTaskMainDetail<TaskMainDetailBO> responseForTaskMainDetail = taskMainService.queryTaskMainDetails(requestForTaskMain);
            jr.fill(responseForTaskMainDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainId", taskMainDTO.getTaskMainId().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainController.queryTaskMainDetailsByCombined", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }
    /**
     * 查询被合并的明细列表
     * @param taskMainDTO
     * @return
     */
    @RequestMapping(value = "/queryTaskMainDetailsForCombined")
    public JsonResult<ResponseForTaskMainDetail> queryTaskMainDetailsForCombined(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult<ResponseForTaskMainDetail> jr = new JsonResult<>();
        try {
//            RequestForTaskMain requestForTaskMain = new RequestForTaskMain();
//            BeanUtils.copyProperties(taskMainDTO, requestForTaskMain);
            //requestForTaskMain.setIsCombined(true);

            EntityWrapper wrapper = new EntityWrapper();
            wrapper.andNew("task_main_id={0}",taskMainDTO.getTaskMainId());
            wrapper.andNew("task_main_detail_id={0}",taskMainDTO.getTaskMainDetailId());
            wrapper.orderBy("id",true);

            ResponseForTaskMainDetail<TaskMainDetailPO> responseForTaskMainDetail = new ResponseForTaskMainDetail();
            responseForTaskMainDetail.setRowList(taskMainDetailService.selectList(wrapper));
            jr.fill(responseForTaskMainDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainId", taskMainDTO.getTaskMainId().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainController.queryTaskMainDetailsForCombined", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }

    /**
     * 合并明细确认
     * @param taskMainDTO
     * @return
     */
    @RequestMapping(value = "/confirmTaskMainDetailforCombined")
    public JsonResult<Boolean> confirmTaskMainDetailforCombined(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();
        try {

            EntityWrapper wrapper = new EntityWrapper();
            wrapper.in("id",taskMainDTO.getTaskMainDetailIds());
            TaskMainDetailPO tpo = new TaskMainDetailPO();
            tpo.setCombineConfirmed(true);
            this.taskMainDetailService.update(tpo,wrapper);//更新合并的明细
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainDetailIds", taskMainDTO.getTaskMainDetailIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainController.confirmTaskMainDetailforCombined", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }

    /**
     * 合并明细调整
     * @param taskMainDTO
     * @return
     */
    @RequestMapping(value = "/unconfirmTaskMainDetailforCombined")
    public JsonResult<Boolean> unconfirmTaskMainDetailforCombined(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();
        try {

            EntityWrapper wrapper = new EntityWrapper();
            wrapper.in("id",taskMainDTO.getTaskMainDetailIds());
            TaskMainDetailPO tpo = new TaskMainDetailPO();
            tpo.setCombineConfirmed(false);
            this.taskMainDetailService.update(tpo,wrapper);//更新合并的明细
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainDetailIds", taskMainDTO.getTaskMainDetailIds().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainController.unconfirmTaskMainDetailforCombined", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }

    /**
     * 更新主任务明细
     * @param taskMainDetailDTO
     * @return
     */
    @RequestMapping(value = "/updateTaskMainDetail")
    public JsonResult<Boolean> updateTaskMainDetail(@RequestBody TaskMainDetailDTO taskMainDetailDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();
        try {

            EntityWrapper wrapper = new EntityWrapper();
            wrapper.andNew("id={0}",taskMainDetailDTO.getTaskMainDetailId());
            TaskMainDetailPO tmdp = new TaskMainDetailPO();
            tmdp.setDeductRetirementInsurance(taskMainDetailDTO.getDeductRetirementInsurance());
            tmdp.setDeductMedicalInsurance(taskMainDetailDTO.getDeductMedicalInsurance());
            tmdp.setDeductDlenessInsurance(taskMainDetailDTO.getDeductDlenessInsurance());
            tmdp.setDeductHouseFund(taskMainDetailDTO.getDeductHouseFund());
            tmdp.setIncomeTotal(taskMainDetailDTO.getIncomeTotal());
            this.taskMainDetailService.update(tmdp,wrapper);//更新明细
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainDetailId", taskMainDetailDTO.getTaskMainDetailId().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskMainController.updateTaskMainDetail", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }


}
