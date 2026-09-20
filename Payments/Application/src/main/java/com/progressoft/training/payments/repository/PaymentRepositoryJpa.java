package com.progressoft.training.payments.repository;

import com.progressoft.training.payments.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface    PaymentRepositoryJpa extends JpaRepository<PaymentEntity, Long> {
}
