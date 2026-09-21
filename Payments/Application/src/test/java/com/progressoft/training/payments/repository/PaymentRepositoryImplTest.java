package com.progressoft.training.payments.repository;

import com.progressoft.training.models.Payment;
import com.progressoft.training.payments.entity.PaymentEntity;
import com.progressoft.training.payments.mapper.PaymentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentRepositoryImplTest {

    @Mock
    private PaymentRepositoryJpa jpaRepository;

    @Mock
    private PaymentMapper mapper;

    @InjectMocks
    private PaymentRepositoryImpl repositoryImpl;

    private Payment domainPayment;
    private PaymentEntity entityPayment;

    @BeforeEach
    void setUp() {
        domainPayment = new Payment();
        entityPayment = new PaymentEntity();
    }

    @Test
    void givenValidEntityAndDomain_whenSave_thenSavesAndReturnsExpected() {
        when(mapper.toEntity(domainPayment)).thenReturn(entityPayment);
        when(jpaRepository.save(entityPayment)).thenReturn(entityPayment);
        when(mapper.toDomain(entityPayment)).thenReturn(domainPayment);

        Payment result = repositoryImpl.save(domainPayment);

        assertEquals(domainPayment, result);
        verify(jpaRepository).save(entityPayment);
    }

    @Test
    void givenAValidID_WhenFindBy_thenReturnMappedDomain() {
        Long id = 1L;
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entityPayment));
        when(mapper.toDomain(entityPayment)).thenReturn(domainPayment);

        Payment result = repositoryImpl.findBy(id);

        assertNotNull(result);
        assertEquals(domainPayment, result);
    }

    @Test
    void givenAnInvalidID_WhenFindBy_thenShouldReturnNull() {
        Long id = 1L;
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        Payment result = repositoryImpl.findBy(id);

        assertNull(result);
    }

    @Test
    void givenListOfPayments_whenFIndAll_thenRetrunsLIstOfPayments() {
        when(jpaRepository.findAll()).thenReturn(Collections.singletonList(entityPayment));
        when(mapper.toDomain(entityPayment)).thenReturn(domainPayment);

        List<Payment> result = repositoryImpl.findAll();

        assertEquals(1, result.size());
        assertEquals(domainPayment, result.get(0));
    }
}