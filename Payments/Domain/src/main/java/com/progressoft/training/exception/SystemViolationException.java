package com.progressoft.training.exception;

import com.progressoft.training.models.Violation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SystemViolationException extends RuntimeException{ private final Set<Violation> violations;

    public SystemViolationException(Set<Violation> violations) {

        super(violations.stream().map(v -> "[" + v.getViolation() + "]" + v.getMessage())
                .reduce((a, b) -> a + ", " + b)
                .orElse("Validation failed"));

        this.violations = violations;
    }
    public List<String> getViolationMessage() {
        List<String> message = new ArrayList<>();
        for (Violation violation : violations) {
            message.add("[" + violation.getViolation() + "]" + violation.getMessage());
        }
        return message;
    }
    public Set<Violation> getViolations() {
        return violations;
    }

}
