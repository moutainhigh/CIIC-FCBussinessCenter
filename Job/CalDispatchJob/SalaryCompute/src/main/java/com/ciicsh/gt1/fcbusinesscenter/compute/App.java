package com.ciicsh.gt1.fcbusinesscenter;

import com.ciicsh.caldispatchjob.compute.util.JavaScriptEngine;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import javax.script.ScriptEngine;

/**
 * Created by bill on 18/4/19.
 */
@SpringBootApplication(scanBasePackages = {
        "com.ciicsh.gt1",
        "com.ciicsh.gto.fcbusinesscenter.util",
        "com.ciicsh.gto.salarymanagementcommandservice.dao",
        "com.ciicsh.gto.salarymanagementcommandservice.service.common",
        "com.ciicsh.caldispatchjob"}
)
@MapperScan("com.ciicsh.gto.salarymanagementcommandservice.dao")
@EnableFeignClients({"com.ciicsh.gto.companycenter.webcommandservice.api"})// 指定对应中心的 @FeignClient 所在对应的包
public class App {
    public static void main(String[] args){
        initScriptEngine(); //初始化javascript 引擎
        SpringApplication.run(App.class, args);
    }

    private static void initScriptEngine(){
        ScriptEngine engine = JavaScriptEngine.createEngine();
        JavaScriptEngine.setEngine(engine);
        System.out.println("initialize script engine successfully...");
    }
}
