package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.host;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * SpringBoot 方式启动类
 * @author chenpb
 * @since 2018-04-18
 */
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao"})
@EnableFeignClients({
        "com.ciicsh.gto.logservice.api",
        "com.ciicsh.gto.basicdataservice.api"
})
@SpringBootApplication(scanBasePackages = {
        "com.ciicsh.gt1",
        "com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice",
        "com.ciicsh.gto.logservice.client"
})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
