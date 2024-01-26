package com.microservice.orderservice.service;

import com.microservice.orderservice.dto.OrderRequest;
import com.microservice.orderservice.model.Order;

import java.util.UUID;

public interface OrderService {
    public void placeOrder(OrderRequest orderRequest);
}
