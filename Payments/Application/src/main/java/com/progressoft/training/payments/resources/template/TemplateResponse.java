package com.progressoft.training.payments.resources.template;

import lombok.Data;

import java.util.List;

@Data
public class TemplateResponse {
    private long id;
    private String name;
    private List<TemplateFieldResponse> fields;
}
