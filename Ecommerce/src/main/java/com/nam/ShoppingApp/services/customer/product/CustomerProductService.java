package com.nam.ShoppingApp.services.customer.product;

import com.nam.ShoppingApp.dto.ProductDetailDto;
import com.nam.ShoppingApp.dto.ProductDto;

import java.util.List;

public interface CustomerProductService {
    List<ProductDto> getAllProducts();

    List<ProductDto> getAllProductsByName(String name);
    ProductDetailDto getProductDetailById(Long productId);
}
