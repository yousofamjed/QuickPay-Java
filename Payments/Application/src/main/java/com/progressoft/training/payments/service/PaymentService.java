package com.progressoft.training.payments.service;

import com.progressoft.training.models.Payment;
import com.progressoft.training.payments.mapper.PaymentMapper;
import com.progressoft.training.repository.PaymentRepository;
import com.progressoft.training.usecases.CreatePaymentUseCase;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentMapper paymentMapper;

    private  final PaymentRepository paymentRepository;
    private final CreatePaymentUseCase createPaymentUseCase;

    public PaymentService(PaymentMapper paymentMapper, PaymentRepository paymentRepository, CreatePaymentUseCase createPaymentUseCase) {
        this.paymentMapper = paymentMapper;
        this.paymentRepository = paymentRepository;

        this.createPaymentUseCase = createPaymentUseCase;
    }
    public Payment save(Payment payment){
       return createPaymentUseCase.execute(payment);
    }

    public Optional<Payment> getBy(Long id){
            return Optional.ofNullable(paymentRepository.findBy(id));

    }

    public Collection<Payment> getAllPayments(){
        return paymentRepository.findAll().stream().toList();
    }

}


//create map struct