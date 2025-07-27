package com.eg.mcp.providers.others;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.logaritex.mcp.annotation.McpResource;

import io.modelcontextprotocol.spec.McpSchema.ReadResourceRequest;
import io.modelcontextprotocol.spec.McpSchema.ResourceContents;
import io.modelcontextprotocol.spec.McpSchema.TextResourceContents;

@Service
public class StoreMcpResourceProvider {

	@McpResource(uri = "mcp://brandz/store/rules", name = "store_rules", description = "return content of store rules in plain text")
	public ResourceContents rules(ReadResourceRequest request)  {
		return new TextResourceContents(request.uri(), MediaType.TEXT_PLAIN_VALUE, "norules");
	}

}
