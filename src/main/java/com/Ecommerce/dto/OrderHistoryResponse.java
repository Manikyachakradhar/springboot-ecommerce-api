package com.Ecommerce.dto;

import com.Ecommerce.entity.OrderItem;
import com.Ecommerce.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderHistoryResponse(

        Long orderId,
        LocalDateTime createdAt,
        OrderStatus status,
        Double totalAmount,
        List<OrderItemResponse> orderItems

) {
}
