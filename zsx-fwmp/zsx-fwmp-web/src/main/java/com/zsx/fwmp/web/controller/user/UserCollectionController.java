package com.zsx.fwmp.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.enums.UserCollectionEnum;
import com.zsx.fwmp.web.service.user.IUserCollectionService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * @ClassName UserCollectionController
 * @author lz
 * @description 用户收藏控制层 
 * @date 2018年6月25日10:42:14
 */
@RestController
@RequestMapping("/api/web/user/collection")
public class UserCollectionController {
	
	@Autowired
	private IUserCollectionService iUserCollectionService;
	
	/**
	  * 
	  * @Title: getCollection 
	  * @Description: 获取用户收藏列表
	  * @param @param type
	  * @param @param userId
	  * @param @param current
	  * @param @param size
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="获取用户收藏列表", 
			notes="获取用户收藏列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户ID",required=true,paramType="path",dataType="int"),
		@ApiImplicitParam(name="t",value="获取的类型",required=true,paramType="query",dataType="int"),
		@ApiImplicitParam(name="c",value="当前页码",required=true,paramType="query",dataType="int"),
		@ApiImplicitParam(name="s",value="数据个数",required=true,paramType="query",dataType="int")
	})
	@GetMapping("/{userId}")
	public Object getCollection(@RequestParam("t") Integer type,@PathVariable("userId") Long userId , @RequestParam("c") Integer current,@RequestParam("s") Integer size){
		if(!UserCollectionEnum.checkKey(type)){
			return ResultfulFactory.getInstance().creator(ResultEnum.CHECK_PARAMETER_NOT_POSTION);
		}
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,iUserCollectionService.getUserCollection(type, userId, new Page<>(current, size)));
	}

}
