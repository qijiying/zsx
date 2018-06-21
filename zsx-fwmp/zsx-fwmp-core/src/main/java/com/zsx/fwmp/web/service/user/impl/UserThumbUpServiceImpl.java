package com.zsx.fwmp.web.service.user.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.zsx.dao.news.NewsCommentMapper;
import com.zsx.dao.news.NewsMapper;
import com.zsx.dao.news.NewsReplyMapper;
import com.zsx.dao.post.PostCommentMapper;
import com.zsx.dao.post.PostMapper;
import com.zsx.dao.post.PostReplyMapper;
import com.zsx.dao.user.UserThumbUpMapper;
import com.zsx.fwmp.web.others.base.ConstantClass;
import com.zsx.fwmp.web.service.user.IUserThumbUpService;
import com.zsx.model.dto.UserThumbUpDto;
import com.zsx.model.pojo.News;
import com.zsx.model.pojo.NewsComment;
import com.zsx.model.pojo.NewsReply;
import com.zsx.model.pojo.Post;
import com.zsx.model.pojo.PostComment;
import com.zsx.model.pojo.PostReply;

/**
 * @ClassName UserThumbUpServiceImpl
 * @author lz
 * @description 用户点赞业务控制类
 * @date 2018年6月21日14:39:26
 */
@Service
public class UserThumbUpServiceImpl implements IUserThumbUpService {

	@Autowired
	private UserThumbUpMapper userThumbUpMapper;
	
	@Autowired
	private NewsMapper newsMapper;
	
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private NewsReplyMapper newsReplyMapper;

	@Autowired
	private NewsCommentMapper newsCommentMapper;
	
	@Autowired
	private PostReplyMapper postReplyMapper;
	
	@Autowired
	private PostCommentMapper postCommentMapper;
	
	@Override
	public Page<UserThumbUpDto> getThumbUpByUser(Map<String, Object> map) {
		Long userId = (long)map.get("userId");
		Integer current = (Integer)map.get("current");
		Integer size = (Integer)map.get("size");
		List<UserThumbUpDto> list = userThumbUpMapper.selectListByPage(userId,current,size);
		int total = userThumbUpMapper.selectTotalByPage(userId);
		//帖子点赞
        list.forEach(item->{
        	String[] result = getFromType(item.getThumbUpType(),item.getPostId(),item.getSourceId());
        	item.setTitle(result[0]);
        	item.setThumbUpOfContent(result[1]);
        });	
		
		
		Page<UserThumbUpDto> page = new Page<>(current,size);

		page.setRecords(list);
		page.setTotal(total);
		return page;
	}
	
	protected String getTitle(Integer flag,Long id){
		List<Object> title;String result = "";
		if(flag==1) title = newsMapper.selectObjs(new EntityWrapper<News>().setSqlSelect(" title ").where(" id={0} ", id));
		else title = postMapper.selectObjs(new EntityWrapper<Post>().setSqlSelect(" post_content ").where(" id={0} ", id));
		if(title.size()>0&&null!=title.get(0)) result = title.get(0).toString();
		return result;
	}
	
	protected String[] getContent(Integer flag,Integer type,Long sourceId,Long postId){
		String title = getTitle(flag,postId); 
		List<Object> obj = Lists.newArrayList();String content = "";
		switch(type){
		   case 1: 
			   obj = newsReplyMapper.selectObjs(new EntityWrapper<NewsReply>().setSqlSelect(" reply_content ").where(" id={0}",sourceId));
			   break;
		   case 2:
			   obj = newsCommentMapper.selectObjs(new EntityWrapper<NewsComment>().setSqlSelect(" comment_content ").where(" id={0}",sourceId));
			   break;
		   case 3:
			   obj = postReplyMapper.selectObjs(new EntityWrapper<PostReply>().setSqlSelect(" reply_content ").where(" id={0}",sourceId));
			   break;
		   case 4:
			   obj = postCommentMapper.selectObjs(new EntityWrapper<PostComment>().setSqlSelect(" comment_content ").where(" id={0}",sourceId));
			   break;
		   case 5:
			   obj.add(title);break;
		   default:
		       obj.add(title);break;
			
		}
		if(obj.size()>0&&null!=obj.get(0)) content = obj.get(0).toString();
		return new String[]{title,content};
	}
	
	protected String[] getFromType(Integer type,Long postId,Long sourceId){
		Integer flag = ConstantClass.ONE_FLAG;
		if(type>2) flag = ConstantClass.TWO_FLAG;
		String[] content = getContent(flag,type,sourceId,postId);
		return content;
	}
	
}
