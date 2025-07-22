package com.eg.mcp.models;

public record OrderItem(
	    String itemName,
	    int qty,
	    float rate,
	    float cost,
	    String image,
	    String currency
	) {}
