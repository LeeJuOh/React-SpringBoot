package com.example.gccoffeeclone.product.controller;

import com.example.gccoffeeclone.product.service.ProductService;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String productListPage(Model model) {
        var products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("/products/{productId}")
    public String productDetailPage(@PathVariable("productId") UUID productId, Model model) {
        var product = productService.getProduct(productId);
        model.addAttribute("product", product);
        return "product-detail";
    }

    @PostMapping("/products/{productId}/delete")
    public String deleteProduct(@PathVariable("productId") UUID productId) {
        productService.deleteById(productId);
        return "redirect:/products";
    }

    @PostMapping("/products/{productId}/update")
    public String updateProduct(
        @PathVariable("productId") UUID productId,
        @Valid ProductDto productDto) {
        productService.updateProduct(productId, productDto);
        return "redirect:/products";
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
