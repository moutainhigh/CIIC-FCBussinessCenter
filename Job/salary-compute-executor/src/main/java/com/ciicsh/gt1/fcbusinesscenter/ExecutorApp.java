package com.ciicsh.gt1.fcbusinesscenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(scanBasePackages = {
        "com.ciicsh.gt1.mongo",
        "com.ciicsh.gt1.fcbusinesscenter.config",
        "com.ciicsh.gt1.fcbusinesscenter.mongo",
        "com.ciicsh.gt1.fcbusinesscenter.jobHandler",
        "com.ciicsh.gt1.fcbusinesscenter.engine"}
)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class ExecutorApp {
    public static void main( String[] args ) {
        SpringApplication.run(ExecutorApp.class, args);
    }
}
