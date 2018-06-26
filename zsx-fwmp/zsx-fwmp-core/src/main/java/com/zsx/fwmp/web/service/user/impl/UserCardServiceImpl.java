package com.zsx.fwmp.web.service.user.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.zsx.dao.user.UserCardMapper;
import com.zsx.fwmp.web.service.user.IUserCardService;
import com.zsx.model.pojo.UserCard;

/**
 * @ClassName UserCardServiceImpl
 * @author lz
 * @description 用户名片业务实现类
 * @date 2018年6月22日11:27:36
 */
@Service
public class UserCardServiceImpl implements IUserCardService {

	@Autowired
	private UserCardMapper userCardMapper;
	
	/**
	 * @Title getCard
	 * @see com.zsx.fwmp.web.service.user.IUserCardService#getCard(java.lang.Long)
	 * @description 根据用户id获得名片
	 */
	@Override
	public Object getCard(Long userId) {
		List<UserCard> list = userCardMapper
								.selectList(new EntityWrapper<UserCard>()
										.where("user_id={0}", userId));
		if(list.size()>0) return list.get(0);
		else return list;
	}

}
