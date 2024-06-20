package com.nam.ShoppingApp.services.customer.cart;

import com.nam.ShoppingApp.dto.AddProductInCartDto;
import com.nam.ShoppingApp.dto.OrderDto;
import com.nam.ShoppingApp.dto.PlaceOrderDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CartService {
    ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto);
    OrderDto getCartByUserId(Long userId);
    OrderDto applyCoupon(Long userId, String couponCode);
    OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto);
    OrderDto decreaseProductQuantity(AddProductInCartDto addProductInCartDto);
    OrderDto placeOrder(PlaceOrderDto placeOrderDto);
    List<OrderDto> getMyPlacedOrders(Long userId);
    OrderDto searchOrderTrackingId(UUID trackingId);
    OrderDto removeItemFromCart(AddProductInCartDto addProductInCartDto);
    Long getCartTotalPrice(Long orderId, Long userId);
}
