package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>Test SalaryGrantSubTaskProcessService</p>
 *
 * @author gaoyang
 * @version 1.0
 * @date 2018/7/3
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryGrantSubTaskProcessServiceTest {
    @Autowired
    SalaryGrantSubTaskProcessService salaryGrantSubTaskProcessService;

    @Test
    public void toSubmitMainTask(){
        SalaryGrantMainTaskPO salaryGrantMainTaskPO = new SalaryGrantMainTaskPO();
        salaryGrantMainTaskPO.setSalaryGrantMainTaskCode("SGT18071100000000012");
        salaryGrantMainTaskPO.setManagementId("GL1800255");
        salaryGrantMainTaskPO.setManagementName("fc管理方1");
        salaryGrantMainTaskPO.setBatchCode("GL1800255_201807_0000000700");
        salaryGrantMainTaskPO.setGrantCycle("201807");
        salaryGrantMainTaskPO.setGrantDate("20180701");
        salaryGrantMainTaskPO.setGrantTime(1);
        salaryGrantMainTaskPO.setGrantType(1);
        salaryGrantMainTaskPO.setAdvance(false);
        salaryGrantMainTaskPO.setAdvanceType(1);
        salaryGrantMainTaskPO.setBalanceGrant(0);
        salaryGrantSubTaskProcessService.toSubmitMainTask(salaryGrantMainTaskPO);
    }

    @Test
    public void toApproveMainTask(){
        SalaryGrantMainTaskPO salaryGrantMainTaskPO = new SalaryGrantMainTaskPO();
        salaryGrantMainTaskPO.setSalaryGrantMainTaskCode("SGT18071100000000012");
        salaryGrantMainTaskPO.setManagementId("GL1800255");
        salaryGrantMainTaskPO.setManagementName("fc管理方1");
        salaryGrantMainTaskPO.setBatchCode("GL1800255_201807_0000000700");
        salaryGrantMainTaskPO.setGrantCycle("201807");
        salaryGrantMainTaskPO.setGrantDate("20180701");
        salaryGrantMainTaskPO.setGrantTime(1);
        salaryGrantMainTaskPO.setGrantType(1);
        salaryGrantMainTaskPO.setAdvance(false);
        salaryGrantMainTaskPO.setAdvanceType(1);
        salaryGrantMainTaskPO.setBalanceGrant(0);
        salaryGrantMainTaskPO.setIncludeForeignCurrency(false);
        salaryGrantSubTaskProcessService.toApproveMainTask(salaryGrantMainTaskPO);
    }
}
