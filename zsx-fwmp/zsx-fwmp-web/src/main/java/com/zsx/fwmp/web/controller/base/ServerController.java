package com.zsx.fwmp.web.controller.base;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;

@RestController
@RequestMapping("/api/web/server")
public class ServerController {

	@GetMapping("/get")
	protected Object getServer(){
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS);
	}
	
}
