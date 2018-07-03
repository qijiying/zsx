package com.zsx.fwmp.web.service.utterance;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.pojo.Utterance;

public interface IUtteranceService extends IService<Utterance> {

	Page<Utterance> dataGrid(Page<Utterance> page);

	Object updateUtterance(Utterance utterance);

	Object deleteUtterance(Integer[] ids);

	Object addUtterance(Utterance utterance);

}
