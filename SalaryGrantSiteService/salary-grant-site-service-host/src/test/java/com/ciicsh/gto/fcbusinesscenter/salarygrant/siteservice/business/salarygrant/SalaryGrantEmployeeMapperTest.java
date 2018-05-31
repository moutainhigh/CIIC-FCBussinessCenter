package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeePaymentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
public class SalaryGrantEmployeeMapperTest {
    @Autowired
    private SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;

    @Test
    public void queryWaitForPaymentEmpList() {
        String taskCode = "CT201803280000000002";
        List<SalaryGrantEmployeePaymentBO> employeePaymentBOList = salaryGrantEmployeeMapper.queryWaitForPaymentEmpList(taskCode);
        System.out.println("--------------------------");
    }
}
