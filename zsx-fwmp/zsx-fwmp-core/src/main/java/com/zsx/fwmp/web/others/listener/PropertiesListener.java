package com.zsx.fwmp.web.others.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
  * 
  * @ClassName: PropertiesListener 
  * @Description: 自定义spring boot 配置文件监听器读取，防止静态文件使用。无法注入
  * @author xiayy 
  * @date 2018年2月8日 上午10:59:40 
  *
 */
public class PropertiesListener implements ApplicationListener<ApplicationEvent> {

	private String propertyFileName; 
	
	public PropertiesListener(String propertyFileName) { 
		this.propertyFileName = propertyFileName; 
	}
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		PropertiesListenerConfig.loadAllProperties(propertyFileName);
	}

}
