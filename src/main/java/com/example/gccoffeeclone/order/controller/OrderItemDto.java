package com.example.gccoffeeclone.order.controller;

import com.example.gccoffeeclone.product.model.Category;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class OrderItemDto {
    @NotNull
    private UUID productId;
    @NotNull
    private Category category;
    @Positive(message = "주문 금액은 0원보다 큰 금액이어야 합니다.")
    private long price;
    @Positive(message = "주문 수량은 0보다 큰 값이어야 합니다.")
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
