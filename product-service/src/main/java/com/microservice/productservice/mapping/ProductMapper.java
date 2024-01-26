package com.microservice.productservice.mapping;

import com.microservice.productservice.dto.ProductResponse;
import com.microservice.productservice.model.Product;

import java.util.List;

public class ProductMapper {
    public static ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .name(product.getName())
                .id(product.getId())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();
    }

    public static List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream().map(ProductMapper::toResponse).toList();
    }
}
