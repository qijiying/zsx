package com.zsx.fwmp.web.service.area.impl;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zsx.dao.basicedata.ProvincesMapper;
import com.zsx.framework.exception.SystemException;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.service.area.IProvincesService;
import com.zsx.model.dto.CascadeDto;
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
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: getCodeByProvincesName</p> 
	  * <p>Description: </p> 
	  * @param privincesCode
	  * @return 
	  * @see com.zsx.service.basicedata.IProvincesService#getCodeByProvincesName(java.lang.Integer)
	 */
	@Override
	public String getCodeByProvincesName(String privincesCode) {
		Predicate<CascadeDto> provincesFilter = (n) -> n.getCode().equals(privincesCode);
		List<CascadeDto> provinces_list=provincesMapper.selectProvincesList();
		if(provinces_list==null || provinces_list.size() ==0){
			throw new SystemException(ResultEnum.CACHE_BASE_DATA_ERROR)
			.set("privincesCode", privincesCode);
		}
		return provinces_list.stream().filter(provincesFilter)
				.collect(Collectors.toList()).get(0).getName();
	}

}
