package com.lb.springcloud.payment.controller;

import com.lb.springcloud.common.CommonResult;
import com.lb.springcloud.payment.entities.Payment;
import com.lb.springcloud.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController
{

    @Autowired
    private PaymentService paymentService;
    @Value("${server.port}")
    private String serverPort;


    @GetMapping("/payment/lb")
    public String getPaymentLB()
    {
        return serverPort;
    }

    @PostMapping("/payment/create")
    public CommonResult create(@RequestBody Payment payment)
    {
        int result  = paymentService.create(payment);
        log.info("插入结果"+result);
        if(result>0)
        {
            return new CommonResult(200,"插入成功,当前服务提供者端口serverPort: "+ serverPort,result);
        }
        else
        {

            return new CommonResult(500,"插入失败,当前服务提供者端口serverPort: "+ serverPort,null);
        }
    }

    @GetMapping("/payment/get/{id}")
    public CommonResult getPaymentBy(@PathVariable("id") Long id)
    {
        Payment result  = paymentService.getPaymentById(id);

        if(result!=null)
        {
            return new CommonResult(200,"查询成功,当前服务提供者端口serverPort: "+ serverPort,result);
        }
        else
        {
            return new CommonResult(404,"查询失败,当前服务提供者端口serverPort: "+ serverPort+"查询Id:"+id,null);
        }
    }

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping(value = "/payment/discovery")
    public Map<String,Object> getDiscoveryClient()
    {
        Map<String,Object>       servicesMap  = new HashMap<>();
        List<Map<String,Object>> servicesList = new ArrayList<>();
        List<String>             services     = discoveryClient.getServices();
        for (String service:services)
        {
            Map<String,Object> serviceMap = new HashMap<>();
            serviceMap.put("Service",service);
            List<Map<String,Object>> instanceList = new ArrayList<>();
            List<ServiceInstance>    instances    = discoveryClient.getInstances(service);
            for (ServiceInstance inst:instances)
            {
                Map<String,Object> instance = new HashMap<>();
                instance.put("Host",inst.getHost());
                instance.put("Port",inst.getPort());
                instance.put("Uri",inst.getUri());
                instance.put("InstanceId",inst.getInstanceId());
                instance.put("Scheme",inst.getScheme());
                instance.put("ServiceId",inst.getServiceId());
                instanceList.add(instance);
            }
            serviceMap.put("Instances",instanceList);
            servicesList.add(serviceMap);
        }
        servicesMap.put("Services",servicesList);
        return servicesMap;
    }

    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout()
    {
        try
        {
            // 暂停3秒钟 模拟超时任务
            TimeUnit.SECONDS.sleep(3);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return serverPort;
    }
}
