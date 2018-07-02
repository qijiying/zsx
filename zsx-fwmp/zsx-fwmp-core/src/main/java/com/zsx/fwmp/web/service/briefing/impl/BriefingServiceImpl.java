package com.zsx.fwmp.web.service.briefing.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zsx.dao.briefing.BriefingMapper;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.base.Log;
import com.zsx.fwmp.web.others.util.LunarUtil;
import com.zsx.fwmp.web.others.util.UserUtil;
import com.zsx.fwmp.web.service.briefing.IBriefingService;
import com.zsx.model.pojo.Briefing;
import com.zsx.thirdparty.jpush.AndroidJpushService;
import com.zsx.thirdparty.jpush.IosJpushService;

/**
 * @ClassName BriefingServiceImpl
 * @author lz
 * @description 简报业务实现类
 * @date 2018年6月26日16:32:50
 */
@Service
public class BriefingServiceImpl extends ServiceImpl<BriefingMapper, Briefing> implements IBriefingService {

	@Autowired
	BriefingMapper briefingMapper;
	
	@Autowired
	IosJpushService iosJpushService;
	
	@Autowired
	AndroidJpushService androidJpushService;
	
    static SimpleDateFormat chineseDateFormat = new SimpleDateFormat(
            "yyyy.MM.dd");
	
	/**
	 * @Title addBriefing
	 * @description 新增简报业务实现类
	 * @see com.zsx.fwmp.web.service.briefing.IBriefingService#addBriefing(com.zsx.model.pojo.Briefing)
	 */
	@Override
	public Object addBriefing(Briefing briefing) {
		briefing.setBriefingDate(new Date());
		briefingMapper.insert(briefing);
        Calendar today = Calendar.getInstance();
        try {
			today.setTime(chineseDateFormat.parse(chineseDateFormat.format(new Date())));
		} catch (ParseException e1) {
			Log.debug("格式化时间失败", BriefingServiceImpl.class);
		}
        LunarUtil lunar = new LunarUtil(today);
        //String cyclical =LunarUtil.cyclical(Calendar.getInstance().get(today.YEAR));
        //String animalsYear=LunarUtil.animalsYear(Calendar.getInstance().get(today.YEAR)); 
		String title = "《蹡蹡简报》 "+chineseDateFormat.format(today.getTime())+" 农历"+lunar;
        Log.debug((title), BriefingServiceImpl.class);
		try {
			//推送个人
			UserUtil.jpush(androidJpushService, "442999574933504", title, briefing.getContent());
			//推送全部
			//UserUtil.jpushTitle(androidJpushService, "", title, briefing.getContent());
			return ResultEnum.SUCCESS;
		} catch (Exception e) {
			return ResultEnum.FAILED;
		}
	}
	
	/**
	 * @Title dataGridBriefing
	 * @description 初始化简报列表业务实现类
	 * @see com.zsx.fwmp.web.service.briefing.IBriefingService#dataGridBriefing(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page<Briefing> dataGridBriefing(Integer current, Integer size) {
		String limit = " LIMIT "+(current-1)*size+","+size;
		List<Briefing> list = briefingMapper.selectList(new EntityWrapper<Briefing>().last(limit));
		Page<Briefing> page = new Page<Briefing>();
		page.setRecords(list);
		return page;
	}

	/**
	 * @Title deleteBriefing
	 * @description 批量删除简报业务实现类
	 * @see com.zsx.fwmp.web.service.briefing.IBriefingService#deleteBriefing(java.lang.Integer[])
	 */
	@Override
	public boolean deleteBriefing(Integer[] ids) {
		List<Integer> list = Arrays.asList(ids);
		boolean flag = deleteBatchIds(list);
		return flag;
	}
}
