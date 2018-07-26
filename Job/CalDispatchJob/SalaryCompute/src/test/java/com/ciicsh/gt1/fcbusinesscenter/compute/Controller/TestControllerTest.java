package com.ciicsh.gt1.fcbusinesscenter.compute.Controller;

import com.ciicsh.gt1.fcbusinesscenter.compute.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/26 0026
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void selectBillGroupListByCompanyId() {
        String url = "/api/fireRules";

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("city", "扬州");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,null);
        String result = restTemplate.postForObject(url, request, String.class);
        System.out.println("最低工资标准: " + result);
    }
}
