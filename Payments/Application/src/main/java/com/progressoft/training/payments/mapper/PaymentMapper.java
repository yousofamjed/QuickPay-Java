package com.progressoft.training.payments.mapper;


import com.progressoft.training.models.Payment;
import com.progressoft.training.payments.entity.PaymentEntity;
import com.progressoft.training.payments.resources.PaymentRequest;
import com.progressoft.training.payments.resources.PaymentResponse;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = "spring")
public interface PaymentMapper {

    Payment toDomain(PaymentRequest request);
    PaymentResponse toResponse(Payment payment);
    PaymentResponse toResponse(PaymentEntity entity);
    PaymentResponse toResponse(PaymentRequest request);
    PaymentEntity toEntity(Payment payment);
    Payment toDomain(PaymentEntity entity);

}
