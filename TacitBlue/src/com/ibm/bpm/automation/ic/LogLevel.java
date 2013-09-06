package com.ibm.bpm.automation.ic;

import java.util.logging.Level;

public class LogLevel extends Level {
	private static final long serialVersionUID = 1L;
	
	public static final Level HEADER = new LogLevel("HEADER", Level.SEVERE.intValue() + 10);
	public static final Level ERROR = new LogLevel("ERROR", Level.SEVERE.intValue() + 20);
	public static final Level ASSERT = new LogLevel("ASSERT", Level.SEVERE.intValue() + 30);
	
	public LogLevel (String levelName, int intValue) {
		super(levelName, intValue);
	}

}
