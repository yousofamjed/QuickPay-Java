package com.progressoft.training.exception;

import com.progressoft.training.models.Violation;

import java.util.Set;

public class SystemViolationException extends RuntimeException{ private final Set<Violation> violations;

    public SystemViolationException(Set<Violation> violations) {

        this.violations = violations;
    }

    public Set<Violation> getViolations() {
        return violations;
    }

}
