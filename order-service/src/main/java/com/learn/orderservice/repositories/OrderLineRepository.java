package com.learn.orderservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learn.orderservice.entities.OrderHeader;
import com.learn.orderservice.entities.OrderLine;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

	public Integer deleteByOrderHeader(OrderHeader orderHeader);
}
