package com.progressoft.training.validation;

import com.progressoft.training.models.Payment;
import com.progressoft.training.models.Violation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotesValidationTest {

    private NotesValidation validation;
    private Payment payment;

    @BeforeEach
    void setUp() {
        validation = new NotesValidation();
        payment = new Payment();
    }

    @Test
    void givenEmptyNote_whenValidate_ShouldReturnEmptyList() {
        payment.setNotes("");
        List<Violation> violations = validation.validate(payment);
        assertTrue(violations.isEmpty());
    }

    @Test
    void givenLOngerTHan500CharactersNote_whenValidate_ShouldReturnViolation() {
        payment.setNotes("A".repeat(501));
        List<Violation> violations = validation.validate(payment);
        assertEquals(1, violations.size());
        assertEquals("notes", violations.get(0).getViolation());
    }

    @Test
    void givenValidNote_whenValidate_ShouldReturnEmptyList() {
        payment.setNotes("Invoice payment for September");
        List<Violation> violations = validation.validate(payment);
        assertTrue(violations.isEmpty());
    }
}