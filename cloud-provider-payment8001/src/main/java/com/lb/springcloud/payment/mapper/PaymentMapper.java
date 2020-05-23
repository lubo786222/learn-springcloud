package com.lb.springcloud.payment.mapper;

import com.lb.springcloud.payment.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentMapper
{
    public int create(@Param("pm") Payment payment);
    public Payment getPaymentById(@Param("id") Long id);
}
