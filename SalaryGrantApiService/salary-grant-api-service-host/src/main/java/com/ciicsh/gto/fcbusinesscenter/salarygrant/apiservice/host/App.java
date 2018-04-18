package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.host;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author chenpb
 * @date 2018-04-18
 */

@SpringBootApplication(scanBasePackages = {"com.ciicsh.gto.fcbusinesscenter.salarygrant"})
@EnableDiscoveryClient
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
