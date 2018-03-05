package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskMainDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubsDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.TaskSubDeclareServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.TaskSubMoneyServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.TaskSubPaymentServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.TaskSubSupplierServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMain;
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


    /**
     * 查询主任务列表
     *
     * @param taskMainDTO
     * @return
     */
    @RequestMapping(value = "/queryTaskMains")
    public JsonResult queryTaskMains(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult jr = new JsonResult();

        try {
            RequestForTaskMain requestForTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForTaskMain);
            ResponseForTaskMain responseForTaskMain = taskMainService.queryTaskMains(requestForTaskMain);
            jr.success(responseForTaskMain);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("managerName", taskMainDTO.getManagerName());
            tags.put("batchNo", taskMainDTO.getBatchNo());
            tags.put("tabsName", taskMainDTO.getTabsName());
            //日志工具类返回
            logService.error(e, "TaskMainController.queryTaskMains", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
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
    public JsonResult queryTaskMainsForDraft(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult jr = new JsonResult();

        try {
            RequestForTaskMain requestForTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForTaskMain);
            ResponseForTaskMain responseForTaskMain = taskMainService.queryTaskMainsForDraft(requestForTaskMain);
            jr.success(responseForTaskMain);

        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("managerName", taskMainDTO.getManagerName());
            tags.put("batchNo", taskMainDTO.getBatchNo());
            //日志工具类返回
            logService.error(e, "TaskMainController.queryTaskMainsForDraft", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
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
    public JsonResult queryTaskMainsForCheck(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult jr = new JsonResult();

        try {
            RequestForTaskMain requestForTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForTaskMain);
            ResponseForTaskMain responseForTaskMain = taskMainService.queryTaskMainsForCheck(requestForTaskMain);
            jr.success(responseForTaskMain);

        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("managerName", taskMainDTO.getManagerName());
            tags.put("batchNo", taskMainDTO.getBatchNo());
            //日志工具类返回
            logService.error(e, "TaskMainController.queryTaskMainsForCheck", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
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
    public JsonResult queryTaskSubs(@RequestBody TaskSubsDTO taskSubsDTO) {

        JsonResult jr = new JsonResult();

        try {

            Map<String, List> map = new HashMap<>();

            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("task_main_id", taskSubsDTO.getTaskMainId());
            map.put("sb", taskSubDeclareService.selectByMap(columnMap));//申报子任务
            map.put("hk", taskSubMoneyService.selectByMap(columnMap));//划款子任务
            map.put("jn", taskSubPaymentService.selectByMap(columnMap));//缴纳子任务
            map.put("su", taskSubSupplierService.selectByMap(columnMap));//供应商处理子任务
            jr.success(map);

        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainId", taskSubsDTO.getTaskMainId().toString());
            //日志工具类返回
            logService.error(e, "TaskMainController.queryTaskSubs", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
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
    public JsonResult createMainTask(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult jr = new JsonResult();
        try {
            RequestForTaskMain requestForMainTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForMainTaskMain);
            taskMainService.submitTaskMains(requestForMainTaskMain);
            jr.success(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainIds", taskMainDTO.getTaskMainIds().toString());
            //日志工具类返回
            logService.error(e, "TaskMainController.createMainTask", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
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
    public JsonResult passMainTask(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult jr = new JsonResult();
        try {
            RequestForTaskMain requestForMainTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForMainTaskMain);
            taskMainService.passTaskMains(requestForMainTaskMain);
            jr.success(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainIds", taskMainDTO.getTaskMainIds().toString());
            //日志工具类返回
            logService.error(e, "TaskMainController.passMainTask", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
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
    public JsonResult invalidMainTask(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult jr = new JsonResult();
        try {
            RequestForTaskMain requestForMainTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForMainTaskMain);
            taskMainService.invalidTaskMains(requestForMainTaskMain);
            jr.success(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainIds", taskMainDTO.getTaskMainIds().toString());
            //日志工具类返回
            logService.error(e, "TaskMainController.invalidMainTask", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
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
    public JsonResult rejectMainTask(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult jr = new JsonResult();
        try {
            RequestForTaskMain requestForMainTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForMainTaskMain);
            taskMainService.rejectTaskMains(requestForMainTaskMain);
            jr.success(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainIds", taskMainDTO.getTaskMainIds().toString());
            //日志工具类返回
            logService.error(e, "TaskMainController.rejectMainTask", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
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
    public JsonResult queryTaskMainDetails(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult jr = new JsonResult();
        try {
            RequestForTaskMain requestForTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForTaskMain);
            ResponseForCalBatchDetail responseForCalBatchDetail = taskMainService.queryTaskMainDetails(requestForTaskMain);
            jr.success(responseForCalBatchDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("taskMainId", taskMainDTO.getTaskMainId().toString());
            //日志工具类返回
            logService.error(e, "TaskMainController.queryTaskMainDetails", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "01"), LogType.APP, tags);
            jr.error();
        }

        return jr;
    }


}
