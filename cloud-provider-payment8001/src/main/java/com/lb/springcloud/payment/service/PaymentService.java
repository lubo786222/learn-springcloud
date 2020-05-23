package com.lb.springcloud.payment.service;

import com.lb.springcloud.payment.entities.Payment;
import com.lb.springcloud.payment.mapper.PaymentMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService
{
    public int create( Payment payment);
    public Payment getPaymentById(Long id);
}
