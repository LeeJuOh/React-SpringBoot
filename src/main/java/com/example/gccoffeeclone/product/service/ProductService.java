package com.example.gccoffeeclone.product.service;

import com.example.gccoffeeclone.product.controller.ProductDto;
import java.util.List;

public interface ProductService {

    List<ProductDto> getProductsByCategory(ProductDto productDto);

    List<ProductDto> getAllProducts();

    ProductDto createProduct(ProductDto productDto);

}
