package com.zsx.fwmp.web.others.setting;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.zsx.fwmp.web.others.setting.bean.Ffmpeg;
import com.zsx.fwmp.web.others.setting.bean.Server;

@Component
@ConfigurationProperties(value = "app")
public class YmlBean {
	
	private Server server;
	private Ffmpeg ffmpeg;
	
	

	/**
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}



	/**
	 * @param server the server to set
	 */
	public void setServer(Server server) {
		this.server = server;
	}



	/**
	 * @return the ffmpeg
	 */
	public Ffmpeg getFfmpeg() {
		return ffmpeg;
	}



	/**
	 * @param ffmpeg the ffmpeg to set
	 */
	public void setFfmpeg(Ffmpeg ffmpeg) {
		this.ffmpeg = ffmpeg;
	}



	@Override
	public String toString(){
		 return "YmlBean{" +
	                "server='" + JSONObject.toJSONString(server) + '\'' +
	                ", ffmpeg='" + JSONObject.toJSONString(ffmpeg) + '\'' +
	                '}';
	}
	
}
