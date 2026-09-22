package com.progressoft.training.payments.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class TemplateFieldEntity {
    private String name;
    private int length;
    private boolean required;

    public TemplateFieldEntity(String name, int length , boolean required){
        this.name = name;
        this.length = length;
        this.required = required;
    }

}
