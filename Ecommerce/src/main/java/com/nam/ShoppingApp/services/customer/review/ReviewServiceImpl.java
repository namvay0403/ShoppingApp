package com.nam.ShoppingApp.services.customer.review;

import com.nam.ShoppingApp.dto.OrderedProductResponseDto;
import com.nam.ShoppingApp.dto.ProductDto;
import com.nam.ShoppingApp.dto.ReviewDto;
import com.nam.ShoppingApp.entity.*;
import com.nam.ShoppingApp.repository.OrderRepository;
import com.nam.ShoppingApp.repository.ProductRepository;
import com.nam.ShoppingApp.repository.ReviewRepository;
import com.nam.ShoppingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements  ReviewService{

    @Autowired private OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ReviewRepository reviewRepository;

    public OrderedProductResponseDto getOrderedProductResponseDto(Long orderId){
        Optional<Order> optionalOrder = Optional.ofNullable(orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found")));
        OrderedProductResponseDto orderedProductResponseDto = new OrderedProductResponseDto();

        if(optionalOrder.isPresent()){
            orderedProductResponseDto.setOrderAmount(optionalOrder.get().getAmount());

            List<ProductDto> productDtoList= new ArrayList<>();

            for(CartItem cartItem : optionalOrder.get().getCartItems()){
                ProductDto productDto = new ProductDto();

                productDto.setId(cartItem.getProduct().getId());
                productDto.setName(cartItem.getProduct().getName());
                productDto.setPrice(cartItem.getProduct().getPrice());
                productDto.setQuantity(cartItem.getQuantity());
                productDto.setByteImg(cartItem.getProduct().getImg());
                productDtoList.add(productDto);
            }
            orderedProductResponseDto.setProductDtoList(productDtoList);
        }
        return orderedProductResponseDto;
    }

    public ReviewDto giveReview(ReviewDto reviewDto){
        Optional<Product> optionalProduct = Optional.ofNullable(productRepository.findById(reviewDto.getProductId()).orElseThrow(() -> new RuntimeException("Product not found")));
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findById(reviewDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found")));

        if (optionalProduct.isPresent() && optionalUser.isPresent()){
            Review review = new Review();

            review.setRating(reviewDto.getRating());
            review.setDescription(reviewDto.getDescription());
            review.setProduct(optionalProduct.get());
            review.setUser(optionalUser.get());
            try {
                review.setImg(reviewDto.getImg().getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return reviewRepository.save(review).getReviewDto();

        }
        return null;
    }
}
