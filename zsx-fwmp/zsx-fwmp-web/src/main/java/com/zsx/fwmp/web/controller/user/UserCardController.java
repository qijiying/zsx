package com.zsx.fwmp.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.service.user.IUserCardService;

/**
 * @ClassName UserCardController
 * @author lz
 * @description 用户名片控制层
 * @date 2018年6月22日11:15:30
 */
@RestController
@RequestMapping("/api/web/user/card")
public class UserCardController {

	@Autowired
	private IUserCardService iUserCardService;
	
	
	/**
	 * @Title getCard
	 * @param userId
	 * @description 根据用户ID查找名片
	 * @return
	 */
	@GetMapping("/{userId}")
	protected Object getCard(@PathVariable Long userId){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iUserCardService.getCard(userId));  
	}
}
