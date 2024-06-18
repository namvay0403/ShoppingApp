package com.nam.ShoppingApp.services.customer.product;

import com.nam.ShoppingApp.dto.ProductDetailDto;
import com.nam.ShoppingApp.dto.ProductDto;
import com.nam.ShoppingApp.entity.FAQ;
import com.nam.ShoppingApp.entity.Product;
import com.nam.ShoppingApp.entity.Review;
import com.nam.ShoppingApp.repository.FAQRepository;
import com.nam.ShoppingApp.repository.ProductRepository;
import com.nam.ShoppingApp.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerProductServiceImpl implements CustomerProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired private FAQRepository faqRepository;
    @Autowired private ReviewRepository reviewRepository;

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::getDto).toList();
    }

    public List<ProductDto> getAllProductsByName(String name) {
        List<Product> products = productRepository.findAllByNameContaining(name);
        return products.stream().map(Product::getDto).toList();
    }

  public ProductDetailDto getProductDetailById(Long productId) {
      Optional<Product> optionalProduct = Optional.ofNullable(productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found")));
      if (optionalProduct.isPresent()) {
          List<FAQ> faqList = faqRepository.findAllByProductId(productId);
          List<Review> reviewList = reviewRepository.findAllByProductId(productId);

            ProductDetailDto productDetailDto = new ProductDetailDto();
            productDetailDto.setFaqDtoList(faqList.stream().map(FAQ::getFAQDto).toList());
            productDetailDto.setReviewDtoList(reviewList.stream().map(Review::getReviewDto).toList());
            productDetailDto.setProductDto(optionalProduct.get().getDto());

            return productDetailDto;
      }
      return null;
  }
}
