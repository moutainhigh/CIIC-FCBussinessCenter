package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * SpringBoot 方式启动类
 * @author gaoyang
 * @since 2018-01-19
 */
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao"})
@EnableFeignClients({
        "com.ciicsh.gto.entityidservice.api",
        "com.ciicsh.gto.sheetservice.api",
        "com.ciicsh.gto.basicdataservice.api"
})
@SpringBootApplication(scanBasePackages = {
        "com.ciicsh.gt1",
        "com.ciicsh.gto.fcbusinesscenter.util",
        "com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice",
})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
