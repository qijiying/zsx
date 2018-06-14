package com.zsx.fwmp.web.others.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
	
	private static Logger logger = null;
	
	public static void debug(String s,Class<?> cla){
		logger = LoggerFactory.getLogger(cla);
		logger.debug(s);
	}
	
	public static void info(String s,Class<?> cla){
		logger = LoggerFactory.getLogger(cla);
		logger.info(s);
	}
	
	public static void warn(String s,Class<?> cla){
		logger = LoggerFactory.getLogger(cla);
		logger.warn(s);
	}

}
