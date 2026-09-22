package com.progressoft.training.payments.resources.template;

import lombok.Data;

@Data
public class TemplateFieldResponse {
    private String name;
    private int length;
    private boolean required;
}
