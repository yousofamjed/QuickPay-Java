package com.progressoft.training.payments.config;


import com.progressoft.training.models.Payment;
import com.progressoft.training.repository.PaymentRepository;
import com.progressoft.training.usecases.CreatePaymentUseCase;
import com.progressoft.training.validation.ValidatorChain;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
@Bean
    @Qualifier("createPaymentValidators")
    public CreatePaymentUseCase createPaymentUseCase(ValidatorChain<Payment> chain , PaymentRepository paymentRepository){
    return new CreatePaymentUseCase(chain , paymentRepository);
}
}
