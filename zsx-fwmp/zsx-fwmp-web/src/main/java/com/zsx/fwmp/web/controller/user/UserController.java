package com.zsx.fwmp.web.controller.user;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Maps;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.base.ResultWebEnum;
import com.zsx.fwmp.web.others.util.Assert;
import com.zsx.fwmp.web.service.user.IUserService;
import com.zsx.model.pojo.User;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName UserController
 * @description 用户注册与登录相关接口
 * @author lz
 * @date 2018年5月28日10:32:47
 *
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private IUserService iUserService;
	
	/**
	  * 
	  * @Title: registerUser 
	  * @Description: 创建用户
	  * @param @param user
	  * @return json    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="创建用户 ", 
			notes="创建用户 ")
	@ApiImplicitParams({
		@ApiImplicitParam(name="user",value="用户",required=true,paramType="path",dataType="User")
	})
	@PostMapping("/add")
	protected Object registerUser(@RequestBody User user){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS,iUserService.insertUserByEntity(user));
	}
	
	
	/**
	 * @param current
	 * @param size
	 * @return
	 * @description 分页查询用户
	 */
	@ApiOperation(
			value="分页初始化用户列表",
			notes="分页初始化用户列表"
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name="source",value="用户来源，ios，Android，web",required=false,paramType="path",dataType="String"),
		@ApiImplicitParam(name="current",value="当前页数",required=false,paramType="query",dataType="Integer"),
		@ApiImplicitParam(name="size",value="每页条数",required=false,paramType="query",dataType="Integer")
	})
	@PostMapping("/dataGrid")
	protected Object selectUserByPage(
			 @RequestParam(value="source",required=false) String source,
			 @RequestParam(value="current",required=false) Integer current,
			 @RequestParam(value="size",required=false) Integer size
			){ 
		Map<String, Object> map=Maps.newHashMap();
		map.put("current", current==null?1:current);
		map.put("size", size==null?10:size);
		map.put("source", source);
		Page<User> user = iUserService.selectUser(map);
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS,user);
	}
	
	/**
	 * @Title selectUserByUserAreaTimeAndPage
	 * @param name
	 * @param areaCode
	 * @param startTime
	 * @param endTime
	 * @param current
	 * @param size
	 * @description 分页搜索用户
	 * @return
	 */
	@ApiOperation(
			value="分页搜索用户",
			notes="分页搜索用户"
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name="name",value="用户的登录名、昵称、实名",required=false,paramType="path",dataType="String"),
		@ApiImplicitParam(name="areaCode",value="区域code",required=false,paramType="path",dataType="int"),
		@ApiImplicitParam(name="startTime",value="注册大于的时间",required=false,paramType="path",dataType="Date"),
		@ApiImplicitParam(name="endTime",value="注册小于的时间",required=false,paramType="path",dataType="Date"),
		@ApiImplicitParam(name="source",value="用户来源，ios，Android，web",required=false,paramType="path",dataType="String"),
		@ApiImplicitParam(name="current",value="当前页码",required=false,paramType="query",dataType="int"),
		@ApiImplicitParam(name="size",value="每页条数",required=false,paramType="query",dataType="int")
	})
	@PostMapping("/dataSearch")
	protected Object selectUserByUserAreaTimeAndPage(
			 @RequestParam(value="name",required=false) String name,
			 @RequestParam(value="areaCode",required=false) Integer areaCode,
			 @RequestParam(value="startTime",required=false) Date startTime,
			 @RequestParam(value="source",required=false) String source,
			 @RequestParam(value="endTime",required=false) Date endTime,
			 @RequestParam(value="current",required=false) Integer current,
			 @RequestParam(value="size",required=false) Integer size
			){
		Map<String,Object> map = Maps.newHashMap();
		map.put("name", name);
		map.put("areaCode", areaCode);
		map.put("startTime", startTime);
		map.put("source", source);
		map.put("endTime", endTime);
		map.put("current", current==null?1:current);
		map.put("size", size==null?10:size);
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum
						.SUCCESS,iUserService
						.selectUserByUserAreaTimeAndPage(map));
	}
	
	
	/**
	 * @Title dataUserNameAndId
	 * @description 查找用所有用户，不分页
	 * @return
	 */
	@ApiOperation(
			value="查找所有用户",
			notes="查找所有用户"
			)
	@GetMapping("/all/user")
	protected Object dataUserNameAndId(){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iUserService.dataAllUserNameAndId());
	}
	
	
	/**
	  * 
	  * @Title: updateUser 
	  * @Description: 更新用户
	  * @param @param user
	  * @return json    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="更新用户",
			notes="更新用户"
			)
	@ApiImplicitParam(name="user",value="用户",required=true,paramType="path",dataType="User")
	@PostMapping("/update")
	protected Object updateUser(@RequestBody User user){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS,iUserService
						.updateById(user));
	}
	
	
	/**
	 * @Title updateUser
	 * @param id
	 * @description 删除用户
	 * @return
	 */
	@ApiOperation(
			value="批量删除用户",
			notes="批量删除用户"
			)
	@ApiImplicitParam(name="ids",value="用户ID数组",required=true,paramType="path",dataType="Long[]")
	@PostMapping("/delete")
	protected Object deleteUser(@RequestParam Long[] ids){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS,iUserService.deleteByUserId(ids));
	}
	
	
	/**
	 * @Title searchUserFriend
	 * @param userId
	 * @description 根据用户id查找用户friend
	 * @return
	 */
	@ApiOperation(
			value="根据用户ID查找friend",
			notes="根据用户ID查找friend"
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户ID",required=true,paramType="path",dataType="User"),
		@ApiImplicitParam(name="current",value="当前页码",required=false,paramType="query",dataType="int"),
		@ApiImplicitParam(name="size",value="每页条数",required=false,paramType="query",dataType="int")
	})
	@PostMapping("/friend/{userId}")
	protected Object searchUserFriend(
			@PathVariable("userId")Long userId,
			@RequestParam(value="current",required=false) Integer current,
			@RequestParam(value="size",required=false) Integer size,
			@RequestParam(value="isEntire",required=false) String isEntire 
			){
		Assert.isNull(userId, ResultWebEnum.PARAM_IS_NULL);
		Map<String,Object> map = Maps.newHashMap();
		map.put("current", current==null?1:current);
		map.put("size", size==null?10:size);
		map.put("isEntire", isEntire);
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iUserService.searchUserFriend(userId,map));
	}
	
	
	
	
     /**
	 * @Title login
	 * @param user
	 * @description 登录web系统
	 * @return
	 */
	@PostMapping("/login")
	protected Object login(
			@RequestBody User user,
			HttpServletRequest request
			){
		Assert.isNull(user.getLoginUsername(), ResultWebEnum.PARAM_IS_NULL);
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iUserService.checkUserLogin(user,request));
	}
	

	/**
	 * @Title quitLogin
	 * @param userId
	 * @param request
	 * @description 用户退出
	 * @return
	 */
	@PostMapping("/quit/{userId}")
	protected Object quitLogin(
			@PathVariable("userId") Long userId,
			HttpServletRequest request
			){
		Assert.isNull(userId, ResultWebEnum.PARAM_IS_NULL);
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iUserService.quitLogin(userId,request));
	}
   
}
