package com.progressoft.training.payments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progressoft.training.exception.SystemViolationException;
import com.progressoft.training.fileparser.domain.FieldDefinition;
import com.progressoft.training.fileparser.domain.Template;
import com.progressoft.training.payments.mapper.PaymentMapperImpl;
import com.progressoft.training.payments.mapper.TemplateMapper;
import com.progressoft.training.payments.mapper.TemplateMapperImpl;
import com.progressoft.training.payments.resources.template.TemplateFieldRequest;
import com.progressoft.training.payments.resources.template.TemplateFieldResponse;
import com.progressoft.training.payments.resources.template.TemplateRequest;
import com.progressoft.training.payments.resources.template.TemplateResponse;
import com.progressoft.training.payments.security.SecurityConfig;
import com.progressoft.training.payments.service.TemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;


import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TemplateController.class)
@Import({TemplateMapperImpl.class, SecurityConfig.class})
@WithMockUser(roles = "TEMPLATES")
class TemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private TemplateService templateService;

    @MockitoBean
    private TemplateMapper templateMapper;

    private Template template;
    private TemplateRequest request;
    private TemplateResponse response;

    @BeforeEach
    void setUp() {
        template = new Template("TEMPLATE", List.of(new FieldDefinition("field1", 10, true)));

        request = new TemplateRequest();
        request.setName("TEMPLATE");
        request.setFields(Collections.singletonList(fieldRequest("field1")));

        response = new TemplateResponse();
        response.setId(1L);
        response.setName("TEMPLATE");
        response.setFields(Collections.singletonList(fieldResponse("field1")));
    }

    private TemplateFieldRequest fieldRequest(String name) {
        TemplateFieldRequest field = new TemplateFieldRequest();
        field.setName(name);
        field.setLength(10);
        field.setRequired(true);
        return field;
    }

    private TemplateFieldResponse fieldResponse(String name) {
        TemplateFieldResponse field = new TemplateFieldResponse();
        field.setName(name);
        field.setLength(10);
        field.setRequired(true);
        return field;
    }

    @Test
    void givenTemplate_whenCreate_thenShouldReturnCreatedAndJson() throws Exception {
        when(templateMapper.toDomain(any(TemplateRequest.class))).thenReturn(template);
        when(templateService.create(template)).thenReturn(response);

        mockMvc.perform(post("/api/template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TEMPLATE"))
                .andExpect(jsonPath("$.fields.length()").value(1))
                .andExpect(jsonPath("$.fields[0].name").value("field1"));
    }

    @Test
    void givenAValidId_whenGetById_thenShouldReturnOkAndJson() throws Exception {
        Long id = 1L;
        when(templateService.findById(id)).thenReturn(response);

        mockMvc.perform(get("/api/template/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TEMPLATE"));
    }

    @Test
    void givenAValidName_whenGetByName_thenShouldReturnOkAndJson() throws Exception {
        String name = "TEMPLATE";
        when(templateService.findByName(name)).thenReturn(response);

        mockMvc.perform(get("/api/template/name/{name}", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TEMPLATE"));
    }

    @Test
    void givenAValidName_whenDelete_thenShouldReturnOk() throws Exception {
        String name = "TEMPLATE";

        mockMvc.perform(delete("/api/template/{name}", name))
                .andExpect(status().isOk());
    }

    @Test
    void givenTemplate_whenUpdate_thenShouldReturnOkAndJson() throws Exception {
        String name = "TEMPLATE";
        when(templateMapper.toDomain(any(TemplateRequest.class))).thenReturn(template);
        when(templateService.update(template)).thenReturn(response);

        mockMvc.perform(put("/api/template/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TEMPLATE"));
    }
}