package com.ciicsh.gto.fcbusinesscenter.webcommandservice.host.controller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: guwei
 * @Description: 单元测试基础类
 * @Date: Created in 14:22 2017/12/28
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

}
