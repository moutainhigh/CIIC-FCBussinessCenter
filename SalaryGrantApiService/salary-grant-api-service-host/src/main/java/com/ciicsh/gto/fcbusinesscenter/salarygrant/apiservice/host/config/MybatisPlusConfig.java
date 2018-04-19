package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.host.config;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.host.interceptor.CatMybatisInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao"})
public class MybatisPlusConfig {
    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * dev,生产请去掉
     *
     * @return
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }

    @Bean
    public CatMybatisInterceptor catMybatisInterceptor() {
        return new CatMybatisInterceptor();
    }
}

