package com.nam.ShoppingApp.services.admin.product;

import com.nam.ShoppingApp.dto.ProductDto;

import java.io.IOException;
import java.util.List;

public interface AdminProductService {
  ProductDto createProduct(ProductDto productDto) throws IOException;
  List<ProductDto> getAllProducts();
  List<ProductDto> getAllProductsByName(String name);
  boolean deleteProduct(Long id);
}
