package com.Ecommerce.controller;

import com.Ecommerce.entity.Product;
import com.Ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping
    public Product addProduct(@Valid @RequestBody Product product){

        return service.addProduct(product);
    }

    @GetMapping
    public Page<Product> getAllProducts(@PageableDefault(size = 10,sort = "price") Pageable pageable)
    {
        return service.getAllProducts(pageable);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id){
        return service.getProductById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        service.deleteProduct(id);
    }

}
