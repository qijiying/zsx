package com.zsx.dao.user;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.model.pojo.User;

public interface UserMapper extends BaseMapper<User> {
	
	List<User> selectUserByPage(Page<User> page);
}
