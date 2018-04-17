package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * SpringBoot 方式启动类
 * @author gaoyang
 * @since 2018-01-19
 */
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.ciicsh.gto.fcbusinesscenter.site.service.dao"})
@SpringBootApplication(scanBasePackages = {
    "com.ciicsh.gt1",
    "com.ciicsh.gto.fcbusinesscenter.util",
    "com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice",
})
public class SalaryGrantSiteApplication {
    public static void main(String[] args) {
        SpringApplication.run(SalaryGrantSiteApplication.class, args);
    }
}
