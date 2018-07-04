package com.zsx.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zsx.model.pojo.UserCollection;
import com.zsx.model.webdto.UserCollectionDto;

public interface UserCollectionMapper extends BaseMapper<UserCollection>{

	List<UserCollectionDto> getUserCollectionList(@Param("type") Integer type,@Param("userId") Long userId,@Param("current")Integer current,@Param("size")Integer size);

}
