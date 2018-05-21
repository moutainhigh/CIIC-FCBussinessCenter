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
//        dataInterceptor.addPathPatterns("/**");
        dataInterceptor.addPathPatterns("/tax/api/queryTaxBatchDetail");
        dataInterceptor.addPathPatterns("/tax/api/queryTaskSubProofDetail");
        dataInterceptor.addPathPatterns("/tax/api/queryEmployee");
        dataInterceptor.addPathPatterns("/tax/api/queryTaskSubProofByRes");
        dataInterceptor.addPathPatterns("/tax/queryCalculationBatchDetails");
        dataInterceptor.addPathPatterns("/tax/queryTaxBatchDetailByRes");
        dataInterceptor.addPathPatterns("/tax/exportSubDeclare/**");
        dataInterceptor.addPathPatterns("/tax/exportDeclareBySubject/**");
        dataInterceptor.addPathPatterns("/tax/querySubDeclareDetailsByParams");
        dataInterceptor.addPathPatterns("/tax/querySubDeclareDetailsByCombined");
        dataInterceptor.addPathPatterns("/tax/querySubDeclareDetailsForCombined");
        dataInterceptor.addPathPatterns("/tax/querySubMoneyDetailsByParams");
        dataInterceptor.addPathPatterns("/tax/querySubPaymentDetailsByParams");
        dataInterceptor.addPathPatterns("/tax/queryTaskSubProofDetailBySubId");
        dataInterceptor.addPathPatterns("/tax/exportSubTaskProof/**");
        dataInterceptor.addPathPatterns("/tax/querySubSupplierDetailsByParams");
        dataInterceptor.addPathPatterns("/tax/querySubSupplierDetailsByCombined");
        dataInterceptor.addPathPatterns("/tax/querySubSupplierDetailsForCombined");
        dataInterceptor.addPathPatterns("/tax/exportQuitPerson/**");

    }
}
