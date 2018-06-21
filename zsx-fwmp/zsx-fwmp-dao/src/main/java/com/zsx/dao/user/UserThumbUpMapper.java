package com.zsx.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zsx.model.dto.UserThumbUpDto;
import com.zsx.model.pojo.UserThumbUp;

public interface UserThumbUpMapper extends BaseMapper<UserThumbUp> {

	List<UserThumbUpDto> selectListByPage(@Param("userId")Long userId,@Param("current") Integer current,@Param("size") Integer size);

	int selectTotalByPage(Long userId);

}
