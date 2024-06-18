package com.nam.ShoppingApp.services.customer.review;

import com.nam.ShoppingApp.dto.OrderedProductResponseDto;
import com.nam.ShoppingApp.dto.ReviewDto;

public interface ReviewService {
    OrderedProductResponseDto getOrderedProductResponseDto(Long orderId);
    ReviewDto giveReview(ReviewDto reviewDto);
}
