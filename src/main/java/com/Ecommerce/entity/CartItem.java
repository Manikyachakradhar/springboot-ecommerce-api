package com.Ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    private double price;
    @ManyToOne
    private Product product;

    @ManyToOne
    private Cart cart;

}
