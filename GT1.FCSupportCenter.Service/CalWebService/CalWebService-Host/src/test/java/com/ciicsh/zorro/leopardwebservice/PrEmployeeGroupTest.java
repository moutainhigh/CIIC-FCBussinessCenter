package com.ciicsh.zorro.leopardwebservice;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ciicsh.zorro.leopardwebservice.entity.PrEmployeeEntity;
import com.ciicsh.zorro.leopardwebservice.entity.PrEmployeeGroupEntity;
import com.ciicsh.zorro.leopardwebservice.service.PrEmployeeGroupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by jiangtianning on 2017/10/31.
 */
@RunWith(SpringRunner.class)
public class PrEmployeeGroupTest extends PrBaseTest{

    @Autowired
    private PrEmployeeGroupService prEmployeeGroupService;

    @Test
    public void getPrEmployeeGroupListTest() {
        JSONArray ja = testRestTemplate.getForObject("/prEmployeeGroup?managementId=001&companyId=001&pageNum=1", JSONArray.class);
        JSONObject firstObj = ja.getJSONObject(0);
        assertTrue(firstObj.getString("entityId").equals("PEG000001"));
        assertTrue(ja.size() == 5);
    }

    @Test
    public void searchPrEmployeeGroupListTest() {

        PrEmployeeGroupEntity param = new PrEmployeeGroupEntity();
        param.setManagementId("001");
        param.setCompanyId("001");
        param.setCode("GYZ-CMY000001-07479");
//        Object jsonParam = JSONObject.toJSON(param);
        JSONArray ja = testRestTemplate.postForObject("/prEmployeeGroupQuery", JSONObject.toJSON(param), JSONArray.class );
        JSONObject onlyObj = ja.getJSONObject(0);
        assertTrue(onlyObj.getString("entityId").equals("PEG145595"));
        assertTrue(ja.size() == 1);
        assertTrue(onlyObj.getString("name").equals("测试雇员组Test233"));
    }

    @Test
    public void getEmployeeGroupNameListTest() {
        String[] ja = testRestTemplate.getForObject("/prEmployeeGroupName?managementId=001&companyId=001", String[].class );
        String firstObj = ja[0];
        assertTrue(firstObj.equals("测试雇员组1"));
    }

    @Test
    @Transactional
    public void insertPrEmployeeGroup() {
        PrEmployeeGroupEntity param = new PrEmployeeGroupEntity();
        param.setManagementId("001");
        param.setCompanyId("001");
        param.setName("测试雇员组Test0079");
        param.setRemark("TEST");

        PrEmployeeEntity employeeEntity = new PrEmployeeEntity();
        employeeEntity.setEmployeeId("10025");
        employeeEntity.setDepartName("TestDepartName");
        employeeEntity.setPosition("TestPosition");
        employeeEntity.setOnBoardDate(new Date());

        List<PrEmployeeEntity> list = new ArrayList<>();
        list.add(employeeEntity);

        param.setEmployeeList(list);

        Integer result = testRestTemplate.postForObject("/prEmployeeGroup", JSONObject.toJSON(param), Integer.class );
        assertTrue(result == 1);

        List<PrEmployeeGroupEntity> insertedEntityList = prEmployeeGroupService.getList(param, 0).getList();
        assertTrue(insertedEntityList.get(0).getRemark().equals(param.getRemark()));
    }

    @Test
    public void getEmployeeGroupTest() {
        JSONObject onlyObj = testRestTemplate.getForObject("/prEmployeeGroup/PEG145595?managementId=001&companyId=001", JSONObject.class );
//        JSONObject onlyObj = ja.getJSONObject(0);
        assertTrue(onlyObj.getString("entityId").equals("PEG145595"));
    }

    @Test
    public void getEmployeeListTest() {
        JSONArray ja = testRestTemplate.getForObject("/prEmployee?pageNum=1", JSONArray.class );
        JSONObject firstObj = ja.getJSONObject(0);
        assertTrue(firstObj.getString("employeeId").equals("1"));
        assertTrue(ja.size() == 10);
    }

    @Test
    public void updateEmployeeGroupTest() {
        PrEmployeeGroupEntity param = new PrEmployeeGroupEntity();
        param.setEntityId("PEG000005");
        param.setManagementId("001");
        param.setCompanyId("001");
        param.setName("测试雇员组Test1111");
        param.setRemark("TEST");
        testRestTemplate.put("/prEmployeeGroup/PEG000005", JSONObject.toJSON(param));

        List<PrEmployeeGroupEntity> insertedEntityList = prEmployeeGroupService.getList(param, 0).getList();
        assertTrue(insertedEntityList.get(0).getName().equals("测试雇员组Test1111"));
    }
}
