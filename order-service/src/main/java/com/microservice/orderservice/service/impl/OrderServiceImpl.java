package com.microservice.orderservice.service.impl;

import com.microservice.orderservice.dto.InventoryResponse;
import com.microservice.orderservice.dto.OrderRequest;
import com.microservice.orderservice.mapper.OrderMapper;
import com.microservice.orderservice.model.Order;
import com.microservice.orderservice.model.OrderLineItem;
import com.microservice.orderservice.repository.OrderRepository;
import com.microservice.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;
    @Override
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderLineItemList(OrderMapper.toEntityList(orderRequest.getOrderLineItemDto()));
        List<String> skuCodes = order.getOrderLineItemList().stream().map(OrderLineItem::getSkuCode).toList();
        InventoryResponse[] inStock = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                        .retrieve()
                                .bodyToMono(InventoryResponse[].class)
                                        .block();
        boolean allProductsInStock = Arrays.stream(inStock).allMatch(InventoryResponse::isInStock);
        if (Boolean.TRUE.equals(allProductsInStock)) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock !");
        }
    }
}
