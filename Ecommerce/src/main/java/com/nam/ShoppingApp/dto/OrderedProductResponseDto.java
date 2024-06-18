package com.nam.ShoppingApp.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderedProductResponseDto {
    private List<ProductDto> productDtoList;
    private Long orderAmount;
}
