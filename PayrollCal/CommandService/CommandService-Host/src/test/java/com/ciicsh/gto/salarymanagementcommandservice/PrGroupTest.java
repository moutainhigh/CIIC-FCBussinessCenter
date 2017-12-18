package com.ciicsh.gto.salarymanagementcommandservice;

import com.alibaba.fastjson.JSONObject;
import com.ciicsh.gto.salarymanagement.entity.PrGroupEntity;
import com.ciicsh.gto.salarymanagementcommandservice.service.impl.PrGroupServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class PrGroupTest extends PrBaseTest {

    @Autowired
    private PrGroupServiceImpl prGroupService;

    @Test
    public void newPrGroupTest(){
        PrGroupEntity groupEntity = new PrGroupEntity();
        groupEntity.setEntityId("PRG1");
        groupEntity.setManagementId("001");
        groupEntity.setName("薪资组001");
        groupEntity.setRemark("添加薪资组测试1");
        groupEntity.setIsTemplate(false);
        groupEntity.setIsActive(true);

        JSONObject jo = testRestTemplate.postForObject("/prGroup", JSONObject.toJSON(groupEntity), JSONObject.class);
        assertTrue(jo.getBoolean("success"));
        assertTrue(jo.get("data").equals(1));

    }

    @Test
    public void getPrGroupListTest() {
        JSONObject jo = testRestTemplate.getForObject("/prGroup?managementId=001&pageNum=1", JSONObject.class);
        assertTrue(jo.getBoolean("success"));
    }

    @Test
    public void getPrGroupQueryListTest() {
        PrGroupEntity param = new PrGroupEntity();
        param.setManagementId("001");
        param.setName("1");
        JSONObject jo = testRestTemplate.postForObject("/prGroupQuery", JSONObject.toJSON(param), JSONObject.class );
        assertTrue(jo.getBoolean("success"));
    }

    @Test
    public void getPrGroupNameListTest(){
        JSONObject jo = testRestTemplate.getForObject("/prGroupName?managementId=001&pageNum=1", JSONObject.class);
        assertTrue(jo.getBoolean("success"));
    }

    @Test
    public void updateTest(){
        PrGroupEntity groupEntity = new PrGroupEntity();
        groupEntity.setEntityId("PRG2");
        groupEntity.setCode("xxx-021");
        groupEntity.setManagementId("001");
        groupEntity.setName("薪资组001");
        groupEntity.setRemark("添加薪资组测试1");
        groupEntity.setIsTemplate(false);
        groupEntity.setIsActive(true);
        groupEntity.setCreatedBy("gu");
        groupEntity.setModifiedBy("gu");
//        prGroupService.addItem(groupEntity);

        groupEntity.setRemark("test");
        testRestTemplate.put("/prGroup/PRG2",JSONObject.toJSON(groupEntity));
        PrGroupEntity item = new PrGroupEntity();
        item.setEntityId("PRG2");
//        PrGroupEntity result = prGroupService.getItem(item);
//        assertTrue(result.getRemark().equals("test"));

//        prGroupService.deletePrGroup("PRG2");
    }

    @Test
    public void deleteTest(){
        testRestTemplate.delete("/prGroup/PRG1",JSONObject.class);
    }

}
