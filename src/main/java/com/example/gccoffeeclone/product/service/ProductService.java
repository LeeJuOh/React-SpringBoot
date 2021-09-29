package com.example.gccoffeeclone.product.service;

import com.example.gccoffeeclone.product.controller.ProductDto;
import com.example.gccoffeeclone.product.model.Category;
import java.util.List;

public interface ProductService {

    List<ProductDto> getProductsByCategory(Category category);

    List<ProductDto> getAllProducts();

    ProductDto createProduct(ProductDto productDto);

}
