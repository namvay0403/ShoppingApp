package com.nam.ShoppingApp.controller.customer;

import com.nam.ShoppingApp.dto.OrderedProductResponseDto;
import com.nam.ShoppingApp.dto.ReviewDto;
import com.nam.ShoppingApp.services.customer.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class ReviewController {
    @Autowired private ReviewService reviewService;

    @GetMapping("/ordered-products/{orderId}")
    public ResponseEntity<OrderedProductResponseDto> getOrderedProductResponseDto(@PathVariable Long orderId) {
        return ResponseEntity.ok(reviewService.getOrderedProductResponseDto(orderId));
    }

    @PostMapping("/review")
    public ResponseEntity<?> giveReview(@ModelAttribute ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewService.giveReview(reviewDto));
    }
}
