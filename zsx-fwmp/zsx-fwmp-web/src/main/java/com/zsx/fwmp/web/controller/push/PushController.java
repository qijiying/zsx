package com.zsx.fwmp.web.controller.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zsx.fwmp.web.others.base.Log;
import com.zsx.fwmp.web.service.push.JPushService;

/**
 * @ClassName PushController
 * @author lz
 * @description 推送controller
 * @date 2018年6月20日10:03:52
 */
@RestController
@RequestMapping("/api/web/push")
public class PushController {
	
	@Autowired
	private JPushService jPushService;
	
	/**
	 * @Title sendOtoO
	 * @param title
	 * @param image
	 * @param content
	 * @description 一推一
	 * @return
	 */
	@PostMapping("/sendToOne")
	protected Object sendOToO(
			@RequestParam String title,
			@RequestParam Long receiveId,
			@RequestParam(value="content",required=false) String content
			){
		return jPushService.sendToOne(title,receiveId,content);
	}
	
	
	/**
	 * @Title sendAll
	 * @param title
	 * @param content
	 * @description 推全部
	 * @return
	 */
	@PostMapping("/sendAll")
	protected Object sendAll(
			@RequestParam String title,
			@RequestParam(value="content",required=false) String content
			){
		return jPushService.sendAll(title,content);
	}
	
	
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
