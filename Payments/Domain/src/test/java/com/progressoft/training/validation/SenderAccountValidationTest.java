package com.progressoft.training.validation;

import com.progressoft.training.models.Payment;
import com.progressoft.training.models.Violation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SenderAccountValidationTest {

    private SenderAccountValidation validation;
    private Payment payment;

    @BeforeEach
    void setUp() {
        validation = new SenderAccountValidation();
        payment = new Payment();
    }

    @Test
    void givenNUllAccount_whenValidate_ShouldReturnViolation() {
        payment.setSenderAccount(null);
        List<Violation> violations = validation.validate(payment);
        assertFalse(violations.isEmpty());
    }

    @Test
    void givenAccountISLOng_whenValidate_ShouldReturnViolation() {
        payment.setSenderAccount("A".repeat(35));
        List<Violation> violations = validation.validate(payment);
        assertFalse(violations.isEmpty());
        assertEquals("Sender account", violations.get(0).getViolation());
    }

    @Test
    void givenValidAccount_whenValidate_ShouldReturnEmptyList() {
        payment.setSenderAccount("SENDER123");
        List<Violation> violations = validation.validate(payment);
        assertTrue(violations.isEmpty());
    }
}