package com.progressoft.training.payments.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Templates")
public class TemplateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "template_sequence")
    @SequenceGenerator(
            name = "template_sequence",
            sequenceName = "template_sequence",
            allocationSize = 1
    )
    private Long id;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
            @CollectionTable( name="template_fields", joinColumns = @JoinColumn(name="template_id"))
            @OrderColumn(name = "field_order")
    List<TemplateFieldEntity> fields = new ArrayList<>();

    public TemplateEntity(String name, List<TemplateFieldEntity> fields){
        this.name = name;
        this.fields=fields;
    }

}
