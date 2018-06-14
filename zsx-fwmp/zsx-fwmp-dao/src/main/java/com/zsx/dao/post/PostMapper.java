package com.zsx.dao.post;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zsx.model.dto.PostDto;
import com.zsx.model.pojo.Post;

public interface PostMapper extends BaseMapper<Post>{

	/**
	 * @param id，key，userId
	 * @param 预留搜索字段  groupName，status，startTime，endTime
	 * @param current，size
	 * @return
	 */
	List<PostDto> selectPostByKewordAndPage(@Param("id")Long id, @Param("key")String key, String groupName, Integer status, Date startTime,
			Date endTime, @Param("userId")Long userId , @Param("current")Integer current,@Param("size")Integer size);

	int getPostPageOfCount(@Param("userId")Long userId, @Param("code")Integer code);

	List<PostDto> getPostPage(@Param("userId")Long userId, @Param("code")Integer code, @Param("current")Integer current, @Param("size")Integer size);

	int getSearchPostPageOfCount(@Param("id")Long id, @Param("key")String key, String groupName, Integer status, Date startTime, Date endTime, @Param("userId")Long userId);

	List<PostDto> searchPostFromUser(@Param("userId")Long userId,@Param("current")Integer current,@Param("size")Integer size);

	int searchTotalPostFromUser(@Param("userId")Long userId,@Param("current")Integer current,@Param("size")Integer size);

}
