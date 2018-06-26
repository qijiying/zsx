package com.zsx.dao.basicedata;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zsx.framework.exception.SystemException;
import com.zsx.model.dto.CascadeDto;
import com.zsx.model.pojo.Provinces;

public interface ProvincesMapper extends BaseMapper<Provinces>{

	List<CascadeDto> selectProvincesList()throws SystemException;

}
