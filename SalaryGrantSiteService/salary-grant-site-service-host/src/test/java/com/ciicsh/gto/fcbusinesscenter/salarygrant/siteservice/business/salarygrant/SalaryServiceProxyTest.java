package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeePaymentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskPaymentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.SalaryServiceProxy;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.common.JsonResult;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.SalaryBatchDTO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.SalaryEmployeeDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/28 0028
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryServiceProxyTest {
    @Autowired
    private SalaryServiceProxy salaryServiceProxy;
    @Autowired
    private CommonService commonService;
    @Autowired
    private SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    private SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;

    @Test
    public void saveSalaryBatchData() {
        SalaryGrantTaskPaymentBO taskPaymentBO = new SalaryGrantTaskPaymentBO();

        SalaryBatchDTO salaryBatchDTO = new SalaryBatchDTO();
        salaryBatchDTO.setBatchCode(taskPaymentBO.getBatchCode()); //计算批次号
        salaryBatchDTO.setSequenceNo(taskPaymentBO.getTaskCode()); //流水号,发放批次号
        salaryBatchDTO.setManagementId(taskPaymentBO.getManagementId()); //管理方ID
        salaryBatchDTO.setManagementName(taskPaymentBO.getManagementName()); //管理方名称
        salaryBatchDTO.setPayMonth(taskPaymentBO.getGrantCycle()); //缴费月份
        salaryBatchDTO.setTotalAmount(taskPaymentBO.getPaymentTotalSum()); //工资清单总额（计算批次的总额）
        salaryBatchDTO.setTotalEmployeeCount(taskPaymentBO.getTotalPersonCount()); //雇员总数（计算批次的雇员数量）
        salaryBatchDTO.setPayAmount(taskPaymentBO.getPayAmount()); //本批次将付金额（计算批次里正常的雇员的实发工资之和）
        salaryBatchDTO.setPayEmployeeCount(taskPaymentBO.getPayEmployeeCount()); //本批次将付雇员数量（正常的雇员）
        salaryBatchDTO.setApplyer(commonService.getUserNameById(taskPaymentBO.getOperatorUserId())); //申请人
        salaryBatchDTO.setApplyDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))); //申请日期
        salaryBatchDTO.setAgentFeeAmount(null); //财务代理费
        if (!ObjectUtils.isEmpty(taskPaymentBO.getBalanceGrant())) {
            if (taskPaymentBO.getBalanceGrant() == 0) {
                //已来款balance_grant=0的就返回标记为0
                salaryBatchDTO.setIsAdvance(0); //是否垫付(0:正常;1-信用期垫付;2-偶然垫付;3-水单垫付;4-AF垫付;5-预付款垫付)信用期垫付就是周期垫付
            }

            if (taskPaymentBO.getBalanceGrant() == 1) {
                if (!ObjectUtils.isEmpty(taskPaymentBO.getAdvanceType())) {
                    //垫付balance_grant=1，则返回对应的垫付类型1\2\3\4\5
                    salaryBatchDTO.setIsAdvance(taskPaymentBO.getAdvanceType()); //是否垫付(0:正常;1-信用期垫付;2-偶然垫付;3-水单垫付;4-AF垫付;5-预付款垫付)信用期垫付就是周期垫付
                }
            }
        }

        List<SalaryGrantEmployeePaymentBO> employeePaymentBOList = new ArrayList<>();

        List<SalaryEmployeeDTO> employeeList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(employeePaymentBOList)) {
            employeePaymentBOList.stream().forEach(employeePaymentBO -> {
                SalaryEmployeeDTO salaryEmployeeDTO = new SalaryEmployeeDTO();
                salaryEmployeeDTO.setCompanyId(employeePaymentBO.getCompanyId()); //公司ID
                salaryEmployeeDTO.setCompanyName(employeePaymentBO.getCompanyName()); //公司名称
                salaryEmployeeDTO.setEmployeeId(employeePaymentBO.getEmployeeId()); //雇员ID
                salaryEmployeeDTO.setEmployeeName(employeePaymentBO.getEmployeeName()); //雇员名称
                salaryEmployeeDTO.setBankId(ObjectUtils.isEmpty(employeePaymentBO.getBankcardType()) ? null : employeePaymentBO.getBankcardType()); //雇员银行卡所属银行编号
                salaryEmployeeDTO.setBankBranchName(employeePaymentBO.getDepositBank()); //支行名称
                salaryEmployeeDTO.setEmployeeBankAccountId(ObjectUtils.isEmpty(employeePaymentBO.getBankcardId()) ? null : employeePaymentBO.getBankcardId().intValue()); //雇员银行账号ID
                salaryEmployeeDTO.setEmployeeBankAccount(employeePaymentBO.getCardNum()); //雇员工资卡银行账号
                salaryEmployeeDTO.setEmployeeBankAccount(employeePaymentBO.getCardNum()); //雇员工资卡银行账号
                salaryEmployeeDTO.setProvinceName(commonService.getProvinceName(employeePaymentBO.getBankcardProvinceCode())); //省名称
                salaryEmployeeDTO.setCityName(commonService.getCityName(employeePaymentBO.getBankcardCityCode())); //城市名
                salaryEmployeeDTO.setAreaCode(employeePaymentBO.getBankcardProvinceCode()); //银行地区码
                salaryEmployeeDTO.setPaySocialinsuranceAmount(employeePaymentBO.getPersonalSocialSecurity()); //社保金额
                salaryEmployeeDTO.setPayHousefundAmount(employeePaymentBO.getIndividualProvidentFund()); //公积金金额
                salaryEmployeeDTO.setIfWelfarePay(employeePaymentBO.getWelfareIncluded() ? 1 : 0); //是否要付社保/公积金(1:是；0:否)
                salaryEmployeeDTO.setPayIncometaxAmount(employeePaymentBO.getPersonalIncomeTax()); //个税金额
                salaryEmployeeDTO.setIfTaxPay(employeePaymentBO.getIfTaxPay()); //是否要付个税(1:是；0:否)
                salaryEmployeeDTO.setSalaryAmount(employeePaymentBO.getWagePayable()); //应付工资
                salaryEmployeeDTO.setIfSalaryPay(employeePaymentBO.getIfSalaryPay()); //是否要付工资(1:是；0:否)
                salaryEmployeeDTO.setPayAmount(employeePaymentBO.getPaymentAmount()); //应付金额 实发工资
                salaryEmployeeDTO.setSalaryMonth(employeePaymentBO.getGrantCycle()); //工资月份
                salaryEmployeeDTO.setTaxMonth(employeePaymentBO.getTaxCycle()); //个税月份
                salaryEmployeeDTO.setFinanceAccountId(ObjectUtils.isEmpty(employeePaymentBO.getContractFirstParty()) ? null : Integer.parseInt(employeePaymentBO.getContractFirstParty())); //帐套ID
                salaryEmployeeDTO.setServiceFee(employeePaymentBO.getServiceFeeAmount()); //个人薪酬服务费
                salaryEmployeeDTO.setSalaryStatus(employeePaymentBO.getGrantStatus()); //雇员薪资明细状态(0:正常;1:暂缓放开;2:退票完成;3:现金完成;4:调整完成;负值标识未完成/未放开)
                salaryEmployeeDTO.setTaxStatus(employeePaymentBO.getTaxStatus()); //雇员明细状态雇员个税状态(0:正常;1:暂缓放开;负值标识未完成/未放开)

                employeeList.add(salaryEmployeeDTO);
            });
        }

        salaryBatchDTO.setEmployeeList(employeeList);

        JsonResult<SalaryBatchDTO> salaryBatchDTOJsonResult = salaryServiceProxy.saveSalaryBatchData(salaryBatchDTO);
        if (!ObjectUtils.isEmpty(salaryBatchDTOJsonResult)) {
            if ("0".equals(salaryBatchDTOJsonResult.getCode())) {
                SalaryBatchDTO batchDTO = salaryBatchDTOJsonResult.getData();

            } else {
                System.out.println("调用结算中心发放工资清单接口返回结果错误 MSG: " + salaryBatchDTOJsonResult.getMsg());
            }
        } else {
            System.out.println("调用结算中心发放工资清单接口异常");
        }

    }
}
