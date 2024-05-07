package com.learn.paymentservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.paymentservice.controller.PaymentController;
import com.learn.paymentservice.entities.Payment;
import com.learn.paymentservice.service.PaymentService;

public class PaymentControllerTests {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    public void testGetAllPayments() throws Exception {
        Payment payment = new Payment();
        payment.setPaymentId(1L);
        payment.setOrderId(100L);
        payment.setAmount(new BigDecimal("100.0"));
        payment.setPaymentMethod("Credit Card");
        payment.setStatus("Pending");
//        payment.setCreatedAt(LocalDateTime.now());
//        payment.setUpdatedAt(LocalDateTime.now());

        when(paymentService.getAllPayments()).thenReturn(Arrays.asList(payment));

        mockMvc.perform(get("/payments/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].paymentId").value(1));
    }

    @Test
    public void testGetPaymentById() throws Exception {
        Payment payment = new Payment();
        payment.setPaymentId(1L);
        payment.setOrderId(100L);
        payment.setAmount(new BigDecimal("100.0"));
        payment.setPaymentMethod("Credit Card");
        payment.setStatus("Pending");
//        payment.setCreatedAt(LocalDateTime.now());
//        payment.setUpdatedAt(LocalDateTime.now());

        when(paymentService.getPaymentById(1L)).thenReturn(Optional.of(payment));

        mockMvc.perform(get("/payments/getPayment/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.paymentId").value(1));
    }

    @Test
    public void testCreatePayment() throws Exception {
        Payment payment = new Payment();
        payment.setOrderId(100L);
        payment.setAmount(new BigDecimal("100.0"));
        payment.setPaymentMethod("Credit Card");
        payment.setStatus("Pending");
//        payment.setCreatedAt(LocalDateTime.now());
//        payment.setUpdatedAt(LocalDateTime.now());

        when(paymentService.createPayment(any())).thenReturn(payment);

        mockMvc.perform(post("/payments/makePayment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(payment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(100.0));
    }

    @Test
    public void testUpdatePayment() throws Exception {
        Payment payment = new Payment();
        payment.setPaymentId(1L);
        payment.setOrderId(100L);
        payment.setAmount(new BigDecimal("100.0"));
        payment.setPaymentMethod("Credit Card");
        payment.setStatus("Pending");
//        payment.setCreatedAt(LocalDateTime.now());
//        payment.setUpdatedAt(LocalDateTime.now());

        when(paymentService.updatePayment(anyLong(), any())).thenReturn(payment);

        mockMvc.perform(put("/payments/updatePayment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(payment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(1));
    }

    @Test
    public void testDeletePayment() throws Exception {
        mockMvc.perform(delete("/payments/deletePayment/1"))
                .andExpect(status().isNoContent());

        verify(paymentService, times(1)).deletePayment(1L);
    }
}

