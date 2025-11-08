package com.example.ecsite.service;

import org.springframework.stereotype.Service;
import com.example.ecsite.repository.ProductRepository;
import com.example.ecsite.model.Product;

import java.util.List;

@Service
public class ProductService {

    private  final ProductRepository productRepository;

    // コンストラクタインジェクション
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 商品一覧取得
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 商品追加
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }
}
