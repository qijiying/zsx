package com.zsx.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.model.pojo.User;
import com.zsx.model.pojo.UserFriend;

public interface UserFriendMapper extends BaseMapper<UserFriend> {

	List<User> getHailFellow(@Param("userId")Long userId, Page<User> page);

	List<User> getAllHailFellow(@Param("userId") Long userId);

}
