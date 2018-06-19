package com.zsx.fwmp.web.service.message.impl;

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
import com.zsx.fwmp.web.others.base.Log;
import com.zsx.fwmp.web.service.message.IMessageService;
import com.zsx.model.pojo.Message;
import com.zsx.model.pojo.User;

/**
 * @ClassName MessageServiceImpl
 * @author lz
 * @descrption 消息业务实现类
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {


	@Autowired
	private UserMapper userMapper;
	
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

}
