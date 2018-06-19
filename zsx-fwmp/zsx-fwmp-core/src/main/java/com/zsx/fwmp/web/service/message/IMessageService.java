package com.zsx.fwmp.web.service.message;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.pojo.Message;

public interface IMessageService extends IService<Message> {

	Object deleteMessageByIds(Long[] ids);

	Object searchMessage(Map<String, Object> map);

}
