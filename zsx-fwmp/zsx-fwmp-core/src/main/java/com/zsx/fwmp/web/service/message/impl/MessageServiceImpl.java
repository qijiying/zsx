package com.zsx.fwmp.web.service.message.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zsx.dao.message.MessageMapper;
import com.zsx.dao.user.UserMapper;
import com.zsx.framework.exception.SystemException;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.base.Log;
import com.zsx.fwmp.web.others.enums.MessageEnum;
import com.zsx.fwmp.web.others.listener.PropertiesListenerConfig;
import com.zsx.fwmp.web.others.util.UserUtil;
import com.zsx.fwmp.web.service.message.IMessageService;
import com.zsx.fwmp.web.service.user.IUserService;
import com.zsx.model.dto.MessageDto;
import com.zsx.model.pojo.Message;
import com.zsx.model.pojo.User;

import com.zsx.thirdparty.jpush.AndroidJpushService;
import com.zsx.thirdparty.jpush.IosJpushService;



/**
 * @ClassName MessageServiceImpl
 * @author lz
 * @descrption 消息业务实现类
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {


	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private MessageMapper messageMapper;
	
	@Autowired
	private IUserService iUserService;
	
	@Autowired
	IosJpushService iosJpushService;
	
	@Autowired
	AndroidJpushService androidJpushService;
	
	
	/**
	 * @Title deleteMessageByIds
	 * @see com.zsx.fwmp.web.service.message.IMessageService#deleteMessageByIds(java.lang.Long[])
	 * @description 批量删除消息
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Object deleteMessageByIds(Long[] ids) {
        StringBuffer sql = new StringBuffer(" ("+ids[0].toString());
		for (int i = 1; i < ids.length; i++) {
			sql.append(","+ids[i]);
		}
		sql.append(")");
		Log.debug("删除消息的id集："+sql, MessageServiceImpl.class);
		return delete(new EntityWrapper<Message>().where(" id in "+sql));
	}

	/**
	 * @Title searchMessage
	 * @see com.zsx.fwmp.web.service.message.IMessageService#searchMessage(java.util.Map)
	 * @description 搜索消息业务实现类
	 */
	@Override
	public Object searchMessage(Map<String, Object> map) {		
		Page<Message> page = searchMessageMethod(map);		
		return page;
	}
	
	
	/**
	 * @Title searchMessageMethod
	 * @param map
	 * @description 搜索消息方法
	 * @return
	 */
	protected Page<Message> searchMessageMethod(Map<String,Object> map){
		String sendUser =(String) map.get("sendUser");
		String receiveUser =(String) map.get("receiveUser");
		//模糊查找send和receive和用户ID
		List<Object> listSend = userMapper.selectObjs(new EntityWrapper<User>()
								.setSqlSelect("id")
								.like("login_username", sendUser) //发送人 like 用户登录名
								.or().like("nick_name", sendUser)); //发送人 like 用户昵称
		List<Object> listRcie = userMapper.selectObjs(new EntityWrapper<User>()
								.setSqlSelect("id")
								.like("login_username", receiveUser) //接收人 like 用户登录名
								.or().like("nick_name", receiveUser)); //接收人 like 用户昵称
		
		Integer current = ((Integer) map.get("current")==null?1:(Integer) map.get("current"));
		Integer size = ((Integer) map.get("size")==null?10:(Integer) map.get("size"));
		String limit = " limit "+(current-1)*size+","+size;
			
		String sendTitle =(String) map.get("sendTitle");
		String sendContent =(String) map.get("sendContent");
		
		//根据搜索条件查找消息
		List<Message> messageList = selectList(new EntityWrapper<Message>()
									.in("send_user_id", listSend)
									.in("receive_user_id", listRcie)
									.like("send_title", sendTitle)
									.like("send_content", sendContent)
									.last(limit));
		//根据搜索条件查找消息总数
		Integer count = selectCount(new EntityWrapper<Message>()
									.in("send_user_id", listSend)
									.in("receive_user_id", listRcie)
									.like("send_title", sendTitle)
									.like("send_content", sendContent)
									.last(limit));                           		                           
		
		Page<Message> page = new Page<>(current,size);
		page.setRecords(messageList);
		page.setTotal(count);
		return page;
	}
	
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: getMessagePage</p> 
	  * <p>Description: </p> 
	  * @param type
	  * @param page
	  * @return 
	  * @see com.zsx.service.message.IMessageService#getMessagePage(java.lang.Integer, com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public Page<MessageDto> getMessagePage(Long userId,Integer type, Page<MessageDto> page) {
		page.setRecords(messageMapper.getMessagePage(userId,type,page));
		return page;
	}

	/**
	 * @Title checkMessageSended
	 * @see com.zsx.fwmp.web.service.message.IMessageService#checkMessageSended(java.lang.Long, java.lang.Long, java.lang.Integer)
	 * @description 检查消息是否重复
	 */
	@Override
	public boolean checkMessageSended(Long sendUserId, Long receiveUserId, Integer type) {
		List<Message> message=messageMapper.selectList(new EntityWrapper<Message>().where("send_user_id={0}", sendUserId).and("receive_user_id={0}", receiveUserId)
				.and("message_type!={0}", MessageEnum.PRIVATE_LETTER.getKey()).and("add_friends_status={0}", MessageEnum.NUAGREE.getKey())
				.and("message_type={0}", type));
		
		if(null == message || message.size() == 0){
			return true;
		};
		return false;
	}

	@Override
	public void sendMessage(Message message) {
		Message mes= saveMessage(message);
		User user= iUserService.getCacheUser(message.getReceiveUserId());
		if(null != user.getAppSoucre()){
			UserUtil.jpush(user.getAppSoucre().equals(1)?iosJpushService:androidJpushService,message.getReceiveUserId().toString(),"私信通知", mes.getSendTitle());
		}		
	}

	@Override
	public Message saveMessage(Message message) {
		User user= iUserService.getCacheUser(message.getSendUserId());
		if(message.getMessageType() ==MessageEnum.POST_COMMENT.getKey() || message.getMessageType()==MessageEnum.POST_THUMP.getKey()  || message.getMessageType() ==MessageEnum.GROUP_PUSH.getKey()){
			if(null == message.getEventId()){
				throw new SystemException(ResultEnum.CHECK_PARAMETER_NOT_POSTION);
			}
		}
		switch (MessageEnum.getByKey(message.getMessageType())) {
			case HAIL_FELLOW :
				message.setSendTitle(MessageFormat.format(PropertiesListenerConfig.getProperty("hail.fellow.message"), user.getNickName()));
				break;
			case POST_THUMP :
				message.setSendTitle(PropertiesListenerConfig.getProperty("thumbUp.message"));
				break;
			case POST_COMMENT :
				message.setSendTitle(MessageFormat.format(PropertiesListenerConfig.getProperty("comment.message"), user.getNickName()));
				break;
			case PRIVATE_LETTER :
				message.setSendTitle(MessageFormat.format(PropertiesListenerConfig.getProperty("private.letter.message"), user.getNickName()));
				break;
			case GROUP_PUSH :
				message.setSendTitle(MessageFormat.format(PropertiesListenerConfig.getProperty("group.add.user.message"), user.getNickName()));
				break;
			default :
				message.setSendTitle(PropertiesListenerConfig.getProperty("default.message"));
				break;
		}
		messageMapper.insert(message);
		return message;
	}

}
