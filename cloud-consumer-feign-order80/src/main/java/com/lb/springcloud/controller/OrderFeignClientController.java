package com.lb.springcloud.controller;

import com.lb.springcloud.common.CommonResult;
import com.lb.springcloud.payment.entities.Payment;
import com.lb.springcloud.service.PaymentFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class OrderFeignClientController
{
    @Autowired
    private PaymentFeignService paymentFeignService;


    @GetMapping("/customer/payment/lb")
    public String getPaymentLB()
    {
        return paymentFeignService.getPaymentLB();
    }

    @PostMapping("/customer/payment/create")
    public CommonResult create(@RequestBody Payment payment)
    {
        return paymentFeignService.create(payment);
    }

    @GetMapping("/customer/payment/get/{id}")
    public CommonResult getPaymentBy(@PathVariable("id") Long id)
    {
        return paymentFeignService.getPaymentBy(id);
    }

    @GetMapping(value = "/customer/payment/discovery")
    public Map<String, Object> getDiscoveryClient()
    {
        return paymentFeignService.getDiscoveryClient();
    }

    /**
     *     * 模拟feign超时
     *     *
     *     * @return
     *    
     */
    @GetMapping(value = "/customer/payment/feign/timeout")
    public String paymentFeignTimeout()
    {
        // openfeign-ribbon, 客户端一般默认等待1秒钟
        return paymentFeignService.paymentFeignTimeout();
    }


}
