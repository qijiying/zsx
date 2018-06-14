package com.zsx.fwmp.web.service.news.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zsx.dao.news.NewsCommentMapper;
import com.zsx.dao.news.NewsMapper;
import com.zsx.dao.news.NewsReplyMapper;
import com.zsx.fwmp.web.others.base.ConstantClass;
import com.zsx.fwmp.web.others.base.Log;
import com.zsx.fwmp.web.service.news.INewsService;
import com.zsx.model.pojo.News;
import com.zsx.model.pojo.NewsComment;
import com.zsx.model.pojo.NewsReply;

/**
 * @author lz
 * @description 新闻业务实现类
 * @date 2018年6月5日11:09:12
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper,News> implements INewsService {

	@Autowired
	private NewsMapper newsMapper;
	
	@Autowired
	private NewsCommentMapper newsCommentMapper;
	
	@Autowired
	private NewsReplyMapper newsReplyMapper;
	
	//@Autowired
	//private FileManageMapper fileMapper;
	

	/**
	 * @Title deleteNewsById
	 * @description 批量删除新闻
	 */
	@SuppressWarnings("null")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Object deleteNewsById(Long[] ids) {
		List<Long> list = Lists.newArrayList();
		Map<String,Object> map = Maps.newHashMap();
		
		try {
			//删除新闻
			if(ids.length>0) list = Arrays.asList(ids);
			int countNews = newsMapper.deleteBatchIds(list);
			map.put("删除新闻条数", countNews);
			
			//删除评论
			list.forEach(item -> {
				newsReplyMapper.delete(new EntityWrapper<NewsReply>().where("new_id={0}",item));
				newsCommentMapper.delete(new EntityWrapper<NewsComment>().where("news_id={0}",item));
			});
		} catch (Exception e) {
			if(null!=map.get("删除新闻条数")) map.remove("删除新闻条数");
			map.put("result",ConstantClass.DELETE_FAILURE);
			Log.debug("--------手动抛出异常，删除新闻回滚----------", NewsServiceImpl.class);
			int[] a = null;
			a[1]=999;
		}
		return map;
	}

	/**
	 * @Title updateNewsById
	 * @see com.zsx.fwmp.web.service.news.INewsService#updateNewsById
	 * @description 新闻更新
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Object updateNewsById(News news) {
		//todo 后期处理
		return updateById(news);
	}

	/**
	 * @Title releaseNewsByIds
	 * @param ids
	 * @description 批量发布新闻业务实现
	 * @return
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Object releaseNewsByIds(Long[] ids) {
		int result = newsMapper.releaseNewsByIds(ids);
		if(result<ids.length)
			try {
				throw new Exception();
			} catch (Exception e) {
				result = 0;
			}
		return result;
	}

	/**
	 * @Title searchNews
	 * @see com.zsx.fwmp.web.service.news.INewsService#searchNews
	 * @description 搜索新闻业务实现方法
	 */
	@Override
	public Page<News> searchNews(News news, Map<String, Object> map) {
		Integer current = (Integer)map.get("current");
		Integer size = (Integer)map.get("size");
		Page<News> page = new Page<>(current,size);
		List<News> list = newsMapper.searchNewsPages(news,page);
		page.setRecords(list);
		return page;
	}

}
