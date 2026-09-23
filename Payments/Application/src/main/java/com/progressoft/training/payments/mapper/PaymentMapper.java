package com.progressoft.training.payments.mapper;


import com.progressoft.training.models.BulkUploadResult;
import com.progressoft.training.models.Payment;
import com.progressoft.training.payments.entity.PaymentEntity;
import com.progressoft.training.payments.resources.bulk.BulkUploadResultResponse;
import com.progressoft.training.payments.resources.payment.PaymentRequest;
import com.progressoft.training.payments.resources.payment.PaymentResponse;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = "spring")
public interface PaymentMapper {

    Payment toDomain(PaymentRequest request);
    PaymentResponse toResponse(Payment payment);
    PaymentEntity toEntity(Payment payment);
    Payment toDomain(PaymentEntity entity);
    BulkUploadResultResponse toResponse(BulkUploadResult result);

}
