package com.learn.inventoryservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.learn.inventoryservice.dto.Product;

@FeignClient(name = "product-service", url = "${PRODUCT_SERVICE_SERVICE_HOST:http://localhost}:9000")
public interface ProductProxy {
	@GetMapping("/products/getProduct/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id);
	
	@PutMapping("/products/updateProduct/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product);
}
