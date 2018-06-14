package com.zsx.fwmp.web.service.area.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zsx.dao.basicedata.CityMapper;
import com.zsx.fwmp.web.service.area.ICityService;
import com.zsx.model.pojo.City;

@Service
public class CityServiceImpl extends ServiceImpl<CityMapper,City> implements ICityService {

	@Autowired
	private CityMapper cityMapper;
	
	@Override
	public Object selectCityList() {
		return cityMapper.selectList(new Wrapper<City>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getSqlSegment() {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

}
