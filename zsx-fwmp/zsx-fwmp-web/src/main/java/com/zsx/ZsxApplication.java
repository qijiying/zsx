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
	
/*	@Bean
    public Converter<String, Date> addNewConvert() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 Date date = null;
            try {
                date = sdf.parse((String) source);
                } catch (Exception e) {
                e.printStackTrace();
                }
             return date;
            }
        };
    }*/
}
