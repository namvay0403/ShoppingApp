package com.nam.ShoppingApp.entity;

import com.nam.ShoppingApp.dto.OrderDto;
import com.nam.ShoppingApp.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String orderDescription;

  private Date date;

  private Long amount;

  private String address;

  private String payment;

  private OrderStatus orderStatus;

  private Long totalAmount;

  private Long discount;

  private UUID trackingId;

  @ManyToOne(cascade = CascadeType.MERGE, optional = false)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  @OneToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "coupon_id", referencedColumnName = "id")
  private Coupon coupon;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<CartItem> cartItems = new ArrayList<>();

  public OrderDto getOrderDto() {
    OrderDto orderDto = new OrderDto();
    orderDto.setId(this.id);
    orderDto.setOrderDescription(this.orderDescription);
    orderDto.setDate(this.date);
    orderDto.setAmount(this.amount);
    orderDto.setAddress(this.address);
    orderDto.setPayment(this.payment);
    orderDto.setOrderStatus(this.orderStatus);
    orderDto.setTotalAmount(this.totalAmount);
    orderDto.setDiscount(this.discount);
    orderDto.setTrackingId(this.trackingId);
    orderDto.setUserName(this.user.getName());
    orderDto.setCartItems(this.cartItems.stream().map(CartItem::getItemDto).toList());
    if (this.coupon != null) {
      orderDto.setCouponCode(this.coupon.getCode());
    }
    return orderDto;
  }
}
