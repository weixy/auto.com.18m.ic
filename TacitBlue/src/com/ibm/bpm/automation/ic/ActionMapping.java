package com.ibm.bpm.automation.ic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class ActionMapping {
	
	private static HashMap<String, String> mapping;

	public static HashMap<String, String> getMapping() throws AutoException{
		
		if (null == mapping) {
			
			mapping = new HashMap<String, String>();
			
			Properties prop = new Properties();
			ActionMapping mappingInst = new ActionMapping();
			
			try {
				prop.load(mappingInst.getClass().getResourceAsStream("ActionMapping.properties"));
				Set keys = prop.keySet();
				for (Iterator it = keys.iterator(); it.hasNext();) {
					String key = (String)it.next();
					String value = prop.getProperty(key);
					//System.out.println(key + ":" + value);
					
					mapping.put(key, value);
				}
				
			} catch (FileNotFoundException e) {
				throw new AutoException("Can't find the mapping file", e);
			} catch (IOException e) {
				throw new AutoException("Can't load the mapping file", e);
			} 
		}
		
		return mapping;
	}
}
