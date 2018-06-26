package com.zsx.fwmp.web.controller.base;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zsx.fwmp.web.others.base.ServerBase;

/**
 * @ClassName ServerController
 * @author lz
 * @description 服务器地址端口获取接口
 * @date 2018年6月25日14:32:19
 */
@RestController
@RequestMapping("/api/web/server")
public class ServerController {

	/**
	 * @Title getServer
	 * @description 获取服务器地址+端口
	 * @return http://10.0.0.21:800/
	 */
	@GetMapping("/get")
	protected Object getServer(){
		return new ServerBase().getServerPort();
	}
	
}
