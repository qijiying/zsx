package com.zsx.dao.post;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zsx.model.pojo.Post;
import com.zsx.model.webdto.PostDto;

public interface PostMapper extends BaseMapper<Post>{

	/**
	 * @param id，key，userId
	 * @param 预留搜索字段  groupName，status，startTime，endTime
	 * @param current，size
	 * @return
	 */
	List<PostDto> selectPostByKewordAndPage(@Param("id")Long id, @Param("key")String key, String groupName, Integer status, Date startTime,
			Date endTime, @Param("userId")Long userId , @Param("current")Integer current,@Param("size")Integer size);

	int getWebPostPageOfCount();

	List<PostDto> getWebPostPage(@Param("current")Integer current, @Param("size")Integer size);

	int getSearchPostPageOfCount(@Param("id")Long id, @Param("key")String key, String groupName, Integer status, Date startTime, Date endTime, @Param("userId")Long userId);

	List<PostDto> searchPostFromUser(@Param("userId")Long userId,@Param("current")Integer current,@Param("size")Integer size);

	int searchTotalPostFromUser(@Param("userId")Long userId,@Param("current")Integer current,@Param("size")Integer size);

}
