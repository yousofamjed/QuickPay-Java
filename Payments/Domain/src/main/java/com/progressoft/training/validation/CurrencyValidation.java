package com.progressoft.training.validation;

import com.progressoft.training.models.Payment;
import com.progressoft.training.models.Violation;

import java.util.ArrayList;
import java.util.List;

public class CurrencyValidation implements Validator<Payment>{
    private final  List<String> defaultCurrencies ;

    public CurrencyValidation(List<String> defaultCurrencies) {
        this.defaultCurrencies = defaultCurrencies;
    }

    @Override
    public List<Violation> validate(Payment payment) {
        List<Violation> violations = new ArrayList<>();
        if (payment.getCurrency() == null) {
            violations.add(new Violation("currency is null", "currency"));
        } else if (!defaultCurrencies.isEmpty() && !defaultCurrencies.contains(payment.getCurrency())) {
            violations.add(new Violation("invalid currency", "currency"));
        } else {
            if (payment.getCurrency().isBlank() || payment.getCurrency().isEmpty()) {
                violations.add(new Violation("currency is blank", "currency"));
            }
            if (!payment.getCurrency().matches("^[A-Z]{3}$")) {
                violations.add(new Violation("currency does not match the role of three uppercase letters", "currency"));
            }
        }
        return violations;
    }
}
