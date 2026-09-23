package com.progressoft.training.payments.resources.payment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {


    private  String senderAccount;
    private  String receiverAccount;
    private BigDecimal amount;
    private  String currency;
    private  String status;
    private  String notes;
    private String creditorName;
}
