package com.nam.ShoppingApp.services.customer.cart;

import com.nam.ShoppingApp.dto.AddProductInCartDto;
import com.nam.ShoppingApp.dto.CartItemDto;
import com.nam.ShoppingApp.dto.OrderDto;
import com.nam.ShoppingApp.dto.PlaceOrderDto;
import com.nam.ShoppingApp.entity.*;
import com.nam.ShoppingApp.enums.OrderStatus;
import com.nam.ShoppingApp.exceptions.ValidationException;
import com.nam.ShoppingApp.repository.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
  private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);
  @Autowired private OrderRepository orderRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private CartItemRepository cartItemRepository;
  @Autowired private ProductRepository productRepository;
  @Autowired private CouponRepository couponRepository;

  public ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto) {
    //    Order activeOrder =
    //        orderRepository
    //            .findByUserIdAndOrderStatus(
    //                addProductInCartDto.getUserId(),
    //                OrderStatus.PENDING);
    //    Optional<CartItem> optionalCartItem =
    //        cartItemRepository.findByProductIdAndOrderIdAndUserId(
    //            addProductInCartDto.getProductId(),
    //            activeOrder.getId(),
    //            addProductInCartDto.getUserId());

    Optional<Order> optionalActiveOrder =
        orderRepository.findByUserIdAndOrderStatus(
            addProductInCartDto.getUserId(), OrderStatus.PENDING);

    log.info("AddProductInCartDto: " + addProductInCartDto);

    Order activeOrder;

    if (optionalActiveOrder.isPresent()) {
      activeOrder = optionalActiveOrder.get();
    } else {
      // Nếu không tìm thấy order đang hoạt động, tạo một order mới
      activeOrder = new Order();
      activeOrder.setOrderStatus(OrderStatus.PENDING);
      activeOrder.setUser(userRepository.findById(addProductInCartDto.getUserId()).get());
      activeOrder.setAmount(0L);
      activeOrder.setTotalAmount(0L);
      activeOrder = orderRepository.save(activeOrder);
      log.info("new order: " + activeOrder);
    }

    Optional<CartItem> optionalCartItem =
            cartItemRepository.findByProductIdAndOrderIdAndUserId(
                    addProductInCartDto.getProductId(),
                    activeOrder.getId(),
                    addProductInCartDto.getUserId());

    if (optionalCartItem.isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Product already in cart");
    } else {
      Optional<Product> optionalProduct =
          productRepository.findById(addProductInCartDto.getProductId());
      Optional<User> optionalUser = userRepository.findById(addProductInCartDto.getUserId());

      if (optionalProduct.isPresent() && optionalUser.isPresent()) {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(optionalProduct.get());
        cartItem.setOrder(activeOrder);
        cartItem.setUser(optionalUser.get());
        cartItem.setQuantity(1L);
        cartItem.setPrice(optionalProduct.get().getPrice());

        CartItem updateCart = cartItemRepository.save(cartItem);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        activeOrder.setUser(updateCart.getUser());
        activeOrder.setAmount(activeOrder.getAmount() + cartItem.getPrice());
        activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cartItem.getPrice());
        activeOrder.setCartItems(cartItems);
        if (activeOrder.getDiscount() != null) {
          double discountAmount =
              (activeOrder.getDiscount() / 100.0) * activeOrder.getTotalAmount();
          activeOrder.setAmount((long) (activeOrder.getTotalAmount() - discountAmount));
        }

        orderRepository.save(activeOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItem.getItemDto());

      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product or User not found");
      }
    }
  }

  public OrderDto getCartByUserId(Long userId) {
    Optional<Order> optionalActiveOrder =
        orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);
    Order activeOrder = optionalActiveOrder.get();
    List<CartItemDto> cartItemDtos =
        activeOrder.getCartItems().stream().map(CartItem::getItemDto).toList();
    OrderDto orderDto = new OrderDto();
    orderDto.setAmount(activeOrder.getAmount());
    orderDto.setId(activeOrder.getId());
    orderDto.setTotalAmount(activeOrder.getTotalAmount());
    orderDto.setOrderStatus(activeOrder.getOrderStatus());
    orderDto.setDiscount(activeOrder.getDiscount());
    orderDto.setCartItems(cartItemDtos);
    if (activeOrder.getCoupon() != null) {
      orderDto.setCouponCode(activeOrder.getCoupon().getCode());
    }
    return orderDto;
  }

  public OrderDto applyCoupon(Long userId, String couponCode) {
    Optional<Order> optionalActiveOrder =
        orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);
    Order activeOrder = optionalActiveOrder.get();
    Coupon coupon =
        couponRepository
            .findByCode(couponCode)
            .orElseThrow(() -> new ValidationException("Coupon not found"));
    if (isExpired(coupon)) {
      throw new ValidationException("Coupon is expired");
    } else {
      activeOrder.setDiscount(coupon.getDiscount());
      double discountAmount = (coupon.getDiscount() / 100.0) * activeOrder.getTotalAmount();
      activeOrder.setAmount((long) (activeOrder.getTotalAmount() - discountAmount));
      activeOrder.setCoupon(coupon);
      orderRepository.save(activeOrder);
      return activeOrder.getOrderDto();
    }
  }

  private boolean isExpired(Coupon coupon) {
    if (coupon.getExpirationDate().before(new Date())) {
      return true;
    } else {
      return false;
    }
  }

  public OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto) {
    Optional<Order> optionalActiveOrder =
        orderRepository.findByUserIdAndOrderStatus(
            addProductInCartDto.getUserId(), OrderStatus.PENDING);
    Order activeOrder = optionalActiveOrder.get();
    Optional<Product> optionalProduct =
        productRepository.findById(addProductInCartDto.getProductId());
    Optional<User> optionalUser = userRepository.findById(addProductInCartDto.getUserId());
    Optional<CartItem> optionalCartItem =
        cartItemRepository.findByProductIdAndOrderIdAndUserId(
            addProductInCartDto.getProductId(),
            activeOrder.getId(),
            addProductInCartDto.getUserId());
    if (optionalProduct.isPresent() && optionalUser.isPresent() && optionalCartItem.isPresent()) {
      CartItem cartItem = optionalCartItem.get();
      cartItem.setQuantity(cartItem.getQuantity() + 1);
      Product product = optionalProduct.get();
      activeOrder.setAmount(activeOrder.getAmount() + product.getPrice());
      activeOrder.setTotalAmount(activeOrder.getTotalAmount() + product.getPrice());

      if (activeOrder.getDiscount() != null) {
        double discountAmount =
            (activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount();
        activeOrder.setAmount((long) (activeOrder.getTotalAmount() - discountAmount));
      }
      cartItemRepository.save(cartItem);
      orderRepository.save(activeOrder);
    }
    return null;
  }

  public OrderDto decreaseProductQuantity(AddProductInCartDto addProductInCartDto) {
    Optional<Order> optionalActiveOrder =
        orderRepository.findByUserIdAndOrderStatus(
            addProductInCartDto.getUserId(), OrderStatus.PENDING);
    Order activeOrder = optionalActiveOrder.get();
    Optional<Product> optionalProduct =
        productRepository.findById(addProductInCartDto.getProductId());
    Optional<User> optionalUser = userRepository.findById(addProductInCartDto.getUserId());
    Optional<CartItem> optionalCartItem =
        cartItemRepository.findByProductIdAndOrderIdAndUserId(
            addProductInCartDto.getProductId(),
            activeOrder.getId(),
            addProductInCartDto.getUserId());
    if (optionalProduct.isPresent() && optionalUser.isPresent() && optionalCartItem.isPresent()) {
      CartItem cartItem = optionalCartItem.get();
      cartItem.setQuantity(cartItem.getQuantity() - 1);
      Product product = optionalProduct.get();
      activeOrder.setAmount(activeOrder.getAmount() - product.getPrice());
      activeOrder.setTotalAmount(activeOrder.getTotalAmount() - product.getPrice());

      if (activeOrder.getDiscount() != null) {
        double discountAmount =
            (activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount();
        activeOrder.setAmount((long) (activeOrder.getTotalAmount() - discountAmount));
      }
      cartItemRepository.save(cartItem);
      orderRepository.save(activeOrder);
    }
    return null;
  }

  public OrderDto placeOrder(PlaceOrderDto placeOrderDto) {
    Optional<Order> optionalActiveOrder =
        orderRepository.findByUserIdAndOrderStatus(placeOrderDto.getUserId(), OrderStatus.PENDING);
    if (optionalActiveOrder.isPresent()) {
      Order activeOrder = optionalActiveOrder.get();
      activeOrder.setOrderStatus(OrderStatus.PLACED);
      activeOrder.setAddress(placeOrderDto.getAddress());
      activeOrder.setOrderDescription(placeOrderDto.getOrderDescription());
      activeOrder.setDate(new Date());
      activeOrder.setTrackingId(UUID.randomUUID());

      orderRepository.save(activeOrder);

      OrderDto orderDto = new OrderDto();
      List<CartItemDto> cartItems = new ArrayList<>();
      orderDto.setAmount(0L);
      orderDto.setId(activeOrder.getId());
      orderDto.setTotalAmount(0L);
      orderDto.setOrderStatus(OrderStatus.PENDING);
      orderDto.setDiscount(0L);
      orderDto.setCartItems(cartItems);

      orderRepository.save(activeOrder);

      cartItemRepository.deleteAll(activeOrder.getCartItems());

      return activeOrder.getOrderDto();
    }
    return null;
  }

  public List<OrderDto> getMyPlacedOrders(Long userId) {
    List<Order> orders =
        orderRepository.findByUserIdAndOrderStatusIn(
            userId, List.of(OrderStatus.PLACED, OrderStatus.SHIPPED, OrderStatus.DELIVERED));
    var sortedOrders = orders.stream().sorted(Comparator.comparing(Order::getDate, Comparator.reverseOrder()));

    return sortedOrders.map(Order::getOrderDto).toList();
  }

  public OrderDto searchOrderTrackingId(UUID trackingId) {
    Optional<Order> optionalOrder = orderRepository.findByTrackingId(trackingId);
    if (optionalOrder.isPresent()) {
      return optionalOrder.get().getOrderDto();
    } else {
      return null;
    }
  }

  public OrderDto removeItemFromCart(AddProductInCartDto addProductInCartDto) {
    Optional<Order> optionalActiveOrder =
        orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.PENDING);
    Order activeOrder = optionalActiveOrder.get();
    Optional<CartItem> optionalCartItem =
        cartItemRepository.findByProductIdAndOrderIdAndUserId(
                addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());
    if (optionalCartItem.isPresent()) {
      CartItem cartItem = optionalCartItem.get();
      activeOrder.setAmount(activeOrder.getAmount() - cartItem.getPrice());
      activeOrder.setTotalAmount(activeOrder.getTotalAmount() - cartItem.getPrice());
      cartItemRepository.delete(cartItem);
      orderRepository.save(activeOrder);
    }
    return activeOrder.getOrderDto();
  }

  public Long getCartTotalPrice(Long orderId, Long userId) {
    Optional<Order> optionalActiveOrder =
        orderRepository.findByIdAndUserId(orderId, userId);
    Order activeOrder = optionalActiveOrder.get();
    return activeOrder.getAmount();
  }
}
