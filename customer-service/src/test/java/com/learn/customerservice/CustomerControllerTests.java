package com.learn.customerservice;

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

import java.time.LocalDateTime;
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
import com.learn.customerservice.controller.CustomerController;
import com.learn.customerservice.entities.Customer;
import com.learn.customerservice.service.CustomerService;

public class CustomerControllerTests {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }
    

    @Test
    public void testGetAllCustomers() throws Exception {
        Customer customer1 = createCustomer(1L, "John", "Doe", "john.doe@example.com", "1234567890", "123 Main St");
        Customer customer2 = createCustomer(2L, "Jane", "Doe", "jane.doe@example.com", "9876543210", "456 Elm St");

        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(customer1, customer2));

        mockMvc.perform(get("/customers/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    public void testGetCustomerById() throws Exception {
        Customer customer = createCustomer(1L, "John", "Doe", "john.doe@example.com", "1234567890", "123 Main St");

        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));

        mockMvc.perform(get("/customers/getCustomer/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    public void testCreateCustomer() throws Exception {
        Customer customer = createCustomer(null, "John", "Doe", "john.doe@example.com", "1234567890", "123 Main St");

        when(customerService.createCustomer(any())).thenReturn(customer);

        mockMvc.perform(post("/customers/addCustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        Customer customer = createCustomer(1L, "John", "Doe", "john.doe@example.com", "1234567890", "123 Main St");

        when(customerService.updateCustomer(anyLong(), any())).thenReturn(customer);

        mockMvc.perform(put("/customers/updateCustomer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/customers/deleteCustomer/1"))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteCustomer(1L);
    }

    private Customer createCustomer(Long id, String firstName, String lastName, String email, String phoneNumber, String address) {
        Customer customer = new Customer();
        customer.setCustomerId(id);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);
        customer.setAddress(address);
//        customer.setCreatedAt(LocalDateTime.now());
//        customer.setUpdatedAt(LocalDateTime.now());
        return customer;
    }
}
