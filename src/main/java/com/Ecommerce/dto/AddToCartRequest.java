package com.Ecommerce.dto;

import jakarta.validation.constraints.Min;

public record AddToCartRequest(
        Long productId,
        @Min(value=1,message = "Quantity Should be greater than 0")
        int quantity
) {
}
