package com.zsx.fwmp.web.controller.extend;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zsx.framework.base.BaseAppClass;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/id")
public class IdController {
	
	@ApiOperation(
			value="获取主键ID",
			notes="获取主键ID"
			)
	@PostMapping("/id")
	protected Object beforeId(){
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,BaseAppClass.giveRandomId());
	}

}
