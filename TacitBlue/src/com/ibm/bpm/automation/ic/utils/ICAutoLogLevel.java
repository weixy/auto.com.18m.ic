package com.ibm.bpm.automation.ic.utils;

import java.util.logging.Level;

public class ICAutoLogLevel extends Level {
	private static final long serialVersionUID = 1L;
	
	public static final Level ERROR = new ICAutoLogLevel("ERROR", Level.SEVERE.intValue() + 10);
	
	public ICAutoLogLevel (String levelName, int intValue) {
		super(levelName, intValue);
	}

}
