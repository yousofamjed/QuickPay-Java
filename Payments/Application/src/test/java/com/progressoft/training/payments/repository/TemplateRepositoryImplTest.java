package com.progressoft.training.payments.repository;

import com.progressoft.training.fileparser.domain.FieldDefinition;
import com.progressoft.training.fileparser.domain.Template;
import com.progressoft.training.fileparser.exception.TemplateNotFoundException;
import com.progressoft.training.payments.entity.TemplateEntity;
import com.progressoft.training.payments.entity.TemplateFieldEntity;
import com.progressoft.training.payments.mapper.TemplateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateRepositoryImplTest {

    @Mock
    private TemplateRepositoryJpa templateRepositoryJpa;

    @Mock
    private TemplateMapper mapper;

    @InjectMocks
    private TemplateRepositoryImpl repositoryImpl;

    private Template domainTemplate;
    private TemplateEntity entityTemplate;
    private TemplateFieldEntity fieldEntity;

    @BeforeEach
    void setUp() {
        domainTemplate = new Template("TEMPLATE", List.of(new FieldDefinition("field1", 10, true)));
        entityTemplate = new TemplateEntity("TEMPLATE", new ArrayList<>());
        fieldEntity = new TemplateFieldEntity("field1", 10, true);
    }

    @Test
    void givenAValidID_WhenFindEntityById_thenReturnTheExpectedEntity() {
        Long id = 1L;
        when(templateRepositoryJpa.findById(id)).thenReturn(Optional.of(entityTemplate));

        Optional<TemplateEntity> result = repositoryImpl.findEntityById(id);

        assertTrue(result.isPresent());
        assertEquals(entityTemplate, result.get());
    }

    @Test
    void givenAnInvalidID_WhenFindEntityById_thenShouldReturnEmpty() {
        Long id = 1L;
        when(templateRepositoryJpa.findById(id)).thenReturn(Optional.empty());

        Optional<TemplateEntity> result = repositoryImpl.findEntityById(id);

        assertFalse(result.isPresent());
    }

    @Test
    void givenAValidName_WhenFindEntityByName_thenReturnTheExpectedEntity() {
        String name = "TEMPLATE";
        when(templateRepositoryJpa.findByName(name)).thenReturn(Optional.of(entityTemplate));

        Optional<TemplateEntity> result = repositoryImpl.findEntityByName(name);

        assertTrue(result.isPresent());
        assertEquals(entityTemplate, result.get());
    }

    @Test
    void givenAValidName_WhenFindByName_thenReturnMappedDomain() {
        String name = "TEMPLATE";
        when(templateRepositoryJpa.findByName(name)).thenReturn(Optional.of(entityTemplate));
        when(mapper.toDomain(entityTemplate)).thenReturn(domainTemplate);

        Optional<Template> result = repositoryImpl.findByName(name);

        assertTrue(result.isPresent());
        assertEquals(domainTemplate, result.get());
    }

    @Test
    void givenAnInvalidName_WhenFindByName_thenShouldReturnEmpty() {
        String name = "TEMPLATE";
        when(templateRepositoryJpa.findByName(name)).thenReturn(Optional.empty());

        Optional<Template> result = repositoryImpl.findByName(name);

        assertFalse(result.isPresent());
    }

    @Test
    void givenListOfEntities_whenFindAll_thenReturnsListOfDomainTemplates() {
        when(templateRepositoryJpa.findAll()).thenReturn(Collections.singletonList(entityTemplate));
        when(mapper.toDomain(anyList())).thenReturn(Collections.singletonList(domainTemplate));

        List<Template> result = repositoryImpl.findAll();

        assertEquals(1, result.size());
        assertEquals(domainTemplate, result.get(0));
    }

    @Test
    void givenExistingTemplate_whenSave_thenUpdatesAndReturnsExpected() {
        String name = "TEMPLATE";
        when(templateRepositoryJpa.findByName(name)).thenReturn(Optional.of(entityTemplate));
        when(mapper.toEmbeddable(domainTemplate.fields())).thenReturn(Collections.singletonList(fieldEntity));
        when(templateRepositoryJpa.save(entityTemplate)).thenReturn(entityTemplate);
        when(mapper.toDomain(entityTemplate)).thenReturn(domainTemplate);

        Template result = repositoryImpl.save(domainTemplate);

        assertEquals(domainTemplate, result);
        verify(templateRepositoryJpa).save(entityTemplate);
    }

    @Test
    void givenNonExistingTemplate_whenSave_thenCreatesAndReturnsExpected() {
        String name = "TEMPLATE";
        when(templateRepositoryJpa.findByName(name)).thenReturn(Optional.empty());
        when(mapper.toEntity(domainTemplate)).thenReturn(entityTemplate);
        when(templateRepositoryJpa.save(entityTemplate)).thenReturn(entityTemplate);
        when(mapper.toDomain(entityTemplate)).thenReturn(domainTemplate);

        Template result = repositoryImpl.save(domainTemplate);

        assertEquals(domainTemplate, result);
        verify(templateRepositoryJpa).save(entityTemplate);
    }

    @Test
    void givenAValidName_whenDeleteByName_thenDeletesTheEntity() {
        String name = "TEMPLATE";
        when(templateRepositoryJpa.findByName(name)).thenReturn(Optional.of(entityTemplate));

        repositoryImpl.deleteByName(name);

        verify(templateRepositoryJpa).delete(entityTemplate);
    }

    @Test
    void givenAnInvalidName_whenDeleteByName_thenShouldThrowTemplateNotFoundException() {
        String name = "TEMPLATE";
        when(templateRepositoryJpa.findByName(name)).thenReturn(Optional.empty());

        assertThrows(TemplateNotFoundException.class, () -> repositoryImpl.deleteByName(name));
    }
}