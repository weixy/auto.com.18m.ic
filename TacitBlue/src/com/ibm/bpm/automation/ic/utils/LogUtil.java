package com.ibm.bpm.automation.ic.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.ibm.bpm.automation.ic.AutoException;

public class LogUtil extends Logger {
	
	private static Logger logger = Logger.getLogger("global");
	
	protected LogUtil(String name, String resourceBundleName) {
		super(name, resourceBundleName);
	}
	
	public static void init(String folderPath) {
		
		File logFolder = new File(folderPath);
		if (!logFolder.exists()) {
			logFolder.mkdir();
			if (!logFolder.exists()) {
				logFolder.mkdir();
				//no exception
				//throw new AutoException("Can't create log folder '" + logFolder.getAbsolutePath() +"'.");
			}
		}
		
		String outLogPath = logFolder.getAbsolutePath() + File.separator + "SystemOut.log";
		File outLogFile = new File(outLogPath);
		/*if (!outLogFile.exists()) {
			try {
				outLogFile.createNewFile();
			} catch (IOException e) {
				throw new AutoException("Can't create log file '" + outLogFile.getAbsolutePath() + "'.", e);
			}
		}*/
		// Clean the log file every initiation
		if (outLogFile.exists()) {
			outLogFile.delete();
		}
		try {
			outLogFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			//throw new AutoException("Can't create log file '" + outLogFile.getAbsolutePath() + "'.", e);
		}
			
		FileHandler fHandler = null;
		try {
			fHandler = new FileHandler(outLogPath, 0, 1, true);
			
			logger.setUseParentHandlers(false);
			
			fHandler.setLevel(LogLevel.INFO);
			fHandler.setFormatter(new LogFormatter());
			logger.addHandler(fHandler);
			
			/*ConsoleHandler cHandler = new ConsoleHandler();
			cHandler.setLevel(LogLevel.WARNING);
			cHandler.setFormatter(new LogFormatter());
			logger.addHandler(cHandler);*/
			
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
