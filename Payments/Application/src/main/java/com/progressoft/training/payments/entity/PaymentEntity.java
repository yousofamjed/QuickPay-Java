package com.progressoft.training.payments.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_sequence")
    @SequenceGenerator(
            name = "payment_sequence",
            sequenceName = "payments_sequence",
            allocationSize = 1
    )
    private Long id;

    private  String senderAccount;
    private  String receiverAccount;
    private  BigDecimal amount;
    private  String currency;
    private  String status;
    private  Instant createdAt;
    private  String notes;
    private String creditorName;

}
