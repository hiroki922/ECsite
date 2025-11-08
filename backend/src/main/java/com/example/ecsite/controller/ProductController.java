package com.example.ecsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.ecsite.model.Product;
import com.example.ecsite.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 商品一覧を取得
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // 商品追加
    @PostMapping
    public  Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }
}
