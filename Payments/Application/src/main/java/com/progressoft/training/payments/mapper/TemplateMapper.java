package com.progressoft.training.payments.mapper;

import com.progressoft.training.fileparser.domain.FieldDefinition;
import com.progressoft.training.fileparser.domain.Template;
import com.progressoft.training.payments.entity.TemplateEntity;
import com.progressoft.training.payments.entity.TemplateFieldEntity;
import com.progressoft.training.payments.resources.template.TemplateRequest;
import com.progressoft.training.payments.resources.template.TemplateResponse;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Mapper(componentModel = "spring")
public interface TemplateMapper {
    Template toDomain(TemplateEntity templateEntity);

    List<Template> toDomain(List<TemplateEntity> templateEntities);

    Template toDomain(TemplateRequest templateRequest);
    TemplateResponse toResponse(TemplateRequest templateRequest);
    TemplateResponse toResponse(Template template);
    TemplateResponse toResponse(TemplateEntity templateEntity);

    TemplateEntity toEntity(Template template);

    List<TemplateFieldEntity> toEmbeddable(List<FieldDefinition> fields);
}
