package com.zsx.fwmp.web.service.push.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.zsx.dao.message.MessageMapper;
import com.zsx.framework.exception.SystemException;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.enums.MessageEnum;
import com.zsx.fwmp.web.others.util.UserUtil;
import com.zsx.fwmp.web.service.push.JPushService;
import com.zsx.fwmp.web.service.user.IUserService;
import com.zsx.model.pojo.Message;
import com.zsx.model.pojo.User;
import com.zsx.thirdparty.base.constant.ThirdpartyConstant;
import com.zsx.thirdparty.jpush.AndroidJpushService;
import com.zsx.thirdparty.jpush.IosJpushService;

@Service
public class JPushServiceImpl implements JPushService {
	
	@Autowired
	private IUserService iUserService;
	
	@Autowired
	MessageMapper messageMapper;

	@Autowired
	IosJpushService iosJpushService;
	
	@Autowired
	AndroidJpushService androidJpushService;

	/**
	 * @Title sendOToO
	 * @see com.zsx.fwmp.web.service.push.JPushService#sendOtoO(java.lang.String, java.lang.Long, java.lang.Long, java.lang.String, java.lang.String)
	 * @description 一推一业务实现类
	 */
	@Override
	public Object sendToOne(Message message, String appSource) {
		//Message mes= saveMessage(message);  //web端推送不存储消息
		User user= iUserService.getCacheUser(message.getReceiveUserId());
		try {
			if(null != user.getAppSoucre()){
				UserUtil.jpush(user.getAppSoucre().equals(1)?iosJpushService:androidJpushService,message.getReceiveUserId().toString(),message.getSendTitle(), message.getSendContent());
				return ResultEnum.SUCCESS;
			}
			return ResultEnum.FAILED;
		} catch (Exception e) {
			return ResultEnum.FAILED;
		}
	}
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: saveMessage</p> 
	  * <p>Description: </p> 
	  * @param message 
	  * @see com.zsx.service.message.IMessageService#saveMessage(com.zsx.model.pojo.Message)
	 */
	@Override
	public Message saveMessage(Message message) {
		//User user= iUserService.getCacheUser(message.getSendUserId());
		if(message.getMessageType() ==MessageEnum.POST_COMMENT.getKey() || message.getMessageType()==MessageEnum.POST_THUMP.getKey()  || message.getMessageType() ==MessageEnum.GROUP_PUSH.getKey()){
			if(null == message.getEventId()){
				throw new SystemException(ResultEnum.CHECK_PARAMETER_NOT_POSTION);
			}
		}
/*		switch (MessageEnum.getByKey(message.getMessageType())) {
			case HAIL_FELLOW :
				message.setSendTitle(MessageFormat.format(PropertiesListenerConfig.getProperty("hail.fellow.message"), "蹡蹡"));
				break;
			case POST_THUMP :
				message.setSendTitle(PropertiesListenerConfig.getProperty("thumbUp.message"));
				break;
			case POST_COMMENT :
				message.setSendTitle(MessageFormat.format(PropertiesListenerConfig.getProperty("comment.message"), "蹡蹡"));
				break;
			case PRIVATE_LETTER :
				message.setSendTitle(MessageFormat.format(PropertiesListenerConfig.getProperty("private.letter.message"), "蹡蹡"));
				break;
			case GROUP_PUSH :
				message.setSendTitle(MessageFormat.format(PropertiesListenerConfig.getProperty("group.add.user.message"), "蹡蹡"));
				break;
			default :
				message.setSendTitle(PropertiesListenerConfig.getProperty("default.message"));
				break;
		}*/
		messageMapper.insert(message);
		return message;
	}

	/**
	 * @Title sendAll
	 * @see com.zsx.fwmp.web.service.push.JPushService#sendAll(java.lang.String, java.lang.String)
	 * @description 推送全部
	 */
	@Override
	public Object sendAll(String title, String content) {
		try {
			//推送全部
			//UserUtil.jpushTitle(iosJpushService, "", title, content);
			androidJpushService.setTitle(title);
			androidJpushService.setMessage(content);
			if(ThirdpartyConstant.profilesVlaue.equals("prod")){
				androidJpushService.setEnvironment(true); 
			}else if(ThirdpartyConstant.profilesVlaue.equals("test")){
				androidJpushService.setEnvironment(false); 
			}else{
				throw new SystemException(ResultEnum.SYSTEM_LOAD_PROPERTIES_DATA_IS_NULL);
			}
			androidJpushService.pushMessageAll();
			return ResultEnum.SUCCESS;
		} catch (Exception e) {
			return ResultEnum.FAILED;
		}
	}

	@Override
	public Object sendAreas(String title, String content, Integer areaCode, Integer[] areas) {
        //User user = iUserService.getCacheUser(Long.parseLong("447936278392832"));
        //Log.debug(user.toString(), JPushService.class);
		//推送android
		int code = 360100;
		List<Object> list = iUserService.selectObjs(new EntityWrapper<User>().setSqlSelect("id").where("home_city_code={0}", code));
		String[] ids = new String[list.size()];
	    for (int i = 0; i < list.size(); i++) {
		ids[i]=list.get(i).toString();
              System.out.println(ids[i]);
	    }
		UserUtil.jpushAreas(androidJpushService, "110101", title, content);
		return ResultEnum.SUCCESS;
	}

}
