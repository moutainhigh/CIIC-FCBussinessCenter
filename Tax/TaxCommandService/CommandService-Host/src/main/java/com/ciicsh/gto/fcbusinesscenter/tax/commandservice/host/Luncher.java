package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.ciicsh.gto.fcbusinesscenter.tax.commandservice","com.ciicsh.gt1"})
@MapperScan("com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.ciicsh.gto.logservice.api","com.ciicsh.gto.settlementcenter"
                                    ,"com.ciicsh.gto.identityservice.api","com.ciicsh.gto.basicdataservice.api"
                                    ,"com.ciicsh.gto.sheetservice.api"})
public class Luncher extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Luncher.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Luncher.class , args);
    }
}
