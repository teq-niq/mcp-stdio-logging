package com.eg.mcp.providers.tools;

import java.io.IOException;

import com.eg.mcp.models.Cart;
import com.eg.mcp.models.Order;
import com.eg.mcp.models.SportsItem;
import com.eg.mcp.models.State;
import com.eg.mcp.utils.MarkdownMapper;
import com.eg.mcp.utils.McpLoggingProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/*
 * Initially was planning of making this a class for Resources and not Tools.
 * But later made it a Tools class.
 */

@Service
public class StoreResourceNowToolsProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(StoreResourceNowToolsProvider.class);

	private static final String RETURNS = "Returns the URL of the image of the ";

	@Value("${mine.mcpurl:false}")
	private boolean mineMcpurl = false;

	private final ObjectMapper jsonMapper;

	private final XmlMapper xmlMapper;

	private final MarkdownMapper markdownMapper;

	private final State state;

	private final McpLoggingProperties mcpLoggingProperties;

	public StoreResourceNowToolsProvider(ObjectMapper jsonMapper, State state, McpLoggingProperties mcpLoggingProperties) {
		super();
		this.jsonMapper = jsonMapper;
		this.mcpLoggingProperties = mcpLoggingProperties;
		this.state = state;
		this.xmlMapper = XmlMapper.builder().build();
		this.markdownMapper = new MarkdownMapper();
	}

	@Tool(name = "get_tennis_ball_image", description = RETURNS + "tennis ball")
	public String getTennisBallImage() {
		return mduri(SportsItem.TENNIS_BALL);
	}

	@Tool(name = "get_tennis_net_image", description = RETURNS + "tennis net")
	public String getTennisNetImage() {
		return mduri(SportsItem.TENNIS_NET);
	}

	@Tool(name = "get_tennis_raquet_image", description = RETURNS + "tennis raquet")
	public String getTennisRaquetImage() {
		return mduri(SportsItem.TENNIS_RAQUET);
	}

	@Tool(name = "get_football_image", description = RETURNS + "football")
	public String getFootballImage() {
		return mduri(SportsItem.FOOTBALL);
	}

	@Tool(name = "get_cart_content_in_json", description = "get cart content formatted in json")
	public String cartjson() throws IOException {
		Cart cart = state.toCart();
		return jsonMapper.writeValueAsString(cart);
	}

	@Tool(name = "get_cart_content_in_xml", description = "get cart content formatted in xml")
	public String cartxml() throws IOException {
		Cart cart = state.toCart();
		return xmlMapper.writeValueAsString(cart);
	}

	@Tool(name = "get_cart_content_in_markdown", description = "get cart content formatted in markdown")
	public String cartmd() {
		Cart cart = state.toCart();
		return markdownMapper.writeValueAsString(cart);
	}

	@Tool(name = "get_last_order_content_in_json", description = "get last order content formatted in json")
	public String lastorderjson() throws IOException {
		Order lastOrder = state.getLastOrder();
		return jsonMapper.writeValueAsString(lastOrder);
	}

	@Tool(name = "get_last_order_content_in_xml", description = "get last order content formatted in xml")
	public String lastorderxml() throws IOException {
		Order lastOrder = state.getLastOrder();
		return xmlMapper.writeValueAsString(lastOrder);
	}

	@Tool(name = "get_last_order_content_in_markdown", description = "get last order content formatted in markdown")
	public String lastordermd() {
		logger.debug("Entered lastordermd");
		Order lastOrder = state.getLastOrder();
		logger.debug("Got lastorder");
		String content = markdownMapper.writeValueAsString(lastOrder);
		logger.debug("Got content");
		logger.debug("Exiting lastordermd");
		return content;

	}

	private String mduri(SportsItem sportsItem) {
		return mineMcpurl ? "mcp://resource/" + sportsItem.touri()
				: mcpLoggingProperties.imagesServerUrl() + sportsItem.touri();
	}

}
