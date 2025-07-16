package com.eg.mcp.providers.others;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.eg.mcp.utils.CountryPromptDatabase;
import com.logaritex.mcp.annotation.McpComplete;

@Service
public class StoreMcpCompleteProvider {

    private CountryPromptDatabase countryPromptDatabase;

    public StoreMcpCompleteProvider(CountryPromptDatabase countryPromptDatabase) {
    	this.countryPromptDatabase = countryPromptDatabase;
    }

   
  
    

	
    
    @McpComplete(prompt = "country-status")
	public List<String> completeCountryname(String countryPrefix) {
    	if (countryPrefix == null || countryPrefix.isBlank()) {
    	    return List.of("Enter a country");
    	}
		String prefix = countryPrefix.toLowerCase();
		

		
		
		Set<String> countries = countryPromptDatabase.getCountryDatabase().get(prefix);
		if(countries==null || countries.size()==0)
		{
			return List.of(countryPrefix +" does not resolve to any country name or its begining");
		}

		return new ArrayList<>(countries);
	}

   
    

    
}
