package com.zsx.fwmp.web.service.area.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.zsx.dao.basicedata.AreasMapper;
import com.zsx.dao.basicedata.CityMapper;
import com.zsx.dao.basicedata.ProvincesMapper;
import com.zsx.fwmp.web.service.area.IAreasService;
import com.zsx.model.dto.AreasDto;
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
	
	
}
