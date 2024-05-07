package com.learn.orderservice.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.learn.orderservice.dto.Payment;
import com.learn.orderservice.dto.Product;
import com.learn.orderservice.entities.OrderHeader;
import com.learn.orderservice.entities.OrderLine;
import com.learn.orderservice.proxy.CustomerServiceProxy;
import com.learn.orderservice.proxy.PaymentServiceProxy;
import com.learn.orderservice.proxy.ProductServiceProxy;
import com.learn.orderservice.repositories.OrderHeaderRepository;
import com.learn.orderservice.repositories.OrderLineRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderService {

	@Autowired
	private OrderHeaderRepository orderHeaderRepository;
	@Autowired
	private OrderLineRepository orderLineRepository;
	@Autowired
	private PaymentServiceProxy paymentServiceProxy;
	@Autowired
	private ProductServiceProxy productServiceProxy;
	@Autowired
	private CustomerServiceProxy customerServiceProxy;

	Logger logger = LoggerFactory.getLogger(OrderService.class);

	public OrderHeader createOrder(OrderHeader orderHeader, List<OrderLine> orderLines) {
		if (customerServiceProxy.getCustomerById(orderHeader.getCustomerId()).hasBody()) {
			BigDecimal totalAmount = BigDecimal.ZERO;
			for (OrderLine orderLine : orderLines) {
				ResponseEntity<Product> productById = productServiceProxy.getProductById(orderLine.getProductId());
				if (!productById.hasBody()) {
					throw new EntityNotFoundException(
							"Order with product id " + orderLine.getProductId() + " not found");
				}
				totalAmount = totalAmount.add(orderLine.getSubtotal());
				orderLine.setOrderHeader(orderHeader);
			}
			orderHeader.setTotalAmount(totalAmount);
			orderHeader.setOrderDate(LocalDateTime.now());
			orderHeader.setStatus("pending");
			orderHeader.setCreatedAt(LocalDateTime.now());
			OrderHeader savedOrderHeader = orderHeaderRepository.save(orderHeader);

			orderLines.forEach(line -> line.setOrderHeader(savedOrderHeader));
			orderLineRepository.saveAll(orderLines);
			Payment pendingPayment = new Payment(null, savedOrderHeader.getOrderId(), totalAmount, "Online", "Pending",
					null, null);
			paymentServiceProxy.createPayment(pendingPayment);
			return savedOrderHeader;
		} else {
			throw new EntityNotFoundException("Order with customer id " + orderHeader.getCustomerId() + " not found");
		}

	}

	public OrderHeader updateOrder(Long orderId, OrderHeader orderHeader, List<OrderLine> orderLines) {
		OrderHeader existingOrderHeader = orderHeaderRepository.findById(orderId)
				.orElseThrow(() -> new EntityNotFoundException("Order with id " + orderId + " not found"));
		BigDecimal totalAmount = BigDecimal.ZERO;
		List<OrderLine> exisingLines = orderLineRepository.findAll();
		for (OrderLine orderLine : orderLines) {
			totalAmount = totalAmount.add(orderLine.getSubtotal());
			orderLine.setOrderHeader(existingOrderHeader);
		}
		List<OrderLine> notReceivedInUpdatedList = exisingLines.stream()
				.filter(existingLine -> !orderLines.contains(existingLine)).collect(Collectors.toList());
		notReceivedInUpdatedList.forEach(line -> orderLineRepository.deleteById(line.getOrderItemId()));
		existingOrderHeader.setTotalAmount(totalAmount);
		existingOrderHeader.setStatus(orderHeader.getStatus());
		existingOrderHeader.setUpdatedAt(LocalDateTime.now());
		orderLineRepository.saveAll(orderLines);
		return orderHeaderRepository.save(existingOrderHeader);
	}

	public OrderHeader getOrder(Long orderId) {
		return orderHeaderRepository.findById(orderId)
				.orElseThrow(() -> new EntityNotFoundException("Order with id " + orderId + " not found"));
	}
}
