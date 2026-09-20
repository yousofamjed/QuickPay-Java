package com.progressoft.training.payments.service;

import com.progressoft.training.models.Payment;
import com.progressoft.training.payments.entity.PaymentEntity;
import com.progressoft.training.payments.mapper.PaymentMapper;
import com.progressoft.training.payments.repository.PaymentRepositoryJpa;
import com.progressoft.training.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentMapper paymentMapper;

    private  final PaymentRepository paymentRepository;

    public PaymentService(PaymentMapper paymentMapper,PaymentRepository paymentRepository) {
        this.paymentMapper = paymentMapper;
        this.paymentRepository = paymentRepository;

    }


    public Payment save(Payment payment){
       return paymentRepository.save(payment);

    }

    public Optional<Payment> getBy(Long id){
            return Optional.ofNullable(paymentRepository.findBy(id));

    }

    public Collection<Payment> getAllPayments(){
        return paymentRepository.findAll().stream().toList();
    }

}


//create map struct