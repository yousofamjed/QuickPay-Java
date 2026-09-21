package com.progressoft.training.validation;

import com.progressoft.training.models.Payment;
import com.progressoft.training.models.Violation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReceiverAccountValidationTest {

    private ReceiverAccountValidation validation;
    private Payment payment;

    @BeforeEach
    void setUp() {
        validation = new ReceiverAccountValidation();
        payment = new Payment();
        payment.setSenderAccount("7amooda");
    }

    @Test
    void givenNullAccount_whenValidate_ShouldReturnViolation() {
        payment.setReceiverAccount(null);
        List<Violation> violations = validation.validate(payment);
        assertFalse(violations.isEmpty());
    }
    @Test
    void givenBlankAccount_whenValidate_ShouldReturnViolation() {
        payment.setReceiverAccount("");
        List<Violation> violations = validation.validate(payment);
        assertFalse(violations.isEmpty());
    }

    @Test
    void givenAlongAccountMoreThan34_whenValidate_ShouldReturnViolation() {
        payment.setReceiverAccount("A".repeat(35));
        List<Violation> violations = validation.validate(payment);
        assertFalse(violations.isEmpty());
    }

    @Test
    void givenSameSenderAndReceiver_whenValidate_ShouldReturnViolation() {
        payment.setReceiverAccount("7amooda");
        payment.setSenderAccount("7amooda");
        List<Violation> violations = validation.validate(payment);
        assertFalse(violations.isEmpty());
        assertEquals("receiver Account is the same as sender Account", violations.get(0).getMessage());
    }

    @Test
    void givenValidAccount_whenValidate_ShouldReturnEmptyList() {
        payment.setReceiverAccount("RECEIVER123");
        List<Violation> violations = validation.validate(payment);
        assertTrue(violations.isEmpty());
    }
}
