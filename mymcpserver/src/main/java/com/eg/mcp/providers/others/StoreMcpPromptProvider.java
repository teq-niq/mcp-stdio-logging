package com.eg.mcp.providers.others;

import java.util.List;
import java.util.random.RandomGenerator;

import org.springframework.stereotype.Service;

import com.eg.mcp.utils.CountryPromptDatabase;
import com.logaritex.mcp.annotation.McpArg;
import com.logaritex.mcp.annotation.McpPrompt;

import io.modelcontextprotocol.spec.McpSchema.GetPromptResult;
import io.modelcontextprotocol.spec.McpSchema.PromptMessage;
import io.modelcontextprotocol.spec.McpSchema.Role;
import io.modelcontextprotocol.spec.McpSchema.TextContent;

@Service
public class StoreMcpPromptProvider {

	private final CountryPromptDatabase countryPromptDatabase;

	private static final RandomGenerator RNG = RandomGenerator.of("L64X128MixRandom");

	public StoreMcpPromptProvider(CountryPromptDatabase countryPromptDatabase) {
		super();
		this.countryPromptDatabase = countryPromptDatabase;
	}

	@McpPrompt(name = "brandz-greeting", description = "Greets the user visiting Brand Z Sports Store")
	public GetPromptResult brandzGreetingPrompt(
			@McpArg(name = "name", description = "The name of the user", required = true) String name) {

		String message = "Hi " + name + "! 👋 Welcome to Brand Z Sports Store.";


		return new GetPromptResult("Brand Z Greeting",
				List.of(new PromptMessage(Role.ASSISTANT, new TextContent(message))));
	}
	
	
	@McpPrompt(name = "country-status", description = "Gives information on how many stores are there in the input country name")
	public GetPromptResult countryStoreStatus(
			@McpArg(name = "country-name",  description = "The name of the country", required = true ) String countryName) {
		String message;
		if (countryName == null || countryName.isBlank()) {
			message = "Enter a country";
		}
		else {
			if (countryPromptDatabase.getCountries().contains(countryName)) {
				int randomInt = RNG.nextInt(10); // [0, 100)
				message = countryName + " has " + randomInt + " stores";
			}
			else {
				message = "Enter a valid country";
			}
		}


		return new GetPromptResult("Number of stores in the country",
				List.of(new PromptMessage(Role.ASSISTANT, new TextContent(message))));
	}
	
	/*
	 * inspired from the prompt example mentioned here- https://github.com/modelcontextprotocol/python-sdk
	 */
	
	@McpPrompt(name = "generate_greeting_prompt", description = "Generate a greeting prompt")
	public PromptMessage generateGreetingPrompt(
			@McpArg(name = "name", description = "The name of the person to greet", required = true) String name,
	        @McpArg(name = "greeting-style", description = "The style of the greeting: formal, casual, or friendly", required = true) String style) {
	    String prompt;
	    switch (style != null ? style : "friendly") {
	        case "formal":
	            prompt = "Please write a formal, professional greeting";
	            break;
	        case "casual":
	            prompt = "Please write a casual, relaxed greeting";
	            break;
	        case "friendly":
	        default:
	            prompt = "Please write a warm, friendly greeting";
	            break;
	    }

	    return new PromptMessage(Role.USER, new TextContent(prompt + " for someone named " + name + "."));
	}

}
