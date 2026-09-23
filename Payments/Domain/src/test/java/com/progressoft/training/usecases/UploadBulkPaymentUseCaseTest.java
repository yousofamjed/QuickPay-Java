package com.progressoft.training.usecases;

import com.progressoft.training.exception.SystemViolationException;
import com.progressoft.training.fileparser.domain.ParseResult;
import com.progressoft.training.fileparser.domain.ValidationError;
import com.progressoft.training.fileparser.usecase.ParseFileUseCase;
import com.progressoft.training.models.BulkError;
import com.progressoft.training.models.BulkUploadResult;
import com.progressoft.training.models.ParsedPayment;
import com.progressoft.training.models.Payment;
import com.progressoft.training.repository.PaymentRepository;
import com.progressoft.training.validation.ValidationResult;
import com.progressoft.training.validation.ValidatorChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadBulkPaymentUseCaseTest {

    @Mock
    private ParseFileUseCase<ParsedPayment> parseFileUseCase;

    @Mock
    private ValidatorChain<Payment> validatorChain;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ValidationResult validationResult;

    private UploadBulkPaymentUseCase uploadBulkPaymentUseCase;

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        uploadBulkPaymentUseCase = new UploadBulkPaymentUseCase(parseFileUseCase, validatorChain, paymentRepository, true);
    }

    @Test
    void givenValidPayments_WhenExecute_ShouldSetAuditFieldsAndSaveAll() {
        List<ParsedPayment> validRows = List.of(new ParsedPayment(2, payment));
        when(parseFileUseCase.execute(any())).thenReturn(new ParseResult<>(validRows, Collections.emptyList()));

        when(validatorChain.validate(payment)).thenReturn(validationResult);

        when(paymentRepository.saveAll(anyList())).thenReturn(List.of(payment));

        BulkUploadResult result = uploadBulkPaymentUseCase.execute("template", Path.of("transactions.csv"));

        assertEquals(1, result.createdCount());
        assertEquals(0, result.errorCount());
        assertEquals("PENDING", payment.getStatus());
        assertNotNull(payment.getCreatedAt());
        verify(paymentRepository).saveAll(anyList());
    }

    @Test
    void givenParseErrorsAndValidPayment_WhenExecute_ShouldSaveValidPayments() {
        List<ParsedPayment> validRows = List.of(new ParsedPayment(2, payment));
        List<ValidationError> errors = List.of(new ValidationError(3, "amount", "must be positive"));
        when(parseFileUseCase.execute(any())).thenReturn(new ParseResult<>(validRows, errors));

        when(validatorChain.validate(payment)).thenReturn(validationResult);

        when(paymentRepository.saveAll(anyList())).thenReturn(List.of(payment));

        BulkUploadResult result = uploadBulkPaymentUseCase.execute("template", Path.of("transactions.csv"));

        assertEquals(1, result.createdCount());
        assertEquals(1, result.errorCount());
        assertEquals("PENDING", payment.getStatus());
        assertNotNull(payment.getCreatedAt());
        verify(paymentRepository).saveAll(anyList());
    }

    @Test
    void givenParseErrorsAndPersistDisabled_WhenExecute_ShouldNotSave() {
        List<ValidationError> errors = List.of(new ValidationError(3, "amount", "must be positive"));
        when(parseFileUseCase.execute(any())).thenReturn(new ParseResult<>(Collections.emptyList(), errors));
        uploadBulkPaymentUseCase = new UploadBulkPaymentUseCase(parseFileUseCase, validatorChain, paymentRepository, false);

        BulkUploadResult result = uploadBulkPaymentUseCase.execute("template", Path.of("transactions.csv"));

        assertEquals(0, result.createdCount());
        assertEquals(1, result.errorCount());
        verify(paymentRepository, never()).saveAll(anyList());
    }

    @Test
    void givenInvalidPayment_WhenExecute_ShouldCollectRowErrorAndNotCreate() {
        List<ParsedPayment> validRows = List.of(new ParsedPayment(2, payment));
        when(parseFileUseCase.execute(any())).thenReturn(new ParseResult<>(validRows, Collections.emptyList()));

        when(validatorChain.validate(payment)).thenReturn(validationResult);

        doThrow(new SystemViolationException(Collections.emptySet()))
                .when(validationResult).throwExceptionIfViolated();

        BulkUploadResult result = uploadBulkPaymentUseCase.execute("template", Path.of("transactions.csv"));

        assertEquals(0, result.createdCount());
        assertEquals(1, result.errorCount());
        assertEquals(2, result.errorsPerRow().get(0).rowNumber());

    }

}