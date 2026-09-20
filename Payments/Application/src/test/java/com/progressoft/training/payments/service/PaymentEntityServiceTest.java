package com.progressoft.training.payments.service;
import com.progressoft.training.models.Payment;
import com.progressoft.training.models.Payment;
import com.progressoft.training.payments.mapper.PaymentMapper;
import com.progressoft.training.payments.repository.PaymentRepositoryJpa;
import com.progressoft.training.repository.PaymentRepository;
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
class PaymentServiceTest {
    PaymentMapper paymentMapper;
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void givenValidInput_whenCreate_thenShouldReturnSameObject() {

        Payment input = new Payment();
        input.setAmount(new BigDecimal("100.00"));

        Payment saved = new Payment();
        saved.setId(1L);
        saved.setAmount(new BigDecimal("100.00"));

        Mockito.when(paymentRepository.save(input)).thenReturn(saved);

        Payment result = paymentService.save(input);

        assertEquals(1L, result.getId());

        Mockito.verify(paymentRepository).save(input);
    }

    @Test
    void givenValidId_whenGetElementsById_thenShouldReturnCorrectObject(){

        Payment input = new Payment();
        input.setAmount(new BigDecimal("100.00"));
        input.setId(1L);

        Mockito.when(paymentRepository.findBy(1L)).thenReturn(input);

         Payment byId =paymentService.getBy(1L).get();

        Mockito.verify(paymentRepository).findBy(byId.getId());
    }

    @Test
    void givenInvalidId_whenGetPaymentsByID_then(){

        Payment input = new Payment();
        input.setAmount(new BigDecimal("100.00"));
        input.setId(1L);



        Mockito.when(paymentRepository.findBy(2L)).thenReturn(input);

    Assertions.assertEquals(Optional.empty() ,paymentService.getBy(2L));

    Mockito.verify(paymentRepository).findBy(2L);

    }
    @Test
    void givenPayments_whenGetAllPayments_thenReturnsAllPayments(){

            Payment input1 = new Payment();
            input1.setAmount(new BigDecimal("100.00"));
            input1.setId(1L);
        Payment input2 = new Payment();
        input2.setAmount(new BigDecimal("100.00"));
        input2.setId(1L);


        List<Payment> list = List.of(input1,input2);

            Mockito.when(paymentRepository.findAll()).thenReturn(list);

            Assertions.assertEquals(list,paymentService.getAllPayments());

            Mockito.verify(paymentRepository).findAll();

    }






}