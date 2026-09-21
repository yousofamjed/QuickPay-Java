package com.progressoft.training.validation;

import com.progressoft.training.models.Payment;
import com.progressoft.training.models.Violation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreditorNameValidationTest {

    private CreditorNameValidation validation;
    private Payment payment;

    @BeforeEach
    void setUp() {
        validation = new CreditorNameValidation();
        payment = new Payment();
    }

    @Test
    void givenNullName_whenValidate_ShouldReturnViolation() {
        payment.setCreditorName(null);
        List<Violation> violations = validation.validate(payment);
        assertFalse(violations.isEmpty());
    }

    @Test
    void givenBlankName_whenValidate_ShouldReturnViolation() {
        payment.setCreditorName("   ");
        List<Violation> violations = validation.validate(payment);
        assertFalse(violations.isEmpty());
    }

    @Test
    void givenLongerThan100LettersName_whenValidate_houldReturnViolation() {
        payment.setCreditorName("A".repeat(101));
        List<Violation> violations = validation.validate(payment);
        assertFalse(violations.isEmpty());
    }

    @Test
    void givenOnePartNameOrMoreThanTwoParts_whenValidate_ShouldReturnViolation() {

        payment.setCreditorName("7amooda");
        List<Violation> violations = validation.validate(payment);
        assertFalse(violations.isEmpty());

        payment.setCreditorName("7amooda 7amada 7ameedo");
        violations = validation.validate(payment);
        assertFalse(violations.isEmpty());
    }

    @Test
    void givenNameeWithSpecialCharacters_whenwhenValidate_ShouldReturnViolation() {
        payment.setCreditorName("7amad@ 7amood!");
        List<Violation> violations = validation.validate(payment);
        assertFalse(violations.isEmpty());
        assertEquals("creditor Name must be alphanumeric only", violations.get(0).getMessage());
    }

    @Test
    void givenValidName_whenValidate_hould_ReturnEmptyList() {
        payment.setCreditorName("John Doe");
        List<Violation> violations = validation.validate(payment);
        assertTrue(violations.isEmpty());
    }
}