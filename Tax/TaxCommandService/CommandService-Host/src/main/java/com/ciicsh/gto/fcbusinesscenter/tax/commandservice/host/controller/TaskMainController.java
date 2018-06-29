package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gt1.common.auth.ManagementInfo;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskMainDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskMainDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubsDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.ConstraintService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.*;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskMainDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMainDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.BatchType;
import com.ciicsh.gto.salarymanagementcommandservice.api.BatchProxy;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrBatchDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wuhua
 */
@RestController
public class TaskMainController extends BaseController {


    @Autowired
    private TaskMainService taskMainService;

    @Autowired
    private TaskSubDeclareServiceImpl taskSubDeclareService;

    @Autowired
    private TaskSubMoneyServiceImpl taskSubMoneyService;

    @Autowired
    private TaskSubPaymentServiceImpl taskSubPaymentService;

    @Autowired
    private TaskSubSupplierServiceImpl taskSubSupplierService;

    @Autowired
    private TaskMainDetailServiceImpl taskMainDetailService;

    @Autowired
    private ConstraintService constraintService;

    @Autowired
    private CalculationBatchServiceImpl calculationBatchService;

    @Autowired
    private BatchProxy batchProxy;

    /**
     * 查询主任务列表
     *
     * @param taskMainDTO
     * @return
     */
    @GetMapping(value = "/queryTaskMains")
    public JsonResult<ResponseForTaskMain> queryTaskMains(TaskMainDTO taskMainDTO) {

        JsonResult<ResponseForTaskMain> jr = new JsonResult<>();


        RequestForTaskMain requestForTaskMain = new RequestForTaskMain();
        BeanUtils.copyProperties(taskMainDTO, requestForTaskMain);
        Optional.ofNullable(UserContext.getManagementInfoLists()).ifPresent(managementInfo -> {
            //设置request请求管理方数组
            requestForTaskMain.setManagerNos(managementInfo.stream().map(ManagementInfo::getManagementId).collect(Collectors.toList()).stream().toArray(String[]::new));
        });
        ResponseForTaskMain responseForTaskMain = taskMainService.queryTaskMains(requestForTaskMain);
        jr.fill(responseForTaskMain);

        return jr;
    }

    /**
     * 查询主任务列表
     *
     * @param taskMainDTO
     * @return
     */
    @GetMapping(value = "/queryTaskMainsForDraft")
    public JsonResult<ResponseForTaskMain> queryTaskMainsForDraft(TaskMainDTO taskMainDTO) {

        JsonResult<ResponseForTaskMain> jr = new JsonResult<>();

        RequestForTaskMain requestForTaskMain = new RequestForTaskMain();
        BeanUtils.copyProperties(taskMainDTO, requestForTaskMain);
        Optional.ofNullable(UserContext.getManagementInfoLists()).ifPresent(managementInfo -> {
            //设置request请求管理方数组
            requestForTaskMain.setManagerNos(managementInfo.stream().map(ManagementInfo::getManagementId).collect(Collectors.toList()).stream().toArray(String[]::new));
        });
        ResponseForTaskMain responseForTaskMain = taskMainService.queryTaskMainsForDraft(requestForTaskMain);
        jr.fill(responseForTaskMain);

        return jr;
    }

    /**
     * 查询主任务列表
     *
     * @param taskMainDTO
     * @return
     */
    @GetMapping(value = "/queryTaskMainsForCheck")
    public JsonResult<ResponseForTaskMain> queryTaskMainsForCheck(TaskMainDTO taskMainDTO) {

        JsonResult<ResponseForTaskMain> jr = new JsonResult<>();

        RequestForTaskMain requestForTaskMain = new RequestForTaskMain();
        BeanUtils.copyProperties(taskMainDTO, requestForTaskMain);
        Optional.ofNullable(UserContext.getManagementInfoLists()).ifPresent(managementInfo -> {
            //设置request请求管理方数组
            requestForTaskMain.setManagerNos(managementInfo.stream().map(ManagementInfo::getManagementId).collect(Collectors.toList()).stream().toArray(String[]::new));
        });
        ResponseForTaskMain responseForTaskMain = taskMainService.queryTaskMainsForCheck(requestForTaskMain);
        jr.fill(responseForTaskMain);

        return jr;
    }

    /**
     * 查询子任务集
     *
     * @param taskSubsDTO
     * @return
     */
    @GetMapping(value = "/queryTaskSubs")
    public JsonResult<Map<String, List>> queryTaskSubs(TaskSubsDTO taskSubsDTO) {

        JsonResult<Map<String, List>> jr = new JsonResult<>();

        Map<String, List> map = new HashMap<>();
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("task_main_id", taskSubsDTO.getTaskMainId());
        map.put("sb", taskSubDeclareService.selectByMap(columnMap));//申报子任务
        map.put("hk", taskSubMoneyService.selectByMap(columnMap));//划款子任务
        map.put("jn", taskSubPaymentService.selectByMap(columnMap));//缴纳子任务
        map.put("su", taskSubSupplierService.selectByMap(columnMap));//供应商处理子任务
        jr.fill(map);

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
        //检查约束
        int i = this.constraintService.checkTask(taskMainDTO.getTaskMainIds(),ConstraintService.TASK_MAIN);
        if(i > 0){
            if(i == ConstraintService.C2){
                jr.fill(JsonResult.ReturnCode.CONSTRAINTS_2);
            }else if(i == ConstraintService.C3){
                jr.fill(JsonResult.ReturnCode.CONSTRAINTS_3);
            }
            return jr;
        }

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

        RequestForTaskMain requestForMainTaskMain = new RequestForTaskMain();
        BeanUtils.copyProperties(taskMainDTO, requestForMainTaskMain);
        taskMainService.passTaskMains(requestForMainTaskMain);

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

        //子任务与主任务的状态是否一致
        boolean flag = this.taskMainService.isStatusSame(taskMainDTO.getTaskMainIds(),taskMainDTO.getStatus());
        if(flag){
            RequestForTaskMain requestForMainTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForMainTaskMain);
            taskMainService.invalidTaskMains(requestForMainTaskMain);
        }else{
            jr.fill(JsonResult.ReturnCode.TM_ER03);
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

        RequestForTaskMain requestForMainTaskMain = new RequestForTaskMain();
        BeanUtils.copyProperties(taskMainDTO, requestForMainTaskMain);
        taskMainService.rejectTaskMains(requestForMainTaskMain);

        return jr;
    }
    /**
     * 查询主任务详情列表
     * @param taskMainDTO
     * @return
     */
    @GetMapping(value = "/queryTaskMainDetails")
    public JsonResult<ResponseForTaskMainDetail> queryTaskMainDetails(TaskMainDTO taskMainDTO) {

        JsonResult<ResponseForTaskMainDetail> jr = new JsonResult<>();

        RequestForTaskMain requestForTaskMain = new RequestForTaskMain();
        BeanUtils.copyProperties(taskMainDTO, requestForTaskMain);
        ResponseForTaskMainDetail<TaskMainDetailBO> responseForTaskMainDetail = taskMainService.queryTaskMainDetails(requestForTaskMain);
        jr.fill(responseForTaskMainDetail);

        return jr;
    }

    /**
     * 查询主任务合并的明细列表
     * @param taskMainDTO
     * @return
     */
    @GetMapping(value = "/queryTaskMainDetailsByCombined")
    public JsonResult<ResponseForTaskMainDetail> queryTaskMainDetailsByCombined(TaskMainDTO taskMainDTO) {

        JsonResult<ResponseForTaskMainDetail> jr = new JsonResult<>();

        RequestForTaskMain requestForTaskMain = new RequestForTaskMain();
        BeanUtils.copyProperties(taskMainDTO, requestForTaskMain);
        requestForTaskMain.setIsCombined(true);
        ResponseForTaskMainDetail<TaskMainDetailBO> responseForTaskMainDetail = taskMainService.queryTaskMainDetails(requestForTaskMain);
        jr.fill(responseForTaskMainDetail);

        return jr;
    }
    /**
     * 查询被合并的明细列表
     * @param taskMainDTO
     * @return
     */
    @GetMapping(value = "/queryTaskMainDetailsForCombined")
    public JsonResult<ResponseForTaskMainDetail> queryTaskMainDetailsForCombined(TaskMainDTO taskMainDTO) {

        JsonResult<ResponseForTaskMainDetail> jr = new JsonResult<>();

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.andNew("task_main_id={0}",taskMainDTO.getTaskMainId());
        wrapper.andNew("task_main_detail_id={0}",taskMainDTO.getTaskMainDetailId());
        wrapper.orderBy("id",true);
        ResponseForTaskMainDetail<TaskMainDetailPO> responseForTaskMainDetail = new ResponseForTaskMainDetail();
        responseForTaskMainDetail.setRowList(taskMainDetailService.selectList(wrapper));
        jr.fill(responseForTaskMainDetail);

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

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("id",taskMainDTO.getTaskMainDetailIds());
        TaskMainDetailPO tpo = new TaskMainDetailPO();
        tpo.setCombineConfirmed(true);
        this.taskMainDetailService.update(tpo,wrapper);//更新合并的明细

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

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("id",taskMainDTO.getTaskMainDetailIds());
        TaskMainDetailPO tpo = new TaskMainDetailPO();
        tpo.setCombineConfirmed(false);
        this.taskMainDetailService.update(tpo,wrapper);//更新合并的明细

        return jr;
    }

    /**
     * 更新主任务明细
     * @param taskMainDetailDTO
     * @return
     */
    @PostMapping(value = "/updateTaskMainDetail")
    public JsonResult<Boolean> updateTaskMainDetail(@RequestBody TaskMainDetailDTO taskMainDetailDTO) {

        JsonResult<Boolean> jr = new JsonResult<>();

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.andNew("id={0}",taskMainDetailDTO.getTaskMainDetailId());
        TaskMainDetailPO tmdp = new TaskMainDetailPO();
        tmdp.setDeductRetirementInsurance(taskMainDetailDTO.getDeductRetirementInsurance());
        tmdp.setDeductMedicalInsurance(taskMainDetailDTO.getDeductMedicalInsurance());
        tmdp.setDeductDlenessInsurance(taskMainDetailDTO.getDeductDlenessInsurance());
        tmdp.setDeductHouseFund(taskMainDetailDTO.getDeductHouseFund());
        tmdp.setIncomeTotal(taskMainDetailDTO.getIncomeTotal());
        this.taskMainDetailService.update(tmdp,wrapper);//更新明细

        return jr;
    }

    /**
     * 根据批次号数组查询未来款的批次号数组
     * @param taskMainDTO
     * @return
     */
    @PostMapping(value = "/queryNotReceived")
    public JsonResult<String> queryNotReceived(@RequestBody TaskMainDTO taskMainDTO) {
        JsonResult jr = new JsonResult();
        //根据主任务ID数组获取主任务信息
        EntityWrapper wrapperCal = new EntityWrapper();
        wrapperCal.setEntity(new CalculationBatchPO());
        wrapperCal.and("is_active = {0} ", true);
        wrapperCal.in("batch_no",taskMainDTO.getBatchNos());
        List<CalculationBatchPO> calculationBatchPOS = calculationBatchService.selectList(wrapperCal);
        //判断是否来款
        List<String> listBatchNos = new ArrayList<>();
        for(CalculationBatchPO po : calculationBatchPOS){
            PrBatchDTO prBatchDTO = batchProxy.getBatchInfo(po.getBatchNo(), getBatchTypeOfInt(po.getBatchType()));
            if(!prBatchDTO.isHasMoney() && prBatchDTO.getHasAdvance() == 0){
                listBatchNos.add(po.getBatchNo());
            }
        }
        if(listBatchNos.size() > 0){
            String batchNosStr = listBatchNos.stream().collect(Collectors.joining(", "));
            jr.fill(batchNosStr);
        }else{
            jr.fill("");
        }
        return jr;
    }

    /**
     * 根据批次类型字符型获取批次类型int型
     * @param batchTypeStr
     * @return
     */
    public int getBatchTypeOfInt(String batchTypeStr){
        if(BatchType.normal.getCode().equals(batchTypeStr)){
            return 1;
        }else if(BatchType.ajust.getCode().equals(batchTypeStr)){
            return 2;
        }else if(BatchType.backdate.getCode().equals(batchTypeStr)){
            return 3;
        }else{
            return 1;
        }
    }

}
