package com.lb.springcloud.controller;

import com.lb.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/consumer")
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod",commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500")
}) //配合全局fallback方法
public class OrderHystrixController
{
    @Autowired
    private PaymentHystrixService paymentHystrixService;

    /**
     * 超时访问全局fallback
     *
     * @param id
     * @return
     */
    @GetMapping("/payment/hystrix/timeout/global/{id}")
    @HystrixCommand
    public String paymentInfo_TimeOut_Global(@PathVariable("id") Integer id) {
       // int age =10/0;
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentHystrixService.paymentInfo_TimeOut(id);
    }

    /**
     * 全局fallback方法
     *
     * @return
     */
    private String payment_Global_FallbackMethod() {
        return "Global异常处理信息,请稍后再试。";
    }



    /**
     * 正常访问
     * http://localhost/consumer/payment/hystrix/ok/1
     *
     * @param id
     * @return
     */
    @GetMapping("/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id) {
        return paymentHystrixService.paymentInfo_OK(id);
    }

    /**
     * 超时访问指定的fallback
     *
     * @param id
     * @return
     */
    @GetMapping("/payment/hystrix/timeout/{id}")
    @HystrixCommand(fallbackMethod = "paymentTimeOutFallbackMethod", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500")
    })
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id) {
        return paymentHystrixService.paymentInfo_TimeOut(id);
    }

    /**
     * 服务降级代码和控制接口层的业务代码耦合，不好可以在“服务调用”，即：
     * 定义@FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT")时，
     * 再定义ｆallｂａｃｋ，服务调用（OｐｅｎFｅｉｇｎ接口的ｆallｂａｃｋ的实现类）
     * @FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT",fallback = PaymentFallbackService.class)
     * 这样就可以接把耦合的fallback方法从控制层解耦
     * @param id
     * @return
     */
    private String paymentTimeOutFallbackMethod(@PathVariable("id") Integer id) {
        return "消费者80，对方支付系统繁忙，或自己运行出错请检查自己，请10秒后再试。";
    }

}
