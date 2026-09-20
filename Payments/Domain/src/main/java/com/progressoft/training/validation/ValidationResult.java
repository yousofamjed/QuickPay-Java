package com.progressoft.training.validation;

import com.progressoft.training.exception.SystemViolationException;
import com.progressoft.training.models.Violation;

import java.util.Set;

public class ValidationResult {
    private final Set<Violation> violations;

    public ValidationResult(Set<Violation> violations) {
        this.violations = violations;
    }

    public void throwExceptionIfViolated() {
        if (!violations.isEmpty()) {
            throw new SystemViolationException(violations);
        }
    }
}
