package com.zsx.fwmp.web.controller.news;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Maps;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.util.Assert;
import com.zsx.fwmp.web.service.news.INewsService;
import com.zsx.model.pojo.News;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName NewsController
 * @author lz
 * @description 新闻控制层接口
 * @date 2018年6月26日15:42:05
 */
@RestController
@RequestMapping("/api/web/news")
public class NewsController {

	@Autowired
	private INewsService newsService;
	
	
	
	/**
	 * @Title addNews
	 * @description 新增新闻
	 * @param news
	 * @return
	 */
	@ApiOperation(
			value="新增新闻",
			notes="新增新闻"
			)
	@ApiImplicitParam(name="news",value="新闻实体类",required=true,paramType="path",dataType="News")
	@PostMapping("/add")
	protected Object addNews(@RequestBody News news){
		Assert.isNull(news.getTitle(),news.getContent(),news.getNewsStatus(),news.getNewsType());
		if(news.getNewsStatus()==0) {
			news.setNewsDate(new Date());
		}
		newsService.insert(news);
		return ResultfulFactory.getInstance().creator(ResultEnum.SUCCESS);
	}
	
	/**
	 * @Title newsList
	 * @param current
	 * @param size
	 * @description 新闻初始化列表
	 * @return
	 */
	@ApiOperation(
			value="新闻初始化列表",
			notes="新闻初始化列表"
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name="current",value="当前页码",required=false,paramType="query",dataType="int"),
		@ApiImplicitParam(name="size",value="每页条数",required=false,paramType="query",dataType="int")
	})
	@PostMapping("/dataGrid")
	protected Object newsList(
			 @RequestParam(value="current",required=false) Integer current,
			 @RequestParam(value="size",required=false) Integer size
			){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS,newsService
						.selectPage(new Page<>(current==null?1:current
								, size==null?10:size)
								,new EntityWrapper<News>().orderBy("create_time", false)));
	}
	
	
	/**
	 * @Title newsSearch
	 * @param current
	 * @param size
	 * @param news
	 * @description 搜索新闻
	 * @return
	 */
	@ApiOperation(
			value="搜索新闻",
			notes="搜索新闻，目前只支持按标题搜索"
			)
	@ApiImplicitParams({
		@ApiImplicitParam(name="current",value="当前页码",required=false,paramType="query",dataType="int"),
		@ApiImplicitParam(name="size",value="每页条数",required=false,paramType="query",dataType="int"),
		@ApiImplicitParam(name="news",value="新闻或资讯实体类",required=true,paramType="path",dataType="News")
	})
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
	@ApiOperation(
			value="编辑新闻，可插入图片",
			notes="编辑新闻，可插入图片"
			)
	@ApiImplicitParam(name="news",value="新闻或资讯实体类",required=true,paramType="path",dataType="News")
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
    @ApiOperation(
    		value="根据ID批量删除新闻",
    		notes="根据ID批量删除新闻"
    		)	
    @ApiImplicitParam(name="ids",value="新闻ID数组",required=true,paramType="path",dataType="long[]")
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
	@ApiOperation(
			value="批量发布新闻",
			notes="批量发布新闻"
			)
	@ApiImplicitParam(name="ids",value="新闻ID数组",required=true,paramType="path",dataType="long[]")
	@PostMapping("/release")
	protected Object releaseNews(@RequestParam("ids") Long[] ids){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS, newsService.releaseNewsByIds(ids));
	}
	
	
}
