package com.progressoft.training.payments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progressoft.training.models.Payment;
import com.progressoft.training.payments.mapper.PaymentMapper;
import com.progressoft.training.payments.resources.PaymentRequest;
import com.progressoft.training.payments.resources.PaymentResponse;
import com.progressoft.training.payments.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private PaymentService paymentService;

    @MockitoBean
    private PaymentMapper paymentMapper;

    private Payment payment;
    private PaymentRequest request;
    private PaymentResponse response;

    @BeforeEach
    void setUp() {
        payment = new Payment();

        request = new PaymentRequest();
        request.setSenderAccount("SENDER123");
        request.setAmount(new BigDecimal("100.50"));

        response = new PaymentResponse();
        response.setId(1L);
        response.setSenderAccount("SENDER123");
        response.setAmount(new BigDecimal("100.50"));
        response.setStatus("PENDING");
    }

    @Test
    void givenPayment_whenCreate_returnCOrrectStatus() throws Exception {

        when(paymentMapper.toDomain(any(PaymentRequest.class))).thenReturn(payment);
        when(paymentService.save(payment)).thenReturn(payment);
        when(paymentMapper.toResponse(payment)).thenReturn(response);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.senderAccount").value("SENDER123"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void givenAValidPayment_whenGetBy_thenShouldReturnOkAndJson() throws Exception {

        Long id = 1L;
        when(paymentService.getBy(id)).thenReturn(Optional.of(payment));
        when(paymentMapper.toResponse(payment)).thenReturn(response);

        mockMvc.perform(get("/api/payments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.senderAccount").value("SENDER123"));
    }

    @Test
    void givenInValidPayment_whenGetBy_thenShouldReturnOkAndJson() throws Exception {
        Long id = 1L;
        when(paymentService.getBy(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/payments/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenPayments_whenFIndALL_thenReturnsListOfPayments() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(Collections.singletonList(payment));
        when(paymentMapper.toResponse(payment)).thenReturn(response);

        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }
}