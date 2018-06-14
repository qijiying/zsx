package com.zsx.fwmp.web.service.user;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.pojo.User;

public interface IUserService extends IService<User>{

	Object selectUserByUserAreaTimeAndPage(String name, Integer areaCode, Date startTime, Date endTime, Page<User> page);

	Object insertUser(User user);

	Object deleteUserById(Long id);

	Page<User> selectUser(Map<String,Object> map);

	Object deleteByUserId(Long[] ids);

	Object checkUserLogin(User user,HttpServletRequest request);

	Page<User> searchUserFriend(Long userId,Map<String,Object> map);

	Object quitLogin(Long userId,HttpServletRequest request);

	Object insertUserByEntity(User user);

	Object dataAllUserNameAndId();

}
