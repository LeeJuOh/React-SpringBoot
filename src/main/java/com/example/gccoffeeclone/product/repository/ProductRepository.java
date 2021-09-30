package com.example.gccoffeeclone.product.repository;

import com.example.gccoffeeclone.product.model.Category;
import com.example.gccoffeeclone.product.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    List<Product> findAll();

    void insert(Product product);

    void update(Product product);

    Optional<Product> findById(UUID productId);

    Optional<Product> findByName(String productName);

    List<Product> findByCategory(Category category);

    void deleteById(UUID productId);

}
