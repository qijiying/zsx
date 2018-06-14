package com.zsx.fwmp.web.service.area;

import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.pojo.City;

public interface ICityService extends IService<City> {

	Object selectCityList();

}
