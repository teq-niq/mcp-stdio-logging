package com.eg.mcp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class CountryPromptDatabase {
	
	private final Map<String, Set<String>> countryDatabase = new TreeMap<>();

	private Set<String> countries;

	private final ResourceLoader resourceLoader;

	private final McpLoggingProperties mcpLoggingProperties;

	public CountryPromptDatabase(ResourceLoader resourceLoader, McpLoggingProperties mcpLoggingProperties) {
		this.resourceLoader = resourceLoader;
		this.mcpLoggingProperties = mcpLoggingProperties;
	}

	public Map<String, Set<String>> getCountryDatabase() {
		return countryDatabase;
	}

	public Set<String> getCountries() {
		return countries;
	}

	@PostConstruct
	void init() throws IOException {
		var resource = resourceLoader.getResource("classpath:" + mcpLoggingProperties.countriesFileName());
		try (var reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
			countries = loadCountries(reader);
			buildDatabase(countries);
		}
	}

	private Set<String> loadCountries(BufferedReader reader) {
		return reader.lines()
				.flatMap(line -> Arrays.stream(line.split(","))) // handle commas across lines
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toCollection(() -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER))); // sorted, no duplicates
	}

	private void buildDatabase(Set<String> countries) {
		for (String country : countries) {
			String lower = country.toLowerCase();
			for (int i = 1; i <= lower.length(); i++) {
				String prefix = lower.substring(0, i);
				countryDatabase.computeIfAbsent(prefix, k -> new TreeSet<>()).add(country);
			}
		}
	}

}
