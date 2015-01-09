package com.dc.model;


public class Tag {
	private String tag_name;
	private String[] tag_values;
	
	public Tag(String tag_name, String[] tag_values) {
		this.tag_name = tag_name;
		this.tag_values = tag_values;
		
	}
	
	public String getTagName() {
		
		return tag_name;
	}
	
	public String getTagValues() {
		String value = "";
		for (int i = 0; i < tag_values.length; i++) {
			value = ";" + tag_values[i] + value;
		}
		
		return value;
	}
	
	public String[] getTagValuesAsArray() {
		return tag_values;
	}
}
