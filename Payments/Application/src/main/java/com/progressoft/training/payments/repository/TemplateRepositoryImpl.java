package com.progressoft.training.payments.repository;

import com.progressoft.training.fileparser.domain.Template;
import com.progressoft.training.fileparser.exception.TemplateNotFoundException;
import com.progressoft.training.fileparser.repository.TemplateRepositoryInterface;
import com.progressoft.training.payments.entity.TemplateEntity;
import com.progressoft.training.payments.mapper.TemplateMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TemplateRepositoryImpl implements TemplateRepositoryInterface {

    private final TemplateRepositoryJpa templateRepositoryJpa;
    private final TemplateMapper mapper;

    public TemplateRepositoryImpl(TemplateRepositoryJpa templateRepositoryJpa, TemplateMapper mapper) {
        this.templateRepositoryJpa = templateRepositoryJpa;
        this.mapper = mapper;
    }



    public Optional<TemplateEntity> findEntityById(Long id) {
        return templateRepositoryJpa.findById(id);
    }

    public Optional<TemplateEntity> findEntityByName(String name) {
        return templateRepositoryJpa.findByName(name);
    }


    @Override
    public Optional<Template> findByName(String name) {
        return templateRepositoryJpa.findByName(name).map(mapper::toDomain);
    }

    @Override
    public List<Template> findAll() {
        return mapper.toDomain(templateRepositoryJpa.findAll());
    }

    @Override
    public Template save(Template template) {

        TemplateEntity entity = templateRepositoryJpa.findByName(template.name())
                .map(it -> {
                    //if found, UPDATE LOGIC
                    it.getFields().clear();
                    it.getFields().addAll(mapper.toEmbeddable(template.fields()));
                    return it;//if not found CREATE Logic
                }).orElseGet(() -> mapper.toEntity(template));



        TemplateEntity result = templateRepositoryJpa.save(entity);
        return mapper.toDomain(result);
    }

    @Override
    public void deleteByName(String name) {
        TemplateEntity entity = templateRepositoryJpa.findByName(name).orElseThrow(()-> new TemplateNotFoundException(name));
        templateRepositoryJpa.delete(entity);
    }
}
