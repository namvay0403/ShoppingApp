package com.nam.ShoppingApp.controller;

import com.nam.ShoppingApp.dto.OrderDto;
import com.nam.ShoppingApp.services.customer.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TrackingController {
  @Autowired private CartService cartService;

  @GetMapping("/order/{trackingId}")
  public ResponseEntity<?> searchOrderTrackingId(@PathVariable UUID trackingId) {
    OrderDto orderDto = cartService.searchOrderTrackingId(trackingId);
    if (orderDto == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(orderDto);
    }
  }
}
