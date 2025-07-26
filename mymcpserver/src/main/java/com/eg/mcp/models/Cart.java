package com.eg.mcp.models;

import java.util.List;

public record Cart(List<OrderItem> orderItems, float total, String currency) {
    public Cart {
        // Recalculate total
        float computedTotal = 0f;
        if (orderItems != null) {
            for (OrderItem item : orderItems) {
                computedTotal += item.cost();
            }
        }
        total = computedTotal;
    }
    
}
