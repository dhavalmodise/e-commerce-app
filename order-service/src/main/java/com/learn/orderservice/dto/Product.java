package com.learn.orderservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	private Long productId;
	private String name;
	private String description;
	private BigDecimal price;
	private Integer quantityAvailable;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
