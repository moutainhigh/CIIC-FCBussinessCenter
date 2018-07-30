package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.ciicsh.gto.fcbusinesscenter","com.ciicsh.gt1"})
@EnableDiscoveryClient
@EnableFeignClients({"com.ciicsh.gto.identityservice.api", "com.ciicsh.gto.salarymanagementcommandservice.api"})
@EnableScheduling
public class MainApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MainApplication.class);
    }
}
