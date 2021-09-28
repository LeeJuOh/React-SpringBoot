package com.example.gccoffeeclone.product.controller;

import com.example.gccoffeeclone.product.service.ProductService;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String productsPage(Model model) {
        var products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("new-product")
    public String newProductPage() {
        return "new-product";
    }

    @PostMapping("/products")
    public String newProduct(@Valid ProductDto productDto) {
        productService.createProduct(productDto);
        return "redirect:/products";
    }

}
