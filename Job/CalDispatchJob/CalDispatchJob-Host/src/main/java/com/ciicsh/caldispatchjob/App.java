package com.ciicsh.caldispatchjob;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by shenjian on 2017/11/14.
 */

@SpringBootApplication(scanBasePackages =
        {"com.ciicsh.gt1","com.ciicsh.gto.fcsupportcenter.util",
                "com.ciicsh.gto.salarymanagementcommandservice.dao",
        "com.ciicsh.caldispatchjob"})
@MapperScan("com.ciicsh.gto.salarymanagementcommandservice.dao")
public class App {
    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }
}
