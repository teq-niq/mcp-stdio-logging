package com.eg.mcp.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author bnasslahsen
 */
@ConfigurationProperties(prefix = "mcp.logging")
public record McpLoggingProperties(String path, String countriesFileName,
								   String inFileName, String outFileName,
								   String combinedFileName) {}
