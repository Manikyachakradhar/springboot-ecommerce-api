package com.Ecommerce.repository;

import com.Ecommerce.entity.Cart;
import com.Ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
   Optional<Cart>  findByUser(User user);

}
