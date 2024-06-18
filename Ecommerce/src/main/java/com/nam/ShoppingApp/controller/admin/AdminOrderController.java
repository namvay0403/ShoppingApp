package com.nam.ShoppingApp.controller.admin;

import com.nam.ShoppingApp.dto.OrderDto;
import com.nam.ShoppingApp.services.admin.adminOrder.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminOrderController {
  @Autowired private AdminOrderService adminOrderService;

  @GetMapping("/placedOrders")
  public ResponseEntity<?> getAllOrders() {
    return ResponseEntity.ok(adminOrderService.getAllOrders());
  }

  @GetMapping("/order/{orderId}/{status}")
  public ResponseEntity<?> changeOrderStatus(
      @PathVariable Long orderId, @PathVariable String status) {
    OrderDto orderDto = adminOrderService.changeOrderStatus(orderId, status);
    if (orderDto != null) {
      return ResponseEntity.ok(orderDto);
    } else {
      return ResponseEntity.badRequest().body("Something went wrong");
    }
  }

  @GetMapping("/order/analytics")
  public ResponseEntity<?> getAnalytics() {
    return ResponseEntity.ok(adminOrderService.calculateAnalytics());
  }
}
