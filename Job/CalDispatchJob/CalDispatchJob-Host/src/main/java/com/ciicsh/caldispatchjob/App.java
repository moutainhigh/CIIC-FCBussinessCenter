package com.ciicsh.caldispatchjob;

import com.ciicsh.caldispatchjob.compute.util.JavaScriptEngine;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import javax.script.ScriptEngine;

/**
 * Created by shenjian on 2017/11/14.
 */

@SpringBootApplication(scanBasePackages = {
        "com.ciicsh.gt1",
        "com.ciicsh.gto.fcbusinesscenter.util",
        "com.ciicsh.gto.salarymanagementcommandservice.service",
        "com.ciicsh.caldispatchjob"}
        )
@MapperScan("com.ciicsh.gto.salarymanagementcommandservice.dao")
@EnableFeignClients({"com.ciicsh.gto.companycenter.webcommandservice.api"})// 指定对应中心的 @FeignClient 所在对应的包
public class App {
    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }
}
