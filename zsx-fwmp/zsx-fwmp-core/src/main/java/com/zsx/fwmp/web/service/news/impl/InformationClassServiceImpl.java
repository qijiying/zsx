package com.zsx.fwmp.web.service.news.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zsx.dao.news.InformationClassMapper;
import com.zsx.fwmp.web.service.news.IInformationClassService;
import com.zsx.model.pojo.InformationClass;

/**
 * @ClassName InformationClassServiceImpl
 * @author lz
 * @description 资讯类型业务实现类
 * @date 2018年6月26日20:25:06
 */
@Service
public class InformationClassServiceImpl extends ServiceImpl<InformationClassMapper, InformationClass> implements IInformationClassService  {

	@Autowired
	InformationClassMapper informationClassMapper;
	
	/**
	 * @Title dataGridInCl
	 * @see com.zsx.fwmp.web.service.news.IInformationClassService#dataGridInCl(int, int)
	 * @description 初始化资讯类型业务实现类
	 */
	@Override
	public Page<InformationClass> dataGridInCl(int current, int size) {
		EntityWrapper<InformationClass> wrapper = new EntityWrapper<InformationClass>();
		String limit = " LIMIT "+(current-1)*size+","+size;
		List<InformationClass> list = informationClassMapper.selectList(wrapper.last(limit));
		Page<InformationClass> page = new Page<>(current,size);
		return page.setRecords(list);
	}

	/**
	 * @Title deleteInCl
	 * @see com.zsx.fwmp.web.service.news.IInformationClassService#deleteInCl(java.lang.Integer[])
	 * @description 批量删除资讯类型业务实现类
	 */
	@Override
	public Object deleteInCl(Integer[] ids) {
		List<Integer> list = Arrays.asList(ids);
		return informationClassMapper.deleteBatchIds(list);
	}
	

}
