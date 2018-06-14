package com.zsx.fwmp.web.controller.feedback;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.framework.designpattern.factory.ResultfulFactory;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.service.feedback.IFeedBackService;
import com.zsx.model.pojo.Feedback;

@RestController
@RequestMapping("/api/feedback")
public class FeedBackController {
	
	@Autowired
	private IFeedBackService iFeedBackService;
	
	/**
	 * @Title insertFeedBack
	 * @param feedBackDto
	 * @description 增加反馈信息
	 * @return
	 */
	@PostMapping("/add")
	protected Object insertFeedBack(
			 @RequestBody Feedback feedback
			){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS
						,iFeedBackService.insert(feedback));
	}
	
	
	/**
	 * @Title selectFeedbackByPage
	 * @param current
	 * @param size
	 * @description 初始化反馈列表
	 * @return
	 */
	@PostMapping("/dataGrid")
	protected Object selectFeedbackByPage(
			@RequestParam(value="current",required=false) Integer current,
			@RequestParam(value="size",required=false) Integer size
			){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS
						,iFeedBackService.selectFeedbackByPage(new Page<>(current==null?1:current, size==null?10:size)));
	}
	
	
	/**
	 * @Title selectFeedbackByKewordAndPage
	 * @param key
	 * @param userId
	 * @param phone
	 * @param startTime
	 * @param endTime
	 * @param current
	 * @param size
	 * @description 搜索反馈信息
	 * @return
	 */
	@PostMapping("/dataSearch")
	protected Object selectFeedbackByKewordAndPage(
			@RequestParam(value="key",required=false) String key,
			@RequestParam(value="userId",required=false) Long userId,
			@RequestParam(value="phone",required=false) String phone,
			@RequestParam(value="st",required=false) Date startTime,
			@RequestParam(value="et",required=false) Date endTime,
			@RequestParam(value="current",required=false) Integer current,
			@RequestParam(value="size",required=false) Integer size
			){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS
						,iFeedBackService.selectFeedbackByKewordAndPage(key,userId,phone,startTime,endTime
								,new Page<>(current==null?1:current, size==null?10:size)));
	}
	
	
	/**
	 * @Title updateFeedback
	 * @param feedback
	 * @description 更新反馈
	 * @return
	 */
	@PostMapping("/update")
	protected Object updateFeedback(@RequestBody Feedback feedback){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS
						,iFeedBackService.updateById(feedback));
	}
	
	
	/**
	 * @Title deleteFeedback
	 * @param id
	 * @description 删除反馈信息
	 * @return
	 */
	@PostMapping("/delete")
	protected Object deleteFeedback(@RequestParam("ids") Integer[] ids){
		return ResultfulFactory
				.getInstance()
				.creator(ResultEnum.SUCCESS
						,iFeedBackService.deleteFeedbackByIds(ids));
	}

}
