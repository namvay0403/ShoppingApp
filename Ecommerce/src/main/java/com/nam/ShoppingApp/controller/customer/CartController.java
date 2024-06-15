package com.nam.ShoppingApp.controller.customer;

import com.nam.ShoppingApp.dto.AddProductInCartDto;
import com.nam.ShoppingApp.dto.OrderDto;
import com.nam.ShoppingApp.dto.PlaceOrderDto;
import com.nam.ShoppingApp.services.customer.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CartController {

  @Autowired private CartService cartService;

  @PostMapping("/cart")
  public ResponseEntity<?> addProductToCart(@RequestBody AddProductInCartDto addProductInCartDto) {
    return cartService.addProductToCart(addProductInCartDto);
  }

  @GetMapping("/cart/{userId}")
  public ResponseEntity<?> addProductToCart(@PathVariable Long userId) {
    OrderDto orderDto = cartService.getCartByUserId(userId);
    return ResponseEntity.ok(orderDto);
  }

  @GetMapping("/coupon/{userId}/{couponCode}")
  public ResponseEntity<?> applyCoupon(@PathVariable Long userId, @PathVariable String couponCode) {
    try {
      OrderDto orderDto = cartService.applyCoupon(userId, couponCode);
      return ResponseEntity.ok(orderDto);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/addition")
  public ResponseEntity<OrderDto> increaseProductQuantity(
      @RequestBody AddProductInCartDto addProductInCartDto) {
    return ResponseEntity.ok(cartService.increaseProductQuantity(addProductInCartDto));
  }

  @PostMapping("/subtraction")
  public ResponseEntity<OrderDto> decreaseProductQuantity(
      @RequestBody AddProductInCartDto addProductInCartDto) {
    return ResponseEntity.ok(cartService.decreaseProductQuantity(addProductInCartDto));
  }

  @PostMapping("/placeOrder")
  public ResponseEntity<OrderDto> placeOrder(@RequestBody PlaceOrderDto placeOrderDto) {
    return ResponseEntity.ok(cartService.placeOrder(placeOrderDto));
  }
}
