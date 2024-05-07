package com.learn.inventoryservice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.inventoryservice.entities.InventoryItem;
import com.learn.inventoryservice.service.InventoryItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/inventory-items")
public class InventoryItemController {

	@Autowired
	private InventoryItemService inventoryItemService;

	@GetMapping("/all")
	public List<InventoryItem> getAllInventoryItems() {
		return inventoryItemService.getAllInventoryItems();
	}

	@GetMapping("/getInventoryItem/{id}")
	public ResponseEntity<InventoryItem> getInventoryItemById(@PathVariable Long id) {
		Optional<InventoryItem> inventoryItem = inventoryItemService.getInventoryItemById(id);
		return inventoryItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/addInventoryItem")
	@CircuitBreaker(name = "default", fallbackMethod = "fallbackMethod")
	public ResponseEntity<InventoryItem> createInventoryItem(@RequestBody InventoryItem inventoryItem) {
		InventoryItem createdInventoryItem = inventoryItemService.createInventoryItem(inventoryItem);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdInventoryItem);
	}

	@PutMapping("/updateInventoryItem/{id}")
	public ResponseEntity<InventoryItem> updateInventoryItem(@PathVariable Long id,
			@RequestBody InventoryItem inventoryItem) {
		InventoryItem updatedInventoryItem = inventoryItemService.updateInventoryItem(id, inventoryItem);
		return updatedInventoryItem != null ? ResponseEntity.ok(updatedInventoryItem)
				: ResponseEntity.notFound().build();
	}

	@DeleteMapping("/deleteInventoryItem/{id}")
	public ResponseEntity<Void> deleteInventoryItem(@PathVariable Long id) {
		inventoryItemService.deleteInventoryItem(id);
		return ResponseEntity.noContent().build();
	}

	public ResponseEntity<InventoryItem> fallbackMethod(InventoryItem inventoryItem, Throwable throwable) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
