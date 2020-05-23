package com.lb.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySelfRule
{
    public IRule myRule()
    {
        return new RandomRule();
    }
}
