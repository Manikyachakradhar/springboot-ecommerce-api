package com.Ecommerce.service;

import com.Ecommerce.entity.Order;
import com.Ecommerce.entity.OrderItem;
import com.Ecommerce.entity.OrderStatus;
import com.Ecommerce.entity.Product;
import com.Ecommerce.repository.OrderRepository;
import com.Ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public String updateOrderStatus(Long id, OrderStatus status) {


        Order order = orderRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Order not found"));

        if (!isValidTransition(order.getStatus(), status)) {
            throw new RuntimeException("Invalid status transition");
        }
        if(OrderStatus.CANCELLED.equals(status)){
            for(OrderItem item:order.getItems()){
                Product product=item.getProduct();
                product.setQuantity(product.getQuantity()+item.getQuantity());
                productRepository.save(product);
            }

        }
        order.setStatus(status);
        orderRepository.save(order);
        return ("Order status updated to "+status);
    }

    private boolean isValidTransition(OrderStatus current, OrderStatus next) {

        switch (current) {
            case CREATED -> {
                return next == OrderStatus.PAID || next == OrderStatus.CANCELLED;
            }
            case PAID -> {
                return next == OrderStatus.CANCELLED || next == OrderStatus.SHIPPED;
            }

            case SHIPPED -> {
                return next == OrderStatus.DELIVERED;
            }
            case DELIVERED, CANCELLED -> {
                return false;
            }

        }
        return false;
    }

}
