package com.example.gccoffeeclone.product.controller.api;

import com.example.gccoffeeclone.product.controller.ProductDto;
import com.example.gccoffeeclone.product.model.Category;
import com.example.gccoffeeclone.product.service.ProductService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/v1/products")
    public ResponseEntity<List<ProductDto>> productList(@RequestParam Optional<Category> category) {
        var products = category
            .map(productService::getProductsByCategory)
            .orElse(productService.getAllProducts());
        return ResponseEntity.ok(products);
    }
}
