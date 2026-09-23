package com.progressoft.training.payments.service;

import com.progressoft.training.fileparser.domain.FieldDefinition;
import com.progressoft.training.fileparser.domain.Template;
import com.progressoft.training.fileparser.exception.TemplateNotFoundException;
import com.progressoft.training.fileparser.usecase.CreateTemplateUseCase;
import com.progressoft.training.fileparser.usecase.DeleteTemplateUseCase;
import com.progressoft.training.fileparser.usecase.UpdateTemplateUseCase;
import com.progressoft.training.payments.entity.TemplateEntity;
import com.progressoft.training.payments.mapper.TemplateMapper;
import com.progressoft.training.payments.repository.TemplateRepositoryImpl;
import com.progressoft.training.payments.resources.template.TemplateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateServiceTest {

    @Mock
    private CreateTemplateUseCase createTemplateUseCase;

    @Mock
    private DeleteTemplateUseCase deleteTemplateUseCase;

    @Mock
    private UpdateTemplateUseCase updateTemplateUseCase;

    @Mock
    private TemplateRepositoryImpl templateRepository;

    @Mock
    private TemplateMapper templateMapper;

    @InjectMocks
    private TemplateService templateService;

    private Template template;
    private TemplateEntity entity;
    private TemplateResponse response;

    @BeforeEach
    void setUp() {
        template = new Template("TEMPLATE", List.of(new FieldDefinition("field1", 10, true)));
        entity = new TemplateEntity("TEMPLATE", new ArrayList<>());

        response = new TemplateResponse();
        response.setId(1L);
        response.setName("TEMPLATE");
    }

    @Test
    void givenAValidTemplate_whenCreate_thenReturnsTheExpectedResponse() {
        String name = "TEMPLATE";
        when(createTemplateUseCase.execute(template)).thenReturn(template);
        when(templateRepository.findEntityByName(name)).thenReturn(Optional.of(entity));
        when(templateMapper.toResponse(entity)).thenReturn(response);

        TemplateResponse result = templateService.create(template);

        assertEquals(response, result);
        Mockito.verify(createTemplateUseCase).execute(template);
    }

    @Test
    void givenAValidId_whenFindById_thenReturnsTheExpectedResponse() {
        Long id = 1L;
        when(templateRepository.findEntityById(id)).thenReturn(Optional.of(entity));
        when(templateMapper.toResponse(entity)).thenReturn(response);

        TemplateResponse result = templateService.findById(id);

        assertEquals(response, result);
    }

    @Test
    void givenInvalidId_whenFindById_thenShouldThrowTemplateNotFoundException() {
        Long id = 1L;
        when(templateRepository.findEntityById(id)).thenReturn(Optional.empty());

        assertThrows(TemplateNotFoundException.class, () -> templateService.findById(id));
    }

    @Test
    void givenAValidName_whenFindByName_thenReturnsTheExpectedResponse() {
        String name = "TEMPLATE";
        when(templateRepository.findEntityByName(name)).thenReturn(Optional.of(entity));
        when(templateMapper.toResponse(entity)).thenReturn(response);

        TemplateResponse result = templateService.findByName(name);

        assertEquals(response, result);
    }

    @Test
    void givenInvalidName_whenFindByName_thenShouldThrowTemplateNotFoundException() {
        String name = "TEMPLATE";
        when(templateRepository.findEntityByName(name)).thenReturn(Optional.empty());

        assertThrows(TemplateNotFoundException.class, () -> templateService.findByName(name));
    }

    @Test
    void givenAValidTemplate_whenUpdate_thenReturnsTheExpectedResponse() {
        String name = "TEMPLATE";
        when(updateTemplateUseCase.execute(template)).thenReturn(template);
        when(templateRepository.findEntityByName(name)).thenReturn(Optional.of(entity));
        when(templateMapper.toResponse(entity)).thenReturn(response);

        TemplateResponse result = templateService.update(template);

        assertEquals(response, result);

        Mockito.verify(updateTemplateUseCase).execute(template);
    }

    @Test
    void givenAValidName_whenDelete_thenDeletesIt() {
        String name = "TEMPLATE";

        templateService.delete(name);

        Mockito.verify(deleteTemplateUseCase).execute(name);
    }
}