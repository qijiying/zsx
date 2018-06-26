package com.zsx.fwmp.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.service.user.IUserGroupService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName UserGroupController
 * @author lz
 * @description 用户群组控制层
 * @date 2018年6月25日14:06:47
 */
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
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS,iUserGroupService.getUserGroup(userId));
	}
	
	/**
	 * @Title searcgUserGroup
	 * @description 搜索用户的群组
	 * @param userId
	 * @param name
	 * @param current
	 * @param size
	 * @return
	 */
	@ApiOperation(
			value="搜索用户的群组",
			notes="搜索用户的群组"
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户的ID",required=true,paramType="path",dataType="long"),
		@ApiImplicitParam(name="name",value="群组名称",required=false,paramType="path",dataType="String"),
		@ApiImplicitParam(name="current",value="当前页码",required=false,paramType="query",dataType="int"),
		@ApiImplicitParam(name="size",value="每页条数",required=false,paramType="query",dataType="int")
	})
	@GetMapping("/search/{userId}")
	protected Object searchUserGroup(
			@PathVariable Long userId,
			@RequestParam(value="name",required=false) String name,
			@RequestParam(value="current",required=false) Integer current,
			@RequestParam(value="size",required=false) Integer size
			){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iUserGroupService
						.searchUserGroup(userId,name,new Page<>(current==null?1:current,size==null?10:size))); 
	}
	
}
