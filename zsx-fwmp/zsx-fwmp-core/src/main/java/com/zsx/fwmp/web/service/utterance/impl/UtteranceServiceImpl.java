package com.zsx.fwmp.web.service.utterance.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zsx.dao.utterance.UtteranceMapper;
import com.zsx.framework.redis.RedisUtil;
import com.zsx.framework.redis.RedisUtils;
import com.zsx.fwmp.web.others.base.Log;
import com.zsx.fwmp.web.others.constant.RedisWebPreFixConstant;
import com.zsx.fwmp.web.others.util.RandomUtil;
import com.zsx.fwmp.web.service.utterance.IUtteranceService;
import com.zsx.model.pojo.Utterance;

/**
 * @ClassName UtteranceServiceImpl
 * @author lz
 * @description 话语业务实现类
 * @date 2018年7月2日17:00:16
 */
@Service
public class UtteranceServiceImpl extends ServiceImpl<UtteranceMapper, Utterance> implements IUtteranceService {

	@Autowired
	private UtteranceMapper utteranceMapper;
	
	//@Autowired
	//RedisUtil redisUtil;
	
	//private static String key = RedisWebPreFixConstant.WEB_UTTERANCE_LIST;

	
	/**
	 * @Title dataGrid
	 * @description 初始化话语列表，实现缓存
	 * @see com.zsx.fwmp.web.service.utterance.IUtteranceService#dataGrid(com.baomidou.mybatisplus.plugins.Page)
	 */
	@Override
	public Page<Utterance> dataGrid(Page<Utterance> page) {
		Integer current = page.getCurrent();
		Integer size = page.getSize();
		List<Utterance> list = Lists.newArrayList();
/*		if(redisUtil.exists(key)){
			list = (List<Utterance>) redisUtil.get(key);
			Log.debug("=======从缓存拿Utterance列表==========", UtteranceServiceImpl.class);
		}else{
			String limit = " LIMIT "+(current-1)*size+","+size;
			list = utteranceMapper.selectList(new EntityWrapper<Utterance>().last(limit).orderBy("class_id", true));
			redisUtil.set(key, list);
			Log.debug("=========从数据库拿Utterance列表，存入缓存===========", UtteranceServiceImpl.class);
		}*/
		String limit = " LIMIT "+(current-1)*size+","+size;
		list = utteranceMapper.selectList(new EntityWrapper<Utterance>().last(limit).orderBy("class_id", true));
		int count = utteranceMapper.selectCount(new EntityWrapper<Utterance>());
		page.setRecords(list);
		page.setTotal(count);
		return page;
	}

	/**
	 * @Title updateUtterance
	 * @description 修改话语业务实现类
	 * @see com.zsx.fwmp.web.service.utterance.IUtteranceService#updateUtterance(com.zsx.model.pojo.Utterance)
	 */
	@Override
	public Object updateUtterance(Utterance utterance) {
		Map<String,Object> map = Maps.newHashMap();
		if(updateById(utterance)){
			map.put("code", 1);
		}else{map.put("code", 0);}
		return map;
	}

	/**
	 * @Title deleteUtterance
	 * @description 批量删除话语业务实现类
	 * @see com.zsx.fwmp.web.service.utterance.IUtteranceService#deleteUtterance(java.lang.Integer[])
	 */
	@Override
	public Object deleteUtterance(Integer[] ids) {
		Map<String,Object> map = Maps.newHashMap();
		List<Integer> list = Arrays.asList(ids);
		if(utteranceMapper.deleteBatchIds(list)>0){
			map.put("code", 1);
		}else{
			map.put("code", 0);
		}
		return map;
	}

	/**
	 * @Title addUtterance
	 * @description 新增话语业务实现类
	 * @see com.zsx.fwmp.web.service.utterance.IUtteranceService#addUtterance(com.zsx.model.pojo.Utterance)
	 */
	@Override
	public Object addUtterance(Utterance utterance) {
		Map<String,Object> map = Maps.newHashMap();
		if(insert(utterance)){
			map.put("code", 1);
		}else{map.put("code", 0);}	
		return map;
	}

	/**
	 * @Title getRandomByClass
	 * @description 根据话语类别，随机返回话语（不够10条则全部返回）
	 * @see com.zsx.fwmp.web.service.utterance.IUtteranceService#getRandomByClass(java.lang.Integer)
	 */
	@Override
	public Object getRandomByClass(Integer id) {
		List<Utterance> list = utteranceMapper
				                 .selectList(new EntityWrapper<Utterance>()
						                  .setSqlSelect("id","content").where("class_id={0}", id));
		Map<String,Object> map =Maps.newHashMap();
		//判断总条数少于10条，返回全部，大于10条则随机返回10条
		if(list.size()<=10){
			map.put("result", list);
		}else{
			Vector<Integer> random = RandomUtil.RandomNum(10,list.size());
			List<Utterance> listu = Lists.newArrayList();
			for (Integer ran : random) {
				listu.add(list.get(ran));
			}
			map.put("result", listu);
		}
		map.put("code", 1);
		return map;
	}
	
}
