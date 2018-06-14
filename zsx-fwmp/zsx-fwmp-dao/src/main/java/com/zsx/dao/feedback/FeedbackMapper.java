package com.zsx.dao.feedback;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zsx.model.dto.FeedBackDto;
import com.zsx.model.pojo.Feedback;

public interface FeedbackMapper extends BaseMapper<Feedback> {

	List<FeedBackDto> selectFeedbackByPage(Page<Feedback> page);

	List<Feedback> selectFeedbackByKewordAndPage(@Param("key") String key,@Param("userId") Long userId,@Param("phone") String phone,@Param("startTime") Date startTime,@Param("endTime") Date endTime,
			Page<Feedback> page);

	int deleteFeedbackByIds(@Param("ids")Integer[] ids);

	List<Feedback> selectChirdrenIds(@Param("ids")Integer[] ids);

}
