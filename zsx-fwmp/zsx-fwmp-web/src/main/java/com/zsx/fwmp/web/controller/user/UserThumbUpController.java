package com.zsx.fwmp.web.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.service.user.IUserThumbUpService;

/**
 * @ClassName UserThumbUpController
 * @author lz
 * @description 用户点赞控制类
 * @date 2018年6月21日14:38:28
 */
@RestController
@RequestMapping("/api/web/user/thumbup")
public class UserThumbUpController {
	
	@Autowired
	private IUserThumbUpService iUserThumbUpService;
	
	/**
	 * @Title getThumbUpByUser
	 * @param userId
	 * @param current
	 * @param size
	 * @description 根据用户查询点赞列表
	 * @return
	 */
	@GetMapping("/{userId}")
    protected Object getThumbUpByUser(
    		@PathVariable Long userId,
    		@RequestParam(value="current",required=false) Integer current,
    		@RequestParam(value="size",required=false) Integer size
    		){
		Map<String,Object> map = Maps.newHashMap();
		map.put("userId", userId);
		map.put("current", current==null?1:current);
		map.put("size", size==null?10:size);
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iUserThumbUpService
						.getThumbUpByUser(map));
	}
	
}
