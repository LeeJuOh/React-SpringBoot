package com.example.gccoffeeclone.order.model;

import com.example.gccoffeeclone.product.model.Category;
import java.util.UUID;

public record OrderItem(UUID productId, Category category, long price, int quantity) {

}
