package com.zsx.fwmp.web.service.area;

import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.pojo.Areas;
import com.zsx.model.pojo.City;

public interface IAreasService extends IService<Areas> {

	Object selectAreasList();

		/**
	  * 
	  * @Title: getCityNameByAreasCode 
	  * @Description: 根据区域code查询城市名称 
	  * @param @param areasCode
	  * @param @return    设定文件 
	  * @return String    返回类型 
	  * @throws
	 */
	City getCityNameByAreasCode(String areasCode);
	
	/**
	  * 
	  * @Title: getCodeByAreasName 
	  * @Description:根据城市code和区域code返回区域名称 
	  * @param @param cityCode
	  * @param @param areasCode
	  * @param @return    设定文件 
	  * @return String    返回类型 
	  * @throws
	 */
	String getCodeByAreasName(String cityCode, String areasCode);

}
