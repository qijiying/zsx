package com.zsx.fwmp.web.service.area.impl;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.zsx.dao.basicedata.AreasMapper;
import com.zsx.dao.basicedata.CityMapper;
import com.zsx.dao.basicedata.ProvincesMapper;
import com.zsx.framework.exception.SystemException;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.service.area.IAreasService;
import com.zsx.fwmp.web.service.area.ICityService;
import com.zsx.model.dto.AreasDto;
import com.zsx.model.dto.CascadeDto;
import com.zsx.model.dto.CityDto;
import com.zsx.model.dto.ProvicesDto;
import com.zsx.model.pojo.Areas;
import com.zsx.model.pojo.City;
import com.zsx.model.pojo.Provinces;

@Service
public class AreasServiceImpl extends ServiceImpl<AreasMapper,Areas> implements IAreasService {
	
	@Autowired
	private AreasMapper areasMapper;
	
	@Autowired
	private CityMapper cityMapper;
	
	@Autowired
	private ProvincesMapper provincesMapper;
	
	@Autowired
	ICityService iCityService;
	

	/**
	 * @Title selectAreasList
	 * @see com.zsx.fwmp.service.area.IAreasService#selectAreasList
	 * @description 处理地区的业务实现方法
	 */
	@Override
	public Object selectAreasList() {
		//获取省份列表
		EntityWrapper<Provinces> wrapperProvince=new EntityWrapper<>();
		List<Provinces> provincesList = provincesMapper.selectList(wrapperProvince);
		//获取市区列表
		EntityWrapper<City> wrapperCity=new EntityWrapper<>();
		List<City> listCity = cityMapper.selectList(wrapperCity);
		//获取区县列表
		EntityWrapper<Areas> wrapperAreas=new EntityWrapper<>();
		List<Areas> listAreas = areasMapper.selectList(wrapperAreas);
				
		//对省份进行排序
		Collections.sort(provincesList,(a,b)->a.getProvinceId().compareTo(b.getProvinceId()));
		List<ProvicesDto> list = Lists.newArrayList();

		//遍历省份
		provincesList.forEach(item->{
			ProvicesDto pDto = new ProvicesDto();
			pDto.setValue(item.getProvinceId());
			pDto.setLabel(item.getProvinceName());
			//创建省份的children（城市集合）
			List<CityDto> coList = Lists.newArrayList();
			int provincesId = item.getProvinceId();
            //遍历城市
			listCity.forEach(itemCity->{
				//根据省份查找城市
                if(itemCity.getProvinceId()==provincesId){
    				CityDto cDto = new CityDto();
    				cDto.setValue(itemCity.getCityId());
    				cDto.setLabel(itemCity.getCityName());
    				//创建城市的children（区县集合）
    				List<AreasDto> aoList = Lists.newArrayList();
    				int cityId = itemCity.getCityId();
    				listAreas.forEach(itemAreas->{
    					//根据城市查找对应的children
    					if(itemAreas.getCityId()==cityId){
    						AreasDto aoAreas = new AreasDto();
        					aoAreas.setValue(itemAreas.getAreaId());
        					aoAreas.setLabel(itemAreas.getAreaName());
        					aoList.add(aoAreas);
    					}
    					//在cityDto里插入children
    					cDto.setChildren(aoList);
    				});
    			    //将城市归入集合
                    coList.add(cDto);
                }
			});
			//在ProvincesDto里插入children
			pDto.setChildren(coList);	
			//将省份归入集合
			list.add(pDto);
		});
		 return list;
	}
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: getCodeByAreasName</p> 
	  * <p>Description: 根据区域code获取区域名称，缓存过滤</p> 
	  * @param areasCode
	  * @return 
	  * @see com.zsx.service.basicedata.IAreasService#getCodeByAreasName(java.lang.Integer)
	 */
	@Override
	public String getCodeByAreasName(String cityCode,String areasCode) {
		Predicate<CascadeDto> areasFilter = (n) -> n.getCode().equals(areasCode);
		List<CascadeDto> ares_list=areasMapper.selectAreasListByCity(Integer.parseInt(cityCode));
		if(ares_list==null || ares_list.size() ==0){
			throw new SystemException(ResultEnum.CACHE_BASE_DATA_ERROR);
		}
		ares_list=ares_list.stream().filter(areasFilter)
				.collect(Collectors.toList());
		if(null == ares_list || ares_list.size() == 0){
			throw new SystemException(ResultEnum.CACHE_BASE_DATA_ERROR).set("cityCode:", cityCode).set("areasCode:", areasCode);
		}
		return ares_list.get(0).getName();
	}

	/**
	  * (非 Javadoc) 
	  * <p>Title: getCityNameByAreasCode</p> 
	  * <p>Description: 根据区域code反查城市名称</p> 
	  * @param areasCode
	  * @return 
	  * @see com.zsx.service.basicedata.IAreasService#getCityNameByAreasCode(java.lang.String)
	 */
	@Override
	public City getCityNameByAreasCode(String areasCode) {
		Predicate<CascadeDto> areasFilter = (n) -> n.getCode().equals(areasCode);
		List<CascadeDto> ares_list=areasMapper.selectAreasList().stream().filter(areasFilter)
				.collect(Collectors.toList());
		if(ares_list==null || ares_list.size() ==0){
			throw new SystemException(ResultEnum.CACHE_BASE_DATA_ERROR);
		}
		Integer cityCode=Integer.parseInt(ares_list.get(0).getSuperCode());
		Predicate<City> cityFilter = (n) -> n.getCityId().equals(cityCode);
		List<City> city_List=iCityService.selectCity();
		city_List=city_List.stream().filter(cityFilter)
				.collect(Collectors.toList());
		if(city_List==null || city_List.size() ==0){
			throw new SystemException(ResultEnum.CACHE_BASE_DATA_ERROR);
		}
		return city_List.get(0);
	}
	
}
