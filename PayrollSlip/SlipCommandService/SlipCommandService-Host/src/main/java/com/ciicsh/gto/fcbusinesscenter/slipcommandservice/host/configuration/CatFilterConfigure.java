package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.configuration;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.interceptor.CatInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CatFilterConfigure extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CatInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
