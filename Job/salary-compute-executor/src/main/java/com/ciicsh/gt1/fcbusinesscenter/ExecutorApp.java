package com.ciicsh.gt1.fcbusinesscenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {
        "com.ciicsh.gt1",
        "com.ciicsh.gt1.fcbusinesscenter.config",
        "com.ciicsh.gt1.fcbusinesscenter.mongo",
        "com.ciicsh.gt1.fcbusinesscenter.jobHandler",
        "com.ciicsh.gt1.fcbusinesscenter.engine"}
)
public class ExecutorApp {
    public static void main( String[] args ) {
        SpringApplication.run(ExecutorApp.class, args);
    }
}
