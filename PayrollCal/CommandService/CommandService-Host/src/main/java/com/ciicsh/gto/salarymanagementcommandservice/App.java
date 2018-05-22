package com.ciicsh.gto.salarymanagementcommandservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * Created by houwanhua on 2017/7/28.
 */
@SpringBootApplication(scanBasePackages =
        {
                "com.ciicsh.gt1",
                "com.ciicsh.gto.fcbusinesscenter.util",
                "com.ciicsh.gto.salarymanagementcommandservice",
        })
@EnableDiscoveryClient
@MapperScan("com.ciicsh.gto.salarymanagementcommandservice.dao")
@EnableFeignClients({
        "com.ciicsh.gto.basicdataservice.api",
        "com.ciicsh.gto.afsystemmanagecenter.apiservice.api",
        "com.ciicsh.gto.salecenter.apiservice.api",
        "com.ciicsh.gto.companycenter.webcommandservice.api",
        "com.ciicsh.gto.identityservice.api",
    })// 指定对应中心的 @FeignClient 所在对应的包
public class App {

    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }

    //    /**
//     * 在这里我们使用 @Bean注入 fastJsonHttpMessageConvert
//     * @return
//     */
//    @Bean
//    public HttpMessageConverters fastJsonHttpMessageConverters() {
//        // 1、需要先定义一个 convert 转换消息的对象;
//        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//
//        //2、添加fastJson 的配置信息，比如：是否要格式化返回的json数据;
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
//
//        //3、在convert中添加配置信息.
//        //处理中文乱码问题
//        List<MediaType> fastMediaTypes = new ArrayList<>();
//        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        fastConverter.setSupportedMediaTypes(fastMediaTypes);
//        fastConverter.setFastJsonConfig(fastJsonConfig);
//
//        HttpMessageConverter<?> converter = fastConverter;
//        return new HttpMessageConverters(converter);
//    }
}
