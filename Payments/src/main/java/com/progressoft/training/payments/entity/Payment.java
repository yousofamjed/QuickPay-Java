package com.progressoft.training.payments.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "Payment")
public class  Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String senderAccount;
    private  String receiverAccount;
    private  BigDecimal amount;
    private  String currency;
    private  String status;
    private  Instant createdAt;
    private  String notes;


}
