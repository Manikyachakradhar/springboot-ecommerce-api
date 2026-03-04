package com.Ecommerce.dto;

public record CartItemResponses(

        Long productId,
        String productName,
        double price,
        int quantity,
        double subtotal
) {
}
