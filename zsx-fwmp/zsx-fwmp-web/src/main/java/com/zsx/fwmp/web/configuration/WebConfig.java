package com.zsx.fwmp.web.configuration;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverter() {
        return new HttpMessageConverters(new FastJsonHttpMessageConverter());
    }
}