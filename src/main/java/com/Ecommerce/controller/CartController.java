package com.Ecommerce.controller;

import com.Ecommerce.dto.AddToCartRequest;
import com.Ecommerce.dto.CartResponses;
import com.Ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {


    private final CartService cartService;
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest request ,Authentication authentication){
        String email= authentication.getName();
        cartService.addToCart(
                email,
                request.productId(),
                request.quantity());
        return ResponseEntity.ok("Product added to cart succesfully");
    }

    @GetMapping
    public ResponseEntity<CartResponses> getCart(Authentication authentication){

        String email= authentication.getName();
        CartResponses cartResponses= cartService.getCart(email);
        return  ResponseEntity.ok(cartResponses);
    }
}
