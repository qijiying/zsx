package com.zsx.dao.basicedata;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zsx.model.pojo.Areas;

public interface AreasMapper extends BaseMapper<Areas> {

	List<Areas> selectByCityId(Integer cityId);

}
