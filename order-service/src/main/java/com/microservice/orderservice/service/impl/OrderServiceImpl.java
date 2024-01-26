package com.microservice.orderservice.service.impl;

import com.microservice.orderservice.dto.OrderRequest;
import com.microservice.orderservice.mapper.OrderMapper;
import com.microservice.orderservice.model.Order;
import com.microservice.orderservice.repository.OrderRepository;
import com.microservice.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowireds
    private final OrderRepository orderRepository;
    @Override
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderLineItemList(OrderMapper.toEntityList(orderRequest.getOrderLineItemDto()));
        orderRepository.save(order);
    }
}
