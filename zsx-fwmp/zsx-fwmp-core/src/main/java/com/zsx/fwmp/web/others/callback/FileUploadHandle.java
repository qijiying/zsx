package com.zsx.fwmp.web.others.callback;


import org.apache.commons.io.FileUtils;

import com.zsx.fwmp.web.others.util.FileUtil;
import com.zsx.fwmp.web.service.file.impl.FileManageServiceImpl;
import com.zsx.model.dto.FileDto;

/**
  * 
  * @ClassName: FileUploadHandle 
  * @Description: 处理上传 
  * @author xiayy 
  * @date 2018年2月25日 下午12:05:22 
  *
 */

public class FileUploadHandle {
	
	public void UMsg(FileManageServiceImpl fileManageServiceImpl,FileDto[] fileDto){
		try {
			/**
			 * 切记，文件名称不能包含中文字体，否则上传失败
			 */
				/**
				 * 处理视频文件
				 */
				if(fileDto[0].getVideo_temp_file() !=null){
					if(FileUtil.isupload(fileDto[0],fileDto[0].getVideo_temp_file(),fileDto[0].getFileName())){
						FileUtils.deleteQuietly(fileDto[0].getVideo_temp_file());  //删除压缩后的视频临时文件
					}
				}
				
				/**
				 * 处理视频截图文件
				 */
				if(fileDto[0].getVideo_image_temp_file() !=null){
					if(FileUtil.isupload(fileDto[0],fileDto[0].getVideo_image_temp_file(),fileDto[0].getVideo_image_temp_name())){
						FileUtils.deleteQuietly(fileDto[0].getVideo_image_temp_file());  //删除压缩后的视频临时文件
					}
				}
				
				fileDto[0].setTemp_file(null);
				fileDto[0].setVideo_temp_file(null);
				fileDto[0].setVideoImage(fileDto[0].getVideo_image_temp_name());
		} catch (Exception e) {
			e.printStackTrace();
		}
		 fileManageServiceImpl.saveUploadFile(fileDto[0]);
	}

}
