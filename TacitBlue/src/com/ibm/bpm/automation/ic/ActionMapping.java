package com.ibm.bpm.automation.ic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class ActionMapping {
	

	public static HashMap<String, Object> getMapping() throws AutoException{
		
		if (null == mapping) {
			
			Properties prop = new Properties();
			ActionMapping mappingInst = new ActionMapping();
			
			try {
				prop.load(mappingInst.getClass().getResourceAsStream("ActionMapping.properties"));
				Set keys = prop.keySet();
				for (Iterator it = keys.iterator(); it.hasNext();) {
					String key = (String)it.next();
					Object cls = prop.getProperty(key);
					System.out.println(key + ":" + cls);
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
