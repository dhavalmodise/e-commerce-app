package com.learn.orderservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.learn.orderservice.dto.Product;


@FeignClient(name = "product-service", url = "${PRODUCT_SERVICE_SERVICE_HOST:http://localhost}:9000")
public interface ProductServiceProxy {
	@GetMapping("/products/getProduct/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id);
}
