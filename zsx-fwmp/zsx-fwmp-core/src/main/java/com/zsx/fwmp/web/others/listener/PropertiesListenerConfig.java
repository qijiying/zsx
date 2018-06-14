package com.zsx.fwmp.web.others.listener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.zsx.framework.exception.enmus.ResultEnum;

/**
  * 
  * @ClassName: PropertiesListenerConfig 
  * @Description: 读取配置文件 
  * @author xiayy 
  * @date 2018年2月8日 上午11:00:19 
  *
 */
public class PropertiesListenerConfig {
	
	static final Logger logger=LoggerFactory.getLogger(PropertiesListenerConfig.class);
	
	public static Map<String, String> propertiesMap = new HashMap<>();
	private static void processProperties(Properties props)throws BeansException {
		propertiesMap = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			try {
				// PropertiesLoaderUtils的默认编码是ISO-8859-1,在这里转码一下
				propertiesMap.put(keyStr, new String(props.getProperty(keyStr).getBytes("ISO-8859-1"),"utf-8"));
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}
		if(propertiesMap == null){
			logger.error("load PropertiesListenerConfig error,data is null {0}",ResultEnum.SYSTEM_LOAD_PROPERTIES_DATA_IS_NULL);
		}
	}

	public static void loadAllProperties(String propertyFileName) {
		try {
			Properties properties = PropertiesLoaderUtils.loadAllProperties(propertyFileName);
			processProperties(properties);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String name) {
		return propertiesMap.get(name).toString();
	}

	public static Map<String, String> getAllProperty() {
		return propertiesMap;
	}

}
