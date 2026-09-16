package com.progressoft.training.payments.service;

import com.progressoft.training.payments.entity.Payment;
import com.progressoft.training.payments.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PaymentService {

    private  final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    public Payment save(Payment payment){
       return paymentRepository.save(payment);

    }


    public Optional<Payment> getBy(Long id){
            return paymentRepository.findById(id);

    }

    public Collection<Payment> getAllPayments(){
        return paymentRepository.findAll();
    }

}


//create map struct