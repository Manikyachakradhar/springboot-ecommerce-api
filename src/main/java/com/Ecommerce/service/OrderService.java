package com.Ecommerce.service;

import com.Ecommerce.dto.OrderHistoryResponse;
import com.Ecommerce.dto.OrderItemResponse;
import com.Ecommerce.entity.*;
import com.Ecommerce.repository.CartRepository;
import com.Ecommerce.repository.OrderRepository;
import com.Ecommerce.repository.ProductRepository;
import com.Ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void placeOrder(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->
                new RuntimeException("User not found"));

      Cart cart=  cartRepository.findByUser(user).orElseThrow(()->
              new RuntimeException("Cart not found"));

      if(cart.getItems().isEmpty()){
          throw new RuntimeException("Cart is empty");
      }
      Order order= new Order();
      order.setUser(user);
      order.setStatus(OrderStatus.CREATED);
      order.setCreatedAt(LocalDateTime.now());
        double total=0;
      for(CartItem cartItem :cart.getItems()){

          Product product= cartItem.getProduct();
          if(product.getQuantity()<cartItem.getQuantity()){
              throw new RuntimeException("Not enough stock for "+product.getName());
          }
          product.setQuantity(product.getQuantity()-cartItem.getQuantity());

          productRepository.save(product);

          OrderItem item= new OrderItem();
          item.setOrder(order);
          item.setProduct(cartItem.getProduct());
          item.setQuantity(cartItem.getQuantity());
          item.setPriceAtPurchase(cartItem.getPrice());
          total+=cartItem.getPrice()*cartItem.getQuantity();
          order.getItems().add(item);
      }
      order.setTotalAmount(total);
      orderRepository.save(order);

      cart.getItems().clear();
      cartRepository.save(cart);
    }

    @Transactional
    public void cancelOrder(Long id, String email) {
        Order order= orderRepository.findById(id).orElseThrow(()->new RuntimeException("order not found"));

        if(!order.getUser().getEmail().equals(email)){
            throw new RuntimeException("You are not allowed to cancel this order");


        }

        if(order.getStatus()!= OrderStatus.CREATED){
            throw new RuntimeException("Order cannot be cancelled");
        }

        for(OrderItem item: order.getItems()){
            Product product= item.getProduct();
            product.setQuantity(product.getQuantity()+item.getQuantity());

            productRepository.save(product);
        }
        order.setStatus(OrderStatus.CANCELLED);
    }

    public List<OrderHistoryResponse> orderHistory(String email) {

        return orderRepository.findByUserEmailOrderByCreatedAtDesc(email).stream()
                .map(order -> new OrderHistoryResponse(
                        order.getId(),
                        order.getCreatedAt(),
                        order.getStatus(),
                        order.getTotalAmount(),
                        order.getItems().stream()
                                .map(item -> new OrderItemResponse(
                                        item.getProduct().getName(),
                                        item.getQuantity(),
                                        item.getPriceAtPurchase()
                                ))
                                .toList()
                ))
                .toList();

    }
}
