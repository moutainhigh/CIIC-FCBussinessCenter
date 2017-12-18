package com.ciicsh.gto.salarymanagementcommandservice;

import com.alibaba.fastjson.JSONObject;
import com.ciicsh.gto.salarymanagement.entity.PrAccountSetEntity;
import com.ciicsh.gto.salarymanagementcommandservice.service.impl.PrAccountSetServiceImpl;
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
//
//    @Autowired
//    private PrAccountSetServiceImpl prAccountSetService;
//
//    @Test
//    @Transactional
//    public void addAccountSetTest() {
//        PrAccountSetEntity accountSetEntity = new PrAccountSetEntity();
//        accountSetEntity.setEntityId("PRA1");
//        accountSetEntity.setManagementId("001");
//        accountSetEntity.setName("薪资账套FROM_JUNIT");
//        accountSetEntity.setPrGroupId("PRG000001");
//        accountSetEntity.setPrEmployeeGroupId("PEG000001");
//        accountSetEntity.setStartSalaryDate(new Date());
//        accountSetEntity.setEndSalaryDate(new Date());
//        accountSetEntity.setTaxPeriod(20);
//        accountSetEntity.setRemark("remark from JUNIT");
//        JSONObject jo = testRestTemplate.postForObject("/prAccountSet", accountSetEntity, JSONObject.class);
//        assertTrue(jo.getBoolean("success"));
//
//    }
//
//    @Test
//    public void getAccountSetListTest() {
//        JSONObject jo = testRestTemplate.getForObject("/prAccountSet?managementId=001&pageNum=1", JSONObject.class);
//        assertTrue(jo.getBoolean("success"));
//    }
//
//
//
//    @Test
//    public void searchAccountSetListTest() {
//        PrAccountSetEntity accountSetEntity = new PrAccountSetEntity();
//        accountSetEntity.setEntityId("PRA1");
//        JSONObject jo = testRestTemplate.postForObject("/prAccountSetQuery?pageNum=1", accountSetEntity, JSONObject.class);
//        assertTrue(jo.getBoolean("success"));
//    }
//
//
//    @Test
//    public void getAccountSet() {
//        JSONObject resultObj = testRestTemplate.getForObject("/prAccountSet/PRAS000001", JSONObject.class);
//        assertTrue(resultObj.getBoolean("success"));
//    }
//
//    @Test
//    public void updateAccountSet() {
//        PrAccountSetEntity accountSetEntity = new PrAccountSetEntity();
//        accountSetEntity.setEntityId("PRA2");
//        accountSetEntity.setManagementId("001");
//        accountSetEntity.setCode("sda-121");
//        accountSetEntity.setName("薪资账套FROM_JUNIT_TEST_UPDATE");
//        accountSetEntity.setPrGroupId("PRG000001");
//        accountSetEntity.setPrEmployeeGroupId("PEG000001");
//        accountSetEntity.setStartSalaryDate(new Date());
//        accountSetEntity.setEndSalaryDate(new Date());
//        accountSetEntity.setTaxPeriod(20);
//        accountSetEntity.setRemark("remark from JUNIT");
//        accountSetEntity.setCreatedBy("gu");
//        accountSetEntity.setModifiedBy("gu");
//        prAccountSetService.addItem(accountSetEntity);
//
//        accountSetEntity.setRemark("测试");
//        testRestTemplate.put("/prAccountSet/PRA2", JSONObject.toJSON(accountSetEntity), JSONObject.class);
//
//        PrAccountSetEntity item = prAccountSetService.getItemById("PRA2");
//        assertTrue(item.getRemark().equals("测试"));
//
//        prAccountSetService.deleteItemById("PRA2");
//    }
//
//    @Test
//    public void getAccountSetNameList() {
//        JSONObject jo = testRestTemplate.getForObject("/prAccountSetName?managementId=001", JSONObject.class );
//        assertTrue(jo.getBoolean("success"));
//    }
//
//    @Test
//    public void deleteTest(){
//        testRestTemplate.delete("/prAccountSet/PRA1");
//    }

}
