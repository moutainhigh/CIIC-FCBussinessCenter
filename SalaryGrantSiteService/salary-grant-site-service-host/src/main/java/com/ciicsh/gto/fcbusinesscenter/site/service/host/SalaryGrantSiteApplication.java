package com.ciicsh.gto.fcbusinesscenter.site.service.host;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * SpringBoot 方式启动类
 * @author gaoyang
 * @since 2018-01-19
 */
@MapperScan(basePackages = {"com.ciicsh.gto.fcbusinesscenter.site.service.dao"})
@EnableFeignClients("**.api")
@SpringBootApplication(scanBasePackages =
        {
                "com.ciicsh.gt1",
                "com.ciicsh.gto.fcbusinesscenter.util",
                "com.ciicsh.gto.fcbusinesscenter.site.service",
        })
public class SalaryGrantSiteApplication {
    //private final static Logger logger = LoggerFactory.getLogger(SalaryGrantSiteApplication.class);

    public static void main(String[] args) {
        //logger.info("starting ...");
        SpringApplication.run(SalaryGrantSiteApplication.class, args);
       // logger.info("start is success!");
    }
}
