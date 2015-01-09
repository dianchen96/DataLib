package com.dc.model;

import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JToggleButton;

public class TagStringMaker {
	
	private static String getTagString(String[] tags) {
		String result = "";
		for (int i = 0; i < tags.length; i++) {
			result = ";"+ tags[i] + result;
		}
		return result;
	}
	
	public static String getTagString(JComponent comp) {
		Vector<String> pre_result = new Vector<String>();
		for (int i = 0; i < comp.getComponentCount(); i++) {
			JToggleButton eachbutt = (JToggleButton) comp.getComponent(i);
			
			pre_result.add(eachbutt.getText()); 
		}
		String[] result = new String[pre_result.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = pre_result.get(i);
		}
		return getTagString(result);
	}
}
