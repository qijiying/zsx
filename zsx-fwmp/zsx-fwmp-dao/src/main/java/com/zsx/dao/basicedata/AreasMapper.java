package com.zsx.dao.basicedata;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zsx.framework.exception.SystemException;
import com.zsx.model.dto.CascadeDto;
import com.zsx.model.pojo.Areas;

public interface AreasMapper extends BaseMapper<Areas> {

	List<Areas> selectByCityId(Integer cityId);

	/**
	  * 
	  * @Title: selectAreasList 
	  * @Description: 根据城市ID查询当前区域，返回统一数据类型
	  * @param @param cityId
	  * @param @return    设定文件 
	  * @return List<CascadeDto>    返回类型 
	  * @throws
	 */
	List<CascadeDto> selectAreasListByCity(@Param("cityId") int cityId)throws SystemException;
	
	
	List<CascadeDto> selectAreasList()throws SystemException;

}
