package com.zsx.fwmp.web.configuration;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
  * 
  * @ClassName: MultipartConfiguration 
  * @Description: 文件上传配置 
  * @author xiayy 
  * @date 2018年2月9日 下午2:36:05 
  *
 */
@Configuration
public class MultipartConfiguration {

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 设置文件大小限制 ,超出设置页面会抛出异常信息，
		// 这样在文件上传的地方就需要进行异常信息的处理了;
		factory.setMaxFileSize("20Mb"); // KB,MB
		/// 设置总上传数据总大小
		factory.setMaxRequestSize("20Mb");
		// Sets the directory location where files will be stored.
		 //factory.setLocation("D:/temp");
		return factory.createMultipartConfig();
	}
}
