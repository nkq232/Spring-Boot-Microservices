package com.microservice.orderservice.mapper;

import com.microservice.orderservice.dto.OrderLineItemDto;
import com.microservice.orderservice.model.OrderLineItem;

import java.util.List;

public class OrderMapper {
    public static OrderLineItemDto toDto(OrderLineItem orderLineItem) {
        return new OrderLineItemDto(orderLineItem.getSkuCode(), orderLineItem.getPrice(),
                orderLineItem.getQuantity());
    }
    public static List<OrderLineItemDto> toDtoList(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream().map(OrderMapper::toDto).toList();
    }

    public static OrderLineItem toEntity(OrderLineItemDto orderLineItemDto) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(orderLineItemDto.getQuantity());
        orderLineItem.setPrice(orderLineItemDto.getPrice());
        orderLineItem.setSkuCode(orderLineItemDto.getSkuCode());
        return orderLineItem;
    }

    public static List<OrderLineItem> toEntityList(List<OrderLineItemDto> orderLineItemDtos) {
        return orderLineItemDtos.stream().map(OrderMapper::toEntity).toList();
    }
}
