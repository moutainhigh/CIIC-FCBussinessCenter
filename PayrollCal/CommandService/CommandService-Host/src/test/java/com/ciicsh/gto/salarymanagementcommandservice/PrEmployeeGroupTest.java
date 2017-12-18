//package com.ciicsh.gto.salarymanagementcommandservice;
//
//import com.alibaba.fastjson.JSONObject;
//import com.ciicsh.gto.salarymanagement.entity.PrEmployeeGroupEntity;
//
//import com.ciicsh.gto.salarymanagementcommandservice.service.impl.EmployeeGroupServiceImpl;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.junit.Assert.assertTrue;
//
///**
// * Created by jiangtianning on 2017/10/31.
// */
//@RunWith(SpringRunner.class)
//public class PrEmployeeGroupTest extends PrBaseTest{
//
//    private EmployeeGroupServiceImpl prEmployeeGroupService;
//
//    @Test
//    public void insertTest(){
//        PrEmployeeGroupEntity employeeGroupEntity = new PrEmployeeGroupEntity();
//        employeeGroupEntity.setEntityId("PREG1");
//        employeeGroupEntity.setCode("GYZ-CMY0001" );
//        employeeGroupEntity.setCreatedBy("gu");
//        employeeGroupEntity.setModifiedBy("gu");
//        JSONObject jo = testRestTemplate.postForObject("/prEmployeeGroup", JSONObject.toJSON(employeeGroupEntity), JSONObject.class);
//        assertTrue(jo.getBoolean("success"));
//    }
//
//    @Test
//    public void getPrEmployeeGroupListTest() {
//        JSONObject jo = testRestTemplate.getForObject("/prEmployeeGroup?managementId=001&companyId=001&pageNum=1", JSONObject.class);
//        assertTrue(jo.getBoolean("success"));
//    }
//
//    @Test
//    public void searchPrEmployeeGroupListTest() {
//
//        PrEmployeeGroupEntity param = new PrEmployeeGroupEntity();
//        param.setManagementId("001");
//        param.setCompanyId("001");
//        param.setCode("GYZ-CMY000001-07479");
//        JSONObject jo = testRestTemplate.postForObject("/prEmployeeGroupQuery", JSONObject.toJSON(param), JSONObject.class );
//        assertTrue(jo.getBoolean("success"));
//    }
//
//    @Test
//    public void getEmployeeListTest() {
//        JSONObject jo = testRestTemplate.getForObject("/prEmployee?pageNum=1", JSONObject.class );
//        assertTrue(jo.getBoolean("success"));
//    }
//
//    @Test
//    public void updateEmployeeGroupTest() {
////        PrEmployeeGroupEntity param = new PrEmployeeGroupEntity();
////        param.setEntityId("PREG2");
////        param.setCode("xxa-981");
////        param.setManagementId("001");
////        param.setCompanyId("001");
////        param.setName("测试雇员组Test1111");
////        param.setRemark("TEST");
////        param.setCreatedBy("gu");
////        param.setModifiedBy("gu");
////        prEmployeeGroupService.addItem(param);
////
////        param.setName("雇员组001");
////
////        testRestTemplate.put("/prEmployeeGroup/PREG2", JSONObject.toJSON(param));
////
////        List<PrEmployeeGroupEntity> insertedEntityList = prEmployeeGroupService.getList(param, 0).getList();
////        assertTrue(insertedEntityList.get(0).getName().equals("雇员组001"));
////
////        prEmployeeGroupService.deleteItem("PREG2");
//    }
//
//    @Test
//    public void deleteTest(){
//        testRestTemplate.delete("/prEmployeeGroup/PREG1");
//    }
//}
