package com.zsx.dao.basicedata;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zsx.model.pojo.City;

public interface CityMapper extends BaseMapper<City> {

	List<City> selectByProviceId(Integer provinceId);

}
