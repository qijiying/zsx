package com.zsx.fwmp.web.service.message.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zsx.dao.message.MessageMapper;
import com.zsx.fwmp.web.others.base.Log;
import com.zsx.fwmp.web.service.message.IMessageService;
import com.zsx.model.pojo.Message;

/**
 * @ClassName MessageServiceImpl
 * @author lz
 * @descrption 消息业务实现类
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {


	
	/**
	 * @Title deleteMessageByIds
	 * @see com.zsx.fwmp.web.service.message.IMessageService#deleteMessageByIds(java.lang.Long[])
	 * @description 批量删除消息
	 */
	@Override
	public Object deleteMessageByIds(Long[] ids) {
        StringBuffer sql = new StringBuffer(" ("+ids[0].toString());
		for (int i = 1; i < ids.length; i++) {
			sql.append(","+ids[i]);
		}
		sql.append(")");
		Log.debug("删除消息的id集："+sql, MessageServiceImpl.class);
		return delete(new EntityWrapper<Message>().where(" id in "+sql));
	}

}
