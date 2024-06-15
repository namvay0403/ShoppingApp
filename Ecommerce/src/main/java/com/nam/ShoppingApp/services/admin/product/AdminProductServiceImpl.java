package com.nam.ShoppingApp.services.admin.product;

import com.nam.ShoppingApp.dto.ProductDto;
import com.nam.ShoppingApp.entity.Product;
import com.nam.ShoppingApp.repository.CategoryRepository;
import com.nam.ShoppingApp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AdminProductServiceImpl implements AdminProductService {

  @Autowired private ProductRepository productRepository;
  @Autowired private CategoryRepository categoryRepository;

  public ProductDto createProduct(ProductDto productDto) throws IOException {
    Product newProduct = new Product();
    newProduct.setName(productDto.getName());
    newProduct.setDescription(productDto.getDescription());
    newProduct.setPrice(productDto.getPrice());
    newProduct.setImg(productDto.getImg().getBytes());
    newProduct.setCategory(categoryRepository.findById(productDto.getCategoryId()).get());
    return productRepository.save(newProduct).getDto();
  }

  public List<ProductDto> getAllProducts() {
    List<Product> products = productRepository.findAll();
    return products.stream().map(Product::getDto).toList();
  }

  public List<ProductDto> getAllProductsByName(String name) {
    List<Product> products = productRepository.findAllByNameContaining(name);
    return products.stream().map(Product::getDto).toList();
  }

  public boolean deleteProduct(Long id) {
    Optional<Product> optionalProduct = productRepository.findById(id);
    if (optionalProduct.isPresent()) {
      productRepository.delete(optionalProduct.get());
      return true;
    }
    return true;
  }
}
