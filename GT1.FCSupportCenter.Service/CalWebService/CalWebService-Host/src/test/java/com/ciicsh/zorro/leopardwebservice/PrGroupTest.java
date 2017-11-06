package com.ciicsh.zorro.leopardwebservice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ciicsh.zorro.leopardwebservice.entity.PrEmployeeGroupEntity;
import com.ciicsh.zorro.leopardwebservice.entity.PrGroupEntity;
import com.ciicsh.zorro.leopardwebservice.service.PrGroupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class PrGroupTest extends PrBaseTest {

    @Autowired
    private PrGroupService prGroupService;

    @Test
    public void getPrGroupListTest() {
        JSONArray ja = testRestTemplate.getForObject("/prGroup?managementId=001&pageNum=1", JSONArray.class);
        JSONObject firstObj = ja.getJSONObject(0);
        assertTrue(firstObj.getString("entityId").equals("PRG000001"));
        assertTrue(ja.size() == 3);
    }

    @Test
    public void getPrGroupQueryListTest() {
        PrGroupEntity param = new PrGroupEntity();
        param.setManagementId("001");
        param.setName("2");
        JSONArray ja = testRestTemplate.postForObject("/prGroupQuery", JSONObject.toJSON(param), JSONArray.class );
        JSONObject obj = ja.getJSONObject(0);
        assertTrue(obj.getString("name").equals("测试薪资组2"));
        assertTrue(ja.size() == 2);
    }

    @Test
    public void getPrGroupNameListTest(){
        String[] ja = testRestTemplate.getForObject("/prGroupName?managementId=001&pageNum=1", String[].class);
        assertTrue(ja[1].equals("测试薪资组2"));
        assertTrue(ja.length==3);
    }

    @Test
    public void updatePrGroupTest(){
        PrGroupEntity param = new PrGroupEntity();
        param.setEntityId("PRG000001");
        param.setParentId("PRG000001");
        param.setCode("XZZ-001-001");
        param.setManagementId("001");
        param.setName("测试薪资组0");
        param.setVersion("1.0");
        param.setRemark("更新测试备注");

        testRestTemplate.put("/prGroup/PRG000001",JSONObject.toJSON(param));
        PrGroupEntity item = prGroupService.getItem(param);
        assertTrue(item.getRemark().equals("更新测试备注"));
    }

    @Test
    public void getPrGroupTest(){
        JSONObject ja = testRestTemplate.getForObject("/prGroup/PRG000001?managementId=001", JSONObject.class);
        assertTrue(ja.getString("entityId").equals("PRG000001"));
        assertTrue(ja.getString("version").equals("1.0"));
    }

    @Test
    public void newPrGroupTest(){

    }
}
