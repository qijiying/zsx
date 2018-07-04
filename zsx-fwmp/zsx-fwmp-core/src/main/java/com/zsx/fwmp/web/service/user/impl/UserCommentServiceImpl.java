package com.zsx.fwmp.web.service.user.impl;

import java.util.Collections;
import java.util.Date;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.dao.user.UserCommentMapper;
import com.zsx.fwmp.web.others.base.ServerBase;
import com.zsx.fwmp.web.service.user.IUserCommentService;
import com.zsx.model.webdto.FileDto;
import com.zsx.model.webdto.UserCommentOfPostDto;

@Service
public class UserCommentServiceImpl implements IUserCommentService {

	@Autowired
	UserCommentMapper userCommentOfPostMapper;
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: getUserCommentOfPostByUserId</p> 
	  * <p>Description: 查询用户评论过的帖子</p> 
	  * @param userId
	  * @param page
	  * @return 
	  * @see com.zsx.service.user.IUserCommentService#getUserCommentOfPostByUserId(java.lang.Long, com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public Page<UserCommentOfPostDto> getUserCommentOfPostByUserId(Long userId,
			Page<UserCommentOfPostDto> page) {
		page.setRecords(userCommentOfPostMapper.getUserCommentOfPostByUserId(userId,page.getCurrent(),page.getSize()));
		Predicate<FileDto> fileStyle_scalca = (n) -> n.getFileStyle()== 3;  //缩略图
		Predicate<FileDto> fileStyle_video = (n) -> n.getFileStyle()== 1;  //视频
		if(null != page.getRecords() && page.getRecords().size() >0){
			for (UserCommentOfPostDto userCommentOfPostDto : page.getRecords()) {
				if(null !=userCommentOfPostDto.getFileManageList() && userCommentOfPostDto.getFileManageList().size() >0){
					userCommentOfPostDto.getFileManageList().forEach((v->{
						v.setFileList(v.getFileList().stream().filter(fileStyle_scalca.or(fileStyle_video)).limit(1).collect(Collectors.toList()));  //只返回一个元素
						if(v.getFileList().size()>0) v.getFileList().get(0).setFileName(ServerBase.getServer()+v.getFilePath()+"/"+v.getFileList().get(0).getFileName());
					}));
				}
			}
		}
		if(null != page.getRecords() && page.getRecords().size() >0){  //排序，后期优化
			 Collections.sort(page.getRecords(), (o1, o2) -> (o2.getCommentTime().compareTo(o1.getCommentTime())));  
		}
		return page;
	}

	/**
	  * (非 Javadoc) 
	  * <p>Title: getUserCommentOfNewsByUserId</p> 
	  * <p>Description: 查询用户评论过的新闻</p> 
	  * @param userId
	  * @param page
	  * @return 
	  * @see com.zsx.service.user.IUserCommentService#getUserCommentOfNewsByUserId(java.lang.Long, com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public Page<UserCommentOfPostDto> getUserCommentOfNewsByUserId(Long userId,
			Page<UserCommentOfPostDto> page) {
		page.setRecords(userCommentOfPostMapper.getUserCommentOfNewsByUserId(userId,page));
		if(null != page.getRecords() && page.getRecords().size() >0){  //排序，后期优化
			 Collections.sort(page.getRecords(), (o1, o2) -> (o2.getCommentTime().compareTo(o1.getCommentTime())));  
		}
		return page;
	}

	@Override
	public Page<UserCommentOfPostDto> searchComment(Long userId,String content, Date createTime, Page<UserCommentOfPostDto> page) {
        //todo 搜索用户评论
		return page;
	}
}
