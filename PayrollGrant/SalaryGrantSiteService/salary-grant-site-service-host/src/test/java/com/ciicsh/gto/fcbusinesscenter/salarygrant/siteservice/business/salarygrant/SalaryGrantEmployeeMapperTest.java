package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeePaymentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
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

    @Test
    public void queryList() {
        EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
        employeePOEntityWrapper.where("salary_grant_sub_task_code = {0} and is_active = 1", "LTB20180328000000002");
        List<SalaryGrantEmployeePO> salaryGrantEmployeePOList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper);
        System.out.println("--------------------------");
    }
}
