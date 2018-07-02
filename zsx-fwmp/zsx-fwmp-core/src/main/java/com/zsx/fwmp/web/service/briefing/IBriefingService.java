package com.zsx.fwmp.web.service.briefing;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.pojo.Briefing;

public interface IBriefingService extends IService<Briefing> {

	Object addBriefing(Briefing briefing);

	Page<Briefing> dataGridBriefing(Integer current, Integer size);

	boolean deleteBriefing(Integer[] ids);

}
