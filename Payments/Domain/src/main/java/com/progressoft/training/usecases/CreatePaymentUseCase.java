package com.progressoft.training.usecases;

import com.progressoft.training.models.Payment;
import com.progressoft.training.repository.PaymentRepository;
import com.progressoft.training.validation.ValidatorChain;

import java.time.Instant;

public class CreatePaymentUseCase implements Usecases<Payment,Payment> {
    private final ValidatorChain<Payment> validatorChain;
    private final PaymentRepository paymentRepository;

    public CreatePaymentUseCase(ValidatorChain<Payment> validatorChain, PaymentRepository paymentRepository) {
        this.validatorChain = validatorChain;
        this.paymentRepository = paymentRepository;
    }


    @Override
    public Payment execute(Payment payment) {
        //validate
        validatorChain.validate(payment).throwExceptionIfViolated();

        //audit

        payment.setStatus("PENDING");
        payment.setCreatedAt(Instant.now());
        //save

        return paymentRepository.save(payment);
    }
}
