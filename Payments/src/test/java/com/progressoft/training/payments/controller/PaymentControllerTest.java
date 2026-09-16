package com.progressoft.training.payments.controller;
import com.progressoft.training.payments.entity.Payment;
import com.progressoft.training.payments.mapper.PaymentMapper;
import com.progressoft.training.payments.resources.PaymentRequest;
import com.progressoft.training.payments.resources.PaymentResponse;
import com.progressoft.training.payments.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private PaymentService paymentService;
    @MockitoBean
    private PaymentMapper paymentMapper;

    @Test
    void givenPayment_whenSave_thenSuccess() throws Exception {
        PaymentRequest saved = new PaymentRequest();

        saved.setAmount(new BigDecimal("100.00"));

        ObjectMapper objectMapper1 = new ObjectMapper();

        Mockito.when(paymentMapper.toEntity(saved)).thenReturn(new Payment());

        Mockito.when(paymentService.save(Mockito.any())).thenReturn(new Payment());

        PaymentResponse expectedResponse = new PaymentResponse();
        expectedResponse.setId(1L);
        expectedResponse.setAmount(BigDecimal.valueOf(100));

        Mockito.when(paymentMapper.toResponse(Mockito.any())).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper1.writeValueAsString(saved)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(100.00));
    }


    @Test
    void givenValidId_whenGetPaymentById_thenReturnsCorrectResponse() throws Exception {

        Payment saved = new Payment();
        saved.setId(1L);
        saved.setAmount(new BigDecimal("100.00"));

        Mockito.when(paymentService.getBy(1L)).thenReturn(Optional.of(saved));

        PaymentResponse expectedResponse = new PaymentResponse();
        expectedResponse.setId(1L);
        expectedResponse.setAmount(BigDecimal.valueOf(100));

        Mockito.when(paymentMapper.toResponse(Mockito.any())).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/payments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 100.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(100.00));

    }

    @Test
    void givenAnInvalidId_whenGetPaymentById_thenReturnsCorrectResponse() throws Exception{

            Payment saved = new Payment();
            saved.setId(1L);
            saved.setAmount(new BigDecimal("100.00"));

            Mockito.when(paymentService.getBy(4L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/payments/4")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

    @Test
    void givenAList_whenGetALlPayments_thenReturnsList() throws Exception{
        Payment input1 = new Payment();
        input1.setAmount(new BigDecimal("100.00"));
        input1.setId(1L);
        Payment input2 = new Payment();
        input2.setAmount(new BigDecimal("100.00"));
        input2.setId(1L);


        List<Payment> list = List.of(input1,input2);

        Mockito.when(paymentService.getAllPayments()).thenReturn(list);

        mockMvc.perform(get("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        ;

    }





}