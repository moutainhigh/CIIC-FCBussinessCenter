package com.ciicsh.gto.salarymanagementcommandservice;

import com.alibaba.fastjson.JSONObject;
import com.ciicsh.gto.salarymanagement.entity.PrGroupTemplateEntity;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrGroupTemplateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;


/**
 * Created by guwei on 2017/11/14
 */
@RunWith(SpringRunner.class)
public class PrGroupTemplateTest extends PrBaseTest{

    @Autowired
    private PrGroupTemplateService prGroupTemplateService;

//    @Test
//    public void newPrGroupTemplate(){
//        PrGroupTemplateEntity prGroupTemplateEntity = new PrGroupTemplateEntity();
//        prGroupTemplateEntity.setEntityId("PRGT1");
//        prGroupTemplateEntity.setCode("xxx-010");
//        prGroupTemplateEntity.setName("薪资组模板001");
//        prGroupTemplateEntity.setRemark("薪资组模板备注1");
//        JSONObject jo = testRestTemplate.postForObject("/prGroupTemplate", JSONObject.toJSON(prGroupTemplateEntity), JSONObject.class);
//        assertTrue(jo.getBoolean("success"));
//    }
//
//    @Test
//    public void getPrGroupTemplateList(){
//        JSONObject jo = testRestTemplate.getForObject("/prGroupTemplate", JSONObject.class);
//        assertTrue(jo.getBoolean("success"));
//    }
//
//    @Test
//    public void prGroupTemplateQueryTest(){
//        PrGroupTemplateEntity prGroupTemplateEntity = new PrGroupTemplateEntity();
//        prGroupTemplateEntity.setName("薪资组模板001");
//        JSONObject jo = testRestTemplate.postForObject("/prGroupTemplateQuery", JSONObject.toJSON(prGroupTemplateEntity), JSONObject.class);
//        assertTrue(jo.getBoolean("success"));
//    }
//
//    @Test
//    public void updatePrGroupTemplateById(){
//        PrGroupTemplateEntity prGroupTemplateEntity = new PrGroupTemplateEntity();
//        prGroupTemplateEntity.setEntityId("PRGT2");
//        prGroupTemplateEntity.setCode("XZZMB-213" );
//        prGroupTemplateEntity.setName("薪资组模板002");
//        prGroupTemplateEntity.setCreatedBy("gu");
//        prGroupTemplateEntity.setModifiedBy("gu");
//        prGroupTemplateService.addItem(prGroupTemplateEntity);
//
//        prGroupTemplateEntity.setRemark("薪资组模板更新");
//        testRestTemplate.put("/prGroupTemplate/PRGT2",JSONObject.toJSON(prGroupTemplateEntity));
//        PrGroupTemplateEntity prGroupItem = prGroupTemplateService.getItemById("PRGT2");
//        assertTrue(prGroupItem.getRemark().equals("薪资组模板更新"));
//
//        prGroupTemplateService.deleteById("PRGT2");
//    }
//
//    @Test
//    public void deleteTest(){
//        testRestTemplate.delete("/prGroupTemplate/PRGT1");
//    }
}
