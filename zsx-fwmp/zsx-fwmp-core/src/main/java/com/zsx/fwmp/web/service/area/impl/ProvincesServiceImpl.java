package com.zsx.fwmp.web.service.area.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zsx.dao.basicedata.ProvincesMapper;
import com.zsx.fwmp.web.service.area.IProvincesService;
import com.zsx.model.pojo.Provinces;

@Service
public class ProvincesServiceImpl extends ServiceImpl<ProvincesMapper,Provinces> implements IProvincesService {

	@Autowired
	private ProvincesMapper provincesMapper;
	
	@Override
	public Object selectProvincesList() {
		return provincesMapper.selectList(new Wrapper<Provinces>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getSqlSegment() {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

}
