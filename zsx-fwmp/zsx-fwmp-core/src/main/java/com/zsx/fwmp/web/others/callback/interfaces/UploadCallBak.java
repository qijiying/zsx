package com.zsx.fwmp.web.others.callback.interfaces;

import com.zsx.model.webdto.FileDto;

/**
  * 
  * @ClassName: UploadCallBak 
  * @Description:上传文件回调方法 
  * @author xiayy 
  * @date 2018年2月25日 下午12:04:34 
  *
 */
public interface UploadCallBak {
	
	public void saveUploadFile(FileDto fileDto);
}
