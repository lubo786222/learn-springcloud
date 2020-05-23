package com.lb.springcloud.load;


import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class MyLoadBalancer implements LoadBalancer
{
    private  AtomicInteger atomicInteger = new AtomicInteger(0);

    //手写一个负载的算法CAS+自旋锁
    private final int getAndIncrement()
    {
        int current;
        int next;
        for(;;)
        {
            current = this.atomicInteger.get();
            next = current>=Integer.MAX_VALUE?0:current+1;
            if(this.atomicInteger.compareAndSet(current,next)) break;
        }
        System.out.println("第几次访问,次数next:" + next);
        return next;
    }

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances)
    {
        int index = getAndIncrement() % serviceInstances.size();
        return serviceInstances.get(index);
    }
}
