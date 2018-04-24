package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
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
 * @date 2018/4/23 0023
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryGrantEmployeeQueryServiceTest {
    @Autowired
    private SalaryGrantEmployeeQueryService queryService;

    /**
     * 查询主表的雇员信息
     */
    @Test
    public void queryEmployeeForMainTask() {
        Page<SalaryGrantEmployeeBO> page = new Page<>();
        page.setCurrent(1);
        page.setSize(10);

        SalaryGrantEmployeeBO salaryGrantEmployeeBO = new SalaryGrantEmployeeBO();
        salaryGrantEmployeeBO.setSalaryGrantMainTaskCode("SGT2018032800000001");
        salaryGrantEmployeeBO.setActive(true);

        Page<SalaryGrantEmployeeBO> retPage = queryService.queryEmployeeForMainTask(page, salaryGrantEmployeeBO);
        System.out.println("查询主表的雇员信息 page: " + retPage + " 记录: " + retPage.getRecords());
    }

    @Test
    public void listAdjustCalcInfo() {
        SalaryGrantEmployeePO salaryGrantEmployeePO = new SalaryGrantEmployeePO();

        queryService.listAdjustCalcInfo(salaryGrantEmployeePO);
    }
}
