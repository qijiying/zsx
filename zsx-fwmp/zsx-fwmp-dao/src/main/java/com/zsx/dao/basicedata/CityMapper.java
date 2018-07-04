package com.zsx.dao.basicedata;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zsx.framework.exception.SystemException;
import com.zsx.model.pojo.City;
import com.zsx.model.webdto.CascadeDto;

public interface CityMapper extends BaseMapper<City> {

	List<City> selectByProviceId(Integer provinceId);

	/**
	  * 
	  * @Title: selectCityList 
	  * @Description: 根据省会ID查询当前城市，返回统一数据类型
	  * @param @param pId
	  * @param @return    设定文件 
	  * @return List<CascadeDto>    返回类型 
	  * @throws
	 */
	List<CascadeDto> selectCityList(@Param("pId") int pId)throws SystemException;

}
