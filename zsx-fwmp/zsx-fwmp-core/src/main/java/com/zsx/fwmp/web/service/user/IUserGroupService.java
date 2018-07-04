package com.zsx.fwmp.web.service.user;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.model.webdto.UserGroupDto;

public interface IUserGroupService {

	/**
	  * 
	  * @Title: getUserGroup 
	  * @Description: 获取用户所参与的群 
	  * @param @param userId
	  * @param @return    设定文件 
	  * @return Page    返回类型 
	  * @throws
	 */
	List<UserGroupDto> getUserGroup(Long userId);

	/**
	 * @Title searchUserGroup
	 * @description 用户搜索群组
	 * @param userId
	 * @param name
	 * @param page
	 * @return
	 */
	Page<UserGroupDto> searchUserGroup(Long userId, String name, Page<UserGroupDto> page);

}
