package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantMainTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
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
 * @date 2018/6/7 0007
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryGrantMainTaskMapperTest {
    @Autowired
    private SalaryGrantMainTaskMapper salaryGrantMainTaskMapper;

    @Test
    public void test() {
        //查询薪资发放任务单主表记录
        EntityWrapper<SalaryGrantMainTaskPO> mainTaskPOEntityWrapper = new EntityWrapper<>();
        mainTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and grant_type in (1, 2, 3) and task_status in (0, 1, 2) and is_active = 1", "SGT2018032800000001");
        List<SalaryGrantMainTaskPO> salaryGrantMainTaskPOList = salaryGrantMainTaskMapper.selectList(mainTaskPOEntityWrapper);
        System.out.println("salaryGrantMainTaskPOList: " + JSONObject.toJSONString(salaryGrantMainTaskPOList));
    }
}
