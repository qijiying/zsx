package com.zsx.fwmp.web.controller.user;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.service.user.IUserCommentService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName UserCommentController
 * @author lz
 * @description 用户回复控制层
 * @date 2018年6月25日14:08:12
 */
@RestController
@RequestMapping("/api/web/user/comment")
public class UserCommentController {
	
	@Autowired
	private IUserCommentService iUserCommnetService;
	
	/**
	  * 
	  * @Title: getUserCommentOfPostByUserId 
	  * @Description: 查询用户评论过的帖子 
	  * @param @param userId
	  * @param @param current
	  * @param @param size
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="查询用户评论过的帖子 ", 
			notes="查询用户评论过的帖子 ")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户ID",required=true,paramType="path",dataType="int"),
		@ApiImplicitParam(name="c",value="当前页码",required=true,paramType="query",dataType="int"),
		@ApiImplicitParam(name="s",value="数据个数",required=true,paramType="query",dataType="int")
	})
	@GetMapping("/post/{userId}")
	protected Object getUserCommentOfPostByUserId(@PathVariable Long userId,
			@RequestParam(value="c") Integer current,
			@RequestParam(value="s") Integer size){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS,iUserCommnetService
						.getUserCommentOfPostByUserId(userId, new Page<>(current, size)));
	}
	
	
	/**
	  * 
	  * @Title: getUserCommentOfNewsByUserId 
	  * @Description: 查询用户评论过的新闻 
	  * @param @param userId
	  * @param @param current
	  * @param @param size
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="查询用户评论过的新闻", 
			notes="查询用户评论过的新闻")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户ID",required=true,paramType="path",dataType="int"),
		@ApiImplicitParam(name="c",value="当前页码",required=true,paramType="query",dataType="int"),
		@ApiImplicitParam(name="s",value="数据个数",required=true,paramType="query",dataType="int")
	})
	@GetMapping("/news/{userId}")
	public Object getUserCommentOfNewsByUserId(@PathVariable Long userId,
			@RequestParam(value="c") Integer current,
			@RequestParam(value="s") Integer size){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS,iUserCommnetService
						.getUserCommentOfNewsByUserId(userId, new Page<>(current, size)));
	}
	
	
	/**
	 * @Title searchComment
	 * @description 用户评论搜索，目前只根据时间和回复内容搜索
	 * @return
	 */
	@ApiOperation(
			value="搜索用户的评论",
			notes="搜索用户的评论，目前只根据时间和回复内容搜索"
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户ID",required=true,paramType="path",dataType="long"),
		@ApiImplicitParam(name="content",value="评论内容",required=false,paramType="path",dataType="String"),
		@ApiImplicitParam(name="createTime",value="评论时间",required=false,paramType="path",dataType="Date"),
		@ApiImplicitParam(name="c",value="当前数据页码",required=false,paramType="query",dataType="int"),
		@ApiImplicitParam(name="s",value="数据个数",required=false,paramType="query",dataType="int")
	})
	@GetMapping("/search/{userId}")
	public Object searchComment(
			@PathVariable Long userId,
			@RequestParam(value="content",required=false) String content,
			@RequestParam(value="createTime",required=false) Date createTime,
			@RequestParam(value="c",required=false) Integer current,
			@RequestParam(value="s",required=false) Integer size
			){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iUserCommnetService.searchComment(userId,content,createTime,new Page<>(current,size)));
	}

}
