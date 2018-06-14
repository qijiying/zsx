package com.zsx.fwmp.web.service.post;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.dto.PostDto;
import com.zsx.model.pojo.Post;

public interface IPostService extends IService<Post> {

	Object selectPostByKewordAndPage(Long id, String key, String groupName, Integer status, Date startTime, Date endTime,
			Long userId,Map<String, Object> map);

	Page<PostDto> getList(Map<String, Object> map);

	Object insertPost(Post post);

	Object searchPostFromUser(Map<String,Object> map);

	Object deletePostById(Long[] ids);

}
