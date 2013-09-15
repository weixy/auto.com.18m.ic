/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;


public class LogFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		
		StringBuilder sb = new StringBuilder();
		if (LogLevel.HEADER.equals(record.getLevel())) {
			sb.append(System.getProperty("line.separator"));
			sb.append("************ Start Display Current Execution************");
			sb.append(System.getProperty("line.separator"));
			sb.append(record.getMessage());
			sb.append(System.getProperty("line.separator"));
			sb.append("************ End  Display Current Execution ************");
			sb.append(System.getProperty("line.separator"));
		}
		else {
			if(LogLevel.ASSERT.equals(record.getLevel())){
	        	sb.append(">");
	        }
			sb.append("[").append(MessageFormat.format("{0,date,MM/dd/yyyy} {0, time,HH:mm:ss zzz}", 
					new Object[] {new Date(record.getMillis())})).append("]\t");
			try {
				Class<?> aClass = Class.forName(record.getSourceClassName());
				sb.append(aClass.getSimpleName()).append(".").append(record.getSourceMethodName()).append("\t");
			} catch (Exception e) {
				e.printStackTrace();
			}
			sb.append(record.getLevel().getName().charAt(0)).append("\t");
			sb.append(record.getMessage()).append(System.getProperty("line.separator"));
			
			//sb.append(formatMessage(record)).append(System.getProperty("line.separator"));
			
			if (null != record.getThrown()) {
				Throwable thr = record.getThrown();
				PrintWriter pwt = null;
				try {
	                StringWriter sw = new StringWriter();
	                pwt = new PrintWriter(sw);
	                thr.printStackTrace(pwt);
	                sb.append(sw.toString());
	            } finally {
	                if (pwt != null) {
	                    try {
	                        pwt.close();
	                    } catch (Exception e) {
	                        // ignore
	                    }
	                }
	            }
			}
			
			if(LogLevel.ERROR.equals(record.getLevel()) ||
					LogLevel.ASSERT.equals(record.getLevel())){
	        	sb.append(System.getProperty("line.separator"));
	        }
		}
		
		return sb.toString();
	}

}
