package com.nam.ShoppingApp.services.admin.product;

import com.nam.ShoppingApp.dto.ProductDto;
import com.nam.ShoppingApp.entity.Category;
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

  public ProductDto getProductById(Long id) {
    Optional<Product> optionalProduct = productRepository.findById(id);
    if (optionalProduct.isPresent()) {
      return optionalProduct.get().getDto();
    }
    return null;
  }

  public ProductDto updateProduct(ProductDto productDto, Long productId) {
    Optional<Product> optionalProduct = productRepository.findById(productId);
    Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
    if (optionalProduct.isPresent() && optionalCategory.isPresent()) {
      Product product = optionalProduct.get();
      product.setName(productDto.getName());
      product.setDescription(productDto.getDescription());
      product.setPrice(productDto.getPrice());
      if (productDto.getImg() != null) {
        try {
          product.setImg(productDto.getImg().getBytes());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      product.setCategory(optionalCategory.get());
      return productRepository.save(product).getDto();
    }
    return null;
  }
}
