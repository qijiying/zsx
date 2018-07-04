package com.zsx.fwmp.web.service.file.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Maps;
import com.zsx.dao.file.FileManageMapper;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.base.ServerBase;
import com.zsx.fwmp.web.others.callback.FileFFmepgHandleThread;
import com.zsx.fwmp.web.others.callback.FileUploadHandle;
import com.zsx.fwmp.web.others.callback.interfaces.UploadCallBak;
import com.zsx.fwmp.web.others.util.Assert;
import com.zsx.fwmp.web.others.util.FileUtil;
import com.zsx.fwmp.web.service.file.IFileManageService;
import com.zsx.fwmp.web.service.file.abs.AbstractFileManageService;
import com.zsx.model.pojo.FileManage;
import com.zsx.model.webdto.FileDto;

/**
 * 
 * @ClassName: FileManageServiceImpl
 * @Description: 文件管理service
 * @author xiayy
 * @date 2018年1月26日 下午4:59:04
 *
 */
@Service
public  class FileManageServiceImpl extends AbstractFileManageService implements IFileManageService,UploadCallBak {

	private static final Logger logger=LoggerFactory.getLogger(FileManageServiceImpl.class);
	
	ThreadLocal<Long> startTime = new ThreadLocal<Long>();
	
	final static String image_compress_01="compress_01";
	final static String image_original="original";
	final static String image_trim="trim";
	final static String image_compress_03="compress_03";
	
	final static String video_key="video";
	final static String video_iamge_key="image";
	
	@Autowired
	FileManageMapper fileManageMapper;
	
	/**
	  * (非 Javadoc) 
	  * <p>Title: isUpload</p> 
	  * <p>Description:处理文件数据插入DB </p> 
	  * @param fileDto
	  * @return 
	  * @see com.zsx.other.callback.interfaces.UploadCallBak#isUpload(java.util.List)
	 */
	@Override
	public void saveUploadFile(FileDto fileDto) {
		fileManageMapper.insert(fileDto);
		logger.info("上传文件耗时（毫秒） : " + (System.currentTimeMillis() - startTime.get()));
		logger.debug("文件全部上传成功，并保存至数据库！");
	}

	/**
	  * (非 Javadoc) 
	  * <p>Title: fileCallBack</p> 
	  * <p>Description: 处理文件上传以及压缩</p> 
	  * @param file
	  * @param id
	  * @param s
	  * @param p
	  * @return
	  * @throws Exception 
	  * @see com.zsx.service.file.IFileManageService#fileCallBack(org.springframework.web.multipart.MultipartFile, java.lang.Long, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Map<String, String> uploadFile(MultipartFile[] file,Long sourceId, Long id, Integer s,
			Integer p) throws Exception {
		startTime.set(System.currentTimeMillis());
		
		logger.info("File ===》 共收到需要上传的文件"+file.length+"个");
		Map<String, String> fileMap=Maps.newHashMap();
		FileUploadHandle uploadServer = new FileUploadHandle();
		FileUtil fileUtil = new FileUtil();
		FileFFmepgHandleThread fFmepgHandleThread=new FileFFmepgHandleThread();
		for (int i = 0; i < file.length; i++) {
			
			Assert.isNull(file[i].getOriginalFilename(),ResultEnum.CHECK_FILE_NULL_NAME_ERROR);
			logger.info("File ===》 准备上传"+file[i].getOriginalFilename());
			
			FileManage fileManage = fileUtil.getReturnPath(new FileManage(file[i].getOriginalFilename(), sourceId, id,s,p));
		    fileManage.setFileType(getFileType(file[i].getOriginalFilename()) == true? 1: 2);
		    fileManage.setSort(i);
		    
	  	    fFmepgHandleThread.set_file(file[i]);
	  	    fFmepgHandleThread.set_fileManage(fileManage);
	  	    fFmepgHandleThread.set_fileManageServiceImpl(FileManageServiceImpl.this);
	  	    fFmepgHandleThread.set_uploadServer(uploadServer);
	  	    fFmepgHandleThread.setBytes(file[i].getBytes());
	  	    
		      if (fileManage.getFileType() == 1) {
		    	  fileMap.put(i+"",fileManage.getFilePath()+fileManage.getFileName()+"_yt.jpg");
		    	  fileMap.put("server",new ServerBase().getServerPort()+"");
		    	  fFmepgHandleThread.run(1);
		      } else {
		    	  fileMap.put(video_iamge_key, fileManage.getFilePath()+fileManage.getFileName()+".jpg");
		    	  fileMap.put(video_key, fileManage.getFilePath()+fileManage.getFileName()+".mp4");
		    	  fFmepgHandleThread.run(2);
		      }
		      logger.info("File ===》 文件："+file[i].getOriginalFilename()+"=====上传成功============");
		}
		
		return fileMap;
	}

	/**
	 * (非 Javadoc) 
	  * <p>Title: deleteFile</p> 
	  * <p>Description: 异步删除文件</p> 
	  * @param soucreId 
	  * @see com.zsx.service.file.IFileManageService#deleteFile(java.lang.Long)
	 */
	@Override
	@Async
	public void deleteFile(Long soucreId,Integer usePotion) {
		fileManageMapper.delete(new EntityWrapper<FileManage>().where("source_id={0}", soucreId).and("use_postion={0}",usePotion));
	}

	/** (非 Javadoc) 
	  * @Title: deleteIds
	  * @Description: TODO
	  * @param ids 
	  * @see com.zsx.service.file.IFileManageService#deleteIds(java.util.List) 
	  */
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public void deleteIds(List<Long> ids) {
		fileManageMapper.deleteBatchIds(ids);
	}
	
}
