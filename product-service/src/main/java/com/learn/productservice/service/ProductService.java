package com.learn.productservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.productservice.entities.Product;
import com.learn.productservice.repositories.ProductRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Optional<Product> getProductById(Long id) {
		Optional<Product> optionalProduct = productRepository.findById(id);
		if (optionalProduct.isPresent()) {
			return optionalProduct;
		} else {
			throw new EntityNotFoundException("Product with id " + id + " not found");
		}

	}

	public Product createProduct(Product product) {
		product.setCreatedAt(LocalDateTime.now());
		product.setUpdatedAt(LocalDateTime.now());
		Product savedProduct = productRepository.save(product);
		return savedProduct;
	}

	public Product updateProduct(Long id, Product productDetails) {
		Optional<Product> optionalProduct = productRepository.findById(id);
		if (optionalProduct.isPresent()) {
			Product product = optionalProduct.get();
			product.setName(productDetails.getName());
			product.setDescription(productDetails.getDescription());
			product.setPrice(productDetails.getPrice());
			System.err.println("New Product Qutntity-->"+productDetails.getQuantityAvailable());
			product.setQuantityAvailable(productDetails.getQuantityAvailable());
			product.setUpdatedAt(LocalDateTime.now());
			return productRepository.save(product);
		} else {
			throw new EntityNotFoundException("Product with id " + id + " not found");
		}
	}

	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}
}
