package com.example.ecsite.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.ecsite.repository.ProductRepository;
import com.example.ecsite.model.Product;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 商品一覧を取得（全ユーザが利用可能）
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * 商品詳細を取得（存在しない場合は例外スロー）
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("指定された商品は見つかりません（id=" + id +"）"));
    }

    /**
     * 商品を登録（管理者用）
     */
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * 商品を更新（管理者用）
     */
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("更新対象の商品が存在しません (id=" + id + ")"));

        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setStock(updatedProduct.getStock());

        return productRepository.save(existing);
    }

    /**
     * 商品を削除（管理者用）
     */
    public void deleteProduct(Long id) {
        Product existing = productRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("削除対象の商品が存在しせん (id=" + id + ")"));

        productRepository.delete(existing);
    }

}
