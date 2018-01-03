package com.ciicsh.gto.fcbusinesscenter.webcommandservice.host;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * Created by guwei on 2017/12/27.
 */
@SpringBootApplication(scanBasePackages = {"com.ciicsh.gto.fcbusinesscenter.webcommandservice"})
@MapperScan("com.ciicsh.gto.fcbusinesscenter.webcommandservice.dao")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.ciicsh.gto.fcbusinesscenter")
public class App extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(App.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }
}
