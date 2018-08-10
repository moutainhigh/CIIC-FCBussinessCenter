package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantEmployeeRefundBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantEmployeeReprieveBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.host.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryGrantTaskProcessServiceTest {
    @Autowired
    private SalaryGrantTaskProcessService salaryGrantTaskProcessService;

    @Test
    public void toCreateRefundTaskTest(){
        String batchCode = "GL1800376_201807_0000001135";
        List<SalaryGrantEmployeeRefundBO> employeeRefundList = new ArrayList<>();
        SalaryGrantEmployeeRefundBO salaryGrantEmployeeRefundBO = new SalaryGrantEmployeeRefundBO();
        salaryGrantEmployeeRefundBO.setTaskCode("LTB18073000000000001");
        salaryGrantEmployeeRefundBO.setBatchCode("GL1800376_201807_0000001135");
        salaryGrantEmployeeRefundBO.setEmployeeId("18026414");
        salaryGrantEmployeeRefundBO.setCompanyId("KH18000516");
        salaryGrantEmployeeRefundBO.setGrantType(5);
        salaryGrantEmployeeRefundBO.setPaymentAmount(new BigDecimal("56221.00"));
        salaryGrantEmployeeRefundBO.setCardNum("6333000040035");
        salaryGrantEmployeeRefundBO.setCardNumNew("6333000040035");
        salaryGrantEmployeeRefundBO.setBankcardId(Long.valueOf("819"));
        salaryGrantEmployeeRefundBO.setBatchCodeNew("");
        employeeRefundList.add(salaryGrantEmployeeRefundBO);


        /*String batchCode = "GL1800376_201807_0000000994";
        List<SalaryGrantEmployeeRefundBO> employeeRefundList = new ArrayList<>();
        SalaryGrantEmployeeRefundBO salaryGrantEmployeeRefundBO = new SalaryGrantEmployeeRefundBO();
        salaryGrantEmployeeRefundBO = new SalaryGrantEmployeeRefundBO();
        salaryGrantEmployeeRefundBO.setTaskCode("LTB18072000000000003");
        salaryGrantEmployeeRefundBO.setBatchCode("GL1800376_201807_0000000994");
        salaryGrantEmployeeRefundBO.setEmployeeId("18026318");
        salaryGrantEmployeeRefundBO.setCompanyId("KH18000516");
        salaryGrantEmployeeRefundBO.setGrantType(5);
        salaryGrantEmployeeRefundBO.setPaymentAmount(new BigDecimal("56221.00"));
        salaryGrantEmployeeRefundBO.setCardNum("6333000027035");
        salaryGrantEmployeeRefundBO.setCardNumNew("63330000270352");
        salaryGrantEmployeeRefundBO.setBankcardId(Long.valueOf("851"));
        salaryGrantEmployeeRefundBO.setBatchCodeNew("");
        employeeRefundList.add(salaryGrantEmployeeRefundBO);*/

        salaryGrantTaskProcessService.createRefundTask(employeeRefundList, batchCode);
    }

    @Test
    public void createReprieveTaskTest(){
        String batchCode = "GL1800376_201807_0000000971";
        String taskCode = "SGT18071900000000027";
        List<SalaryGrantEmployeeReprieveBO> employeeReprieveList = new ArrayList<>();
        SalaryGrantEmployeeReprieveBO salaryGrantEmployeeReprieveBO1 = new SalaryGrantEmployeeReprieveBO();
        salaryGrantEmployeeReprieveBO1.setTaskCode("SGT18071900000000027");
        salaryGrantEmployeeReprieveBO1.setBatchCode("GL1800376_201807_0000000971");
        salaryGrantEmployeeReprieveBO1.setEmployeeId("18025894");
        salaryGrantEmployeeReprieveBO1.setCompanyId("KH18000516");
        salaryGrantEmployeeReprieveBO1.setCardNum("6333000020035");
        salaryGrantEmployeeReprieveBO1.setAccountName("建行020");
        salaryGrantEmployeeReprieveBO1.setBankCode("1423");
        salaryGrantEmployeeReprieveBO1.setDepositBank("建行徐泾支行020");
        salaryGrantEmployeeReprieveBO1.setPaymentAmountRMB(new BigDecimal("56221.00"));
        salaryGrantEmployeeReprieveBO1.setPaymentAmount(new BigDecimal("56221.00"));
        salaryGrantEmployeeReprieveBO1.setCurrencyCode("CNY");
        salaryGrantEmployeeReprieveBO1.setGrantMode(1);
        salaryGrantEmployeeReprieveBO1.setGrantStatus(1);
        salaryGrantEmployeeReprieveBO1.setReprieveType(0);
        salaryGrantEmployeeReprieveBO1.setCountryCode("CN");
        salaryGrantEmployeeReprieveBO1.setCardNumNew("6333000020035");
        salaryGrantEmployeeReprieveBO1.setBankcardId(Long.valueOf("745"));
        salaryGrantEmployeeReprieveBO1.setBatchCodeNew("");

        SalaryGrantEmployeeReprieveBO salaryGrantEmployeeReprieveBO2 = new SalaryGrantEmployeeReprieveBO();
        salaryGrantEmployeeReprieveBO2.setTaskCode("SGT18071900000000027");
        salaryGrantEmployeeReprieveBO2.setBatchCode("GL1800376_201807_0000000971");
        salaryGrantEmployeeReprieveBO2.setEmployeeId("18026308");
        salaryGrantEmployeeReprieveBO2.setCompanyId("KH18000516");
        salaryGrantEmployeeReprieveBO2.setCardNum("6333000023035");
        salaryGrantEmployeeReprieveBO2.setAccountName("中行023");
        salaryGrantEmployeeReprieveBO2.setBankCode("0123");
        salaryGrantEmployeeReprieveBO2.setDepositBank("中行徐泾支行023");
        salaryGrantEmployeeReprieveBO2.setPaymentAmountRMB(new BigDecimal("56221.00"));
        salaryGrantEmployeeReprieveBO2.setPaymentAmount(new BigDecimal("56221.00"));
        salaryGrantEmployeeReprieveBO2.setCurrencyCode("CNY");
        salaryGrantEmployeeReprieveBO2.setGrantMode(1);
        salaryGrantEmployeeReprieveBO2.setGrantStatus(1);
        salaryGrantEmployeeReprieveBO2.setReprieveType(0);
        salaryGrantEmployeeReprieveBO2.setCountryCode("DE");
        salaryGrantEmployeeReprieveBO2.setCardNumNew("6333000023035");
        salaryGrantEmployeeReprieveBO2.setBankcardId(Long.valueOf("766"));
        salaryGrantEmployeeReprieveBO2.setBatchCodeNew("");

        employeeReprieveList.add(salaryGrantEmployeeReprieveBO1);
        employeeReprieveList.add(salaryGrantEmployeeReprieveBO2);

        salaryGrantTaskProcessService.createReprieveTask(employeeReprieveList, batchCode, taskCode);
    }
}

