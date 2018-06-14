package com.zsx.fwmp.web.service.news;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.pojo.News;

public interface INewsService extends IService<News> {

	Object deleteNewsById(Long[] ids);

	Object updateNewsById(News news);

	Object releaseNewsByIds(Long[] ids);

	Page<News> searchNews(News news,Map<String,Object> map);

	
	
}
