package com.example.ecsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ecsite.model.Product;

// Product エンティティとDB（products table)を繋ぐリポジトリ
public interface ProductRepository extends JpaRepository<Product, Long> {
}
