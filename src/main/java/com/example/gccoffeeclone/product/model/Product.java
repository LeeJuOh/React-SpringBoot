package com.example.gccoffeeclone.product.model;

import static com.example.gccoffeeclone.utils.MapperUtils.getModelMapper;

import com.example.gccoffeeclone.product.controller.ProductDto;
import java.time.LocalDateTime;
import java.util.UUID;

public class Product {

    private final UUID productId;
    private String productName;
    private Category category;
    private long price;
    private String description;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(UUID productId, String productName, Category category, long price) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Product(UUID productId, String productName, Category category, long price,
        String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ProductDto toDto() {
        return getModelMapper().map(this, ProductDto.class);
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Category getCategory() {
        return category;
    }

    public long getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setProductName(String productName) {
        this.productName = productName;
        setUpdatedAt();
    }

    public void setCategory(Category category) {
        this.category = category;
        setUpdatedAt();
    }

    public void setPrice(long price) {
        this.price = price;
        setUpdatedAt();
    }

    public void setDescription(String description) {
        this.description = description;
        setUpdatedAt();
    }

    private void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
