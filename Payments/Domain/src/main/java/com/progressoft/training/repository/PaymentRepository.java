package com.progressoft.training.repository;

import com.progressoft.training.models.Payment;

import java.util.List;

public interface PaymentRepository {
    Payment save(Payment payment);
    List<Payment> findAll();
    Payment findBy(Long id);
    List<Payment> saveAll(List<Payment> validPayments);
}
