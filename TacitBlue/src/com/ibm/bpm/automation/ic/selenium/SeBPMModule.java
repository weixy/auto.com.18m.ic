/**
 * @author Wei,Xinyan (weixiny@cn.ibm.com)
 * 
 * "I'm bad, but that's good. I will never be good, but that's not bad." - Ralph
 */
package com.ibm.bpm.automation.ic.selenium;


public abstract class SeBPMModule {
	
	protected String DEFAULT_CONTEXTROOT;
	protected SeRuntimeOptions options;
	public int failedPoints;
	public int successPoints;
	
	public SeBPMModule() {
		failedPoints = 0;
		successPoints = 0;
	}
	
	public String getURL() {
		String url = null;
		
		String contextRoot = options.getCtxRoot();
		contextRoot = (null != contextRoot) ? ((contextRoot.startsWith("/") ? "" : "/") + contextRoot + "/") : "/";
		contextRoot = contextRoot + DEFAULT_CONTEXTROOT;
		
		url = (options.isUseSecurity() ? "https://" : "http://")
				+ options.getHost() + ":"
				+ (options.isUseSecurity() ? options.getSecurePort() : options.getPort())
				+ contextRoot;
		
		return url;
	}

}
