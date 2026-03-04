package com.Ecommerce.dto;

import java.util.List;

public record CartResponses(
        List<CartItemResponses> items,
        double totalAmount
) {
}
