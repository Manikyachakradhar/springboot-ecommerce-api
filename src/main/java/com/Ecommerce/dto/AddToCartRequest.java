package com.Ecommerce.dto;

public record AddToCartRequest(
        Long productId,
        int quantity
) {
}
