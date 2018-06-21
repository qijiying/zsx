package com.zsx.fwmp.web.service.user;

import java.util.List;

import com.zsx.model.dto.UserGroupDto;

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

}
