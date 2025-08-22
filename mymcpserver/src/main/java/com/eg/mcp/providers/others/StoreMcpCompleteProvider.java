package com.eg.mcp.providers.others;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.eg.mcp.utils.CountryPromptDatabase;
import com.logaritex.mcp.annotation.McpArg;
import com.logaritex.mcp.annotation.McpComplete;

@Service
public class StoreMcpCompleteProvider {

	private final CountryPromptDatabase countryPromptDatabase;

	public StoreMcpCompleteProvider(CountryPromptDatabase countryPromptDatabase) {
		this.countryPromptDatabase = countryPromptDatabase;
	}

	@McpComplete(prompt = "country-status")
	public List<String> completeCountryname(@McpArg(name = "country-name",  description = "The name of the country", required = true ) String countryPrefix) {
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
	
	@McpComplete(prompt = "generate_greeting_prompt")
	public List<String> greetingStyles(@McpArg(name = "greeting-style", 
	description = "The style of the greeting: formal, casual, or friendly", required = true) 
	String stylePrefix) {
		
		
		List<String> styles = List.of("formal", "casual", "friendly");
		if (stylePrefix == null || stylePrefix.isBlank()) {
		    return styles;
		}
		String prefix = stylePrefix.toLowerCase();
		return styles.stream()
		             .filter(s -> s.startsWith(prefix))
		             .toList();
	}
	

	@McpComplete(prompt = "fun_prompt")
	public List<String> sportName(@McpArg(name = "sports-name", description = "name of the sport", required = true) String sportPrefix) {
		
		
		List<String> sports = List.of("tennis", "football", "badminton", "cricket", "hockey", "swimming", "cycling", "running");
		if (sportPrefix == null || sportPrefix.isBlank()) {
		    return sports;
		}
		String prefix = sportPrefix.toLowerCase();
		return sports.stream()
		             .filter(s -> s.startsWith(prefix))
		             .toList();
	}

}
