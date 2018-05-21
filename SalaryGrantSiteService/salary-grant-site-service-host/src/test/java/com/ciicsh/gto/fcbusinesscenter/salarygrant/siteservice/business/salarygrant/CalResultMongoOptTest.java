package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gt1.CalResultMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.PayrollCalcResultDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import com.mongodb.DBObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/5/16 0016
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class CalResultMongoOptTest {
    @Autowired
    CalResultMongoOpt calResultMongoOpt;

    @Test
    public void list() {

        Map<String, String> itemMap = new HashMap<>();

        Query query = Query.query(Criteria.where("emp_id").ne(""));
        query.fields().include("salary_calc_result_items.item_name")
                .include("salary_calc_result_items.item_code");
        query.skip(0).limit(1);
        List<DBObject> list = calResultMongoOpt.getMongoTemplate().find(query,DBObject.class,CalResultMongoOpt.PR_BATCH);
        if (!CollectionUtils.isEmpty(list)) {
            List<DBObject> payItems = (List<DBObject>)list.get(0).get("salary_calc_result_items");
            if (!CollectionUtils.isEmpty(payItems)) {
//                payItems.forEach(dbObject1 -> System.out.println("item_name: " + dbObject1.get("item_name") + " item_code: " + dbObject1.get("item_code")));
                payItems.forEach(dbObject1 -> itemMap.put(dbObject1.get("item_code").toString(), dbObject1.get("item_name").toString()));
            }
        }

        System.out.println("itemMap: " + itemMap);
    }

    @Test
    public void list2() {
        String batchCode = "GL000007_201801_0000000179";
        Integer batchType = 1;
        List<DBObject> list = calResultMongoOpt.list(Criteria.where("batch_id").is(batchCode).and("batch_type").is(batchType));
        if (!CollectionUtils.isEmpty(list)) {
            List<DBObject> payItems = (List<DBObject>)list.get(0).get("salary_calc_result_items");
            if (!CollectionUtils.isEmpty(payItems)) {
//                payItems.forEach(dbObject1 -> System.out.println("item_name: " + dbObject1.get("item_name") + " item_code: " + dbObject1.get("item_code")));
            }
        }

    }
}
