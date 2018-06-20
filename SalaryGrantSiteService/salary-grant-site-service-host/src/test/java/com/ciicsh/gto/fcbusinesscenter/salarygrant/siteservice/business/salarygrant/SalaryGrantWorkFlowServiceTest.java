package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/6/6 0006
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryGrantWorkFlowServiceTest {
    @Autowired
    private SalaryGrantWorkFlowService salaryGrantWorkFlowService;

    @Test
    public void doCancelTask() throws Exception {
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
        salaryGrantTaskBO.setTaskCode("SGT2018032800000001");
        salaryGrantTaskBO.setInvalidReason("失效原因");
        Boolean result = salaryGrantWorkFlowService.doCancelTask(salaryGrantTaskBO);
        System.out.println("result: " + result);
    }
}
