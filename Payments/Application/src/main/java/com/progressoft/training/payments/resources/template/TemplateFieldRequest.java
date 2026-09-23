package com.progressoft.training.payments.resources.template;

import lombok.Data;

@Data
public class TemplateFieldRequest {
    private String name;
    private int length;
    private boolean required;
}
