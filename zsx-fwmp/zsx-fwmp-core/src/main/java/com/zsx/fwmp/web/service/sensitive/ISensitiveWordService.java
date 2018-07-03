package com.zsx.fwmp.web.service.sensitive;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.pojo.SensitiveWord;

public interface ISensitiveWordService extends IService<SensitiveWord> {

	Page<SensitiveWord> sensitiveWord(Page<SensitiveWord> page);

	Object addSensitiveWord(SensitiveWord sensitiveWord);

	Object updateSensitiveWord(SensitiveWord sensitiveWord);

	Object deleteSensitiveWord(Integer[] ids);

	Object batchImport(String fileName, MultipartFile file) throws IOException;


}
