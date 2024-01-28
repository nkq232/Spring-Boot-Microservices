package com.microservice.orderservice.service.impl;

import com.microservice.orderservice.dto.InventoryResponse;
import com.microservice.orderservice.dto.OrderRequest;
import com.microservice.orderservice.mapper.OrderMapper;
import com.microservice.orderservice.model.Order;
import com.microservice.orderservice.model.OrderLineItem;
import com.microservice.orderservice.repository.OrderRepository;
import com.microservice.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClient;
    @Override
    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderLineItemList(OrderMapper.toEntityList(orderRequest.getOrderLineItemDto()));
        //Get all skuCode to check in stock
        List<String> skuCodes = order.getOrderLineItemList().stream().map(OrderLineItem::getSkuCode).toList();
        //check in stock using inventory service
        InventoryResponse[] inStock = webClient.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                        .retrieve()
                                .bodyToMono(InventoryResponse[].class)
                                        .block();
        boolean allProductsInStock = Arrays.stream(inStock).allMatch(InventoryResponse::isInStock);
        if (Boolean.TRUE.equals(allProductsInStock) && skuCodes.size() == inStock.length) {
            orderRepository.save(order);
            log.info("Order {} created !", order.getId());
            return "Order Placed Successfully";
        } else {
            throw new IllegalArgumentException("Product is not in stock !");
        }
    }
}
