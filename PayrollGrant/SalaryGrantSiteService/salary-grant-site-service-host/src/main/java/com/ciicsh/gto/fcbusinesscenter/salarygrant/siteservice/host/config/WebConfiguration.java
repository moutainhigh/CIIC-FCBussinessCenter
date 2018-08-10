package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.config;

import com.ciicsh.gt1.common.auth.AuthenticateInterceptor;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.interceptor.CatInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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
    public CatInterceptor getCatInterceptor() {
        return new CatInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration catInterceptor = registry.addInterceptor(getCatInterceptor());
        catInterceptor.addPathPatterns("/**");

        InterceptorRegistration addInterceptor = registry.addInterceptor(getAuthenticateInterceptor());
        // 拦截配置
        addInterceptor.addPathPatterns("/**");
        // 排除配置
        addInterceptor.excludePathPatterns("/api/sg/exportEmpInfo**");
        addInterceptor.excludePathPatterns("/api/sg/exportFailList**");
        addInterceptor.excludePathPatterns("/offerFile/download/**");
        addInterceptor.excludePathPatterns("/admin/**");
        addInterceptor.excludePathPatterns("/error/**");
    }

    /**
     * 跨域
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("*");
        super.addCorsMappings(registry);
    }
}
