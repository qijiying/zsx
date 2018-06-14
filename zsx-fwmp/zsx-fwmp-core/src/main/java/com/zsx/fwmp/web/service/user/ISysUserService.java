package com.zsx.fwmp.web.service.user;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.pojo.SysUser;

public interface ISysUserService extends IService<SysUser> {

	Object insertSysUser(SysUser sysUser);

	Object login(SysUser sysUser,HttpServletRequest req);

	Object deleteSysUser(Integer[] ids);

	Object quitLogin(Integer userId, HttpServletRequest request);

}
