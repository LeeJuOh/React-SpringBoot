package com.example.gccoffeeclone.product.service;

import com.example.gccoffeeclone.product.controller.ProductDto;
import com.example.gccoffeeclone.product.model.Category;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ProductDto> getProductsByCategory(Category category);

    List<ProductDto> getAllProducts();

    ProductDto createProduct(ProductDto productDto);

    ProductDto getProduct(UUID productId);

    void deleteById(UUID productId);

    void updateProduct(UUID productId, ProductDto productDto);
}
