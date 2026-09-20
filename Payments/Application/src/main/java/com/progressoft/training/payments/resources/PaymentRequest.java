package com.progressoft.training.payments.resources;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Setter
@Getter
public class PaymentRequest {


    private  String senderAccount;
    private  String receiverAccount;
    private BigDecimal amount;
    private  String currency;
    private  String status;
    private  String notes;
}
