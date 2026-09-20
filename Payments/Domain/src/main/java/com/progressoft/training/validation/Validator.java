package com.progressoft.training.validation;

import com.progressoft.training.models.Violation;

import java.util.List;

public interface Validator<T>{
    public List<Violation> validate(T t);

}
