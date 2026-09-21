package com.progressoft.training.usecases;

import com.progressoft.training.exception.SystemViolationException;
import com.progressoft.training.models.Payment;
import com.progressoft.training.repository.PaymentRepository;
import com.progressoft.training.validation.ValidationResult;
import com.progressoft.training.validation.ValidatorChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePaymentUseCaseTest {

    @Mock
    private ValidatorChain<Payment> validatorChain;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ValidationResult validationResult;

    @InjectMocks
    private CreatePaymentUseCase createPaymentUseCase;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
    }

    @Test
    void givenValidPayment_WhenExecute_ShouldSetAuditFieldsAndSave() {
        when(validatorChain.validate(payment)).thenReturn(validationResult);

        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment result = createPaymentUseCase.execute(payment);

        assertEquals("PENDING", result.getStatus());
        assertNotNull(result.getCreatedAt());
        verify(paymentRepository).save(payment);
    }

    @Test
    void givenInvalidWhenExecute_ShouldThrowExceptionAndNotSave() {
        when(validatorChain.validate(payment)).thenReturn(validationResult);

        doThrow(new SystemViolationException(Collections.emptySet()))
                .when(validationResult).throwExceptionIfViolated();

        assertThrows(SystemViolationException.class, () -> createPaymentUseCase.execute(payment));
        assertNull(payment.getStatus()); // Audit fields should not be set

    }

}