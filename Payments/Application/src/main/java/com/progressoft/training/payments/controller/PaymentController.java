package com.progressoft.training.payments.controller;


import com.progressoft.training.models.Payment;
import com.progressoft.training.payments.mapper.PaymentMapper;
import com.progressoft.training.payments.resources.bulk.BulkUploadRequest;
import com.progressoft.training.payments.resources.payment.PaymentRequest;
import com.progressoft.training.payments.resources.payment.PaymentResponse;
import com.progressoft.training.payments.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Value("${payment.default-template:default}")
    private  String defaultTemplate;

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;
    public PaymentController(PaymentService paymentService, PaymentMapper paymentMapper) {
        this.paymentService = paymentService;
        this.paymentMapper = paymentMapper;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@RequestBody PaymentRequest paymentRequest){
        Payment payment= paymentMapper.toDomain(paymentRequest);
        PaymentResponse response = paymentMapper.toResponse(paymentService.save(payment));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping(value = "/bulk", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadBulk(@RequestPart("file") MultipartFile file,
                                        @RequestPart(value = "metadata", required = false) String metadataJson)
            throws IOException {
        String templateName = resolveTemplateName(metadataJson);

        Path tempFile = Files.createTempFile("upload-", "-" + file.getOriginalFilename());
        file.transferTo(tempFile);

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(paymentMapper.toResponse(paymentService.uploadBulk(templateName, tempFile)));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }
    private  String resolveTemplateName(String metadataJson) {
        return metadataJson == null || metadataJson.isBlank()
                ? defaultTemplate
                : new ObjectMapper().readValue(metadataJson, BulkUploadRequest.class).getTemplateName();

    }


    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getBy(@PathVariable Long id){
        Optional<Payment> paymentOptional = paymentService.getBy(id);

        if(paymentOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(paymentMapper.toResponse(paymentOptional.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<Collection<PaymentResponse>> getAll(){

        Collection<Payment> allPaymentEntities = paymentService.getAllPayments();
        Collection<PaymentResponse> allResponses = allPaymentEntities.stream().map(paymentMapper::toResponse).toList();

        return ResponseEntity.status(HttpStatus.OK).body(allResponses);
    }
}
