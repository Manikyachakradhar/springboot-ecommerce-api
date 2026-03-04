package com.Ecommerce.controller;

import com.Ecommerce.entity.Order;
import com.Ecommerce.entity.OrderStatus;
import com.Ecommerce.repository.OrderRepository;
import com.Ecommerce.service.AdminOrderService;
import com.Ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {
    private final AdminOrderService adminOrderService;


    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
         String changedStatus = adminOrderService.updateOrderStatus(id, status);
         return ResponseEntity.ok(changedStatus);

        }

}
