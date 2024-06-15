package com.nam.ShoppingApp.services.admin.adminOrder;

import com.nam.ShoppingApp.dto.OrderDto;
import com.nam.ShoppingApp.entity.Order;
import com.nam.ShoppingApp.enums.OrderStatus;
import com.nam.ShoppingApp.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {
    @Autowired private OrderRepository orderRepository;

    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAllByOrderStatusIn(List.of(OrderStatus.PLACED, OrderStatus.SHIPPED, OrderStatus.DELIVERED));
        return orders.stream().map(Order::getOrderDto).toList();
    }
}
