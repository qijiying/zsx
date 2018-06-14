package com.zsx.fwmp.web.controller.news;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Maps;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.service.news.INewsService;
import com.zsx.model.pojo.News;

@RestController
@RequestMapping("/api/web/news")
public class NewsController {

	@Autowired
	private INewsService newsService;
	
	
	/**
	 * @Title newsList
	 * @param current
	 * @param size
	 * @description 新闻初始化列表
	 * @return
	 */
	@PostMapping("/dataGrid")
	protected Object newsList(
			 @RequestParam(value="current",required=false) Integer current,
			 @RequestParam(value="size",required=false) Integer size
			){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS,newsService.selectPage(new Page<>(current==null?1:current, size==null?10:size)));
	}
	
	
	/**
	 * @Title newsSearch
	 * @param current
	 * @param size
	 * @param news
	 * @description 搜索新闻
	 * @return
	 */
	@PostMapping("/dataSearch")
	protected Object newsSearch(
			 @RequestParam(value="current",required=false) Integer current,
			 @RequestParam(value="size",required=false) Integer size,
			 @RequestBody News news
			){
		Map<String,Object> map = Maps.newHashMap();
		map.put("current", current==null?1:current);
		map.put("size", size==null?10:size);
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS,newsService.searchNews(news,map));
	}
	
	
	/**
	 * @Title updateNews
	 * @param news
	 * @description 编辑新闻，可插入图片
	 * @return
	 */
	@PostMapping("/update")
	protected Object updateNews(@RequestBody News news){		
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS,newsService.updateNewsById(news));
	}
	
	
	/**
	 * @Title deleteNews
	 * @param id
	 * @description 根据id删除news
	 * @return
	 */
	@PostMapping("/delete")
	protected Object deleteNews(@RequestParam("ids") Long[] ids){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, newsService.deleteNewsById(ids));
	}
	
	/**
	 * @Title releaseNews
	 * @param ids
	 * @description 批量发布新闻
	 * @return
	 */
	@PostMapping("/release")
	protected Object releaseNews(@RequestParam("ids") Long[] ids){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, newsService.releaseNewsByIds(ids));
	}
	
	
}
