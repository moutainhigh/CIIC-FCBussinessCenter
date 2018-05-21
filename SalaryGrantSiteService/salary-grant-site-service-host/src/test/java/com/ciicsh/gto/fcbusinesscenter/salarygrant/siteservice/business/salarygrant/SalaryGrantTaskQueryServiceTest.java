package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.WorkFlowTaskInfoBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/17 0017
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryGrantTaskQueryServiceTest {
    @Autowired
    SalaryGrantTaskQueryService taskQueryService;

    @Test
    public void operation() {
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
        salaryGrantTaskBO.setTaskCode("ST201803280000000001");
        salaryGrantTaskBO.setCurrent(1);
        salaryGrantTaskBO.setSize(10);
        Page<WorkFlowTaskInfoBO> workFlowTaskInfoBOPage = taskQueryService.operation(salaryGrantTaskBO);
        if (!ObjectUtils.isEmpty(workFlowTaskInfoBOPage)) {
            List<WorkFlowTaskInfoBO> workFlowTaskInfoBOList = workFlowTaskInfoBOPage.getRecords();
        }
    }
}
