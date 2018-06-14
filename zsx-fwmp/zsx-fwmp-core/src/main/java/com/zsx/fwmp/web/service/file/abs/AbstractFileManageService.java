/*   
 * Copyright © 2018 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.zsx.service.file.abs 
 * @author: xiayy   
 * @date: 2018年4月28日 下午4:46:52 
 */
package com.zsx.fwmp.web.service.file.abs;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.zsx.dao.file.FileManageMapper;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.callback.FileFFmepgHandleThread;
import com.zsx.fwmp.web.others.callback.FileUploadHandle;
import com.zsx.fwmp.web.others.util.Assert;
import com.zsx.fwmp.web.others.util.FileUtil;
import com.zsx.fwmp.web.service.file.IFileManageService;
import com.zsx.model.pojo.FileManage;

/** 
 * @ClassName: AbstractFileManageService 
 * @Description: TODO
 * @author: xiayy
 * @date: 2018年4月28日 下午4:46:52  
 */
public abstract class AbstractFileManageService extends ServiceImpl<FileManageMapper, FileManage> implements IFileManageService{
	
	static Logger logger=LoggerFactory.getLogger(AbstractFileManageService.class);
	
	
	ThreadLocal<Long> startTime = new ThreadLocal<Long>();
	
	final static String image_compress_01="compress_01";
	final static String image_original="original";
	final static String image_trim="trim";
	final static String image_compress_03="compress_03";
	
	final static String video_key="video";
	final static String video_iamge_key="image";
	
	public boolean getFileType(String fileName){
		String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(fileName.toLowerCase());
		if(matcher.find()){
			return true;
		}else{
			return false;
		}
	}
	
	public Map<String, String> uploadFile(MultipartFile[] file,Long sourceId, Long id, Integer s,
			Integer p) throws Exception {
		startTime.set(System.currentTimeMillis());
		logger.info("File ===》 共收到需要上传的文件"+file.length+"个");
		Map<String, String> fileMap=Maps.newHashMap();
		FileUploadHandle uploadServer = new FileUploadHandle();
		FileUtil fileUtil = new FileUtil();
		FileFFmepgHandleThread fFmepgHandleThread=new FileFFmepgHandleThread();
		
		for (MultipartFile multipartFile : file) {
			Assert.isNull(multipartFile.getOriginalFilename(),ResultEnum.CHECK_FILE_NULL_NAME_ERROR);
			logger.info("File ===》 准备上传"+multipartFile.getOriginalFilename());
			
			FileManage fileManage = fileUtil.getReturnPath(new FileManage(multipartFile.getOriginalFilename(), sourceId, id,s,p));
		    fileManage.setFileType(getFileType(multipartFile.getOriginalFilename()) == true? 1: 2);
		    
	  	    fFmepgHandleThread.set_file(multipartFile);
	  	    fFmepgHandleThread.set_fileManage(fileManage);
	  	   // fFmepgHandleThread.set_fileManageServiceImpl(FileManageServiceImpl.this);
	  	    fFmepgHandleThread.set_uploadServer(uploadServer);
	  	    fFmepgHandleThread.setBytes(multipartFile.getBytes());
	  	    
		      if (fileManage.getFileType() == 1) {
		    	  fileMap.put(image_compress_01, fileManage.getFilePath()+fileManage.getFileName()+"_0.1_.jpg");
		    	  fileMap.put(image_original, fileManage.getFilePath()+fileManage.getFileName()+"_yt.jpg");
		    	  fileMap.put(image_trim, fileManage.getFilePath()+fileManage.getFileName()+"_cj.jpg");
		    	  fileMap.put(image_compress_03, fileManage.getFilePath()+fileManage.getFileName()+"_0.3_.jpg");
		    	  fFmepgHandleThread.run(1);
		      } else {
		    	  fileMap.put(video_iamge_key, fileManage.getFilePath()+fileManage.getFileName()+".jpg");
		    	  fileMap.put(video_key, fileManage.getFilePath()+fileManage.getFileName()+".mp4");
		    	  fFmepgHandleThread.run(2);
		      }
		      logger.info("File ===》 文件："+multipartFile.getOriginalFilename()+"=====上传成功============");
		}
		
		  Timer timer=new Timer();//实例化Timer类 
		  timer.schedule(new TimerTask(){ 
			public void run(){ 
				logger.info("上传处理，设置主程序延时3秒");
				this.cancel();
			}
		  },3000);//五百毫秒 
		return fileMap;
	}

	
}
