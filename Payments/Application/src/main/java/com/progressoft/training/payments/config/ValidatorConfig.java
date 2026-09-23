package com.progressoft.training.payments.config;

import com.progressoft.training.models.Payment;
import com.progressoft.training.validation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ValidatorConfig {

    @Bean
    public NotesValidation notesValidation( @Value("${quickpay.payments.notesMaxlength:}") int maxLength){return new NotesValidation(maxLength);}

    @Bean
    public AmountValidation amountValidation(){return new AmountValidation();}

    @Bean
    public CreditorNameValidation creditorNameValidation(){return new CreditorNameValidation();}

    @Bean
    public CurrencyValidation currencyValidation(
            @Value("${quickpay.payments.defaultCurrencies:}")
            List<String>currencies

    ){return new CurrencyValidation(currencies);}

    @Bean
    public ReceiverAccountValidation receiverAccountValidation(){return new ReceiverAccountValidation();}

    @Bean
    public SenderAccountValidation senderAccountValidation(){return new SenderAccountValidation();}

    @Bean("createPaymentValidators")
    public ValidatorChain<Payment> createPaymentValidators(NotesValidation notesValidation, AmountValidation amountValidation, CreditorNameValidation creditorNameValidation, CurrencyValidation currencyValidation, ReceiverAccountValidation receiverAccountValidation, SenderAccountValidation senderAccountValidation){
        return new ValidatorChain<>(List.of(notesValidation, amountValidation, creditorNameValidation,currencyValidation,receiverAccountValidation, senderAccountValidation ));
    }

    @Bean("uploadBulkPayment")
    public ValidatorChain<Payment> uploadBulkPayment(NotesValidation notesValidation, AmountValidation amountValidation, CreditorNameValidation creditorNameValidation, CurrencyValidation currencyValidation, ReceiverAccountValidation receiverAccountValidation, SenderAccountValidation senderAccountValidation){
        return new ValidatorChain<>(List.of(notesValidation, amountValidation, creditorNameValidation,currencyValidation,receiverAccountValidation, senderAccountValidation ));
    }


}
