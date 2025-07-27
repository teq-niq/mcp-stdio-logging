# The Sample MCP Artifacts

| Artifact Type | Description                            |
|---------------|----------------------------------------|
| Tool          | Executable unit or external function   |
| Resource      | External data or document reference    |
| Prompt        | Structured input to the model          |
| Completion    | Output or response from the model      |


## Tools
Found here:  
mymcpserver/src/main/java/com/eg/mcp/providers/tools/StoreToolsProvider.java   

| Tool name     | Description                            |
|---------------|----------------------------------------|
| get_store_speciality          | Describe whats special or unique about Brand Z sports store   |
| get_items      | Get a list of sports equipments or sporting goods that Brand Z Sports store sells.    |
| get_selling_price_currency        | Get the currency for the various items selling price. Brand Z Sports store's selling price for an item would also be the same as the cost price of the item from a vendor's point of view.          |
| get_selling_price_of_item    | Get selling price of items that Brand Z Sports store sells. Is also the item's cost from buyers point of view      |
| get_details_of_item        | Get details of items that Brand Z Sports store sells.          |
| add_to_cart_item    | Add to cart item by specifying item name and its quantity      |
| change_quantity_of_cart_item        | Change the quantity of the specified cart item to specified quantity          |
| remove_item_from_cart_completely    | Remove item  from cart item by specifying item name. Can think that its quantity was reduced to 0. Item will no longer occur in the cart.      |
| checkout_and_pay        | Check out items in the cart. Payment is automatic. After checkout order is available as last order.          |


## Tools that were initially planned as Resources   

Found here:  
mymcpserver/src/main/java/com/eg/mcp/providers/tools/StoreResourceNowToolsProvider.java

| Tool name     | Description                            |
|---------------|----------------------------------------|
| get_tennis_ball_image          | Returns the URL of the image of the tennis ball   |
| get_tennis_net_image      | Returns the URL of the image of the tennis net    |
| get_tennis_raquet_image          | Returns the URL of the image of the tennis raquet   |
| get_football_image      | Returns the URL of the image of the football    |
| get_cart_content_in_json        | get cart content formatted in json          |
| get_cart_content_in_xml        | get cart content formatted in xml          |
| get_cart_content_in_markdown        | get cart content formatted in markdown          |
| get_last_order_content_in_json        | get last order content formatted in json          |
| get_last_order_content_in_xml        | get last order content formatted in xml          |
| get_last_order_content_in_markdown        | get last order content formatted in markdown          |


## Resources

## Prompts

## Completions