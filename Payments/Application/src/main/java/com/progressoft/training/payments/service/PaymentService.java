package com.progressoft.training.payments.service;

import com.progressoft.training.models.BulkUploadResult;
import com.progressoft.training.models.Payment;
import com.progressoft.training.payments.mapper.PaymentMapper;
import com.progressoft.training.repository.PaymentRepository;
import com.progressoft.training.usecases.CreatePaymentUseCase;
import com.progressoft.training.usecases.UploadBulkPaymentUseCase;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentMapper paymentMapper;

    private  final PaymentRepository paymentRepository;
    private final CreatePaymentUseCase createPaymentUseCase;
    private final UploadBulkPaymentUseCase uploadBulkPaymentUseCase;

    public PaymentService(PaymentMapper paymentMapper, PaymentRepository paymentRepository, CreatePaymentUseCase createPaymentUseCase, UploadBulkPaymentUseCase uploadBulkPaymentUseCase) {
        this.paymentMapper = paymentMapper;
        this.paymentRepository = paymentRepository;
        this.createPaymentUseCase = createPaymentUseCase;
        this.uploadBulkPaymentUseCase = uploadBulkPaymentUseCase;
    }
    public Payment save(Payment payment){
       return createPaymentUseCase.execute(payment);
    }

    public Optional<Payment> getBy(Long id){
            return Optional.ofNullable(paymentRepository.findBy(id));

    }

    public BulkUploadResult uploadBulk(String templateName, Path file) {

        BulkUploadResult result = uploadBulkPaymentUseCase.execute(templateName, file);
        return result;
    }

    public Collection<Payment> getAllPayments(){
        return paymentRepository.findAll().stream().toList();
    }

}

