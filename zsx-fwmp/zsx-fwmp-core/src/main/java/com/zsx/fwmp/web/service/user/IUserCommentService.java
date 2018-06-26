package com.zsx.fwmp.web.service.user;

import java.util.Date;

import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.model.dto.UserCommentOfPostDto;

public interface IUserCommentService {

	/**
	  * 
	  * @Title: getUserCommentOfPostByUserId 
	  * @Description: 用户评论过的帖子列表 
	  * @param @param userId
	  * @param @param page
	  * @param @return    设定文件 
	  * @return Page<UserCommentOfPostDto>    返回类型 
	  * @throws
	 */
	Page<UserCommentOfPostDto> getUserCommentOfPostByUserId(Long userId, Page<UserCommentOfPostDto> page);

	/**
	  * 
	  * @Title: getUserCommentOfNewsByUserId 
	  * @Description: 用户评论过的新闻列表
	  * @param @param userId
	  * @param @param page
	  * @param @return    设定文件 
	  * @return Page<UserCommentOfPostDto>    返回类型 
	  * @throws
	 */
	Page<UserCommentOfPostDto> getUserCommentOfNewsByUserId(Long userId, Page<UserCommentOfPostDto> page);

	/**
	 * @Title searchComment
	 * @description 搜索用户评论
	 * @param content
	 * @param createTime
	 * @param page
	 * @return
	 */
	Page<UserCommentOfPostDto> searchComment(Long userId,String content, Date createTime, Page<UserCommentOfPostDto> page);

}
