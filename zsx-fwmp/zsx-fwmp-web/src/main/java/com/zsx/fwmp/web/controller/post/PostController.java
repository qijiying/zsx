package com.zsx.fwmp.web.controller.post;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Maps;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.service.post.IPostService;
import com.zsx.model.dto.PostDto;
import com.zsx.model.pojo.Post;

/**
 * @Title PostController
 * @author lz
 * @description 帖子
 * @date 2018年5月28日15:21:17
 */
@RestController
@RequestMapping("/api/post")
public class PostController {

	@Autowired
	private IPostService postService;
	
	/**
	 * @Title insertPost
	 * @param post
	 * @description 增加帖子
	 * @return
	 */
	@PostMapping("/add")
	protected Object insertPost(@RequestBody Post post){
         	return ResultfulFactory
         			.getInstance()
         			.creator(ResultEnum.SUCCESS, postService.insert(post));	
	}
	
	
	
	/**
	 * @Title selectPostByPage
	 * @param current
	 * @param size
	 * @description 初始化帖子列表
	 * @return
	 */
	@PostMapping("/dataGrid")
	protected Object selectPostByPage(
			 @RequestParam(value="current",required=false) Integer current,
			 @RequestParam(value="size",required=false) Integer size
			){
		Page<PostDto> post=null;
		Map<String, Object> map=Maps.newHashMap();
		map.put("current", current==null?1:current);
		map.put("size", size==null?10:size);
		post = postService.getList(map);
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS,post);
	}
	
	
	
	/**
	 * @Title selectPostByKewordAndPage
	 * @param id
	 * @param key
	 * @param groupName
	 * @param status
	 * @param startTime
	 * @param endTime
	 * @param current
	 * @param size
	 * @description 搜索帖子
	 * @return
	 */
	@PostMapping("/dataSearch")
	protected Object selectPostByKewordAndPage(
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="key",required=false) String key,
			@RequestParam(value="groupName",required=false) String groupName,
			@RequestParam(value="status",required=false) Integer status,
			@RequestParam(value="st",required=false) Date startTime,
			@RequestParam(value="et",required=false) Date endTime,
			@RequestParam(value="userId",required=false) Long userId,
			@RequestParam(value="current",required=false) Integer current,
			@RequestParam(value="size",required=false) Integer size
			){
		Map<String,Object> map = Maps.newHashMap();
		map.put("current", current==null?1:current);
		map.put("size", size==null?10:size);
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS
						,postService.selectPostByKewordAndPage(id,key,groupName,status,startTime,endTime,userId,map));
	}
	
	
	/**
	 * @Title searchPostFromUser
	 * @param userId
	 * @description 根据用户查看帖子
	 * @return
	 */
	@PostMapping("/search/userId/{userId}")
	protected Object searchPostFromUser(
			  @RequestParam(value="current",required=false) Integer current,
			  @RequestParam(value="size",required=false) Integer size,
			  @PathVariable("userId") Long userId
			){
		Map<String,Object> map = Maps.newHashMap();
		map.put("current", current==null?1:current);
		map.put("size", size==null?10:size);
		map.put("userId", userId);
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS,postService.searchPostFromUser(map));
	}
	
	
	/**
	 * @Title updatePost
	 * @param post
	 * @Description 修改帖子
	 * @return
	 */
	@PostMapping("/update")
	protected Object updatePost(@RequestBody Post post){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS,postService.updateById(post));
	}
	
	
	/**
	 * @Title deletePost
	 * @param id
	 * @description 删除帖子
	 * @return
	 */
	@PostMapping("/delete")
	protected Object deletePost(@RequestParam Long[] ids){
         	return ResultfulFactory
         			.getInstance()
         			.creator(ResultEnum.SUCCESS, postService.deletePostById(ids));	
	}
	
	
}
