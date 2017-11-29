package com.ciicsh.gto.fcsupportcenter.tax.queryservice.host;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.ciicsh.gto.fcsupportcenter.tax.commandservice"})
@MapperScan("com.ciicsh.gto.fcsupportcenter.tax.commandservice.dao")
@EnableDiscoveryClient
//@EnableFeignClients (basePackages = "com.ciicsh.gto.afsystemmanagecenter")
public class Luncher extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Luncher.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Luncher.class);
    }
}
