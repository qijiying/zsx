package com.zsx.dao.advert;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.model.dto.AdvertDto;
import com.zsx.model.pojo.Advert;

public interface AdvertMapper extends BaseMapper<Advert> {

	public Page<Advert> selectAdByAreadAndPostionPage(@Param("advert") Advert advert,Page<Advert> page);

	public List<Advert> selectAaByPage(@Param("advert") Advert advert,  Page<Advert> page);

	public List<AdvertDto> selectAdvertByAreaAndStatusAndPage(@Param("advert") Advert advert, @Param("current")Integer current,@Param("size")Integer size);

	public int disAdvertById(@Param("id")long id,@Param("flag")int flag);

	public AdvertDto getAdvertDto(Long id);

	public List<AdvertDto> selectAdByPage(@Param("current")Integer current,@Param("size")Integer size);

	public int getAdvertPageOfCount();

	public int getSearchAdvertPageOfCount(@Param("advert") Advert advert);
}
