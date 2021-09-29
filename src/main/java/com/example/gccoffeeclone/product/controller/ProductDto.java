package com.example.gccoffeeclone.product.controller;

import static com.example.gccoffeeclone.utils.MapperUtils.getModelMapper;

import com.example.gccoffeeclone.product.model.Category;
import com.example.gccoffeeclone.product.model.Product;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ProductDto {

    private UUID productId;
    @NotBlank
    private String productName;
    @NotNull
    private Category category;
    @Positive(message = "상품 가격은 0원 이상이어야 합니다")
    private long price;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductDto from(Product product) {
        return getModelMapper().map(product, ProductDto.class);
    }

    public Product toEntity() {
        return (description == null) ?
            new Product(UUID.randomUUID(), productName, category, price) :
            new Product(UUID.randomUUID(), productName, category, price, description,
                LocalDateTime.now(), LocalDateTime.now());

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

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
