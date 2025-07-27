package com.eg.mcp.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.eg.mcp.utils.McpLoggingProperties;

@Component
public class State {

	private final Map<String, Integer> cart = new HashMap<>();
	private static final String currency = "USD";

	private final McpLoggingProperties mcpLoggingProperties;

	public State(McpLoggingProperties mcpLoggingProperties) {
		this.mcpLoggingProperties = mcpLoggingProperties;
	}

	public Order toOrder() {
		List<OrderItem> items = toOrderItems(currency);
		return new Order(
				"ORD-" + System.currentTimeMillis(),  // simple order number
				LocalDateTime.now(),
				items,
				0f, // total will be recomputed in Order constructor
				currency
		);
	}

	public Cart toCart() {
		List<OrderItem> items = toOrderItems(currency);
		return new Cart(
				items,
				0f, // total will be recomputed in Order constructor
				currency
		);
	}

	private List<OrderItem> toOrderItems(String currency) {
		List<OrderItem> items = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : cart.entrySet()) {
			String normalizedLabel = entry.getKey();
			int quantity = entry.getValue();
			SportsItem item = SportsItem.internalLookupByNormalizedLabel(normalizedLabel);

			if (item != null) {
				float rate = item.price();
				float cost = rate * quantity;
				items.add(new OrderItem(item.label(), quantity, rate, cost, mcpLoggingProperties.imagesServerUrl() + item.touri(), currency));
			}
		}
		return items;
	}


	public void addToCart(String itemName, int quantity) {
		SportsItem sportsItem = SportsItem.labelOf(itemName);
		if (sportsItem != null) {
			cart.merge(sportsItem.normalizedLabel(), quantity, Integer::sum);
		}
		else {
			throw new RuntimeException("Brand Z Sports store does not stock " + itemName);
		}

	}

	public Order getLastOrder() {
		Order lastOrder;
		if (!orderHistory.isEmpty()) {
			lastOrder = orderHistory.getLast();
		}
		else {
			throw new RuntimeException("There is no order history yet");
		}
		return lastOrder;
	}

	public void changeQuantityOfCartItem(String itemName, int quantity) {
		SportsItem sportsItem = SportsItem.labelOf(itemName);
		if (sportsItem != null) {
			cart.put(sportsItem.normalizedLabel(), quantity);
		}
		else {
			throw new RuntimeException("Brand Z Sports store does not stock " + itemName);
		}

	}

	public void removeFromCart(String itemName) {
		SportsItem sportsItem = SportsItem.labelOf(itemName);
		if (sportsItem != null) {
			cart.remove(sportsItem.normalizedLabel());
		}
		else {
			throw new RuntimeException("Brand Z Sports store does not stock " + itemName);
		}

	}

	public Order checkout() {
		Order order = toOrder();
		this.orderHistory.add(order);
		cart.clear();
		return order;
	}

	public String getCurrency() {
		return currency;
	}

	private List<Order> orderHistory = new ArrayList<>();
}
