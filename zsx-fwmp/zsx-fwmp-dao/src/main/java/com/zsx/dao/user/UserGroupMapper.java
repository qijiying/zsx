package com.zsx.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zsx.model.dto.UserGroupDto;

public interface UserGroupMapper {
	
	List<UserGroupDto> getUserGroup(@Param("userId")Long userId);
}
