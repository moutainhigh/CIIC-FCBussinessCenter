package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>Description: 薪资发放报盘任务单 服务类</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/4/24 0024
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryGrantOfferDocumentTaskServiceTest {
    @Autowired
    private SalaryGrantOfferDocumentTaskService documentTaskService;

    @Test
    public void queryOfferDocumentTaskPage() {
        Page<SalaryGrantTaskBO> page = new Page<>();
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
        Page<SalaryGrantTaskBO> taskBOPage = documentTaskService.queryOfferDocumentTaskPage(page, salaryGrantTaskBO);
        System.out.println("查询薪资发放报盘任务单列表 page: " + taskBOPage + " 记录: " + taskBOPage.getRecords());
    }
}
