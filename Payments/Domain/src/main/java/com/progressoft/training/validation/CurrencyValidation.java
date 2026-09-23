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
        }

        return violations;
    }
}
