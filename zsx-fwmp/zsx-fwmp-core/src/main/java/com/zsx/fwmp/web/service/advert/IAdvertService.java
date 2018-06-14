package com.zsx.fwmp.web.service.advert;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.dto.AdvertDto;
import com.zsx.model.pojo.Advert;

public interface IAdvertService extends IService<Advert> {
	

	/**
	  * 
	  * @Title: selectAdByAreadAndPostion 
	  * @Description: 根据位置和范围查询广告 -分页
	  * @param @param advert
	  * @param @return    设定文件 
	  * @return List<Advert>    返回类型 
	  * @throws
	 */
	public Page<Advert> selectAdByAreadAndPostionPage(Advert advert,Page<Advert> page);


	public List<Advert> selectAaByPage(Advert advert, Page<Advert> page);


	public Object selectAdvertByAreaAndStatusAndPage(Advert advert,Integer current,Integer size);


	public Object disAdvertById(long id);


	public AdvertDto get(Long id);


	public Page<AdvertDto> selectAdByPage(Integer current,Integer size);
}

