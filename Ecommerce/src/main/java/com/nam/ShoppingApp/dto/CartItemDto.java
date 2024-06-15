package com.nam.ShoppingApp.dto;

import com.nam.ShoppingApp.entity.Order;
import com.nam.ShoppingApp.entity.Product;
import com.nam.ShoppingApp.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
public class CartItemDto {
    private Long id;

    private Long price;

    private Long quantity;

    private Long productId;

    private Long userId;

    private Long orderId;

    private String productName;

    private byte[] returnedImg;

}
