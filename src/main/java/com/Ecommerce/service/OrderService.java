package com.Ecommerce.service;

import com.Ecommerce.dto.OrderHistoryResponse;
import com.Ecommerce.dto.OrderItemResponse;
import com.Ecommerce.entity.*;
import com.Ecommerce.exception.CartNotFoundException;
import com.Ecommerce.exception.EmptyCartException;
import com.Ecommerce.exception.InsufficientStockException;
import com.Ecommerce.exception.UserNotFoundException;
import com.Ecommerce.repository.CartRepository;
import com.Ecommerce.repository.OrderRepository;
import com.Ecommerce.repository.ProductRepository;
import com.Ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger log =
            LoggerFactory.getLogger(OrderService.class);

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void placeOrder(String email){

        log.info("placeOrder() method called");
        User user = userRepository.findByEmail(email).orElseThrow(()->{
            log.warn("placeOrder() User Not found for Email: {}",email);
              return  new UserNotFoundException("User not found");
    });

      Cart cart=  cartRepository.findByUser(user).orElseThrow(()-> {
          log.warn("placeOrder() Cart Not found");
          return new CartNotFoundException("Cart not found");
      });

      if(cart.getItems().isEmpty()){
          log.warn("placeOrder() Cart is empty");
          throw new EmptyCartException("Cart is empty");
      }
      Order order= new Order();
      order.setUser(user);
      order.setStatus(OrderStatus.CREATED);
      order.setCreatedAt(LocalDateTime.now());
        double total=0;
      for(CartItem cartItem :cart.getItems()){

          Product product= cartItem.getProduct();
          if(product.getQuantity()<cartItem.getQuantity()){
              log.warn("placeOrder() Not enough stock for {}",product.getName());

              throw new InsufficientStockException("Not enough stock for "+product.getName());
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
        log.info("placeOrder() Order placed successfully for user: {}, totalAmount: {}",
                email,
                total
        );
      cart.getItems().clear();
      cartRepository.save(cart);
    }

    @Transactional
    public void cancelOrder(Long id, String email) {

        log.info("cancelOrder() called - orderId: {}, user: {}", id, email);
        Order order= orderRepository.findById(id).orElseThrow(()->new RuntimeException("order not found"));

        if(!order.getUser().getEmail().equals(email)){
            log.warn("cancelOrder() Unauthorized cancel attempt for orderId: {} by user: {}",
                    id,
                    email);
            throw new RuntimeException("You are not allowed to cancel this order");


        }

        if(order.getStatus()!= OrderStatus.CREATED){
            log.warn("cancelOrder() Order is Created it cannot cancelled at this stage orderId: {} ",id);
            throw new RuntimeException("Order cannot be cancelled");
        }

        for(OrderItem item: order.getItems()){
            Product product= item.getProduct();
            product.setQuantity(product.getQuantity()+item.getQuantity());

            productRepository.save(product);
        }
        order.setStatus(OrderStatus.CANCELLED);
        log.info("cancelOrder() successful - orderId: {}", id);
    }

    public List<OrderHistoryResponse> orderHistory(String email) {
        log.info("orderHistory() called - user: {}", email);

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
