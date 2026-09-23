package com.progressoft.training.payments.service;

import com.progressoft.training.fileparser.domain.Template;
import com.progressoft.training.fileparser.exception.TemplateNotFoundException;
import com.progressoft.training.fileparser.usecase.CreateTemplateUseCase;
import com.progressoft.training.fileparser.usecase.DeleteTemplateUseCase;
import com.progressoft.training.fileparser.usecase.UpdateTemplateUseCase;
import com.progressoft.training.models.Payment;
import com.progressoft.training.payments.entity.TemplateEntity;
import com.progressoft.training.payments.mapper.TemplateMapper;
import com.progressoft.training.payments.repository.TemplateRepositoryImpl;
import com.progressoft.training.payments.resources.template.TemplateResponse;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class TemplateService {
    private final CreateTemplateUseCase createTemplateUseCase;
    private final DeleteTemplateUseCase deleteTemplateUseCase;
    private final UpdateTemplateUseCase updateTemplateUseCase;
    private final TemplateRepositoryImpl templateRepository;
    private final TemplateMapper templateMapper;

    public TemplateService(CreateTemplateUseCase createTemplateUseCase,
                           DeleteTemplateUseCase deleteTemplateUseCase,
                           UpdateTemplateUseCase updateTemplateUseCase,
                           TemplateRepositoryImpl templateRepository,
                           TemplateMapper templateMapper) {
        this.createTemplateUseCase = createTemplateUseCase;
        this.deleteTemplateUseCase = deleteTemplateUseCase;
        this.updateTemplateUseCase = updateTemplateUseCase;
        this.templateRepository = templateRepository;
        this.templateMapper = templateMapper;
    }

    public TemplateResponse create(Template template) {
        createTemplateUseCase.execute(template);
        return toResponse(templateRepository.findEntityByName(template.name()), template.name());
    }

    public TemplateResponse findById(Long id) {
        return toResponse(templateRepository.findEntityById(id), String.valueOf(id));
    }

    public TemplateResponse findByName(String name) {
        return toResponse(templateRepository.findEntityByName(name), name);
    }

    public Collection<Template> findAllTemplates(){
        return templateRepository.findAll().stream().toList();
    }
    public TemplateResponse update(Template template) {
        updateTemplateUseCase.execute(template);
        return toResponse(templateRepository.findEntityByName(template.name()), template.name());
    }

    public void delete(String name) {
        deleteTemplateUseCase.execute(name);
    }

    private TemplateResponse toResponse(Optional<TemplateEntity> entity, String name) {
        return templateMapper.toResponse(entity.orElseThrow(() -> new TemplateNotFoundException(name)));
    }

}