package com.ciicsh.grantqueryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Created by shenjian on 2017/11/28.
 */
@SpringBootApplication (exclude = { DataSourceAutoConfiguration.class })
public class App {
    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }
}