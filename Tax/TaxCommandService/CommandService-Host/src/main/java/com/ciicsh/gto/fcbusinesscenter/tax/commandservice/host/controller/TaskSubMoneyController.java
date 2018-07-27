package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gt1.common.auth.ManagementInfo;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubMoneyDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.*;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.TaskMainDetailServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.TaskMainServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskWorkflowHistoryMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubMoneyBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.*;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money.RequestForSubMoney;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money.ResponseForSubMoney;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import com.ciicsh.gto.identityservice.api.dto.response.UserInfoResponseDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.PayapplyServiceProxy;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author yuantongqing
 * on create 2018/1/8
 */
@RestController
public class TaskSubMoneyController extends BaseController {

    public static final String SOURCE_BIG = "1";

    @Autowired
    private TaskSubMoneyService taskSubMoneyService;

    @Autowired
    private PayapplyServiceProxy payapplyServiceProxy;

    @Autowired
    private TaskSubMoneyDetailService taskSubMoneyDetailService;

    @Autowired
    private TaskMainDetailServiceImpl taskMainDetailServiceImpl;

    @Autowired
    private EmployeeInfoBatchService employeeInfoBatchService;

    @Autowired
    private CalculationBatchAccountService calculationBatchAccountService;

    @Autowired
    private TaskWorkflowHistoryMapper taskWorkflowHistoryMapper;

    @Autowired
    private TaskMainServiceImpl taskMainServiceImpl;


    /**
     * 条件查询划款子任务
     *
     * @param taskSubMoneyDTO
     * @return
     */
    @GetMapping(value = "querySubMoney")
    public JsonResult<ResponseForSubMoney> querySubMoney(TaskSubMoneyDTO taskSubMoneyDTO) {
        JsonResult<ResponseForSubMoney> jr = new JsonResult<>();

        RequestForSubMoney requestForSubMoney = new RequestForSubMoney();
        BeanUtils.copyProperties(taskSubMoneyDTO, requestForSubMoney);
        Optional.ofNullable(UserContext.getManagementInfoLists()).ifPresent(managementInfo -> {
            //设置request请求管理方数组
            requestForSubMoney.setManagerNos(managementInfo.stream().map(ManagementInfo::getManagementId).collect(Collectors.toList()).stream().toArray(String[]::new));
        });
        ResponseForSubMoney responseForSubMoney = taskSubMoneyService.querySubMoney(requestForSubMoney);
        jr.fill(responseForSubMoney);

        return jr;
    }


    /**
     * 批量完成划款子任务
     *
     * @param taskSubMoneyDTO
     * @return
     */
    @PostMapping(value = "completeTaskSubMoney")
    public JsonResult<Boolean> completeTaskSubMoney(@RequestBody TaskSubMoneyDTO taskSubMoneyDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();

        RequestForSubMoney requestForSubMoney = new RequestForSubMoney();
        BeanUtils.copyProperties(taskSubMoneyDTO, requestForSubMoney);
        //任务状态
        requestForSubMoney.setStatus("04");
        taskSubMoneyService.completeTaskSubMoney(requestForSubMoney);

        return jr;
    }

    /**
     * 批量退回划款子任务
     *
     * @param taskSubMoneyDTO
     * @return
     */
    @PostMapping(value = "rejectTaskSubMoney")
    public JsonResult<Boolean> rejectTaskSubMoney(@RequestBody TaskSubMoneyDTO taskSubMoneyDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();

        RequestForSubMoney requestForSubMoney = new RequestForSubMoney();
        BeanUtils.copyProperties(taskSubMoneyDTO, requestForSubMoney);
        //任务状态
        requestForSubMoney.setStatus("03");
        Boolean flag = taskSubMoneyService.rejectTaskSubMoney(requestForSubMoney);
        if(!flag){
            jr.fill(JsonResult.ReturnCode.BILLCENTER_2);
        }
        return jr;
    }

    /**
     * 根据划款子任务ID查询划款任务信息
     *
     * @param subMoneyId
     * @return
     */
    @GetMapping(value = "/querySubMoneyById/{subMoneyId}")
    public JsonResult<TaskSubMoneyDTO> querySubMoneyById(@PathVariable Long subMoneyId) {
        JsonResult<TaskSubMoneyDTO> jr = new JsonResult<>();

        TaskSubMoneyPO taskSubMoneyPO = taskSubMoneyService.querySubMoneyById(subMoneyId);
        TaskSubMoneyDTO taskSubMoneyDTO = new TaskSubMoneyDTO();
        BeanUtils.copyProperties(taskSubMoneyPO, taskSubMoneyDTO);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM");
        taskSubMoneyDTO.setPeriod(taskSubMoneyPO.getPeriod().format(formatter));
        jr.fill(taskSubMoneyDTO);

        return jr;
    }

    /**
     * 划款接口
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/taxPayment/{id}")
    public JsonResult<Boolean> taxPayment(@PathVariable Long id) {
        JsonResult<Boolean> jr = new JsonResult<>();
        TaskSubMoneyPO taskSubMoneyPO = taskSubMoneyService.querySubMoneyById(id);
        //获取主任务信息
        TaskMainPO taskMainPO = taskMainServiceImpl.selectById(taskSubMoneyPO.getTaskMainId());
        EntityWrapper historyWrapper = new EntityWrapper();
        historyWrapper.and("mission_id = {0}",taskMainPO.getTaskNo());
        historyWrapper.and("operation_type = {0}", WorkflowService.operationType.completeTask.toString());
        historyWrapper.orderBy("modified_time",false);
        List<TaskWorkflowHistoryPO> taskWorkflowHistoryPOS = taskWorkflowHistoryMapper.selectList(historyWrapper);
        //根据扣缴义务人编码查询批次账户信息
        CalculationBatchAccountPO calculationBatchAccountPO = calculationBatchAccountService.getCalculationBatchAccountInfoByAccountNo(taskSubMoneyPO.getPaymentAccount());
        //调用结算中心上海个税接口
        PayApplyProxyDTO payApplyProxyDTO = new PayApplyProxyDTO();
        //部门名称
        payApplyProxyDTO.setDepartmentName("财务咨询业务中心");
        //是否财务部0:否:1:是
        payApplyProxyDTO.setIsFinancedept(0);
        //业务类型6:大库个税,13:独立库个税
        if (SOURCE_BIG.equals(calculationBatchAccountPO.getSource())) {
            payApplyProxyDTO.setBusinessType(6);
            //付款账号ID(大库时不为空)
            payApplyProxyDTO.setPayAccountId(calculationBatchAccountPO.getBankAccountId());
        } else {
            payApplyProxyDTO.setBusinessType(13);
            //付款银行（独立库时必填）1建行，12，中信，5其他
            if (calculationBatchAccountPO.getBelongBankType() != null && 1 == calculationBatchAccountPO.getBelongBankType()) {
                payApplyProxyDTO.setPayBankId(12);
            } else if (calculationBatchAccountPO.getBelongBankType() != null && 2 == calculationBatchAccountPO.getBelongBankType()) {
                payApplyProxyDTO.setPayBankId(1);
            } else if (calculationBatchAccountPO.getBelongBankType() != null && 3 == calculationBatchAccountPO.getBelongBankType()) {
                payApplyProxyDTO.setPayBankId(5);
            } else {
                payApplyProxyDTO.setPayBankId(5);
            }
            //收款账号ID(独立库时不为空)
            payApplyProxyDTO.setReceiveAccountId(calculationBatchAccountPO.getBankAccountId());
        }
        //业务方主键ID
        payApplyProxyDTO.setBusinessPkId(id);
        //付款方式1:现金:2:支票:3:转账:4:微信:5:支付宝;
        payApplyProxyDTO.setPayWay(3);
        //申请支付金额
        payApplyProxyDTO.setPayAmount(taskSubMoneyPO.getTaxAmount());
        //收款方名称(纳税人名称)
        payApplyProxyDTO.setReceiver(calculationBatchAccountPO.getTaxpayerName());
        //申请人
        UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
        payApplyProxyDTO.setApplyer(userInfoResponseDTO.getDisplayName());
        //申请日期(yyyy-MM-dd)
        payApplyProxyDTO.setApplyDate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
        //TODO 付款用途
        payApplyProxyDTO.setPayPurpose("个税");
        //TODO 付款原因
        payApplyProxyDTO.setPayReason("个税");
        //财务账套（1AF,2FC,3BPO）
        payApplyProxyDTO.setFinanceAccountCode(2);
        //总经理president
        //分管领导leader
        //部门经理departmentManager
        //审核人reviewer
        payApplyProxyDTO.setReviewer(taskWorkflowHistoryPOS.size() > 0 ? taskWorkflowHistoryPOS.get(0).getOwnerName() : "");
        //根据任务ID查询所有雇员
        List<TaskSubMoneyDetailPO> taskSubMoneyDetailPOList = taskSubMoneyDetailService.querySubMonetDetailsBySubMoneyId(id);
        //划款明细计算批次明细ID
        List<Long> batchIds = taskSubMoneyDetailPOList.stream().map(TaskSubMoneyDetailPO::getCalculationBatchDetailId).collect(Collectors.toList());
        //根据划款明细计算批次明细ID查询雇员个税信息
        List<EmployeeInfoBatchPO> employeeInfoBatchPOList = employeeInfoBatchService.queryEmployeeInfoBatchesByBatchIds(batchIds);
        //公司编号为key的雇员信息map
        Map<String, List<EmployeeInfoBatchPO>> companyMap =
                employeeInfoBatchPOList.stream().collect(groupingBy(EmployeeInfoBatchPO::getCompanyNo));
        Map<Long, String> batchDetailIdMap = employeeInfoBatchPOList.stream().collect(Collectors.toMap(EmployeeInfoBatchPO::getCalBatchDetailId, EmployeeInfoBatchPO::getCompanyNo));
        //设置公司信息companyList
        List<PayapplyCompanyProxyDTO> companyList = new ArrayList<>();
        for (String key : companyMap.keySet()) {
            //根据key获取对应的雇员个税信息
            List<EmployeeInfoBatchPO> employeeInfoBatchPOS = companyMap.get(key);
            List<Long> taskSubMoneyDetailIdsList = employeeInfoBatchPOS.stream().map(EmployeeInfoBatchPO::getCalBatchDetailId).collect(Collectors.toList());
            List<TaskSubMoneyDetailPO> taskSubMoneyDetailPOS = taskSubMoneyDetailService.querySubMonetDetailsByBatchDetailIds(taskSubMoneyDetailIdsList);
            //根据划款明细对象集合求出个税总金额
            BigDecimal taxAmountSum = taskSubMoneyDetailPOS.stream()
                    //将TaskSubMoneyDetailPO对象的tax_amount取出来map为Bigdecimal
                    .map(TaskSubMoneyDetailPO::getTaxReal)
                    //使用reduce聚合函数,实现累加器
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            PayapplyCompanyProxyDTO payapplyCompanyProxyDTO = new PayapplyCompanyProxyDTO();
            payapplyCompanyProxyDTO.setCompanyId(key);
            //公司名称
            if (employeeInfoBatchPOS.size() > 0) {
                payapplyCompanyProxyDTO.setCompanyName(employeeInfoBatchPOS.get(0).getCompanyName());
            }
            payapplyCompanyProxyDTO.setPayMonth(DateTimeFormatter.ofPattern("yyyy-MM").format(taskSubMoneyPO.getPeriod()));
            payapplyCompanyProxyDTO.setPayAmount(taxAmountSum);
            companyList.add(payapplyCompanyProxyDTO);
        }
        payApplyProxyDTO.setCompanyList(companyList);
        //所有薪酬计算批次号集合
        List<String> batchNoListAll = new ArrayList<>();
        //设置雇员信息employeeList
        List<PayapplyEmployeeProxyDTO> employeeList = new ArrayList<>();
        for (TaskSubMoneyDetailPO taskSubMoneyDetailPO : taskSubMoneyDetailPOList) {
            //获取公司编号
            String companyNo = taskSubMoneyDetailPO.getCalculationBatchDetailId() == null ? "" : batchDetailIdMap.getOrDefault(taskSubMoneyDetailPO.getCalculationBatchDetailId(), "");
            PayapplyEmployeeProxyDTO payapplyEmployeeProxyDTO = new PayapplyEmployeeProxyDTO();
            payapplyEmployeeProxyDTO.setEmployeeId(taskSubMoneyDetailPO.getEmployeeNo());
            payapplyEmployeeProxyDTO.setEmployeeName(taskSubMoneyDetailPO.getEmployeeName());
            payapplyEmployeeProxyDTO.setCompanyId(companyNo);
            payapplyEmployeeProxyDTO.setPayMonth(DateTimeFormatter.ofPattern("yyyy-MM").format(taskSubMoneyDetailPO.getPeriod()));
            payapplyEmployeeProxyDTO.setPayAmount(taskSubMoneyDetailPO.getTaxReal());
            //薪酬计算批次号集合
            List<String> batchCodeList = new ArrayList<>();
            //设置雇员薪酬计算批次
            if (StrKit.notBlank(taskSubMoneyDetailPO.getBatchNo())) {
                batchCodeList.add(taskSubMoneyDetailPO.getBatchNo());
                batchNoListAll.add(taskSubMoneyDetailPO.getBatchNo());
            } else {
                //根据task_main_detail_id 查询 batch_no taskMainDetailServiceImpl
                Long taskMainDetailId = taskSubMoneyDetailPO.getTaskMainDetailId();
                EntityWrapper wrapper = new EntityWrapper();
                wrapper.setEntity(new TaskMainDetailPO());
                wrapper.and(" task_main_detail_id = {0}", taskMainDetailId);
                wrapper.and("is_active = {0} ", true);
                //根据task_main_detail_id获取主任务明细集合
                List<TaskMainDetailPO> taskMainDetailPOS = taskMainDetailServiceImpl.selectList(wrapper);
                List<String> batchNoList = taskMainDetailPOS.stream().map(TaskMainDetailPO::getBatchNo).collect(Collectors.toList());
                batchCodeList.addAll(batchNoList);
                batchNoListAll.addAll(batchNoList);
            }
            //设置批次号
            payapplyEmployeeProxyDTO.setBatchCodeList(batchCodeList);
            employeeList.add(payapplyEmployeeProxyDTO);
        }
        payApplyProxyDTO.setEmployeeList(employeeList);
        //执行划款
        com.ciicsh.gto.settlementcenter.payment.cmdapi.common.JsonResult paymentJr = payapplyServiceProxy.shIncomeTax(payApplyProxyDTO);
        if ("0".equals(paymentJr.getCode())) {
            //更改划款任务状态以及划款凭单号
            PayApplyProxyDTO payApplyProxyDTO1 = (PayApplyProxyDTO) paymentJr.getData();
            //付款申请ID
            int payApplyId = payApplyProxyDTO1.getPayapplyId();
            //付款凭证编号(结算单编号)
            String payApplyCode = payApplyProxyDTO1.getPayapplyCode();
            try {
                TaskSubMoneyBO taskSubMoneyBO = new TaskSubMoneyBO();
                taskSubMoneyBO.setId(id);
                taskSubMoneyBO.setPayStatus("01");
                taskSubMoneyBO.setPayApplyId((long) payApplyId);
                taskSubMoneyBO.setPayApplyCode(payApplyCode);
                taskSubMoneyService.updateTaskSubMoneyById(taskSubMoneyBO);
            } catch (Exception e) {
                //日志工具类返回
                LogTaskFactory.getLogger().error(e, "TaskSubMoneyController.taxPayment", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "03"), LogType.APP, null);
                jr.error();
            }
        } else {
            jr.fill(JsonResult.ReturnCode.SETTLEMENT_1);
        }
        return jr;
    }

    /**
     * 打印划款凭单
     *
     * @param id
     */
    @GetMapping(value = "/printVoucher/{id}")
    public void printVoucher(@PathVariable Long id, HttpServletResponse response) {

        TaskSubMoneyPO taskSubMoneyPO = taskSubMoneyService.querySubMoneyById(id);
        com.ciicsh.gto.settlementcenter.payment.cmdapi.common.JsonResult jr = payapplyServiceProxy.downloadPayVoucher(taskSubMoneyPO.getPayApplyCode());
        if ("0".equals(jr.getCode())) {
            byte[] bs = jr.getContents();
            super.downloadFile(response, "划款凭证.xlsx", bs);
        }

    }

    /**
     * 更新滞纳金、罚金
     *
     * @param taskSubMoneyDTO
     * @return
     */
    @PostMapping(value = "/updateTaskSubMoney")
    public JsonResult<Boolean> updateTaskSubMoney(@RequestBody TaskSubMoneyDTO taskSubMoneyDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        taskSubMoneyService.updateTaskSubMoneyOverdueAndFine(taskSubMoneyDTO.getId(), taskSubMoneyDTO.getOverdue(), taskSubMoneyDTO.getFine());
        return jr;
    }

    /**
     * 根据划款子任务ID判断滞纳金和罚金是否已经全部来款
     * @param subMoneyId
     * @return
     */
    @GetMapping(value = "/hasMoneyBySubMoneyId/{subMoneyId}")
    public JsonResult<Boolean> hasMoneyBySubMoneyId(@PathVariable Long subMoneyId){
        JsonResult<Boolean> jr = new JsonResult<Boolean>();
        List<Long> subMoneyIdList = new ArrayList<>();
        subMoneyIdList.add(subMoneyId);
        //将list转为数组
        Long[] subMoneyIds = subMoneyIdList.toArray(new Long[subMoneyIdList.size()]);
        DisposableChargeProxyDTO disposableChargeProxyDTO = taskSubMoneyService.whetherHasMoneyBySubMoneyIds(subMoneyIds);
        List<DisposableChargeDetailProxyDTO> list = disposableChargeProxyDTO.getList();
        //筛选出未来款集合result为false
        List<DisposableChargeDetailProxyDTO> hasNoMoneyList = list.stream().filter(item -> !item.getResult()).collect(Collectors.toList());
        if(hasNoMoneyList.size() > 0){
            jr.setData(false);
        }else{
            jr.setData(true);
        }
        return jr;
    }

}
