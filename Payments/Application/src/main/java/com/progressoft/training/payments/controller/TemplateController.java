package com.progressoft.training.payments.controller;

import com.progressoft.training.fileparser.domain.Template;
import com.progressoft.training.models.Payment;
import com.progressoft.training.payments.entity.TemplateEntity;
import com.progressoft.training.payments.mapper.TemplateMapper;
import com.progressoft.training.payments.resources.payment.PaymentResponse;
import com.progressoft.training.payments.resources.template.TemplateRequest;
import com.progressoft.training.payments.resources.template.TemplateResponse;
import com.progressoft.training.payments.service.TemplateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/template")
public class TemplateController
{
    private final TemplateService service;
    private final TemplateMapper mapper;

    public TemplateController(TemplateService service, TemplateMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<TemplateResponse> create(@RequestBody TemplateRequest templateRequest) {
        Template template = mapper.toDomain(templateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(template));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateResponse> getById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<TemplateResponse> getByName(@PathVariable("name") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findByName(name));
    }


    @GetMapping
    public ResponseEntity<Collection<TemplateResponse>> getAll(){
        Collection<Template> allTemplates =  service.findAllTemplates();
        Collection<TemplateEntity> allTemplateEntities = allTemplates.stream().map(e-> mapper.toEntity(e)).toList();
        Collection<TemplateResponse> allResponses = allTemplateEntities.stream().map(e-> mapper.toResponse(e)).toList();
        return ResponseEntity.status(HttpStatus.OK).body(allResponses);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> delete(@PathVariable String name) {
        service.delete(name);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{name}")
    public ResponseEntity<TemplateResponse> update(@PathVariable String name, @RequestBody TemplateRequest templateRequest) {
        templateRequest.setName(name);
        return ResponseEntity.ok(service.update(mapper.toDomain(templateRequest)));
    }
}