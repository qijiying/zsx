package com.zsx.dao.sensitive;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zsx.model.pojo.SensitiveWord;

public interface SensitiveWordMapper extends BaseMapper<SensitiveWord> {

	int insertExcelValues(@Param("values")String values);

}
