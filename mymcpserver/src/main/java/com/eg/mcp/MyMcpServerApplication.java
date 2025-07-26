package com.eg.mcp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.eg.mcp.models.State;
import com.eg.mcp.providers.others.StoreMcpCompleteProvider;
import com.eg.mcp.providers.others.StoreMcpPromptProvider;
import com.eg.mcp.providers.others.StoreMcpResourceProvider;
import com.eg.mcp.providers.tools.StoreResourceNowToolsProvider;
import com.eg.mcp.providers.tools.StoreToolsProvider;
import com.eg.mcp.utils.McpLoggingProperties;
import com.logaritex.mcp.spring.SpringAiMcpAnnotationProvider;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpServerFeatures.SyncCompletionSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.SyncPromptSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.SyncResourceSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.TeeInputStream;
import org.apache.commons.io.output.TeeOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;

@SpringBootApplication
@EnableConfigurationProperties(McpLoggingProperties.class)
public class MyMcpServerApplication {
	private static Logger logger = LoggerFactory.getLogger(StoreToolsProvider.class);

	private FileOutputStream fos;

	private FileOutputStream fis;

	private FileOutputStream fcs;

	private final McpLoggingProperties mcpLoggingProperties;

	public MyMcpServerApplication(McpLoggingProperties mcpLoggingProperties) {
		this.mcpLoggingProperties = mcpLoggingProperties;
	}

	@PostConstruct
	private void open() throws FileNotFoundException {
		//just ensure path is there and accessible
		//follow better practices to avoid hardcoding
		File dir = new File(mcpLoggingProperties.path());

		PrintStream originalOutputStream = System.out;
		InputStream originalInputStream = System.in;

		fos = new FileOutputStream(new File(dir, "out.txt"));
		fis = new FileOutputStream(new File(dir, "in.txt"));
		fcs = new FileOutputStream(new File(dir, "combined.txt"));
		TeeOutputStream to = new TeeOutputStream(originalOutputStream, new TeeOutputStream(fos, fcs));
		PrintStream ps = new PrintStream(to);

		TeeInputStream ti = new TeeInputStream(originalInputStream, new TeeOutputStream(fis, fcs));
		System.setOut(ps);
		System.setIn(ti);

	}

	@Bean
	public State state() {
		return new State();
	}

	@PreDestroy
	private void close() {
		IOUtils.closeQuietly(fos);
		IOUtils.closeQuietly(fis);
		IOUtils.closeQuietly(fcs);
	}

	public static void main(String[] args) {
		SpringApplication.run(MyMcpServerApplication.class, args);
	}


	@Bean
	public ToolCallbackProvider brandZTools(StoreToolsProvider storeToolsProvider, StoreResourceNowToolsProvider storeResourceNowToolsProvider) {
		return MethodToolCallbackProvider.builder().toolObjects(storeToolsProvider, storeResourceNowToolsProvider).build();
	}


	@Bean
	public List<SyncResourceSpecification> brandZPlaceHolderResources(StoreMcpResourceProvider storeMcpResourceProvider
	) {
		List<SyncResourceSpecification> specifications = new ArrayList<>();
		createPlaceHolderFaqResource(specifications);

		List<SyncResourceSpecification> otherResourceSpecifications = SpringAiMcpAnnotationProvider.createSyncResourceSpecifications(List.of(storeMcpResourceProvider));
		for (SyncResourceSpecification otherResourceSpecification : otherResourceSpecifications) {
			logger.debug("Adding MCP resource " + otherResourceSpecification.resource().name());
			specifications.add(otherResourceSpecification);
		}

		return specifications;
	}

	private void createPlaceHolderFaqResource(List<SyncResourceSpecification> specifications) {
		McpSchema.Resource mcpResource = new McpSchema.Resource(
				"mcp://brandz/store/faqs",                            // URI for MCP to reference
				"store_faqs",                          // Name (for UI)
				"content of store faqs in plain text",            // Description (for LLM)
				MediaType.TEXT_PLAIN_VALUE,                                  // MIME type
				null                                           // Optional annotations
		);
		logger.debug("Adding faqs ");
		var resourceSpecification = new McpServerFeatures.SyncResourceSpecification(mcpResource, (exchange, request) -> {


			return new McpSchema.ReadResourceResult(List.of(
					new McpSchema.TextResourceContents(
							request.uri(),
							MediaType.TEXT_PLAIN_VALUE,
							"placeholder faq"
					)
			));

		});
		specifications.add(resourceSpecification);

	}

	@Bean
	public List<SyncPromptSpecification> promptSpecs(StoreMcpPromptProvider storeMcpPromptProvider) {
		return SpringAiMcpAnnotationProvider.createSyncPromptSpecifications(List.of(storeMcpPromptProvider));
	}

	@Bean
	public List<SyncCompletionSpecification> completionSpecs(StoreMcpCompleteProvider storeMcpCompleteProvider) {
		return SpringAiMcpAnnotationProvider.createSyncCompleteSpecifications(List.of(storeMcpCompleteProvider));
	}

}
