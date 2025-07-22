package com.eg.mcp.models;

import java.time.LocalDateTime;
import java.util.List;

public record Order(String orderNumber, LocalDateTime orderDateTime, List<OrderItem> orderItems, 
		float total, String currency) {
    public Order {
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
