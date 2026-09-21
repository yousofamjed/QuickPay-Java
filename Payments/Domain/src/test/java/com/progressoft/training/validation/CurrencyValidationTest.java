package com.progressoft.training.validation;

import com.progressoft.training.models.Payment;
import com.progressoft.training.models.Violation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyValidationTest {

    private CurrencyValidation validation;
    private Payment payment;

    @BeforeEach
    void setUp() {
        validation = new CurrencyValidation();
        payment = new Payment();
    }

    @Test
    void givenNUllCUrrency_whenValidate_ShouldReturnViolation() {
        payment.setCurrency(null);
        List<Violation> violations = validation.validate(payment);
        assertFalse(violations.isEmpty());
    }

    @Test
    void givenLessThanOrMoreThan3letterCurrencyOrLowercase_whenValidate_ShouldReturnViolation() {
        payment.setCurrency("usd");
        assertFalse(validation.validate(payment).isEmpty());

        payment.setCurrency("US");
        assertFalse(validation.validate(payment).isEmpty());

        payment.setCurrency("USDA");
        assertFalse(validation.validate(payment).isEmpty());
    }

    @Test
    void givenValidCUrrency_whenValidate_ShouldReturnEmptyList() {
        payment.setCurrency("JOD");
        List<Violation> violations = validation.validate(payment);
        assertTrue(violations.isEmpty());
    }
}