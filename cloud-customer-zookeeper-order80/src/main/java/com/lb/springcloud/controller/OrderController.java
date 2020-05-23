package com.lb.springcloud.controller;

import com.lb.springcloud.common.CommonResult;
import com.lb.springcloud.payment.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class OrderController
{
    @Autowired
    private RestTemplate restTemplate;
    //private static final String PAYMENT_URL="http://localhost:8006";
    private static final String PAYMENT_URL="http://cloud-payment-service";
    @PostMapping("/customer/payment/create")
    public CommonResult<Payment> create(@RequestBody Payment payment)
    {
        return restTemplate.postForObject(PAYMENT_URL+"/payment/create",payment,CommonResult.class);
    }

    @GetMapping("/customer/payment/get/{id}")
    public CommonResult<Payment> getPaymentBy(@PathVariable("id") Long id)
    {
        return restTemplate.getForObject(PAYMENT_URL+"/payment/get/"+id,CommonResult.class);
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
}
