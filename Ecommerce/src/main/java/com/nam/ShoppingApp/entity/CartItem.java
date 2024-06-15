package com.nam.ShoppingApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nam.ShoppingApp.dto.CartItemDto;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@Table(name = "cart_item")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long price;

    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public CartItemDto getItemDto() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(this.id);
        cartItemDto.setPrice(this.price);
        cartItemDto.setQuantity(this.quantity);
        cartItemDto.setProductId(this.product.getId());
        cartItemDto.setUserId(this.user.getId());
        cartItemDto.setOrderId(this.order.getId());
        cartItemDto.setProductName(this.product.getName());
        cartItemDto.setReturnedImg(this.product.getImg());
        return cartItemDto;
    }

}
