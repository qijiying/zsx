package com.zsx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import com.zsx.fwmp.web.others.listener.PropertiesListener;

@SpringBootApplication
@ServletComponentScan
public class ZsxApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(ZsxApplication.class);
		springApplication.addListeners(new PropertiesListener("setting/setting.properties"));
		springApplication.run(args);
	}
}
