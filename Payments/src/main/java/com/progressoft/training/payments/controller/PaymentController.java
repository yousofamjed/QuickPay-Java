package com.progressoft.training.payments.controller;


import com.progressoft.training.payments.entity.Payment;
import com.progressoft.training.payments.mapper.PaymentMapper;
import com.progressoft.training.payments.resources.PaymentRequest;
import com.progressoft.training.payments.resources.PaymentResponse;
import com.progressoft.training.payments.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;
    public PaymentController(PaymentService paymentService, PaymentMapper paymentMapper) {
        this.paymentService = paymentService;
        this.paymentMapper = paymentMapper;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@RequestBody PaymentRequest paymentRequest){

        Payment entity = paymentMapper.toEntity(paymentRequest);
        PaymentResponse response = paymentMapper.toResponse(paymentService.save(entity));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getBy(@PathVariable Long id){

        if(paymentService.getBy(id).isPresent())
        return ResponseEntity.status(HttpStatus.OK).body(paymentMapper.toResponse(paymentService.getBy(id).get()));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();


    }

    @GetMapping
    public ResponseEntity<Collection<PaymentResponse>> getAll(){

        Collection<Payment> allPayments = paymentService.getAllPayments();
        Collection<PaymentResponse> allResponses = allPayments.stream().map(p->paymentMapper.toResponse(p)).toList();

        return ResponseEntity.status(HttpStatus.OK).body(allResponses);
    }
}
