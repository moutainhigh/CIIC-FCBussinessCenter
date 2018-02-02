package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskSubMoneyControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(TaskSubMoneyControllerTest.class);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        //MockMvcBuilders使用构建MockMvc对象   （项目拦截器有效）
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        //单个类  拦截器无效
    }

    @Test
    public void querySubMoneyById() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/tax/querySubMoneyById/32/")
                .header("SESSIONNO", "");
        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertTrue("正确", status == 200);
//        Assert.assertFalse("错误", status != 200);
        logger.info("返回结果：" + status);
        logger.info(content);
    }
}
