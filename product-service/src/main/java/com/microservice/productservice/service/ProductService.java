package com.microservice.productservice.service;

import com.microservice.productservice.dto.ProductRequest;
import com.microservice.productservice.dto.ProductResponse;

import java.util.List;


public interface ProductService {
    public void createProduct(ProductRequest productRequest);

    public List<ProductResponse> getAllProducts();
}
