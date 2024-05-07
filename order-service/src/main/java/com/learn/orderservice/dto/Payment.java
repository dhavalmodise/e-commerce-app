package com.learn.orderservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
	private Long paymentId;

	private Long orderId;
	private BigDecimal amount;
	private String paymentMethod;
	private String status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
