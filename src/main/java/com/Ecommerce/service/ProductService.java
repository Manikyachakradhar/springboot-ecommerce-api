package com.Ecommerce.service;

import com.Ecommerce.entity.Product;
import com.Ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;


    public Product addProduct(Product product){

        return repository.save(product);
    }

    public List<Product> getAllProducts(){

        return repository.findAll();
    }

    public Product getProductById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new
                RuntimeException("Product not found"));
    }

    public void deleteProduct(Long id){
        repository.deleteById(id);
    }
}
