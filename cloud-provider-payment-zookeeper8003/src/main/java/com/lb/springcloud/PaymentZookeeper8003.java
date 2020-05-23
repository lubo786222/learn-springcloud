package com.lb.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PaymentZookeeper8003
{
    public static void main(String[] args)
    {
        SpringApplication.run(PaymentZookeeper8003.class,args);
    }
}
