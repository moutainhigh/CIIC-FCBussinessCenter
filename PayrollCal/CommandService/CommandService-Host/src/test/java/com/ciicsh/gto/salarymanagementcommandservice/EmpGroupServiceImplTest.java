package com.ciicsh.gto.salarymanagementcommandservice;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.EmpGroupMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmpGroupMapper;
import com.mongodb.DBObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/8/2 0002
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class EmpGroupServiceImplTest {
    @Autowired
    private PrEmpGroupMapper empGroupMapper;
    @Autowired
    private EmpGroupMongoOpt empGroupMongoOpt;

    @Test
    public void getEmployeesByEmpGroupCode() {
        EntityWrapper<PrEmpGroupPO> empGroupPOEntityWrapper = new EntityWrapper<>();
        empGroupPOEntityWrapper.where("emp_group_code = {0}", "GYZ_GL1800371_00171");

        List<PrEmpGroupPO> employees = empGroupMapper.selectList(empGroupPOEntityWrapper);
        System.out.println("雇员组雇员List: " + JSONObject.toJSONString(employees));
    }

    @Test
    public void ewrewr() {
        List<DBObject> employees = empGroupMongoOpt.list(Criteria.where("emp_group_code").is("GYZ_GL1800255_00090"));
        System.out.println("employees: " + JSONObject.toJSONString(employees));
    }
}
