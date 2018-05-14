package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.configuration;

import com.ciicsh.gt1.common.auth.AuthenticateInterceptor;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.interceptor.DataInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 服务前置拦截
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public AuthenticateInterceptor getAuthenticateInterceptor() {
        return new AuthenticateInterceptor();
    }

    @Bean
    public DataInterceptor getDataInterceptor() {
        return new DataInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //token Interceptor
        InterceptorRegistration addInterceptor = registry.addInterceptor(getAuthenticateInterceptor());

        // 排除配置
        addInterceptor.excludePathPatterns("/swagger-resources/**");
//        addInterceptor.excludePathPatterns("/login**");

        // 拦截配置
        addInterceptor.addPathPatterns("/**");

        //data Interceptor
        InterceptorRegistration dataInterceptor = registry.addInterceptor(getDataInterceptor());

        // 拦截配置
        dataInterceptor.addPathPatterns("/**");
    }
}
