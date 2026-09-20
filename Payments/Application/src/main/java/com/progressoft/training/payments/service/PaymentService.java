package com.progressoft.training.payments.service;

import com.progressoft.training.payments.entity.PaymentEntity;
import com.progressoft.training.payments.repository.PaymentRepositoryJpa;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PaymentService {

    private  final PaymentRepositoryJpa paymentRepositoryJpa;

    public PaymentService(PaymentRepositoryJpa paymentRepositoryJpa) {
        this.paymentRepositoryJpa = paymentRepositoryJpa;
    }


    public PaymentEntity save(PaymentEntity paymentEntity){
       return paymentRepositoryJpa.save(paymentEntity);

    }


    public Optional<PaymentEntity> getBy(Long id){
            return paymentRepositoryJpa.findById(id);

    }

    public Collection<PaymentEntity> getAllPayments(){
        return paymentRepositoryJpa.findAll();
    }

}


//create map struct