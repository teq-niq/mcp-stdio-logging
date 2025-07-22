package com.eg.mcp.models;

import java.util.HashMap;
import java.util.Map;

/**
 * In a real project it may not always be possible to leverage a Enum 
 * as conveniently as I used for SportsItem. But it was convenient for 
 * this demo purpose
 */

public enum SportsItem {
	
	
	 TENNIS_NET("Tennis net", 10.0f,"Standard net used while playing tennis"),
	    FOOTBALL("Football", 10.1f,"Also known as a soccer ball. This is not a rugby ball"),
	    TENNIS_RAQUET("Tennis raquet", 10.2f, "Standard Tennis Raquet"),
	    TENNIS_BALL("Tennis ball", 10.3f, "Standard Tennis ball");

	    private final String label;
	    private final String detail;
		

		private final float price;
		private String normalizedLabel;
		private String uri;
		private static final Map<String, SportsItem> labelMap= loadLabelMap();


		

		/**
		 * @return
		 */
		private static HashMap<String, SportsItem> loadLabelMap() {
			HashMap<String, SportsItem> map = new HashMap<>();
			SportsItem[] values = SportsItem.values();
			for (SportsItem sportsItem : values) {
				map.put(normalizeLabel(sportsItem.label), sportsItem);
			}
			return map;
		}
		
		

	    public float price() {
			return price;
		}

		SportsItem(String label, float price, String detail) {
	        this.label = label;
	        this.detail=detail;
	        this.price=price;
	        this.normalizedLabel = normalizeLabel(label);
	        this.uri=this.label.replace(' ', '_').toLowerCase() + ".png";
	    }

		public String detail() {
			return detail;
		}
		

	    public String label() {
	        return label;
	    }
	    public String normalizedLabel() {
	        return normalizedLabel;
	    }

	    public String touri() {
	        return uri;
	    }

	    @Override
	    public String toString() {
	        return label;
	    }
	    
	    public static String normalizeLabel(String label) {
			return label.toLowerCase().replaceAll("[^a-z0-9]", "");
		}
	    
	    public static SportsItem labelOf(String label)
		{
	    	if (label == null) return null;
	    	String normalizeLabel = normalizeLabel(label);
	    	return labelMap.get(normalizeLabel);
		}
	    
	    /*
	     * use only when sure its normalzed already
	     */
	    static SportsItem internalLookupByNormalizedLabel(String normalizeLabel)
		{
	    	if (normalizeLabel == null) return null;
	    	
	    	return labelMap.get(normalizeLabel);
		}


}
