package com.zsx.fwmp.web.service.area;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.pojo.City;

public interface ICityService extends IService<City> {

	Object selectCityList();

		/**
	  * 
	  * @Title: getCodeByCityName 
	  * @Description: 根据code获取对应名称
	  * @param @param privincesCode
	  * @param @param cityCode
	  * @param @return    设定文件 
	  * @return String    返回类型 
	  * @throws
	 */
	String getCodeByCityName(String privincesCode, String cityCode);

	List<City> selectCity();

}
