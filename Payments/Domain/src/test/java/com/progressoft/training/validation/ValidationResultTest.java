package com.progressoft.training.validation;

import com.progressoft.training.exception.SystemViolationException;
import com.progressoft.training.models.Violation;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationResultTest {

    @Test
    void givenNoViolation_WhenThrowExceptionIfVIolated_ShouldNotThrowException() {

        Set<Violation> emptyViolations = Collections.emptySet();
        ValidationResult validationResult = new ValidationResult(emptyViolations);
        assertDoesNotThrow(() -> validationResult.throwExceptionIfViolated());
    }

    @Test
    void givenViolation_henThrowExceptionIfVIolatedShouldThrowSystemViolationException() {

        Set<Violation> violations = new HashSet<>();
        violations.add(new Violation("Invalid field", "fieldName"));
        ValidationResult validationResult = new ValidationResult(violations);
        assertThrows(SystemViolationException.class, () -> validationResult.throwExceptionIfViolated());
    }
}