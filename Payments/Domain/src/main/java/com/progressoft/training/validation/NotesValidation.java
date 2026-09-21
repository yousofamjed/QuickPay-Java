package com.progressoft.training.validation;

import com.progressoft.training.models.Payment;
import com.progressoft.training.models.Violation;

import java.util.ArrayList;
import java.util.List;

public class NotesValidation implements Validator<Payment>{
    @Override
    public List<Violation> validate(Payment payment) {

        List<Violation> violations = new ArrayList<>();
        if (!payment.getNotes().isBlank()) {
            if (payment.getNotes().length() > 500) {
                violations.add(new Violation("the length is longer than 500", "notes"));
            }
        }
        return violations;
    }
}
