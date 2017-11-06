package com.ciicsh.zorro.leopardwebservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

/**
 * Created by jiangtianning on 2017/10/31.
 */

@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
public abstract class PrBaseTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;
}
