package com.zsx.fwmp.web.controller.message;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.zsx.fwmp.web.others.enums.MessageEnum;
import com.zsx.fwmp.web.others.util.Assert;
import com.zsx.fwmp.web.service.message.IMessageService;
import com.zsx.model.pojo.Message;
import com.zsx.model.webdto.MessageDto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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
	
	
	@PostMapping("/send")
	protected Object sendMessage(@RequestBody Message message){
		Assert.isNull(message.getSendUserId(),message.getReceiveUserId(),message.getMessageType());
		
		if(!message.getMessageType().equals(MessageEnum.PRIVATE_LETTER.getKey())){  //私信不校验
			if(!iMessageService.checkMessageSended(message.getSendUserId(), message.getReceiveUserId(), message.getMessageType())){
				return ResultfulFactory.getInstance().creator(ResultEnum.CHECK_MESSAGE_EXISTS);
			}
		}
		iMessageService.sendMessage(message);
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS);
	}
	
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
	
	
	/**
	  * 
	  * @Title: getMessage 
	  * @Description: 获取用户消息
	  * @param @param userId
	  * @param @param messageType
	  * @param @param current
	  * @param @param size
	  * @param @return    设定文件 
	  * @return Object    返回类型 
	  * @throws
	 */
	@ApiOperation(
			value="获取用户消息", 
			notes="获取用户消息")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户ID",required=true,paramType="path",dataType="int"),
		@ApiImplicitParam(name="t",value="消息类型",required=false,paramType="query",dataType="int"),
		@ApiImplicitParam(name="c",value="当前页码",required=true,paramType="query",dataType="int"),
		@ApiImplicitParam(name="s",value="数据个数",required=true,paramType="query",dataType="int")
	})
	@GetMapping("/{userId}")
	public Object getMessage(@PathVariable Long userId,
			@RequestParam(value="t",required=false) Integer messageType,
			@RequestParam("c")Integer current,
			@RequestParam("s")Integer size){
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS,iMessageService.getMessagePage(userId,messageType, new Page<MessageDto>(current, size)));
	}
	
}
