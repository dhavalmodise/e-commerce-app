package com.learn.orderservice.dto;

import java.util.List;

import com.learn.orderservice.entities.OrderHeader;
import com.learn.orderservice.entities.OrderLine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
	private OrderHeader orderHeader;
    private List<OrderLine> orderLines;
}
