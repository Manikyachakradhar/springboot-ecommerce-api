package com.Ecommerce.service;

import com.Ecommerce.entity.Product;
import com.Ecommerce.exception.ProductNotFoundException;
import com.Ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private static final Logger log =
            LoggerFactory.getLogger(ProductService.class);


    public Product addProduct(Product product){
        log.info("addProduct() called - Adding product: {}", product.getName());
        return repository.save(product);
    }

    public Page<Product> getAllProducts(Pageable pageable){

        log.info("Fetching products with pagination: {}", pageable);
        return repository.findAll(pageable);
    }

    public Product getProductById(Long id){
        log.info("getProductById() called - productId: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found for id: {}", id);
                    return new ProductNotFoundException("Product not found");
                });
    }

    public void deleteProduct(Long id){
        log.info("deleteProduct() called - productId: {}", id);
    try {
       repository.findById(id)
                .orElseThrow(() -> {
                    return new ProductNotFoundException("Product not found");
                });
        repository.deleteById(id);
        log.info("deleteProduct() called - Deleting productId: {}", id);
    }
    catch (Exception e){
        log.error("deleteProduct() failed for productId: {}",e.getMessage());
        throw (e);
    }

    }
}
