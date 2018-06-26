package com.zsx.fwmp.web.service.user.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.dao.user.UserGroupMapper;
import com.zsx.fwmp.web.service.user.IUserGroupService;
import com.zsx.model.dto.UserGroupDto;

/**
 * @ClassName UserGroupServiceImpl
 * @author lz
 * @description 用户群组业务实现类
 */
@Service
public class UserGroupServiceImpl implements IUserGroupService {

	@Autowired
	UserGroupMapper userGroupMapper;
	

	/**
	  * (非 Javadoc) 
	  * <p>Title: getUserGroup</p> 
	  * <p>Description:获取用户群组 </p> 
	  * @param userId
	  * @param page
	  * @return 
	  * @see com.zsx.service.user.IUserGroupService#getUserGroup(java.lang.Long, com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public List<UserGroupDto> getUserGroup(Long userId) {
		return userGroupMapper.getUserGroup(userId);
	}


	/**
	 * @Title searchUserGroup
	 * @description 用户查找群组
	 * @see com.zsx.fwmp.web.service.user.IUserGroupService#searchUserGroup(java.lang.Long, java.lang.String, com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public Page<UserGroupDto> searchUserGroup(Long userId, String name, Page<UserGroupDto> page) {
		List<UserGroupDto> list = userGroupMapper.searchUserGroup(userId,name,page);
		page.setRecords(list);
		return page;
	}
	
}
