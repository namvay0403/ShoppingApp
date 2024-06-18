package com.nam.ShoppingApp.services.admin.adminOrder;

import com.nam.ShoppingApp.dto.AnalyticsResponse;
import com.nam.ShoppingApp.dto.OrderDto;
import com.nam.ShoppingApp.entity.Order;
import com.nam.ShoppingApp.enums.OrderStatus;
import com.nam.ShoppingApp.repository.OrderRepository;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {
  @Autowired private OrderRepository orderRepository;

  public List<OrderDto> getAllOrders() {
    List<Order> orders =
        orderRepository.findAllByOrderStatusIn(
            List.of(OrderStatus.PLACED, OrderStatus.SHIPPED, OrderStatus.DELIVERED));
    return orders.stream().map(Order::getOrderDto).toList();
  }

  public OrderDto changeOrderStatus(Long orderId, String orderStatus) {
    Optional<Order> optionalOrder =
        Optional.ofNullable(
            orderRepository
                .findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found")));
    if (optionalOrder.isPresent()) {
      Order order = optionalOrder.get();

      if (orderStatus.equals("Shipped")) {
        order.setOrderStatus(OrderStatus.SHIPPED);
      } else if (orderStatus.equals("Delivered")) {
        order.setOrderStatus(OrderStatus.DELIVERED);
      } else {
        throw new RuntimeException("Invalid order status");
      }

      return orderRepository.save(order).getOrderDto();
    }
    return null;
  }

  public AnalyticsResponse calculateAnalytics() {
    LocalDate today = LocalDate.now();
    LocalDate previousMonth = today.minusMonths(1);

    Long currentMonthOrders = getTotalOrderForMonth(today.getMonthValue(), today.getYear());

    Long previousMonthOrders =
        getTotalOrderForMonth(previousMonth.getMonthValue(), previousMonth.getYear());

    Long currentMonthEarned = getTotalEarnedForMonth(today.getMonthValue(), today.getYear());

    Long previousMonthEarned =
        getTotalEarnedForMonth(previousMonth.getMonthValue(), previousMonth.getYear());

    Long placed = orderRepository.countByOrderStatus(OrderStatus.PLACED);
    Long shipped = orderRepository.countByOrderStatus(OrderStatus.SHIPPED);
    Long delivered = orderRepository.countByOrderStatus(OrderStatus.DELIVERED);

    return new AnalyticsResponse(
        placed,
        shipped,
        delivered,
        currentMonthOrders,
        previousMonthOrders,
        currentMonthEarned,
        previousMonthEarned);
  }

  private Long getTotalEarnedForMonth(int monthValue, int year) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.MONTH, monthValue - 1);
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);

    Date startOfMonth = calendar.getTime();

    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);

    Date endOfMonth = calendar.getTime();

    List<Order> orders =
        orderRepository.findByDateBetweenAndOrderStatus(
            startOfMonth, endOfMonth, OrderStatus.DELIVERED);

    Long sum = 0L;
    for (Order order : orders) {
      sum += order.getAmount();
    }
    return sum;
  }

  private Long getTotalOrderForMonth(int monthValue, int year) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.MONTH, monthValue - 1);
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);

    Date startOfMonth = calendar.getTime();

    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);

    Date endOfMonth = calendar.getTime();

    List<Order> orders =
        orderRepository.findByDateBetweenAndOrderStatus(
            startOfMonth, endOfMonth, OrderStatus.DELIVERED);

    return (long) orders.size();
  }
}
