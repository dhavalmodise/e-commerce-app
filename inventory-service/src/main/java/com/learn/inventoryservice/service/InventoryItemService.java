package com.learn.inventoryservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.learn.inventoryservice.dto.Product;
import com.learn.inventoryservice.entities.InventoryItem;
import com.learn.inventoryservice.proxy.ProductProxy;
import com.learn.inventoryservice.repositories.InventoryItemRepository;

import io.github.resilience4j.retry.annotation.Retry;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class InventoryItemService {

	private Logger logger = LoggerFactory.getLogger(InventoryItemService.class);

	@Autowired
	private InventoryItemRepository inventoryItemRepository;

	@Autowired
	private ProductProxy productProxy;

	public List<InventoryItem> getAllInventoryItems() {
		return inventoryItemRepository.findAll();
	}

	public Optional<InventoryItem> getInventoryItemById(Long id) {
		Optional<InventoryItem> optionalInventoryItem = inventoryItemRepository.findById(id);
		if (optionalInventoryItem.isPresent()) {
			return optionalInventoryItem;
		} else {
			throw new EntityNotFoundException("Inventory item with id " + id + " not found");
		}
	}

	@Transactional
	public InventoryItem createInventoryItem(InventoryItem inventoryItem) {
		logger.info("Add item:--->{}", inventoryItem.toString());
		ResponseEntity<Product> productById = productProxy.getProductById(inventoryItem.getProductId());
		if (productById.hasBody()) {
			inventoryItem.setCreatedAt(LocalDateTime.now());
			inventoryItem.setUpdatedAt(LocalDateTime.now());
			InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem);
			if (savedInventoryItem != null) {
				productById.getBody().setQuantityAvailable(
						productById.getBody().getQuantityAvailable() + inventoryItem.getQuantity());
				productProxy.updateProduct(inventoryItem.getProductId(), productById.getBody());
			}
			return savedInventoryItem;
		} else {
			throw new EntityNotFoundException("Product with id " + inventoryItem.getProductId() + " not found");
		}

	}

	@Transactional
	public InventoryItem updateInventoryItem(Long id, InventoryItem inventoryItemDetails) {
		Optional<InventoryItem> optionalInventoryItem = inventoryItemRepository.findById(id);
		if (optionalInventoryItem.isPresent()) {
			InventoryItem inventoryItem = optionalInventoryItem.get();
			inventoryItem.setProductId(inventoryItemDetails.getProductId());
			inventoryItem.setQuantity(inventoryItemDetails.getQuantity());
			inventoryItem.setUpdatedAt(LocalDateTime.now());
			InventoryItem updatedInventoryItem = inventoryItemRepository.save(inventoryItem);
			if (updatedInventoryItem != null) {
				ResponseEntity<Product> productById = productProxy.getProductById(inventoryItem.getProductId());
				Product productBody = productById.getBody();
				productBody.setQuantityAvailable(productBody.getQuantityAvailable() + inventoryItem.getQuantity());
				productProxy.updateProduct(inventoryItem.getProductId(), productBody);
			}
			return updatedInventoryItem;
		} else {
			throw new EntityNotFoundException("Inventory item with id " + id + " not found");
		}
	}

	public void deleteInventoryItem(Long id) {
		inventoryItemRepository.deleteById(id);
	}
}
