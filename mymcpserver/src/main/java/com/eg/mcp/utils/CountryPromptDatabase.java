package com.eg.mcp.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;

public class CountryPromptDatabase {
	private final Map<String, Set<String>> countryDatabase = new TreeMap<>();
	private Set<String> countries;
	
	public Map<String, Set<String>> getCountryDatabase() {
		return countryDatabase;
	}

	public Set<String> getCountries() {
		return countries;
	}

	@PostConstruct
	void init()
	{
		 try {
	            var resource = new ClassPathResource("countries.txt");
	            try (BufferedReader reader = new BufferedReader(
	                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

	                countries = loadCountries(reader);

	               
	                

	                buildDatabase(countries);

	            }
	        } catch (Exception e) {
	            throw new RuntimeException("Failed to load countries from file", e);
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
