package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SpringBoot 方式启动类
 * @author gaoyang
 * @since 2018-01-19
 */
@EnableScheduling
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao"})
@EnableFeignClients({
        "com.ciicsh.gto.entityidservice.api",
        "com.ciicsh.gto.sheetservice.api",
        "com.ciicsh.gto.basicdataservice.api",
        "com.ciicsh.gto.logservice.api",
        "com.ciicsh.gto.identityservice.api",
        "com.ciicsh.gto.salarymanagementcommandservice.api",
        "com.ciicsh.gto.salecenter.apiservice.api",
        "com.ciicsh.gto.employeecenter.apiservice.api",
        "com.ciicsh.gto.companycenter.webcommandservice.api",
        "com.ciicsh.gto.settlementcenter.payment.cmdapi",
        "com.ciicsh.gto.billcenter.fcmodule.api",
        "com.ciicsh.gto.afsystemmanagecenter.apiservice.api",
        "com.ciicsh.gto.companycenter.webcommandservice.api",
        "com.ciicsh.gto.settlementcenter.gathering.queryapi",
        "com.ciicsh.gt1.exchangerate"
})
@SpringBootApplication(scanBasePackages = {
        "com.ciicsh.gt1",
        "com.ciicsh.gto.fcbusinesscenter.util",
        "com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice",
        "com.ciicsh.gto.logservice.client"
})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
