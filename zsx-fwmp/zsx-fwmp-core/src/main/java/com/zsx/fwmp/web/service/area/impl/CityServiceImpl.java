package com.zsx.fwmp.web.service.area.impl;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zsx.dao.basicedata.CityMapper;
import com.zsx.framework.exception.SystemException;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.service.area.ICityService;
import com.zsx.model.dto.CascadeDto;
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
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: selectCity</p> 
	  * <p>Description: 获取所有城市信息</p> 
	  * @return
	  * @throws SystemException 
	  * @see com.zsx.service.basicedata.ICityService#selectCity()
	 */
	@Override
	public List<City> selectCity() throws SystemException {
		return cityMapper.selectList(null);
	}
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: getCodeByCityName</p> 
	  * <p>Description: 根据code获取名称</p> 
	  * @param privincesCode
	  * @param cityCode
	  * @return 
	  * @see com.zsx.service.basicedata.ICityService#getCodeByCityName(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public String getCodeByCityName(String privincesCode, String cityCode) {
		Predicate<CascadeDto> cityFilter = (n) -> n.getCode().equals(cityCode);
		List<CascadeDto> city_list=cityMapper.selectCityList(Integer.parseInt(privincesCode));
		if(city_list==null || city_list.size() ==0){
			throw new SystemException(ResultEnum.CACHE_BASE_DATA_ERROR);
		}
		return city_list.stream().filter(cityFilter)
				.collect(Collectors.toList()).get(0).getName();
	}

}
