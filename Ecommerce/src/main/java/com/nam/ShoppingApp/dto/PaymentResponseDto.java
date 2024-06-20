package com.nam.ShoppingApp.dto;

import lombok.Data;

@Data
public class PaymentResponseDto {
    private String orderId;
    private String paymentUrl;
}
