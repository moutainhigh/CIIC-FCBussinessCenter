package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.PayrollCalcResultDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Test CommonService</p>
 *
 * @author gaoyang
 * @version 1.0
 * @date 2018/6/8
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryGrantTaskProcessServiceTest {
    @Autowired
    private SalaryGrantTaskProcessService salaryGrantTaskProcessService;

    @Test
    public void toConvertToPayrollCalcResultDTO(){
        Map batchParam = new HashMap();
        batchParam.put("batchCode","GL1800255_201806_0000000299");
        batchParam.put("batchType",1);
        List<PayrollCalcResultDTO> payrollCalcResultDTOList = salaryGrantTaskProcessService.listPayrollCalcResult(batchParam);
    }
}
