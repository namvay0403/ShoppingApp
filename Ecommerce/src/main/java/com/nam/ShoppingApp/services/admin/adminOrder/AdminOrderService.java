package com.nam.ShoppingApp.services.admin.adminOrder;

import com.nam.ShoppingApp.dto.AnalyticsResponse;
import com.nam.ShoppingApp.dto.OrderDto;

import java.util.List;

public interface AdminOrderService {
    List<OrderDto> getAllOrders();
    OrderDto changeOrderStatus(Long orderId, String orderStatus);
    AnalyticsResponse calculateAnalytics();
}
