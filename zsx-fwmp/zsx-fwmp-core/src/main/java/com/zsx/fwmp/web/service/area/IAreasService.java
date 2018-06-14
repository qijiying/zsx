package com.zsx.fwmp.web.service.area;

import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.pojo.Areas;

public interface IAreasService extends IService<Areas> {

	Object selectAreasList();

}
