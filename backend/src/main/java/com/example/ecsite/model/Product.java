package com.example.ecsite.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.*;

@Entity
@Table(name = "products") // DBのテーブル名
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動採番
    private Long id;
    @Column(nullable = false)
    private String name; // 商品名
    private String description; // 商品説明
    @Column(nullable = false)
    private Double price; // 価格
    private String imageUrl; // 画像URL
    @Column(nullable = false)
    private Integer stock = 0; // 在庫数（初期値0)
}
