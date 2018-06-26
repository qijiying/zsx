package com.zsx.fwmp.web.service.push.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.base.Log;
import com.zsx.fwmp.web.others.util.UserUtil;
import com.zsx.fwmp.web.service.push.JPushService;
import com.zsx.fwmp.web.service.user.IUserService;
import com.zsx.model.pojo.User;
import com.zsx.thirdparty.jpush.AndroidJpushService;
import com.zsx.thirdparty.jpush.IosJpushService;

@Service
public class JPushServiceImpl implements JPushService {
	
	@Autowired
	private IUserService iUserService;

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
	public Object sendToOne(String title, Long receiveId, String content) {
		User user= iUserService.getCacheUser(receiveId);
		try {
			if(null != user.getAppSoucre()){
				UserUtil.jpushTitle(user.getAppSoucre().equals(1)?iosJpushService:androidJpushService,receiveId.toString(),title, content);
				return ResultEnum.SUCCESS;
			}
			return ResultEnum.FAILED;
		} catch (Exception e) {
			return ResultEnum.FAILED;
		}
	}

	/**
	 * @Title sendAll
	 * @see com.zsx.fwmp.web.service.push.JPushService#sendAll(java.lang.String, java.lang.String)
	 * @description 推送全部
	 */
	@Override
	public Object sendAll(String title, String content) {
		try {
			//推送ios
			UserUtil.jpushTitle(iosJpushService, "", title, content);
			//推送android
			UserUtil.jpushTitle(androidJpushService, "", title, content);
			return ResultEnum.SUCCESS;
		} catch (Exception e) {
			return ResultEnum.FAILED;
		}
	}

	@Override
	public Object sendAreas(String title, String content, Integer areaCode, Integer[] areas) {
        User user = iUserService.getCacheUser(Long.parseLong("447936278392832"));
        Log.debug(user.toString(), JPushService.class);
		//推送ios
		//UserUtil.jpushTitle(androidJpushService, "447936278392832", title, content);
		return ResultEnum.SUCCESS;
	}

}
