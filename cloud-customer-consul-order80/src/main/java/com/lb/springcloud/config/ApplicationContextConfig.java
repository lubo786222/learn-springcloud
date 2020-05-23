package com.lb.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationContextConfig
{
    // 1、直接链接时不用 @LoadBalanced：private static final String PAYMENT_URL="http://localhost:8001";
    //使用 @LoadBalanced注解赋予RestTemplate负载均衡的能力
    // 就可以使用服务名链接：private static final String PAYMENT_URL="http://CLOUD-PAYMENT-SERVICE";
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
