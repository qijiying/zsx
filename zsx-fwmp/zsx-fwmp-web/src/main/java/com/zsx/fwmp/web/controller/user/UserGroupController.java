package com.zsx.fwmp.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.service.user.IUserGroupService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/web/user/group")
public class UserGroupController {

	@Autowired
	IUserGroupService iUserGroupService;
	
	
	
	/**
	  * 
	  * @Title: getUserGroup 
	  * @Description: 获取我的群信息 
	  * @param @param userId
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="获取我的群信息", 
			notes="获取我的群信息")
	@ApiImplicitParam(name="userId",value="用户ID",required=true,paramType="path",dataType="int")
	@GetMapping("/{userId}")
	public Object getUserGroup(@PathVariable Long userId){
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,iUserGroupService.getUserGroup(userId));
	}
}
