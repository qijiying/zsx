package com.zsx.fwmp.web.controller.message;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Maps;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.service.message.IMessageService;
import com.zsx.model.pojo.Message;

/**
 * @ClassName MessageController
 * @author lz
 * @description 消息控制层
 * @date 2018年6月14日14:45:45
 */
@RestController
@RequestMapping("/api/web/message")
public class MessageController {

	@Autowired
	private IMessageService iMessageService;
	
	/**
	 * @Title addMessage
	 * @param message
	 * @description 添加消息
	 * @return
	 */
	@PostMapping("/add")
	protected Object addMessage(@RequestBody Message message){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iMessageService.insert(message));
	}
	
	/**
	 * @Title dataGridMessage
	 * @param current
	 * @param size
	 * @description 初始化消息列表
	 * @return
	 */
	@GetMapping("/dataGrid")
	protected Object dataGridMessage(
			@RequestParam(value="current",required=false) Integer current,
			@RequestParam(value="size",required=false) Integer size
			){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS
						, iMessageService
						.selectPage(new Page<Message>(current==null?1:current,size==null?10:size)));
	}
	
	/**
	 * @Title dataSearchMessage
	 * @param sendUser
	 * @param receiveUser
	 * @param sendTitle
	 * @param sendContent
	 * @param current
	 * @param size
	 * @description 搜索消息controller
	 * @return
	 */
	@PostMapping("/search")
	protected Object dataSearchMessage(
			 @RequestParam(value="sendUser",required=false) String sendUser,
			 @RequestParam(value="receiveUser",required=false) String receiveUser,
			 @RequestParam(value="sendTitle",required=false) String sendTitle,
			 @RequestParam(value="sendContent",required=false) String sendContent,
			 @RequestParam(value="current",required=false) Integer current,
			 @RequestParam(value="size",required=false) Integer size
			){
		Map<String,Object> map = Maps.newHashMap();
		map.put("sendUser", sendUser);
		map.put("receiveUser", receiveUser);
		map.put("sendTitle", sendTitle);
		map.put("sendContent", sendContent);
		map.put("current", current);
		map.put("size", size);
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iMessageService.searchMessage(map));
	}
	
	/**
	 * @Title uodateMessage
	 * @param message
	 * @description 更新消息
	 * @return
	 */
	@PostMapping("/update")
	protected Object updateMessage(@RequestBody Message message){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iMessageService.updateById(message));
	}
	
	
	/**
	 * @Title deleteMessages
	 * @param ids
	 * @description 删除消息
	 * @return
	 */
	@PostMapping("/delete")
	protected Object deleteMessages(@RequestParam Long[] ids){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, iMessageService.deleteMessageByIds(ids));
	}
	
}
