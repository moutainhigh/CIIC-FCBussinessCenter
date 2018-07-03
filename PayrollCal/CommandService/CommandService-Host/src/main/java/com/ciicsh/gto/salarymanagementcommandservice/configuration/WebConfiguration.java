package com.ciicsh.gto.salarymanagementcommandservice.configuration;

import com.ciicsh.gt1.common.auth.AuthenticateInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by bill on 18/4/26.
 */


@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public AuthenticateInterceptor getAuthenticateInterceptor() {
        return new AuthenticateInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAuthenticateInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/api/prBatch/**","/api/prAccountSet/**");

         super.addInterceptors(registry);

    }
}
