package com.learn.orderservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.learn.orderservice.dto.OrderRequestDto;
import com.learn.orderservice.entities.OrderHeader;
import com.learn.orderservice.entities.OrderLine;
import com.learn.orderservice.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/createOrder")
	@CircuitBreaker(name = "default", fallbackMethod = "fallbackMethod")
	public ResponseEntity<OrderHeader> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
		OrderHeader orderHeader = orderRequestDto.getOrderHeader();
		List<OrderLine> orderLines = orderRequestDto.getOrderLines();
		OrderHeader savedOrderHeader = orderService.createOrder(orderHeader, orderLines);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedOrderHeader);
	}

	@PutMapping("/updateOrder/{orderId}")
	public OrderHeader updateOrder(@PathVariable Long orderId, @RequestBody OrderRequestDto orderRequestDto) {
		OrderHeader orderHeader = orderRequestDto.getOrderHeader();
		List<OrderLine> orderLines = orderRequestDto.getOrderLines();
		return orderService.updateOrder(orderId, orderHeader, orderLines);
	}

	@GetMapping("/getOrderDetail/{orderId}")
	public OrderHeader getOrder(@PathVariable Long orderId) {
		return orderService.getOrder(orderId);
	}

	public ResponseEntity<OrderHeader> fallbackMethod(OrderRequestDto orderRequestDto, Throwable throwable) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
