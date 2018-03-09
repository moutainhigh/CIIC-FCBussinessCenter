package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration //让Spring来加载该类配置

@EnableSwagger2 //启用Swagger2
/**
 * @author yuantongqing on 2017/03/08
 */
public class Swagger2 {

    @Bean
    public Docket test1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("测试1")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ciicsh.gto.fcbusinesscenter.tax.commandservice"))
                .build();
    }

    @Bean
    public Docket test2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("测试2")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ciicsh.gto.fcbusinesscenter.tax.commandservice"))
                .build();
    }
}