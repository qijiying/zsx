package com.zsx.fwmp.web.service.area;

import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.pojo.Provinces;

public interface IProvincesService extends IService<Provinces> {

	Object selectProvincesList();

}
