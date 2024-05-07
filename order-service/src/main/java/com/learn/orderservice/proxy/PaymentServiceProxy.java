package com.learn.orderservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.learn.orderservice.dto.Payment;

@FeignClient(name = "payment-service", url = "${PAYMENT_SERVICE_SERVICE_HOST:http://localhost}:9100")
public interface PaymentServiceProxy {
	@PostMapping("/payments/makePayment")
	public ResponseEntity<Payment> createPayment(@RequestBody Payment payment);
}
