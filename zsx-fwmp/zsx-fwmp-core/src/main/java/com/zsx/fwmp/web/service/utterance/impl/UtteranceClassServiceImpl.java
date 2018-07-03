package com.zsx.fwmp.web.service.utterance.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.zsx.dao.utterance.UtteranceClassMapper;
import com.zsx.fwmp.web.service.utterance.IUtteranceClassService;
import com.zsx.model.pojo.UtteranceClass;

@Service
public class UtteranceClassServiceImpl extends ServiceImpl<UtteranceClassMapper, UtteranceClass> implements IUtteranceClassService {

	@Autowired
	private UtteranceClassMapper utteranceClassMapper;
	
	@Override
	public Page<UtteranceClass> dataGrid(Page<UtteranceClass> page) {
		Integer current = page.getCurrent();
		Integer size = page.getSize();
		String limit = " LIMIT "+(current-1)*size+","+size;
		List<UtteranceClass> list = utteranceClassMapper
				                       .selectList(new EntityWrapper<UtteranceClass>().last(limit));
		int count = utteranceClassMapper.selectCount(new EntityWrapper<UtteranceClass>());
		page.setRecords(list);
		page.setTotal(count);
		return page;
	}

	@Override
	public Object updateUtteranceClass(UtteranceClass utteranceClass) {
		Map<String,Object> map = Maps.newHashMap();
		map.put("code", 0);
		if(utteranceClassMapper.updateById(utteranceClass)>0){
			map.replace("code",1);
		}
		return map;
	}

	@Override
	public Object deleteUtteranceClass(Integer[] ids) {
		Map<String,Object> map = Maps.newHashMap();
		map.put("code", 0);
		List<Integer> list = Arrays.asList(ids);
		if(utteranceClassMapper.deleteBatchIds(list)>0){
			map.replace("code", 1);
		}
		return map;
	}

}
