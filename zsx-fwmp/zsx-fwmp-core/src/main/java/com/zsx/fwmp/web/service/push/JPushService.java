package com.zsx.fwmp.web.service.push;

import com.zsx.model.pojo.Message;

public interface JPushService {

	/**
	 * @Title sendOToO
	 * @param title
	 * @param sendId
	 * @param receiveId
	 * @param image
	 * @param content
	 * @description 一推一service接口 
	 * @return
	 */
	Object sendToOne(Message message, String appSource);

	/**
	 * @Title sendAll
	 * @param title
	 * @param content
	 * @description 推送全部service接口
	 * @return
	 */
	Object sendAll(String title, String content);

	Object sendAreas(String title, String content, Integer areaCode, Integer[] areas);

	Message saveMessage(Message message);



}
