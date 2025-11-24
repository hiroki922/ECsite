package com.example.ecsite.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.ecsite.model.Product;
import com.example.ecsite.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 商品一覧を取得
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // 商品詳細を取得
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
}
