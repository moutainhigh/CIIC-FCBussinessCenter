package com.ciicsh.zorro.leopardwebservice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ciicsh.zorro.leopardwebservice.entity.PrAccountSetEntity;
import com.ciicsh.zorro.leopardwebservice.service.PrAccountSetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by jiangtianning on 2017/11/2.
 */
@RunWith(SpringRunner.class)
public class PrAccountSetTest extends PrBaseTest{

    @Autowired
    private PrAccountSetService prAccountSetService;

    @Test
    public void getAccountSetListTest() {
        JSONArray ja = testRestTemplate.getForObject("/prAccountSet?managementId=001&pageNum=1", JSONArray.class);
        JSONObject firstObj = ja.getJSONObject(0);
        assertTrue(firstObj.getString("entityId").equals("PRAS000001"));
    }

    @Test
    public void searchAccountSetListTest() {
        PrAccountSetEntity accountSetEntity = new PrAccountSetEntity();
        accountSetEntity.setEntityId("PRAS000001");
        JSONArray ja = testRestTemplate.postForObject("/prAccountSetQuery?pageNum=1", accountSetEntity, JSONArray.class);
        JSONObject firstObj = ja.getJSONObject(0);
        assertTrue(firstObj.getString("entityId").equals("PRAS000001"));
    }

    @Test
    @Transactional
    public void addAccountSetTest() {
        PrAccountSetEntity accountSetEntity = new PrAccountSetEntity();
        accountSetEntity.setManagementId("001");
        accountSetEntity.setName("薪资账套FROM_JUNIT");
        accountSetEntity.setPrGroupId("PRG000001");
        accountSetEntity.setPrEmployeeGroupId("PEG000001");
        accountSetEntity.setStartSalaryDate(new Date());
        accountSetEntity.setEndSalaryDate(new Date());
        accountSetEntity.setTaxPeriod(20);
        accountSetEntity.setRemark("remark from JUNIT");
        int result = testRestTemplate.postForObject("/prAccountSet", accountSetEntity, Integer.class);
        assertTrue(result == 1);

        accountSetEntity.setStartSalaryDate(null);
        accountSetEntity.setEndSalaryDate(null);
        JSONArray ja = testRestTemplate.postForObject("/prAccountSetQuery?pageNum=1", accountSetEntity, JSONArray.class);
        JSONObject firstObj = ja.getJSONObject(0);
        assertTrue(firstObj.getString("name").equals("薪资账套FROM_JUNIT"));
    }

    @Test
    public void getAccountSet() {
        JSONObject resultObj = testRestTemplate.getForObject("/prAccountSet/PRAS000001", JSONObject.class);
        assertTrue(resultObj.getString("entityId").equals("PRAS000001"));
    }

    @Test
    public void updateAccountSet() {
        PrAccountSetEntity accountSetEntity = new PrAccountSetEntity();
        accountSetEntity.setManagementId("001");
        accountSetEntity.setName("薪资账套FROM_JUNIT_TEST_UPDATE");
        accountSetEntity.setPrGroupId("PRG000001");
        accountSetEntity.setPrEmployeeGroupId("PEG000001");
        accountSetEntity.setStartSalaryDate(new Date());
        accountSetEntity.setEndSalaryDate(new Date());
        accountSetEntity.setTaxPeriod(20);
        accountSetEntity.setRemark("remark from JUNIT");
        testRestTemplate.put("/prAccountSet/PRAS332000", accountSetEntity, Integer.class);

        JSONObject resultObj = testRestTemplate.getForObject("/prAccountSet/PRAS332000", JSONObject.class);
        assertTrue(resultObj.getString("name").equals("薪资账套FROM_JUNIT_TEST_UPDATE"));
    }

    @Test
    public void getAccountSetNameList() {
        String[] ja = testRestTemplate.getForObject("/prAccountSetName?managementId=001", String[].class );
        String firstObj = ja[0];
        assertTrue(firstObj.equals("薪资账套A"));
    }
}
