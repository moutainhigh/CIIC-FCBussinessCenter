package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gto.companycenter.webcommandservice.api.DeferredPoolProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.PaymentDeferredRequestDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 调用客服中心暂缓池操作接口 测试类</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/6/5 0005
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class DeferredPoolProxyTest {
    @Autowired
    private DeferredPoolProxy deferredPoolProxy;
    @Autowired
    private SalaryGrantSupplierSubTaskService salaryGrantSupplierSubTaskService;
    @Autowired
    private SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;

    @Test
    public void addDeferredPool() {
        SalaryGrantSubTaskPO salaryGrantSubTaskPO = salaryGrantSupplierSubTaskService.selectById(5);
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
        salaryGrantTaskBO.setTaskCode(salaryGrantSubTaskPO.getSalaryGrantSubTaskCode());
        salaryGrantTaskBO.setBatchCode(salaryGrantSubTaskPO.getBatchCode());
        salaryGrantTaskBO.setManagementId(salaryGrantSubTaskPO.getManagementId());
        salaryGrantTaskBO.setManagementName(salaryGrantSubTaskPO.getManagementName());
        salaryGrantTaskBO.setGrantCycle(salaryGrantSubTaskPO.getGrantCycle());

        EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
        employeePOEntityWrapper.where("salary_grant_sub_task_code = {0} and is_active = 1", salaryGrantSubTaskPO.getSalaryGrantSubTaskCode());
        List<SalaryGrantEmployeePO> employeeList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper);

        PaymentDeferredRequestDTO paymentDeferredRequestDTO = new PaymentDeferredRequestDTO();
        paymentDeferredRequestDTO.setTaskCode(salaryGrantTaskBO.getTaskCode());             //任务单编号
        paymentDeferredRequestDTO.setBatchCode(salaryGrantTaskBO.getBatchCode());           //薪酬计算批次号
        paymentDeferredRequestDTO.setManagementId(salaryGrantTaskBO.getManagementId());     //管理方编号
        paymentDeferredRequestDTO.setManagementName(salaryGrantTaskBO.getManagementName()); //管理方名称
        paymentDeferredRequestDTO.setGrantCycle(salaryGrantTaskBO.getGrantCycle());         //薪资周期

        List<PaymentDeferredRequestDTO.EmployeeInfo> employeeInfoList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(employeeList)) {
            employeeList.stream().forEach(employeePO -> {
                PaymentDeferredRequestDTO.EmployeeInfo employeeInfo = new PaymentDeferredRequestDTO.EmployeeInfo();
                employeeInfo.setEmployeeId(employeePO.getEmployeeId());             //雇员编号
                employeeInfo.setEmployeeName(employeePO.getEmployeeName());         //雇员名称
                employeeInfo.setCompanyId(employeePO.getCompanyId());               //公司编号
                employeeInfo.setCompanyName(employeePO.getCompanyName());           //公司名称
                employeeInfo.setCardNum(employeePO.getCardNum());                   //收款人账号
                employeeInfo.setAccountName(employeePO.getAccountName());           //收款人姓名
                employeeInfo.setBankcode(employeePO.getBankCode());                 //收款行行号
                employeeInfo.setDepositeBank(employeePO.getDepositBank());          //收款行名称
                employeeInfo.setPaymentAmountRmb(employeePO.getPaymentAmountRMB()); //人民币金额
                employeeInfo.setPaymentAmount(employeePO.getPaymentAmount());       //发放金额
                employeeInfo.setCurrencyCode(employeePO.getCurrencyCode());         //发放币种
                employeeInfo.setGrantMode(employeePO.getGrantMode());               //发放方式
                employeeInfo.setGrantStatus(employeePO.getGrantStatus());           //发放状态
                employeeInfo.setReprieveType(employeePO.getReprieveType());         //暂缓类型
                employeeInfo.setCountryCode(employeePO.getCountryCode());           //国籍

                employeeInfoList.add(employeeInfo);
            });
        }

        paymentDeferredRequestDTO.setEmployeeInfos(employeeInfoList);

        JsonResult jsonResult = deferredPoolProxy.addDeferredPool(paymentDeferredRequestDTO);
        System.out.println("----------------------------------------");
    }
}
