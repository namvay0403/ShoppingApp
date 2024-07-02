package com.nam.ShoppingApp.repository;

import com.nam.ShoppingApp.entity.Order;
import com.nam.ShoppingApp.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {

  @Query("SELECT o FROM Order o WHERE o.user.id = ?1 AND o.orderStatus = ?2")
  Optional<Order> findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);

  List<Order> findAllByOrderStatusIn(List<OrderStatus> orderStatuses);

  List<Order> findByUserIdAndOrderStatusIn(Long userId, List<OrderStatus> orderStatus);

  Optional<Order> findByTrackingId(UUID trackingId);

  List<Order> findByDateBetweenAndOrderStatus(
      Date startOfMonth, Date endOfMonth, OrderStatus orderStatus);

  Long countByOrderStatus(OrderStatus orderStatus);

  Optional<Order> findByIdAndUserId(Long orderId, Long userId);
}
