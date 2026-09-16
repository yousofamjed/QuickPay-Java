package com.progressoft.training.payments.mapper;


import com.progressoft.training.payments.entity.Payment;
import com.progressoft.training.payments.resources.PaymentRequest;
import com.progressoft.training.payments.resources.PaymentResponse;
import org.mapstruct.Mapper;
import org.springframework.http.ResponseEntity;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    //we take a paymentRequest and we return it to an entity
    public Payment toEntity(PaymentRequest paymentRequest);

    //we take a paymententitty and we return it as a payment response
    public PaymentResponse toResponse(Payment payment);

}
