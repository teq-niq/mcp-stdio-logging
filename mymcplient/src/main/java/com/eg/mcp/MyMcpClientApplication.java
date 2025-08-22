package com.eg.mcp;

import java.util.List;
import java.util.Map;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema.CompleteRequest;
import io.modelcontextprotocol.spec.McpSchema.CompleteResult;
import io.modelcontextprotocol.spec.McpSchema.GetPromptRequest;
import io.modelcontextprotocol.spec.McpSchema.GetPromptResult;
import io.modelcontextprotocol.spec.McpSchema.ListPromptsResult;
import io.modelcontextprotocol.spec.McpSchema.ListResourcesResult;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;
import io.modelcontextprotocol.spec.McpSchema.PromptReference;
import io.modelcontextprotocol.spec.McpSchema.ReadResourceRequest;
import io.modelcontextprotocol.spec.McpSchema.ReadResourceResult;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyMcpClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyMcpClientApplication.class, args);
	}


	@Bean
	public CommandLineRunner demo(List<McpSyncClient> mcpSyncClients, ConfigurableApplicationContext context) {
		return args -> {
			if (mcpSyncClients.size() == 1) {
				McpSyncClient client = mcpSyncClients.getFirst();
				ListToolsResult toolsList = client.listTools();
				System.out.println("Available Tools = " + toolsList);
				toolsList.tools().forEach(tool -> {
					System.out.println("Tool: " + tool.name() + ", description: " + tool.description() + ", schema: "
							+ tool.inputSchema());
				});

				ListResourcesResult listResources = client.listResources();
				listResources.resources().forEach(resource -> {
					System.out.println("Resource: " + resource.name() + ", " + resource.uri() + ", " + resource.description());
				});
				ReadResourceResult resource = client.readResource(new ReadResourceRequest("mcp://brandz/store/rules"));
				System.out.println("result = " + resource);

				ListPromptsResult listPrompts = client.listPrompts();
				listPrompts.prompts().forEach(prompt -> {
					System.out.println("Prompt: " + prompt.name() + ", " + prompt.description());
				});
				GetPromptResult prompt = client.getPrompt(
						new GetPromptRequest("brandz-greeting", Map.of("name", "Doe")));
				System.out.println("result = " + prompt);

				// Completions
				CompleteResult completion = client.completeCompletion(new CompleteRequest(new PromptReference("country-status"),
						new CompleteRequest.CompleteArgument("country-name", "a")));
				System.out.println("Completion = " + completion);
				
				GetPromptResult geneteratedPrompt = client.getPrompt(new GetPromptRequest("generate_greeting_prompt", Map.of("name", "Doe", "greeting-style","friendly")));
				System.out.println("generatedPrompt = " + geneteratedPrompt);
				context.close();
			}

		};
	}

}