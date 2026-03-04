package com.Ecommerce.dto;

public record OrderItemResponse(
        String productName,
        Integer quantity,
        Double price
) {}
