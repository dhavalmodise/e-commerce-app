package com.learn.productservice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem {
	private Long inventoryItemId;
	private Long productId;
	private Integer quantity;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
