package com.zsx.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.model.dto.UserGroupDto;

public interface UserGroupMapper {
	
	List<UserGroupDto> getUserGroup(@Param("userId")Long userId);

	List<UserGroupDto> searchUserGroup(@Param("userId") Long userId,@Param("name") String name,@Param("page")Page<UserGroupDto> page);
}
