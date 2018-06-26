package com.zsx.fwmp.web.service.message;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.dto.MessageDto;
import com.zsx.model.pojo.Message;

public interface IMessageService extends IService<Message> {

	Object deleteMessageByIds(Long[] ids);

	Object searchMessage(Map<String, Object> map);

	boolean checkMessageSended(Long sendUserId, Long receiveUserId, Integer messageType);

	void sendMessage(Message message);

	/**
	  * 
	  * @Title: saveMessage 
	  * @Description: 保存消息
	  * @param @param message    设定文件 
	  * @return void    返回类型 
	  * @throws
	 */
	Message saveMessage(Message message);

	/**
	  * 
	  * @Title: getMessagePage 
	  * @Description: 获取我的消息列表
	  * @param @param userId
	  * @param @param type
	  * @param @param page
	  * @param @return    设定文件 
	  * @return Page<MessageDto>    返回类型 
	  * @throws
	 */
	Page<MessageDto> getMessagePage(Long userId,Integer type,Page<MessageDto> page);

}
