package com.nam.ShoppingApp.dto;

import lombok.Data;

@Data
public class WishlistDto {
    private Long id;
    private Long productId;
    private Long userId;
    private String productName;
    private String productDescription;
    private byte[] returnedImg;
    private Long price;
}
