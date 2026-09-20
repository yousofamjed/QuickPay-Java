package com.progressoft.training.payments.repository;

import com.progressoft.training.models.Payment;
import com.progressoft.training.payments.mapper.PaymentMapper;
import com.progressoft.training.repository.PaymentRepository;


import java.util.List;

public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentRepositoryJpa jpaRepository;
    private final PaymentMapper mapper;

    public PaymentRepositoryImpl(PaymentRepositoryJpa jpaRepository, PaymentMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Payment save(Payment payment)    {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(payment)));
    }

    @Override
    public Payment findBy(Long id) {
        return mapper.toDomain(jpaRepository.findById(id).isPresent() ? jpaRepository.findById(id).get() : null);
    }

    @Override
    public List<Payment> findAll() {
        return (jpaRepository.findAll().stream().map(
                s -> mapper.toDomain(s)
        ).toList());
    }
}
