package com.eg.mcp.providers.tools;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.eg.mcp.models.SportsItem;
import com.eg.mcp.models.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class StoreToolsProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(StoreToolsProvider.class);

	private final State state;
	
	public StoreToolsProvider(State state) {
		super();
		this.state = state;
	}

	@Tool(name = "get_store_speciality", description = "Describe whats special or unique about Brand Z sports store")
	public String getStoreSpeciality() {
		return "Brand Z Sports store only sells sports equipments or sporting goods that it manufactures";
	}

	@Tool(name = "get_items", description = "Get a list of sports equipments or sporting goods that Brand Z Sports store sells.")
	public List<String> getSportsEquipments() {
		List<String> labels = Arrays.stream(SportsItem.values())
				.map(SportsItem::label)
				.collect(Collectors.toList());
		return labels;
	}

	@Tool(name = "get_selling_price_currency", description = "Get the currency for the various items selling price. Brand Z Sports store's selling price for an item would also be the same as the cost price of the item from a vendor's point of view.")
	public String getSportsEquipmentSellingPriceCurrency() {
		return state.getCurrency();
	}

	@Tool(name = "get_selling_price_of_item", description = "Get selling price of items that Brand Z Sports store sells. Is also the item's cost from buyers point of view")
	public float getSellingPriceOfItem(String itemName) {
		SportsItem sportsItem = SportsItem.labelOf(itemName);
		Float price = null;
		if (sportsItem != null) {
			price = sportsItem.price();
		}

		if (price == null) {
			throw new RuntimeException("Brand Z Sorts store does not stock " + itemName);
		}
		return price;
	}

	@Tool(name = "get_details_of_item", description = "Get details of items that Brand Z Sports store sells.")
	public String getDetailOfItem(String itemName) {

		SportsItem sportsItem = SportsItem.labelOf(itemName);
		String detail = null;
		if (sportsItem != null) {
			detail = sportsItem.detail();
		}
		if (detail == null) {
			throw new RuntimeException("Brand Z Sports store does not stock " + itemName);
		}
		return detail;
	}

	@Tool(name = "add_to_cart_item", description = "Add to cart item by specifying item name and its quantity")
	public void addToCart(String itemName, int quantity) {
		logger.debug("addingTocart " + itemName + " with quantity=" + quantity);
		state.addToCart(itemName, quantity);
	}

	@Tool(name = "change_quantity_of_cart_item", description = "Change the quantity of the specified cart item to specified quantity")
	public void changeQuantityOfCartItem(String itemName, int quantity) {
		state.changeQuantityOfCartItem(itemName, quantity);
	}

	@Tool(name = "remove_item_from_cart_completely", description = "Remove item  from cart item by specifying item name. Can think that its quantity was reduced to 0. Item will no longer occur in the cart.")
	public void removeFromCart(String itemName) {
		state.removeFromCart(itemName);
	}

	@Tool(name = "checkout_and_pay", description = "Check out items in the cart. Payment is automatic. After checkout order is available as last order.")
	public void checkout() {
		state.checkout();
	}

}
