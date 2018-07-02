package com.zsx.fwmp.web.controller.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zsx.framework.base.BaseController;
import com.zsx.fwmp.web.others.base.ConstantClass;
import com.zsx.fwmp.web.others.base.Log;
import com.zsx.fwmp.web.service.push.JPushService;
import com.zsx.model.pojo.Message;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName PushController
 * @author lz
 * @description 推送controller
 * @date 2018年6月20日10:03:52
 */
@RestController
@RequestMapping("/api/web/push")
public class PushController extends BaseController {
	
	@Autowired
	private JPushService jPushService;
	
	/**
	 * @Title sendOtoO
	 * @param title
	 * @param image
	 * @param content
	 * @description 推一
	 * @return
	 */
	@ApiOperation(
			value="推送消息到一个用户",
			notes="推送消息到一个用户"
			)
	@ApiImplicitParam(name="message",value="消息实体类",required=true,paramType="path",dataType="Message")
	@PostMapping("/sendToOne")
	protected Object sendOToO(
			@RequestBody Message message
			){
		return jPushService
				.sendToOne(message,getRequest().getHeader(ConstantClass.USER_SOUCRE));
	}
	
	
	/**
	 * @Title sendAll
	 * @param title
	 * @param content
	 * @description 推全部
	 * @return
	 */
	@ApiOperation(
			value="推送消息到全部用户",
			notes="推送消息到全部用户"
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name="title",value="推送标题",required=true,paramType="path",dataType="String"),
		@ApiImplicitParam(name="content",value="推送内容",required=false,paramType="path",dataType="String")
	})
	@PostMapping("/sendAll")
	protected Object sendAll(
			@RequestParam String title,
			@RequestParam(value="content",required=false) String content
			){
		return jPushService.sendAll(title,content);
	}
	
	
	/**
	 * @Title sendAreas
	 * @description 根据区域推送
	 * @param title
	 * @param content
	 * @param areaCode
	 * @param areas
	 * @return
	 */
	@ApiOperation(
			value="根据区域推送",
			notes="根据区域推送"
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name="title",value="标题",required=true,paramType="path",dataType="String"),
		@ApiImplicitParam(name="content",value="推送内容",required=false,paramType="path",dataType="String"),
	})
	@PostMapping("/sendAreas")
	protected Object sendAreas(
			@RequestParam String title,
			@RequestParam(value="content",required=false) String content,
			@RequestParam Integer areaCode,
			@RequestParam Integer[] areas
			){
		Log.debug("title:"+title+"---content:"+content+"---areaCode:"+areaCode+"---areas:"+areas.toString(), PushController.class);
		return jPushService.sendAreas(title,content,areaCode,areas);
	}

}
