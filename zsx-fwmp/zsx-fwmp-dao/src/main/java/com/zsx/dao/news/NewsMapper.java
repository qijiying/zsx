package com.zsx.dao.news;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.model.pojo.News;

public interface NewsMapper extends BaseMapper<News> {

	Integer releaseNewsByIds(@Param("ids")Long[] ids);

	List<News> searchNewsPages(@Param("news")News news, Page<News> page);

}
