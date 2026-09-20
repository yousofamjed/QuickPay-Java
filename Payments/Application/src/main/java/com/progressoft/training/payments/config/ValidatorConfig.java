package com.progressoft.training.payments.config;

import com.progressoft.training.models.Payment;
import com.progressoft.training.validation.*;
import org.apache.el.util.Validation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ValidatorConfig {

    @Bean
    public NotesValidation notesValidation(){return new NotesValidation();}

    @Bean
    public AmountValidation amountValidation(){return new AmountValidation();}

    @Bean
    public CreditorNameValidation creditorNameValidation(){return new CreditorNameValidation();}

    @Bean
    public CurrencyValidation currencyValidation(){return new CurrencyValidation();}

    @Bean
    public ReceiverAccountValidation receiverAccountValidation(){return new ReceiverAccountValidation();}

    @Bean
    public SenderAccountValidation senderAccountValidation(){return new SenderAccountValidation();}

    @Bean
    public ValidatorChain<Payment> validatorChain(){
        return new ValidatorChain<>(List.of(new NotesValidation(), new AmountValidation(), new CreditorNameValidation(), new CurrencyValidation(),new ReceiverAccountValidation(), new SenderAccountValidation() ));
    }

}
