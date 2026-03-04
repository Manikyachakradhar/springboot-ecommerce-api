package com.Ecommerce.controller;

import com.Ecommerce.dto.OrderHistoryResponse;
import com.Ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(Authentication authentication){

       String email=  authentication.getName();

       orderService.placeOrder(email);
       return ResponseEntity.ok("Order placed successfully");

    }

    @PutMapping ("/cancel/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id,  Authentication authentication){

        String email= authentication.getName();
        orderService.cancelOrder(id, email);


        return ResponseEntity.ok("Order Cancelled successfully");

    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderHistoryResponse>> orderHistory(Authentication authentication){

        String email= authentication.getName();
        List<OrderHistoryResponse> orderHistoryResponses = orderService.orderHistory(email);
        return ResponseEntity.ok(orderHistoryResponses);

    }
}
