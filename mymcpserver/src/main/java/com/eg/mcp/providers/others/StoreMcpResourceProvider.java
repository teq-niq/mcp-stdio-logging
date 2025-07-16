package com.eg.mcp.providers.others;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.eg.mcp.models.State;
import com.eg.mcp.utils.MarkdownMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.logaritex.mcp.annotation.McpResource;

import io.modelcontextprotocol.spec.McpSchema.ReadResourceRequest;
import io.modelcontextprotocol.spec.McpSchema.ResourceContents;
import io.modelcontextprotocol.spec.McpSchema.TextResourceContents;

@Service
public class StoreMcpResourceProvider {
	private State state;
	 private final ObjectMapper jsonMapper;
	    private final XmlMapper xmlMapper;
	    private final MarkdownMapper markdownMapper;
	
	public StoreMcpResourceProvider(State state) {
		super();
		this.state = state;
		this.jsonMapper = new ObjectMapper();
        this.xmlMapper = new XmlMapper(); // for XML
        this.markdownMapper= new MarkdownMapper();
	}
	@McpResource(uri = "mcp://brandz/store/rules", name = "store_rules", description = "return content of store rules in plain text",
			mimeType = MediaType.TEXT_PLAIN_VALUE)
	public ResourceContents rules(ReadResourceRequest request) throws IOException {
		
		
		return new TextResourceContents(request.uri(), MediaType.TEXT_PLAIN_VALUE, "norules");
	}
	
	
	

}
