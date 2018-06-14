package com.zsx.fwmp.web.service.user.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zsx.dao.user.system.SysUserMapper;
import com.zsx.fwmp.web.others.base.Log;
import com.zsx.fwmp.web.others.util.UserUtil;
import com.zsx.fwmp.web.service.user.ISysUserService;
import com.zsx.model.pojo.SysUser;
import com.zsx.utils.id.IdGen;

/**
 * @ClassName SysUserServiceImpl
 * @description 系统用户业务实现类
 * @author lz
 * @date 2018年6月11日17:39:04
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

	@Autowired
	private SysUserMapper sysUserMapper;
	
	/**
	 * @Title insertSysUser
	 * @see com.zsx.fwmp.web.service.user.ISysUserService#insertSysUser
	 * @description 添加系统用户
	 */
	@Override
	public Object insertSysUser(SysUser sysUser) {
		String uuid = IdGen.uuid();
		//生成盐
		sysUser.setSalt(uuid);
		//密码加盐
		sysUser.setLoginPass(UserUtil.Md5Password(sysUser.getLoginPass(), uuid));
		return sysUserMapper.insert(sysUser);
	}

	/**
	 * @Title login
	 * @see com.zsx.fwmp.web.service.user.ISysUserService#login
	 * @description 登录业务实现
	 */
	@Override
	public Object login(SysUser sysUser,HttpServletRequest req) {
		Map<String,Object> map = Maps.newHashMap();
		map.put("login_name", sysUser.getLoginName());
		List<SysUser> sys = sysUserMapper.selectByMap(map);
		map.put("code", 0);
		if(sys.size()>0) {
			Log.debug("------------登录名正确："+sysUser.getLoginName()+"-------------", SysUserServiceImpl.class);
			if(UserUtil.Md5Password(sysUser.getLoginPass(), sys.get(0).getSalt()).equals(sys.get(0).getLoginPass())){
				Log.debug("----------------密码正确-------------", SysUserServiceImpl.class);
				map.put("sysUser", sys);
				map.put("code", 1);
				req.getSession().setAttribute("sysUser",sys);
			}
			else{
				Log.debug("----------------密码不匹配------------", SysUserServiceImpl.class);
			}
		}
		return map;
	}

	/**
	 * @Title deleteSysUser
	 * @see com.zsx.fwmp.web.service.user.ISysUserService#deleteSysUse
	 * @description 批量删除系统用户业务实现类
	 */
	@Override
	public Object deleteSysUser(Integer[] ids) {
		List<Integer> list = Lists.newArrayList();
		list = Arrays.asList(ids);
		return deleteBatchIds(list);
	}
	
	
	/**
	 * @Title quitLogin
	 * @see com.zsx.fwmp.web.service.user.IUserService#quitLogin
	 * @description 退出登录
	 */
	@Override
	public Object quitLogin(Integer userId,HttpServletRequest request) {
		if(null!=request.getSession().getAttribute("user"))
			request.getSession().removeAttribute("user");
		return true;
	}

}
