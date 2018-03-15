package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubMoneyDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubMoneyDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubMoneyService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.intercept.LoginInfoHolder;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubMoneyBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubMoneyDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubMoneyPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money.RequestForSubMoney;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money.ResponseForSubMoney;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.identityservice.api.dto.response.UserInfoResponseDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.PayapplyServiceProxy;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.PayApplyProxyDTO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.PayapplyCompanyProxyDTO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.PayapplyEmployeeProxyDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author yuantongqing
 * on create 2018/1/8
 */
@RestController
public class TaskSubMoneyController extends BaseController {


    @Autowired
    private TaskSubMoneyService taskSubMoneyService;

    @Autowired
    private PayapplyServiceProxy payapplyServiceProxy;

    @Autowired
    private TaskSubMoneyDetailService taskSubMoneyDetailService;

    /**
     * 条件查询划款子任务
     *
     * @param taskSubMoneyDTO
     * @return
     */
    @PostMapping(value = "querySubMoney")
    public JsonResult<ResponseForSubMoney> querySubMoney(@RequestBody TaskSubMoneyDTO taskSubMoneyDTO) {
        JsonResult<ResponseForSubMoney> jr = new JsonResult<>();
        try {
            RequestForSubMoney requestForSubMoney = new RequestForSubMoney();
            BeanUtils.copyProperties(taskSubMoneyDTO, requestForSubMoney);
            ResponseForSubMoney responseForSubMoney = taskSubMoneyService.querySubMoney(requestForSubMoney);
            jr.fill(responseForSubMoney);
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
    public JsonResult<Boolean> completeTaskSubMoney(@RequestBody TaskSubMoneyDTO taskSubMoneyDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForSubMoney requestForSubMoney = new RequestForSubMoney();
            BeanUtils.copyProperties(taskSubMoneyDTO, requestForSubMoney);
            //任务状态
            requestForSubMoney.setStatus("04");
            taskSubMoneyService.completeTaskSubMoney(requestForSubMoney);
            //jr.fill(true);
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
    public JsonResult<Boolean> rejectTaskSubMoney(@RequestBody TaskSubMoneyDTO taskSubMoneyDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForSubMoney requestForSubMoney = new RequestForSubMoney();
            BeanUtils.copyProperties(taskSubMoneyDTO, requestForSubMoney);
            //任务状态
            requestForSubMoney.setStatus("03");
            taskSubMoneyService.rejectTaskSubMoney(requestForSubMoney);
            //jr.fill(true);
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
    public JsonResult<TaskSubMoneyDTO> querySubMoneyById(@PathVariable Long subMoneyId) {
        JsonResult<TaskSubMoneyDTO> jr = new JsonResult<>();
        try {
            TaskSubMoneyPO taskSubMoneyPO = taskSubMoneyService.querySubMoneyById(subMoneyId);
            TaskSubMoneyDTO taskSubMoneyDTO = new TaskSubMoneyDTO();
            BeanUtils.copyProperties(taskSubMoneyPO, taskSubMoneyDTO);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM");
            taskSubMoneyDTO.setPeriod(taskSubMoneyPO.getPeriod().format(formatter));
            jr.fill(taskSubMoneyDTO);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subMoneyId", subMoneyId.toString());
            //日志工具类返回
            logService.error(e, "TaskSubMoneyController.querySubMoneyById", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "03"), LogType.APP, tags);
            jr.error();
        }
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
            //调用结算中心上海个税接口
            PayApplyProxyDTO payApplyProxyDTO = new PayApplyProxyDTO();
            //部门名称
            payApplyProxyDTO.setDepartmentManager("财务咨询业务中心");
            //是否财务部
            payApplyProxyDTO.setIsFinancedept(0);
            //业务类型
            payApplyProxyDTO.setBusinessType(6);
            //业务方主键ID
            payApplyProxyDTO.setBusinessPkId(id);
            //付款方式
            payApplyProxyDTO.setPayWay(3);
            //申请支付金额
            payApplyProxyDTO.setPayAmount(taskSubMoneyPO.getTaxAmount());
            //TODO 收款方名称
            payApplyProxyDTO.setReceiver("中智");
            //申请人
            UserInfoResponseDTO userInfoResponseDTO = LoginInfoHolder.get().getResult().getObject();
            payApplyProxyDTO.setApplyer(userInfoResponseDTO.getLoginName());
            //申请日期(yyyy-MM-dd)
            payApplyProxyDTO.setApplyDate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
            //TODO 付款用途
            payApplyProxyDTO.setPayPurpose("个税");
            //TODO 付款原因
            payApplyProxyDTO.setPayReason("个税");
            //收款账号ID
            payApplyProxyDTO.setReceiveAccountId(12);
            //收款账号
            payApplyProxyDTO.setReceiveAccount(taskSubMoneyPO.getPaymentAccount());
            //总经理
            //分管领导
            //部门经理
            //审核人
            //payApplyProxyDTO.setReviewer("admin");
            //根据任务ID查询所有雇员
            List<TaskSubMoneyDetailPO> taskSubMoneyDetailPOList = taskSubMoneyDetailService.querySubMonetDetailsBySubMoneyId(id);
            //设置公司信息companyList
            List<PayapplyCompanyProxyDTO> companyList = new ArrayList<>();
            //根据公司ID
            Map<String, List<TaskSubMoneyDetailPO>> taskSubMoneyDetailPOMap =
                    taskSubMoneyDetailPOList.stream().collect(groupingBy(TaskSubMoneyDetailPO::getIncomeSubject));
            for (String key : taskSubMoneyDetailPOMap.keySet()) {
                //根据key获取对应的划款明细对象集合
                List<TaskSubMoneyDetailPO> taskSubMoneyDetailPOS = taskSubMoneyDetailPOMap.get(key);
                //根据划款明细对象集合求出个税总金额
                BigDecimal taxAmountSum = taskSubMoneyDetailPOS.stream()
                        //将TaskSubMoneyDetailPO对象的tax_aount取出来map为Bigdecimal
                        .map(TaskSubMoneyDetailPO::getTaxAmount)
                        //使用reduce聚合函数,实现累加器
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                PayapplyCompanyProxyDTO payapplyCompanyProxyDTO = new PayapplyCompanyProxyDTO();
                payapplyCompanyProxyDTO.setCompanyId(key);
                payapplyCompanyProxyDTO.setCompanyName(taskSubMoneyDetailPOS.get(0).getIncomeSubject());
                payapplyCompanyProxyDTO.setPayMonth(DateTimeFormatter.ofPattern("yyyy-MM").format(taskSubMoneyPO.getPeriod()));
                payapplyCompanyProxyDTO.setPayAmount(taxAmountSum);
                companyList.add(payapplyCompanyProxyDTO);
            }
            payApplyProxyDTO.setCompanyList(companyList);
            //设置雇员信息employeeList
            List<PayapplyEmployeeProxyDTO> employeeList = new ArrayList<>();
            for (TaskSubMoneyDetailPO taskSubMoneyDetailPO : taskSubMoneyDetailPOList) {
                PayapplyEmployeeProxyDTO payapplyEmployeeProxyDTO = new PayapplyEmployeeProxyDTO();
                payapplyEmployeeProxyDTO.setEmployeeId(taskSubMoneyDetailPO.getEmployeeNo());
                payapplyEmployeeProxyDTO.setEmployeeName(taskSubMoneyDetailPO.getEmployeeName());
                payapplyEmployeeProxyDTO.setCompanyId(taskSubMoneyDetailPO.getIncomeSubject());
                payapplyEmployeeProxyDTO.setPayMonth(DateTimeFormatter.ofPattern("yyyy-MM").format(taskSubMoneyDetailPO.getPeriod()));
                payapplyEmployeeProxyDTO.setPayAmount(taskSubMoneyDetailPO.getTaxAmount());
                employeeList.add(payapplyEmployeeProxyDTO);
            }
            payApplyProxyDTO.setEmployeeList(employeeList);

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
                    taskSubMoneyBO.setModifiedTime(LocalDateTime.now());
                    taskSubMoneyBO.setPayApplyId((long) payApplyId);
                    taskSubMoneyBO.setPayApplyCode(payApplyCode);
                    taskSubMoneyService.updateTaskSubMoneyById(taskSubMoneyBO);
                } catch (Exception e) {
//                    Map<String, String> tags = new HashMap<>(16);
                    //日志工具类返回
                    logService.error(e, "TaskSubMoneyController.taxPayment", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "03"), LogType.APP, null);
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
            logService.error(e, "TaskSubMoneyController.taxPayment", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "03"), LogType.APP, tags);
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
        try {
            TaskSubMoneyPO taskSubMoneyPO = taskSubMoneyService.querySubMoneyById(id);
            com.ciicsh.gto.settlementcenter.payment.cmdapi.common.JsonResult jr = payapplyServiceProxy.downloadPayVoucher(taskSubMoneyPO.getPayApplyCode());
            if ("0".equals(jr.getCode())) {
                byte[] bs = jr.getContents();
                super.downloadFile(response, "划款凭证.xlsx", bs);
            }
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("subMoneyId", id.toString());
            //日志工具类返回
            logService.error(e, "TaskSubMoneyController.printVoucher", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "03"), LogType.APP, tags);
        }
    }
}
