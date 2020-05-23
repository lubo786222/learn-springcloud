package com.lb.springcloud.payment.service.impl;

import com.lb.springcloud.payment.entities.Payment;
import com.lb.springcloud.payment.mapper.PaymentMapper;
import com.lb.springcloud.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService
{
    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
    public int create(Payment payment)
    {
        return  paymentMapper.create(payment);
    }

    @Override
    public Payment getPaymentById(Long id)
    {
        return paymentMapper.getPaymentById(id);
    }
}
