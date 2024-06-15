package com.nam.ShoppingApp.services.customer.cart;

import com.nam.ShoppingApp.dto.AddProductInCartDto;
import com.nam.ShoppingApp.dto.OrderDto;
import com.nam.ShoppingApp.dto.PlaceOrderDto;
import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto);
    OrderDto getCartByUserId(Long userId);
    OrderDto applyCoupon(Long userId, String couponCode);
    OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto);
    OrderDto decreaseProductQuantity(AddProductInCartDto addProductInCartDto);
    OrderDto placeOrder(PlaceOrderDto placeOrderDto);
}
