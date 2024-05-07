package com.learn.orderservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.learn.orderservice.dto.Customer;

@FeignClient(name = "customer-service", url = "${CUSTOMER_SERVICE_SERVICE_HOST:http://localhost}:7000")
public interface CustomerServiceProxy {
	@GetMapping("/customers/getCustomer/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long id);
}
