package com.Ecommerce.service;

import com.Ecommerce.dto.CartItemResponses;
import com.Ecommerce.dto.CartResponses;
import com.Ecommerce.entity.Cart;
import com.Ecommerce.entity.CartItem;
import com.Ecommerce.entity.Product;
import com.Ecommerce.entity.User;
import com.Ecommerce.exception.CartNotFoundException;
import com.Ecommerce.exception.InsufficientStockException;
import com.Ecommerce.exception.ProductNotFoundException;
import com.Ecommerce.repository.CartRepository;
import com.Ecommerce.repository.ProductRepository;
import com.Ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;


    public void addToCart(String email,Long productId,int quantity){
        User user = userRepository.findByEmail(email).orElseThrow();
        Cart cart=cartRepository.findByUser(user).orElseGet(()->{
            Cart newCart=new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });


        Product product= productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException("product is not found"));
        Optional<CartItem> existingItems= cart.getItems()
                .stream()
                .filter(item ->
                        item.getProduct()
                                .getId()
                                .equals(productId)).findFirst();

       int availableQuantity= product.getQuantity();
       int existingQuantity= existingItems.map(CartItem::getQuantity).orElse(0);
       if(existingQuantity+quantity>availableQuantity){
           throw new InsufficientStockException("Only "+availableQuantity+" items available in stock");
       }
        if(existingItems.isPresent()){
            CartItem item = existingItems.get();
            item.setQuantity(item.getQuantity()+quantity);
        }else{
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setCart(cart);
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice());
            cart.getItems().add(newItem);
        }
        cartRepository.save(cart);

    }
    public CartResponses getCart(String email) {

        User user = userRepository.findByEmail(email).orElseThrow();
        Cart cart= cartRepository.findByUser(user).orElseThrow(()->new CartNotFoundException("Cart not foud"));
        List<CartItemResponses> cartItemResponses=cart.getItems().stream().map(item ->
                new CartItemResponses(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getPrice()*item.getQuantity()


        )).toList();

        double total = cartItemResponses.stream().mapToDouble(CartItemResponses::subtotal).sum();
        return new CartResponses( cartItemResponses,total);

    }

}
