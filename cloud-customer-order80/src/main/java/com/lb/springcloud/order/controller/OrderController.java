package com.lb.springcloud.order.controller;

import com.lb.springcloud.common.CommonResult;
import com.lb.springcloud.load.LoadBalancer;
import com.lb.springcloud.payment.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
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

    @Autowired
    private LoadBalancer loadBalancer;


    @Autowired
    private DiscoveryClient discoveryClient;


    //private static final String PAYMENT_URL="http://localhost:8001";
    private static final String PAYMENT_URL="http://CLOUD-PAYMENT-SERVICE";

    @GetMapping("/customer/payment/lb")
    public String getPaymentUrl()
    {
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        if(serviceInstances==null || serviceInstances.size()<=0)
        {
            return null;
        }
        ServiceInstance serviceInstance = loadBalancer.instances(serviceInstances);
        URI             uri             = serviceInstance.getUri();

        return restTemplate.getForObject(uri+"/payment/lb",String.class);

    }


    @PostMapping("/customer/payment/create")
    public CommonResult<Payment> create(@RequestBody Payment payment)
    {
        return restTemplate.postForObject(PAYMENT_URL+"/payment/create",payment,CommonResult.class);
    }


    @PostMapping("/customer/payment/create-entity")
    public CommonResult<Payment> createForEntity(@RequestBody Payment payment)
    {
        ResponseEntity<CommonResult> forEntity = restTemplate.postForEntity(PAYMENT_URL+"/payment/create",payment,CommonResult.class);
        if(forEntity.getStatusCode().is2xxSuccessful())
        {
            return forEntity.getBody();
        }
        return new CommonResult<>(444,"操作失败");
    }

    @GetMapping("/customer/payment/get/{id}")
    public CommonResult<Payment> getPaymentBy(@PathVariable("id") Long id)
    {
        return restTemplate.getForObject(PAYMENT_URL+"/payment/get/"+id,CommonResult.class);
    }


    @GetMapping("/customer/payment/get-entity/{id}")
    public CommonResult<Payment> getPaymentForEntity(@PathVariable("id") Long id)
    {
        ResponseEntity<CommonResult> forEntity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id,
                CommonResult.class);
        if(forEntity.getStatusCode().is2xxSuccessful())
        {
            return forEntity.getBody();
        }
        return new CommonResult<>(444,"操作失败");
    }


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
