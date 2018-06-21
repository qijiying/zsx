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
	@PostMapping("/dataGrid")
	protected Object selectUserByPage(
			 @RequestParam(value="source",required=false) String source,
			 @RequestParam(value="current",required=false) Integer current,
			 @RequestParam(value="size",required=false) Integer size
			){ 
		Page<User> post=null;
		Map<String, Object> map=Maps.newHashMap();
		map.put("current", current==null?1:current);
		map.put("size", size==null?10:size);
		map.put("source", source);
		post = iUserService.selectUser(map);
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS,post);
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
	@PostMapping("/friend/{userId}")
	protected Object searchUserFriend(
			@PathVariable("userId")Long userId,
			@RequestParam(value="current",required=false) Integer current,
			@RequestParam(value="size",required=false) Integer size
			){
		Assert.isNull(userId, ResultWebEnum.PARAM_IS_NULL);
		Map<String,Object> map = Maps.newHashMap();
		map.put("current", current==null?1:current);
		map.put("size", size==null?10:size);
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
