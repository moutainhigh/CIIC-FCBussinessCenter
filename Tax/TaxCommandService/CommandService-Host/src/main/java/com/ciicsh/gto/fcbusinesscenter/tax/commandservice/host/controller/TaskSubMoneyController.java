package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gt1.common.auth.ManagementInfo;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubMoneyDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.*;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.TaskMainDetailServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubMoneyBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.*;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money.RequestForSubMoney;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money.ResponseForSubMoney;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import com.ciicsh.gto.identityservice.api.dto.response.UserInfoResponseDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.PayapplyServiceProxy;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.SalaryServiceProxy;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author yuantongqing
 * on create 2018/1/8
 */
@RestController
public class TaskSubMoneyController extends BaseController {

    public static final String SOURCE_BIG = "0";

    @Autowired
    private TaskSubMoneyService taskSubMoneyService;

    @Autowired
    private PayapplyServiceProxy payapplyServiceProxy;

    @Autowired
    private SalaryServiceProxy salaryServiceProxy;

    @Autowired
    private TaskSubMoneyDetailService taskSubMoneyDetailService;

    @Autowired
    private TaskMainDetailServiceImpl taskMainDetailServiceImpl;

    @Autowired
    private EmployeeInfoBatchService employeeInfoBatchService;

    @Autowired
    private CalculationBatchAccountService calculationBatchAccountService;

    @Autowired
    private CalculationBatchService calculationBatchService;

    @Autowired
    private CalculationBatchDetailService calculationBatchDetailService;


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
        taskSubMoneyService.rejectTaskSubMoney(requestForSubMoney);

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
        try {
            TaskSubMoneyPO taskSubMoneyPO = taskSubMoneyService.querySubMoneyById(id);
            //根据扣缴义务人编码查询批次账户信息
            CalculationBatchAccountPO calculationBatchAccountPO = calculationBatchAccountService.getCalculationBatchAccountInfoByAccountNo(taskSubMoneyPO.getPaymentAccount());
            //调用结算中心上海个税接口
            PayApplyProxyDTO payApplyProxyDTO = new PayApplyProxyDTO();
            //部门名称
            payApplyProxyDTO.setDepartmentName("财务咨询业务中心");
            //是否财务部0:否:1:是
            payApplyProxyDTO.setIsFinancedept(0);
            //业务类型6:独立库个税,13:大库个税
            if(SOURCE_BIG.equals(calculationBatchAccountPO.getSource())){
                payApplyProxyDTO.setBusinessType(13);
            }else{
                payApplyProxyDTO.setBusinessType(6);
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
            payApplyProxyDTO.setApplyer(userInfoResponseDTO.getLoginName());
            //申请日期(yyyy-MM-dd)
            payApplyProxyDTO.setApplyDate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
            //TODO 付款用途
            payApplyProxyDTO.setPayPurpose("个税");
            //TODO 付款原因
            payApplyProxyDTO.setPayReason("个税");
            //TODO 收款账号ID
            payApplyProxyDTO.setReceiveAccountId(12);
            //收款账号
            payApplyProxyDTO.setReceiveAccount(calculationBatchAccountPO.getAccount());
            //总经理president
            //分管领导leader
            //部门经理departmentManager
            //审核人reviewer
            //payApplyProxyDTO.setReviewer("admin");
            //根据任务ID查询所有雇员
            List<TaskSubMoneyDetailPO> taskSubMoneyDetailPOList = taskSubMoneyDetailService.querySubMonetDetailsBySubMoneyId(id);
            //划款明细计算批次明细ID
            List<Long> batchIds = taskSubMoneyDetailPOList.stream().map(TaskSubMoneyDetailPO::getCalculationBatchDetailId).collect(Collectors.toList());
            //根据划款明细计算批次明细ID查询雇员个税信息
            List<EmployeeInfoBatchPO> employeeInfoBatchPOList = employeeInfoBatchService.queryEmployeeInfoBatchesByBatchIds(batchIds);
            //公司编号为key的雇员信息map
            Map<String, List<EmployeeInfoBatchPO>> companyMap =
                    employeeInfoBatchPOList.stream().collect(groupingBy(EmployeeInfoBatchPO::getCompanyNo));
            Map<Long,String> batchDetailIdMap = employeeInfoBatchPOList.stream().collect(Collectors.toMap(EmployeeInfoBatchPO::getCalBatchDetailId,EmployeeInfoBatchPO ::getCompanyNo));
            //设置公司信息companyList
            List<PayapplyCompanyProxyDTO> companyList = new ArrayList<>();
            for (String key : companyMap.keySet()) {
                //根据key获取对应的雇员个税信息
                List<EmployeeInfoBatchPO> employeeInfoBatchPOS = companyMap.get(key);
                List<Long> taskSubMoneyDetailIdsList = employeeInfoBatchPOS.stream().map(EmployeeInfoBatchPO :: getCalBatchDetailId).collect(Collectors.toList());
                List<TaskSubMoneyDetailPO> taskSubMoneyDetailPOS = taskSubMoneyDetailService.querySubMonetDetailsByBatchDetailIds(taskSubMoneyDetailIdsList);
                //根据划款明细对象集合求出个税总金额
                BigDecimal taxAmountSum = taskSubMoneyDetailPOS.stream()
                        //将TaskSubMoneyDetailPO对象的tax_amount取出来map为Bigdecimal
                        .map(TaskSubMoneyDetailPO::getTaxAmount)
                        //使用reduce聚合函数,实现累加器
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                PayapplyCompanyProxyDTO payapplyCompanyProxyDTO = new PayapplyCompanyProxyDTO();
                payapplyCompanyProxyDTO.setCompanyId(key);
                //公司名称
                if(employeeInfoBatchPOS.size() > 0 ){
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
                String companyNo = taskSubMoneyDetailPO.getCalculationBatchDetailId() == null ? "" : batchDetailIdMap.getOrDefault(taskSubMoneyDetailPO.getCalculationBatchDetailId(),"");
                PayapplyEmployeeProxyDTO payapplyEmployeeProxyDTO = new PayapplyEmployeeProxyDTO();
                payapplyEmployeeProxyDTO.setEmployeeId(taskSubMoneyDetailPO.getEmployeeNo());
                payapplyEmployeeProxyDTO.setEmployeeName(taskSubMoneyDetailPO.getEmployeeName());
                payapplyEmployeeProxyDTO.setCompanyId(companyNo);
                payapplyEmployeeProxyDTO.setPayMonth(DateTimeFormatter.ofPattern("yyyy-MM").format(taskSubMoneyDetailPO.getPeriod()));
                payapplyEmployeeProxyDTO.setPayAmount(taskSubMoneyDetailPO.getTaxAmount());
                //薪酬计算批次号集合
                List<String> batchCodeList = new ArrayList<>();
                //设置雇员薪酬计算批次
                if(StrKit.notBlank(taskSubMoneyDetailPO.getBatchNo())){
                    batchCodeList.add(taskSubMoneyDetailPO.getBatchNo());
                    batchNoListAll.add(taskSubMoneyDetailPO.getBatchNo());
                }else{
                    //根据task_main_detail_id 查询 batch_no taskMainDetailServiceImpl
                    Long taskMainDetailId = taskSubMoneyDetailPO.getTaskMainDetailId();
                    EntityWrapper wrapper = new EntityWrapper();
                    wrapper.setEntity(new TaskMainDetailPO());
                    wrapper.and(" task_main_detail_id = {0}",taskMainDetailId);
                    wrapper.and("is_active = {0} ", true);
                    //根据task_main_detail_id获取主任务明细集合
                    List<TaskMainDetailPO> taskMainDetailPOS = taskMainDetailServiceImpl.selectList(wrapper);
                    List<String> batchNoList = taskMainDetailPOS.stream().map(TaskMainDetailPO :: getBatchNo).collect(Collectors.toList());
                    batchCodeList.addAll(batchNoList);
                    batchNoListAll.addAll(batchNoList);
                }
                //设置批次号
                payapplyEmployeeProxyDTO.setBatchCodeList(batchCodeList);
                employeeList.add(payapplyEmployeeProxyDTO);
            }
            payApplyProxyDTO.setEmployeeList(employeeList);

            List<String> disBatchNoList = batchNoListAll.stream().distinct().collect(Collectors.toList());
            SalaryBatchDTO salaryBatchDTO = new SalaryBatchDTO();
            salaryBatchDTO.setBatchCodeList(disBatchNoList);
            List<String> unExistBatchNoList = new ArrayList<>();
            //验证薪酬批次号是否存在
            com.ciicsh.gto.settlementcenter.payment.cmdapi.common.JsonResult batchNoExistResult = salaryServiceProxy.notExistsBatchCodeList(salaryBatchDTO);
            if ("0".equals(batchNoExistResult.getCode())) {
                SalaryBatchDTO salaryBatchDTO1 = (SalaryBatchDTO)batchNoExistResult.getData();
                unExistBatchNoList = salaryBatchDTO1.getBatchCodeList();
            }else{
                jr.error();
            }
            //不存在的薪酬批次号工资清单数据(即打卡操作)
            for (String batchNo : unExistBatchNoList){
                //根据批次号查询计算批次信息
                CalculationBatchPO calculationBatchPO = calculationBatchService.queryCalculationBatchPOByBatchNo(batchNo);
                //调用薪资发放接口获取数据，再进行打卡
                SalaryBatchDTO salaryBatchDTO1 = new SalaryBatchDTO();
                //计算批次号
                salaryBatchDTO1.setBatchCode(batchNo);
                //管理方ID
                salaryBatchDTO1.setManagementId(calculationBatchPO.getManagerNo());
                //管理方名称
                salaryBatchDTO1.setManagementName(calculationBatchPO.getManagerName());
                //TODO 缴费月份（计算批次的月份）
                //工资清单总额（计算批次的总额）
                salaryBatchDTO1.setTotalAmount(calculationBatchPO.getTaxAmount());
                //雇员总数（计算批次的雇员数量）
                salaryBatchDTO1.setTotalEmployeeCount(calculationBatchPO.getHeadcount());

                //TODO
                //本批次将付金额（计算批次里正常的雇员的实发工资之和）
                //本批次将付雇员数量（正常的雇员）
                //申请人
                salaryBatchDTO1.setApplyer(userInfoResponseDTO.getLoginName());
                //申请日期
                salaryBatchDTO1.setApplyDate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
                //TODO 财务代理费
                //TODO 状态0:待上传;2:已打卡
                //批次中所有雇员
                //步骤一:根据批次ID获取批次明细信息
                List<CalculationBatchDetailPO> calculationBatchDetailPOList = calculationBatchDetailService.queryCalculationBatchDetailByBatchId(calculationBatchPO.getId());
                List<Long> batchDetailIds = calculationBatchDetailPOList.stream().map(CalculationBatchDetailPO::getId).collect(Collectors.toList());
                //步骤二:根据批次明细ID筛选出相关雇员
                List<TaskSubMoneyDetailPO> taskSubMoneyDetailPOListAboutBatch = taskSubMoneyDetailPOList.stream().filter(item -> batchDetailIds.contains(item.getCalculationBatchDetailId())).collect(Collectors.toList());
                List<SalaryEmployeeDTO> salaryEmployeeDTOS = new ArrayList<>();
                for(TaskSubMoneyDetailPO taskSubMoneyDetailPO:taskSubMoneyDetailPOListAboutBatch){
                    EmployeeInfoBatchPO employeeInfoBatchPO = employeeInfoBatchPOList.stream().filter(item -> item.getCalBatchDetailId().equals(taskSubMoneyDetailPO.getCalculationBatchDetailId())).findFirst().orElse(null);
                    CalculationBatchAccountPO calculationBatchAccountPO1 = calculationBatchAccountService.getCalculationBatchAccountInfoByAccountNo(taskSubMoneyDetailPO.getPayAccount());
                    //
                    SalaryEmployeeDTO salaryEmployeeDTO = new SalaryEmployeeDTO();
                    //公司ID
                    salaryEmployeeDTO.setCompanyId(employeeInfoBatchPO.getCompanyNo());
                    //公司名称
                    salaryEmployeeDTO.setCompanyName(employeeInfoBatchPO.getCompanyName());
                    //雇员ID
                    salaryEmployeeDTO.setEmployeeId(taskSubMoneyDetailPO.getEmployeeNo());
                    //雇员名称
                    salaryEmployeeDTO.setEmployeeName(taskSubMoneyDetailPO.getEmployeeName());
                    //雇员银行卡所属银行编号
                    //雇员银行卡所属银行名称
                    //支行名称
                    //雇员银行账号ID
                    //雇员工资卡银行账号
                    //社保金额
                    //公积金金额
                    //是否要付社保/公积金(1:是；0:否)
                    //个税金额
                    //是否要付个税(1:是；0:否)
                    //薪资金额
                    //是否要付工资(1:是；0:否)
                    //应付金额
                    //工资月份
                    //个税月份
                    salaryEmployeeDTO.setTaxMonth(DateTimeFormatter.ofPattern("yyyy-MM").format(taskSubMoneyDetailPO.getPeriod()));
                    //帐套ID(1:AF;2:FC;3:BPO)
                    salaryEmployeeDTO.setFinanceAccountId(2);
                    //个人薪酬服务费
                    //雇员薪资明细状态(0:正常;1:暂缓放开;2:退票完成;3:现金完成;4:调整完成;负值标识未完成/未放开)
                    //雇员个税状态(0:正常;1:暂缓放开;负值标识未完成/未放开)
                    //是否垫付(0:正常;1:垫付)

                    salaryEmployeeDTOS.add(salaryEmployeeDTO);
                }
                salaryBatchDTO1.setEmployeeList(salaryEmployeeDTOS);
                com.ciicsh.gto.settlementcenter.payment.cmdapi.common.JsonResult salaryResult = salaryServiceProxy.saveSalaryBatchData(salaryBatchDTO1);
                if (!"0".equals(salaryResult.getCode())) {
                    jr.error();
                }
            }
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
//                    taskSubMoneyBO.setModifiedTime(LocalDateTime.now());
                    taskSubMoneyBO.setPayApplyId((long) payApplyId);
                    taskSubMoneyBO.setPayApplyCode(payApplyCode);
                    taskSubMoneyService.updateTaskSubMoneyById(taskSubMoneyBO);
                } catch (Exception e) {
//                    Map<String, String> tags = new HashMap<>(16);
                    //日志工具类返回
                    LogTaskFactory.getLogger().error(e, "TaskSubMoneyController.taxPayment", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "03"), LogType.APP, null);
                    jr.error();
                }
                //jr.fill(true);
            } else {
                jr.error();
            }
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subMoneyId", id.toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubMoneyController.taxPayment", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "03"), LogType.APP, tags);
            jr.error();
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
        taskSubMoneyService.updateTaskSubMoneyOverdueAndFine(taskSubMoneyDTO.getId(),taskSubMoneyDTO.getOverdue(),taskSubMoneyDTO.getFine());
        return jr;
    }
}
