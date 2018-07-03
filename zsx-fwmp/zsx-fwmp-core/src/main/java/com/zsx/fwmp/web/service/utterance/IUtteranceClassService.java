package com.zsx.fwmp.web.service.utterance;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.pojo.UtteranceClass;

public interface IUtteranceClassService extends IService<UtteranceClass>{

	Page<UtteranceClass> dataGrid(Page<UtteranceClass> page);

	Object updateUtteranceClass(UtteranceClass utteranceClass);

	Object deleteUtteranceClass(Integer[] ids);

}
