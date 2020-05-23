package com.lb.springcloud.payment.service;

import com.lb.springcloud.payment.entities.Payment;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService
{
    public int create(Payment payment);
    public Payment getPaymentById(Long id);
}
