package com.zsx.fwmp.web.configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;

/**
 * @author lz
 * @description 全局日期处理
 * @date 2018年5月29日17:46:20
 */
@Configuration
public class DateConverterConfig {

    /*** 
     * Date日期类型转换器 
     * @return 
     */  
    @Bean  
    public Formatter<Date> dateFormatter() {  
        return new Formatter<Date>() {  
  
            @Override  
            public Date parse(String text, Locale locale) throws ParseException {  
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                Date date = null;  
                try {  
                    date = sdf.parse(text);  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
                return date;  
            }  
  
            @Override  
            public String print(Date object, Locale locale) {  
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                return sdf.format(object);  
            }  
        };  
    } 

}
