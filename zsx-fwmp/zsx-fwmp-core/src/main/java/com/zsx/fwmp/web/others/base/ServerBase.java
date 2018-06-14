package com.zsx.fwmp.web.others.base;

import com.zsx.fwmp.web.others.listener.PropertiesListenerConfig;

public class ServerBase {

	public StringBuffer getServerPort(){
		String host=PropertiesListenerConfig.propertiesMap.get("ftp.host");
		//String port=PropertiesListenerConfig.propertiesMap.get("ftp.port");
		StringBuffer sb = new StringBuffer("http://");
		sb.append(host);
		sb.append(":");
		sb.append(800);
		sb.append("/");
		return sb;
	}
	
	private static String server = null;
	
	public static String getServer(){
        if (server == null) {    
        	server = new ServerBase().getServerPort().toString();  
        }    
       return server;  
	}
}
