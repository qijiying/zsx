package com.zsx.dao.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.zsx.model.dto.UserCommentOfPostDto;

/**
  * 
  * @ClassName: UserCommentOfPostMapper 
  * @Description: 用户评论 
  * @author xiayy 
  * @date 2018年3月20日 下午2:29:26 
  *
 */
public interface UserCommentMapper  {
	
	/**
	  * 
	  * @Title: getUserCommentOfPostByUserId 
	  * @Description: 获取用户评论过的帖子 
	  * @param @param userId
	  * @param @return    设定文件 
	  * @return List<UserCommentOfPostDto>    返回类型 
	  * @throws
	 */
	public List<UserCommentOfPostDto> getUserCommentOfPostByUserId(@Param("userId") Long userId,@Param("current")Integer current,@Param("size")Integer size);
	
	
	/**
	  * 
	  * @Title: getUserCommentOfNewsByUserId 
	  * @Description: 获取用户评论过的新闻
	  * @param @param userId
	  * @param @return    设定文件 
	  * @return List<UserCommentOfPostDto>    返回类型 
	  * @throws
	 */
	public List<UserCommentOfPostDto> getUserCommentOfNewsByUserId(@Param("userId") Long userId,Pagination page);
}
