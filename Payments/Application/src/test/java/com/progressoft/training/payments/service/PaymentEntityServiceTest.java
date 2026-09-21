package com.progressoft.training.payments.service;

import com.progressoft.training.models.Payment;
import com.progressoft.training.repository.PaymentRepository;
import com.progressoft.training.usecases.CreatePaymentUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentEntityServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CreatePaymentUseCase createPaymentUseCase;

    @InjectMocks
    private PaymentService paymentService;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
    }

    @Test
    void givenAValidPayment_whenSave_thenSavesItAndReturnsIt() {
        when(createPaymentUseCase.execute(payment)).thenReturn(payment);

        Payment result = paymentService.save(payment);

        assertEquals(payment, result);
        Mockito.verify(createPaymentUseCase).execute(payment);
    }

    @Test
    void givenAValidId_whenGetBY_thenReturnsTheExpectedPayment() {

        Long id = 1L;
        when(paymentRepository.findBy(id)).thenReturn(payment);

        Optional<Payment> result = paymentService.getBy(id);

        assertTrue(result.isPresent());
        assertEquals(payment, result.get());
    }

    @Test
    void givenInvalidId_whenFindBY_ReturnEmptyOptional() {
        Long id = 1L;
        when(paymentRepository.findBy(id)).thenReturn(null);

        Optional<Payment> result = paymentService.getBy(id);

        assertFalse(result.isPresent());
    }

    @Test
    void givenValidPayment_whenFindALl_thenReturnListOfPayments() {
        when(paymentRepository.findAll()).thenReturn(Collections.singletonList(payment));

        Collection<Payment> result = paymentService.getAllPayments();

        assertEquals(1, result.size());
        Mockito.verify(paymentRepository).findAll();
    }
}