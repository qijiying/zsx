package com.zsx.fwmp.web.service.file;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.service.IService;
import com.zsx.model.pojo.FileManage;

/**
  * 
  * @ClassName: IFileManageService 
  * @Description: 文件管理service
  * @author xiayy 
  * @date 2018年1月26日 下午4:58:21 
  *
 */
public interface IFileManageService extends IService<FileManage>{
	
	/**
	  * 
	  * @Title: fileCallBack 
	  * @Description: 文件上传 
	  * @param @param file
	  * @param @param sourceId
	  * @param @param id
	  * @param @param s
	  * @param @param p
	  * @param @return
	  * @param @throws Exception    设定文件 
	  * @return Map<String,String>    返回类型 
	  * @throws
	 */
	public Map<String, String> uploadFile(MultipartFile[] file,Long sourceId,Long id,Integer s,Integer p)throws Exception;
	
	/**
	 * 
	  * @Title: deleteFile 
	  * @Description: 文件删除 
	  * @param @param soucreId    设定文件 
	  * @return void    返回类型 
	  * @throws
	 */
	public void deleteFile(Long soucreId,Integer usePotion); 
	
	public void deleteIds(List<Long> ids);
}
