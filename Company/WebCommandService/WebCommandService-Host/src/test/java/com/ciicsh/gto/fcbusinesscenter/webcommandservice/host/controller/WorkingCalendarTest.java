package com.ciicsh.gto.fcbusinesscenter.webcommandservice.host.controller;

import com.alibaba.fastjson.JSONObject;
import com.ciicsh.gto.fcbusinesscenter.webcommandservice.entity.dto.WorkingCalendarPageDTO;
import com.ciicsh.gto.fcsupportcenter.util.result.JsonResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * @Author: guwei
 * @Description: 工作日历单元测试
 * @Date: Created in 14:20 2017/12/28
 */
@RunWith(SpringRunner.class)
public class WorkingCalendarTest extends BaseTest{

    @Test
    public void testAdd(){
        WorkingCalendarPageDTO workingCalendarPageDTO = new WorkingCalendarPageDTO();
        Random random = new Random();
        workingCalendarPageDTO.setWorkingCalendarName("2017CIICSH标准年历"+random.nextInt()+"测试");
        workingCalendarPageDTO.setManagementId("02134846");
        workingCalendarPageDTO.setYear("2017");
        workingCalendarPageDTO.setWorkDayJanuary("1,2,3,4,5,6,7");
        workingCalendarPageDTO.setRestDayJanuary("8,9,10");

        JSONObject jo = testRestTemplate.postForObject("/api/addWorkingCalendarItem", JSONObject.toJSON(workingCalendarPageDTO), JSONObject.class);
        assertTrue(jo.get("errCode").equals("0"));
    }


    @Test
    public void testSelect(){
        JSONObject jr = testRestTemplate.getForObject("/api/getWorkingCalendarPage", JSONObject.class);
        assertTrue(jr.get("errCode").equals("0"));
    }


//    @Test
//    public void testupdate(){
//
//    }
//
//    @Test
//    public void testDelect(){
//
//    }
}
