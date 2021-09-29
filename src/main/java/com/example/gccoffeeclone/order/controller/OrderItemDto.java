package com.example.gccoffeeclone.order.controller;

import com.example.gccoffeeclone.product.model.Category;
import java.util.UUID;

public class OrderItemDto {
    private UUID productId;
    private Category category;
    private long price;
    private int quantity;

    public UUID getProductId() {
        return productId;
    }

    public Category getCategory() {
        return category;
    }

    public long getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
