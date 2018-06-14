package com.zsx.fwmp.web.controller.user;

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
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.base.ResultWebEnum;
import com.zsx.fwmp.web.others.util.Assert;
import com.zsx.fwmp.web.service.user.ISysUserService;
import com.zsx.model.pojo.SysUser;

/**
 * @ClassName SysUserController
 * @description 系统用户接口
 * @author lz
 * @date 2018年6月11日17:31:14
 */
@RestController
@RequestMapping("/api/web/sys/user")
public class SysUserController {
	
	@Autowired
	private ISysUserService iSysUserService;
	
	/**
	 * @Title insertSysUser
	 * @param sysUser
	 * @description 添加系统用户
	 * @return
	 * @date 2018年6月11日17:32:28
	 */
	@PostMapping("/add")
	protected Object insertSysUser(@RequestBody SysUser sysUser){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS
						, iSysUserService.insertSysUser(sysUser));
	}
	
	
	/**
	 * @Title login
	 * @param sysUser
	 * @description 系统用户登录
	 * @return
	 */
	@PostMapping("/login")
	protected Object login(@RequestBody SysUser sysUser,HttpServletRequest req){
		return ResultfulFactory.getInstance()
				.creator(ResultEnum.SUCCESS
						, iSysUserService.login(sysUser,req));
	}
	
	
	/**
	 * @Title SysUserGrid
	 * @param current
	 * @param size
	 * @description 系统用户列表
	 * @return
	 */
	@PostMapping("/dataGrid")
	protected Object SysUserGrid(
			@RequestParam(value="current",required=false) Integer current,
			@RequestParam(value="size",required=false) Integer size
			){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS
						, iSysUserService.selectPage(new Page<SysUser>(current==null?1:current,size==null?10:size)));
	}
	
	
	/**
	 * @Title deleteSysUser
	 * @param ids
	 * @description 批量删除系统用户
	 * @return
	 */
	@GetMapping("/delete")
	protected Object deleteSysUser(@RequestParam("ids") Integer[] ids){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS,iSysUserService.deleteSysUser(ids));
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
			@PathVariable("userId") Integer userId,
			HttpServletRequest request
			){
		Assert.isNull(userId, ResultWebEnum.PARAM_IS_NULL);
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iSysUserService.quitLogin(userId,request));
	}

}
