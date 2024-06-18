package com.nam.ShoppingApp.controller.admin;

import com.nam.ShoppingApp.dto.FAQDto;
import com.nam.ShoppingApp.dto.ProductDto;
import com.nam.ShoppingApp.services.admin.faq.FAQService;
import com.nam.ShoppingApp.services.admin.product.AdminProductService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminProductController {
  @Autowired private AdminProductService adminProductService;

  @Autowired private FAQService faqService;

  @PostMapping("/product")
  public ResponseEntity<?> createProduct(@ModelAttribute ProductDto productDto) throws IOException {
    ProductDto newProduct = adminProductService.createProduct(productDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
  }

  @GetMapping("/products")
  public ResponseEntity<?> getAllProducts() {
    return ResponseEntity.ok(adminProductService.getAllProducts());
  }

  @GetMapping("/search/{name}")
  public ResponseEntity<?> getAllProductsByName(@PathVariable String name) {
    return ResponseEntity.ok(adminProductService.getAllProductsByName(name));
  }

  @DeleteMapping("/product/{productId}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
    boolean isDeleted = adminProductService.deleteProduct(productId);
    if (isDeleted) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping("/faq/{productId}")
  public ResponseEntity<FAQDto> postFAQ(@PathVariable Long productId, @RequestBody FAQDto faqDto) {
    return ResponseEntity.ok(faqService.postFAQ(productId, faqDto));
  }

  @GetMapping("/product/{productId}")
  public ResponseEntity<?> getProductById(@PathVariable Long productId) {
    return ResponseEntity.ok(adminProductService.getProductById(productId));
  }

  @PutMapping("/product/{productId}")
  public ResponseEntity<?> updateProduct(
      @ModelAttribute ProductDto productDto, @PathVariable Long productId) {
    return ResponseEntity.ok(adminProductService.updateProduct(productDto, productId));
  }
}
