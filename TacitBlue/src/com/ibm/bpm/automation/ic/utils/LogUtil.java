package com.ibm.bpm.automation.ic.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.ibm.bpm.automation.ic.AutoException;

public class LogUtil extends Logger {
	
	private static Logger logger = Logger.getLogger("global");
	
	protected LogUtil(String name, String resourceBundleName) {
		super(name, resourceBundleName);
	}
	
	public static void init(String folderPath) throws AutoException{
		
		File logFolder = new File(folderPath);
		if (!logFolder.exists()) {
			logFolder.mkdir();
			if (!logFolder.exists()) {
				throw new AutoException("Can't create log folder '" + logFolder.getAbsolutePath() +"'.");
			}
		} 
		
		String outLogPath = logFolder.getAbsolutePath() + File.separator + "SystemOut.log";
		File outLogFile = new File(outLogPath);
		if (!outLogFile.exists()) {
			try {
				outLogFile.createNewFile();
			} catch (IOException e) {
				throw new AutoException("Can't create log file '" + outLogFile.getAbsolutePath() + "'.", e);
			}
		}
			
		FileHandler fHandler = null;
		try {
			fHandler = new FileHandler(outLogPath, 0, 1, true);
			
			logger.setUseParentHandlers(false);
			
			fHandler.setLevel(Level.INFO);
			fHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fHandler);
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		//String errLogPath = logFolder.getAbsolutePath() + File.separator + "SystemErr.log";
		//File errLogFile = new File(errLogPath);
		
	}
	
	public static Logger getLogger(String name) {
		return logger;
	}

}
