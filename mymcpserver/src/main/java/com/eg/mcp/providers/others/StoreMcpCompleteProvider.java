package com.eg.mcp.providers.others;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.eg.mcp.utils.CountryPromptDatabase;
import com.logaritex.mcp.annotation.McpComplete;

@Service
public class StoreMcpCompleteProvider {

	private final CountryPromptDatabase countryPromptDatabase;

	public StoreMcpCompleteProvider(CountryPromptDatabase countryPromptDatabase) {
		this.countryPromptDatabase = countryPromptDatabase;
	}

	@McpComplete(prompt = "country-name")
	public List<String> completeCountryname(String countryPrefix) {
		if (countryPrefix == null ) {
			countryPrefix="";
		}
		String prefix = countryPrefix.toLowerCase();
		Set<String> countries = countryPromptDatabase.getCountryDatabase().get(prefix);
		if (countries == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(countries);
	}
	
	@McpComplete(prompt = "greeting-style")
	public List<String> greetingStyles(String stylePrefix) {
		
		
		List<String> styles = List.of("formal", "casual", "friendly");
		if (stylePrefix == null || stylePrefix.isBlank()) {
		    return styles;
		}
		String prefix = stylePrefix.toLowerCase();
		return styles.stream()
		             .filter(s -> s.startsWith(prefix))
		             .toList();
	}

}
