package com.zsx.dao.user;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.model.pojo.User;

public interface UserMapper extends BaseMapper<User> {

	List<User> selectUserByPage(Page<User> page);

	List<User> selectUserByUserAreaTimeAndPage(@Param("name") String name,@Param("areaCode") Integer areaCode,@Param("source") Integer source,@Param("startTime") Date startTime,@Param("endTime") Date endTime,
			Page<User> page);

	int selectTotalUserByUserAreaTimeAndPage(@Param("name") String name,@Param("areaCode") Integer areaCode,@Param("source") Integer source,@Param("startTime") Date startTime,@Param("endTime") Date endTime,
			Page<User> page);
}
