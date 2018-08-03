package com.ciicsh.gto.salarymanagementcommandservice;

import com.ciicsh.gto.fcbusinesscenter.util.mongo.EmpGroupMongoOpt;
import com.ciicsh.gto.salarymanagementcommandservice.service.common.CommonServiceImpl;
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
public class CommonServiceImplTest {
    @Autowired
    private CommonServiceImpl commonService;
    @Autowired
    private EmpGroupMongoOpt empGroupMongoOpt;

    /**
     * 批量更新或者插入数据
     */
    @Test
    public void batchInsertOrUpdateNormalBatch() {
        String batchCode = "GL1800371_201806_0000001071";
        int batchType = 1;

        List<DBObject> employees = empGroupMongoOpt.list(Criteria.where("emp_group_code").is("GYZ_GL1800255_00090"));
        int result = commonService.batchInsertOrUpdateNormalBatch(batchCode, batchType, employees);
        System.out.println("批量更新或者插入数据 返回: " + result);
    }

}
