package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantEmployeeRefundBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.host.App;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryGrantServiceTest {
    @Autowired
    private SalaryGrantService salaryGrantService;

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

        salaryGrantService.toCreateRefundTask(employeeRefundList, batchCode);
    }
}
