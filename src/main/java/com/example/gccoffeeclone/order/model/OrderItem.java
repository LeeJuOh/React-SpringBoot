package com.example.gccoffeeclone.order.model;

import com.example.gccoffeeclone.product.model.Category;
import java.util.Objects;
import java.util.UUID;

public record OrderItem(UUID productId,
                        Category category, long price,
                        int quantity) {

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (OrderItem) obj;
        return Objects.equals(this.productId, that.productId) &&
            Objects.equals(this.category, that.category) &&
            this.price == that.price &&
            this.quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, category, price, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem[" +
            "productId=" + productId + ", " +
            "category=" + category + ", " +
            "price=" + price + ", " +
            "quantity=" + quantity + ']';
    }


}
