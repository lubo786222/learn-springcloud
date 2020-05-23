package com.lb.springcloud.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//OpenFeign日志增强
//
//openfeign提供了日志打印功能。
//
//Logger有四种类型：NONE(默认)、BASIC、HEADERS、FULL，通过注册Bean来设置日志记录级别
//
//1、配置类
@Configuration
public class FeignConfig
{
    /**
     * feignClient日志级别配置
     * @return
     */
    @Bean
    public Logger.Level feignLoggerLevel()
    {
        // 请求和响应的头信息,请求和响应的正文及元数据
        return Logger.Level.FULL;

    }
}
