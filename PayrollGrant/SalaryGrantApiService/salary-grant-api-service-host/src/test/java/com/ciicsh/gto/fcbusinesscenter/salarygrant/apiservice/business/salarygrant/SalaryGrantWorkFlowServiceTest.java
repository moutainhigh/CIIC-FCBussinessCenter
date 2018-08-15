package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.host.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Test SalaryGrantWorkFlowService</p>
 *
 * @author gaoyang
 * @version 1.0
 * @date 2018/08/13
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryGrantWorkFlowServiceTest {
    @Autowired
    private  SalaryGrantWorkFlowService salaryGrantWorkFlowService;

    @Test
    public void rejectTaskTest(){
        List<SalaryGrantTaskBO> codeList = new ArrayList<>();
        /*SalaryGrantTaskBO bo1 = new SalaryGrantTaskBO();
        bo1.setTaskCode("LTB18080800000000005");
        bo1.setBatchCode("GL1800338_201808_0000001520");
        codeList.add(bo1);*/
        SalaryGrantTaskBO bo2 = new SalaryGrantTaskBO();
        bo2.setTaskCode("LTB18080900000000001");
        bo2.setBatchCode("GL1800338_201808_0000001571");
        codeList.add(bo2);
        salaryGrantWorkFlowService.rejectTask(codeList);

    }
}
