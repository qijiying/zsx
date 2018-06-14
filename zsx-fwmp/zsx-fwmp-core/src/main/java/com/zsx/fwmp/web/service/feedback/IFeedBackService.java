package com.zsx.fwmp.web.service.feedback;

import java.util.Date;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.pojo.Feedback;

public interface IFeedBackService extends IService<Feedback> {

	Object selectFeedbackByPage(Page<Feedback> page);

	Object selectFeedbackByKewordAndPage(String key, Long userId, String phone, Date startTime, Date endTime,
			Page<Feedback> page);

	Object deleteFeedbackByIds(Integer[] ids);
}
