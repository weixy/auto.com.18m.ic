/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.selenium;

public enum Browsers {
	FF("Firefox", "FF"),
	IE("Internet Explorer", "IE"),
	CHROME("Chrome", "Chrome"),
	SAFARI("Safari", "Safari");
	
	private String name;
	private String abbr;
	
	private Browsers(String name, String abs) {
		this.name = name;
		this.abbr = abs;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAbbreviation() {
		return abbr;
	}
}
