package com.ciicsh.caldispatchjob;

import com.ciicsh.caldispatchjob.compute.util.JavaScriptEngine;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.script.ScriptEngine;

/**
 * Created by shenjian on 2017/11/14.
 */

@SpringBootApplication(scanBasePackages = {
        "com.ciicsh.gt1",
        "com.ciicsh.gto.fcbusinesscenter.util",
        "com.ciicsh.gto.salarymanagementcommandservice.dao",
        "com.ciicsh.caldispatchjob"}
        )
@MapperScan("com.ciicsh.gto.salarymanagementcommandservice.dao")
//@Import(com.ciicsh.gt1.config.MongoConfig.class)
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
