package com.progressoft.training.validation;

import com.progressoft.training.models.Payment;
import com.progressoft.training.models.Violation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AmountValidationTest {

    private AmountValidation validation;
    private Payment payment;

    @BeforeEach
    void setUp() {
        validation = new AmountValidation();
        payment = new Payment();
    }

    @Test
    void givenNUllAmount_whenValidate_ShouldReturnViolation() {
        payment.setAmount(null);
        List<Violation> violations = validation.validate(payment);

        assertEquals(1, violations.size());
        assertEquals("amount", violations.get(0).getViolation());
    }

    @Test
    void givenZeroOrNegativeAmount_whenValidate_ShouldReturnViolation() {
        payment.setAmount(BigDecimal.ZERO);
        List<Violation> violations = validation.validate(payment);
        assertFalse(violations.isEmpty());

        payment.setAmount(new BigDecimal("-10.50"));
        violations = validation.validate(payment);
        assertFalse(violations.isEmpty());
    }

    @Test
    void givenScaleBiggerThanTwo_whenValidateShouldReturnViolation() {
        payment.setAmount(new BigDecimal("10.123"));
        List<Violation> violations = validation.validate(payment);
        assertEquals(1, violations.size());
        assertEquals("Amount scale is more than two", violations.get(0).getMessage());
    }

    @Test
    void givenValidAmount_whenValidate_ShouldReturnEmptyList() {
        payment.setAmount(new BigDecimal("100.50"));
        List<Violation> violations = validation.validate(payment);
        assertTrue(violations.isEmpty());
    }
}