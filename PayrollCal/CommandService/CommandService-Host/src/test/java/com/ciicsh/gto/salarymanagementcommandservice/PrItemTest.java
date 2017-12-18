//package com.ciicsh.gto.salarymanagementcommandservice;
//
//import com.alibaba.fastjson.JSONObject;
//import com.ciicsh.gto.salarymanagement.entity.PrItemEntity;
//import com.ciicsh.gto.salarymanagementcommandservice.service.PrItemService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.junit.Assert.assertTrue;
//
//@RunWith(SpringRunner.class)
//public class PrItemTest extends PrBaseTest {
//
//    @Autowired
//    private PrItemService prItemService;
//
//    @Test
//    public void newPrItemTest(){
//        PrItemEntity prItemEntity = new PrItemEntity();
//        prItemEntity.setEntityId("PRI1");
//        prItemEntity.setManagementId("001");
//        prItemEntity.setPrGroupId("PRG1");
//        prItemEntity.setName("公积金1");
//        prItemEntity.setAliasName("昵称");
//        prItemEntity.setType(3);
//        prItemEntity.setFormula("公积金1*9");
//        prItemEntity.setDataType(1);
//        prItemEntity.setDefaultValueStyle(1);
//        prItemEntity.setIsHidden(true);
//        prItemEntity.setIsActive(true);
//
//        JSONObject jo = testRestTemplate.postForObject("/prItem", JSONObject.toJSON(prItemEntity), JSONObject.class);
//        assertTrue(jo.getBoolean("success"));
//        assertTrue(jo.get("data").equals(1));
//
//        prItemService.deleteItemById("PRI1");
//    }
//
//    @Test
//    public void getPrItemListTest() {
//        JSONObject jo = testRestTemplate.getForObject("/prItem?pageNum=1", JSONObject.class);
//        assertTrue(jo.getBoolean("success"));
//    }
//
//    @Test
//    public void getPrItemNameListTest(){
//        JSONObject jo = testRestTemplate.getForObject("/prItemName?managementId=001&pageNum=1", JSONObject.class);
//        assertTrue(jo.getBoolean("success"));
//        assertTrue(jo.get("data")!=null);
//    }
//
//    @Test
//    public void getPrItemTypeListTest(){
//        JSONObject jo = testRestTemplate.getForObject("/prItemType?managementId=001&pageNum=1", JSONObject.class);
//        assertTrue(jo.getBoolean("success"));
//    }
//
//    @Test
//    public void searchPrItemListTest() {
//        PrItemEntity prItemEntity = new PrItemEntity();
//        prItemEntity.setManagementId("001");
//        prItemEntity.setName("1");
//        prItemEntity.setType(3);
//
//        JSONObject jo = testRestTemplate.postForObject("/prItemQuery", JSONObject.toJSON(prItemEntity), JSONObject.class );
//        assertTrue(jo.getBoolean("success"));
//    }
//
//    @Test
//    public void updatePrItemTest(){
//        //新增
//        PrItemEntity prItemEntity = new PrItemEntity();
//        prItemEntity.setEntityId("PRI2");
//        prItemEntity.setManagementId("001");
//        prItemEntity.setPrGroupId("PRG1");
//        prItemEntity.setCode("aaa");
//        prItemEntity.setName("公积金2");
//        prItemEntity.setType(3);
//        prItemEntity.setFormula("公积金1*1");
//        prItemEntity.setDataType(1);
//        prItemEntity.setDefaultValueStyle(1);
//        prItemEntity.setIsHidden(true);
//        prItemEntity.setIsActive(true);
//        prItemEntity.setCreatedBy("gu");
//        prItemEntity.setModifiedBy("gu");
//        prItemService.addItem(prItemEntity);
//
//        prItemEntity.setCode("XZZ-001-001");
//        prItemEntity.setAliasName("公积金别名");
//        prItemEntity.setRemark("更新测试备注");
//
//        testRestTemplate.put("/prItem/PRI2",JSONObject.toJSON(prItemEntity));
//        PrItemEntity item = prItemService.getItem("PRI2");
//        assertTrue(item.getCode().equals("XZZ-001-001"));
//
//        //删除
//        prItemService.deleteItemById("PRI2");
//    }
//
//}
