package com.progressoft.training.payments.service;
import com.progressoft.training.payments.entity.PaymentEntity;
import com.progressoft.training.payments.repository.PaymentRepositoryJpa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PaymentEntityServiceTest {

    @Mock
    private PaymentRepositoryJpa paymentRepositoryJpa;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void givenValidInput_whenCreate_thenShouldReturnSameObject() {

        PaymentEntity input = new PaymentEntity();
        input.setAmount(new BigDecimal("100.00"));

        PaymentEntity saved = new PaymentEntity();
        saved.setId(1L);
        saved.setAmount(new BigDecimal("100.00"));

        Mockito.when(paymentRepositoryJpa.save(input)).thenReturn(saved);

        PaymentEntity result = paymentService.save(input);

        assertEquals(1L, result.getId());

        Mockito.verify(paymentRepositoryJpa).save(input);
    }

    @Test
    void givenValidId_whenGetElementsById_thenShouldReturnCorrectObject(){

        PaymentEntity input = new PaymentEntity();
        input.setAmount(new BigDecimal("100.00"));
        input.setId(1L);

        Mockito.when(paymentRepositoryJpa.findById(1L)).thenReturn(Optional.of(input));

         PaymentEntity byId =paymentService.getBy(1L).get();

        Mockito.verify(paymentRepositoryJpa).findById(byId.getId());
    }

    @Test
    void givenInvalidId_whenGetPaymentsByID_then(){

        PaymentEntity input = new PaymentEntity();
        input.setAmount(new BigDecimal("100.00"));
        input.setId(1L);



        Mockito.when(paymentRepositoryJpa.findById(2L)).thenReturn(Optional.empty());

    Assertions.assertEquals(Optional.empty() ,paymentService.getBy(2L));

    Mockito.verify(paymentRepositoryJpa).findById(2L);

    }
    @Test
    void givenPayments_whenGetAllPayments_thenReturnsAllPayments(){

            PaymentEntity input1 = new PaymentEntity();
            input1.setAmount(new BigDecimal("100.00"));
            input1.setId(1L);
        PaymentEntity input2 = new PaymentEntity();
        input2.setAmount(new BigDecimal("100.00"));
        input2.setId(1L);


        List<PaymentEntity> list = List.of(input1,input2);

            Mockito.when(paymentRepositoryJpa.findAll()).thenReturn(list);

            Assertions.assertEquals(list,paymentService.getAllPayments());

            Mockito.verify(paymentRepositoryJpa).findAll();

    }






}