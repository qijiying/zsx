package com.zsx.fwmp.web.service.feedback.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.zsx.dao.feedback.FeedbackMapper;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.framework.redis.RedisUtil;
import com.zsx.fwmp.web.others.base.ResultWebEnum;
import com.zsx.fwmp.web.service.feedback.IFeedBackService;
import com.zsx.model.pojo.Feedback;

@Service
public class IFeedBackServiceImpl extends ServiceImpl<FeedbackMapper,Feedback> implements IFeedBackService{

	@Autowired
	private FeedbackMapper feedbackMapper;
    
	@Autowired
	RedisUtil redisUtil;

	/**
	 * @Title selectFeedbackByPage
	 * @see com.zsx.fwmp.web.service.feedback.IFeedBackService#selectFeedbackByPage
	 * @description 分页查询反馈
	 */
	@Override
	public Object selectFeedbackByPage(Page<Feedback> page) {
		EntityWrapper<Feedback> wrapper=new EntityWrapper<>();
		 List<Feedback> list = feedbackMapper.selectList(wrapper);
/*		 List<FeedBackDto> dtoList = Lists.newArrayList();
		 list.forEach(item->{
				dtoList.add(new FeedBackDto(item.getIsSkip().toString(),item));
			});*/
		 return list;
	}

	/**
	 * @Title selectFeedbackByKewordAndPage
	 * @see com.zsx.fwmp.web.service.feedback.IFeedBackService#selectFeedbackByKewordAndPage
	 * @description 搜索反馈
	 */
	@Override
	public Object selectFeedbackByKewordAndPage(String key, Long userId, String phone, Date startTime,
			Date endTime, Page<Feedback> page) {
		return feedbackMapper.selectFeedbackByKewordAndPage(key,userId,phone,startTime,endTime,page);
	}

	/**
	 * @Title deleteFeedbackByIds
	 * @see com.zsx.fwmp.web.service.feedback.IFeedBackService#deleteFeedbackByIds
	 * @description 删除反馈（此处要删除缓存和子节点）
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Object deleteFeedbackByIds(Integer[] ids) {
		Integer[] childrenIds = recursive(ids);
		int count = childrenIds.length+ids.length;
		while(childrenIds.length>0){
			childrenIds = recursive(childrenIds);
			count+=childrenIds.length;
		}
		return count;
	}
	
	/**
	 * @Title recursive
	 * @param ids
	 * @description 递归删除反馈子节点
	 * @return
	 */
	protected Integer[] recursive(Integer[] ids){
		//根据id数组查询所有的子节点
		List<Feedback> list = feedbackMapper.selectChirdrenIds(ids);
		Integer[] childrenIds = new Integer[list.size()];
		Integer[] parentIds = new Integer[list.size()];
        for(int i=0;i<list.size();i++){
        	childrenIds[i] = list.get(i).getId();
        	parentIds[i] = list.get(i).getParentId();
        }
		//删除所有id在ids里的数据
		Map<String,Object> map = Maps.newHashMap();
		if(ids.length<=0) map.put("result",ResultWebEnum.PARAM_IS_EMPTY);
		else {
			int result = feedbackMapper.deleteFeedbackByIds(ids);
			if(result<ids.length){
				 map.put("Result", ResultWebEnum.DELETE_DATA_EXCEPTION);
				 try {throw new Exception();
				 } catch (Exception e) {
					 e.getMessage();
				 }
			}else{
				//删除反馈时删掉缓存
		        for (Integer parentId : parentIds) {
		            // 反馈缓存key
		            String key = "app:cache:feekback:list:"+parentId;
		            // 缓存存在
		            boolean hasKey = redisUtil.exists(key);
		            if (hasKey) {
		            	redisUtil.remove(key);
		            }
				}
				map.put("result", ResultEnum.SUCCESS);
			}
		}
	
		return childrenIds;
	}

}
