package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.SalaryGrantEmpHisOpt;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/22 0022
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryGrantEmpHisOptTest {
    @Autowired
    private SalaryGrantEmpHisOpt salaryGrantEmpHisOpt;

    @Test
    public void inset() {
        long task_his_id = 1;
        List<SalaryGrantEmployeeBO> employeeBOList = new ArrayList<>();
        SalaryGrantEmployeeBO employeeBO;

        for (int i = 0; i < 32; i++) {
            employeeBO = new SalaryGrantEmployeeBO();
            employeeBO.setTaskCode("10001" + i);
            employeeBO.setTaskType(1);
            employeeBOList.add(employeeBO);
        }

        DBObject dbObject = new BasicDBObject();
        dbObject.put("task_his_id", task_his_id);
        dbObject.put("employeeInfo", employeeBOList);
        salaryGrantEmpHisOpt.getMongoTemplate().insert(dbObject, "sg_emp_his_table");
    }
}
