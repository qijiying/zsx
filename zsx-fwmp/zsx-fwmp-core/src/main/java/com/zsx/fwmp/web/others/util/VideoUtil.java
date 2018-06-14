package com.zsx.fwmp.web.others.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zsx.framework.exception.SystemException;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.listener.PropertiesListenerConfig;
import com.zsx.fwmp.web.service.file.impl.FileManageServiceImpl;
import com.zsx.model.dto.FileDto;
import com.zsx.model.pojo.FileManage;
import com.zsx.utils.bean.ObjectUtils;
import com.zsx.utils.file.FileUtils;
import com.zsx.utils.file.FtpUtils;

/**
  * 
  * @ClassName: VideoUtil 
  * @Description: 视频工具类处理
  * @author xiayy 
  * @date 2018年2月6日 下午4:12:02 
  *
 */
public class VideoUtil {
	
	static final Logger logger=LoggerFactory.getLogger(VideoUtil.class);

	public static final String temp_video_directory = System.getProperty("java.io.tmpdir") + "/video/";
	
	public static final String default_image_prxx=PropertiesListenerConfig.propertiesMap.get("file.imagePrxx");
	public static final String default_video_prxx=PropertiesListenerConfig.propertiesMap.get("file.videoPrxx");
	

	
	
	/**
	 * @throws Exception
	 * @Title: videoCutImage 
	 * @Description: 视频截图 
	 * @param file 
	 * @return 
	 * @throws IOException 设定文件 
	 * @return File
	 * 返回类型 @throws
	 */
	public FileDto handle(String fileName,byte[] bytes,Long size, FileManage fileManage,FileManageServiceImpl fileManageServiceImpl)throws Exception {
		
		

		/**
		 * 设置文件名称
		 */
		FileDto fileDto = (FileDto) ObjectUtils.convertObject(new FileDto(), fileManage);
		
		Long default_video_size=15728640L; //默认压缩大小  15MB
		
		/**
		 * 定义生成临时文件名称
		 */
		String temp_video_path=temp_video_directory +"_y_"+ fileName;
		String temp_video_scale_path=temp_video_directory +"_s_"+fileDto.getFileName()+default_video_prxx;
		String temp_video_image_path=temp_video_directory+fileDto.getFileName()+default_image_prxx;
		String temp_video_convert_mp4_path=temp_video_directory+fileDto.getFileName()+"_convert_"+default_video_prxx;
		
		/**
		 * 将上传的源文件写入临时目录
		 */
		File temp_file= new File(temp_video_path);
		if(!temp_file.exists()){
			try {
				FileUtils.writeByteArrayToFile(temp_file, bytes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		//视频截图
		if(FFmpegUtil.trimImage(fileName,temp_video_path, temp_video_image_path)){
			//判断文件是否生成，生成就上传
			if(!FileUtil.checkfile(temp_video_image_path)){
				logger.error("------------------截图还没生成成功~--------------------");
				throw new  SystemException(ResultEnum.SERVER_FILE_UOLPAD_ERROR);
			}else{
				fileDto.setVideo_image_temp_name(fileDto.getFileName()+default_image_prxx);
				/**
				 * 以下读取截图文件转为图片，并获取宽高
				 */
				byte[] byts= FileUtils.readFileToByteArray(new File(temp_video_image_path));
				Image src = ImageIO.read(new ByteArrayInputStream(byts)); 
				BufferedImage iamge = (BufferedImage) src; 
				fileDto.setFileStyle(1);
				fileDto.setFileHeight(iamge.getHeight());
				fileDto.setFileWidth(iamge.getWidth());
				fileDto.setVideo_image_temp_file(new File(temp_video_image_path));
			}
			
		}else{
			throw new  SystemException(ResultEnum.SERVER_FILE_UOLPAD_ERROR);
		}
		
		/**
		 * 检查文件是否属于ffmepg识别格式,目前非0以外都属于无法识别格式，后续处理转码
		 */
		
		logger.error("当前视频大小："+size);
		logger.error("当前默认压缩小大："+default_video_size);
		
		//TODO 全部不走压缩和转换，直接文件流处理
		/*if(size<default_video_size){
			int status =FFmpegUtil.checkContentType(fileName);
			if(status != 0){
				FFmpegUtil.processMP4(temp_video_path, temp_video_convert_mp4_path);
			}
			if(FileUtil.checkfile(temp_video_convert_mp4_path)){
				FFmpegUtil.scalaMP4(temp_video_convert_mp4_path, temp_video_scale_path);
			}else{
				FFmpegUtil.scalaMP4(temp_video_path, temp_video_scale_path);
			}
			if(!FileUtil.checkfile(temp_video_scale_path)){
				logger.error("------------------文件还没生成成功~--------------------");
			}else{
				fileDto.setVideo_temp_file(new File(temp_video_scale_path));
			}
		}else{
			fileDto.setVideo_temp_file(new File(temp_video_path));  //直接写入临时源文件
		}*/
		fileDto.setVideo_temp_file(new File(temp_video_path));  //直接写入临时源文件
		fileDto.setFileSize(size.intValue());
		fileDto.setFileType(2);
		fileDto.setImage_temp_name(fileDto.getFileName()+default_image_prxx);
		fileDto.setFileName(fileDto.getFileName()+default_video_prxx);
		return fileDto;
		
	}
	

	/**
	 * 
	 * @Title: videoImageUpload 
	 * @Description: 处理视频封面图
	 * @param fileManage 
	 * @throws Exception 设定文件
	 * @return FileManage 返回类型 
	 */
	public FileManage videoImageUpload(FileManage fileManage) throws Exception {
		FileManage fileManage2 = (FileManage) ObjectUtils.convertObject(new FileManage(), fileManage);
		if(FileUtil.checkfile(temp_video_directory + fileManage.getFileName() +default_image_prxx)){
			File videoFile = new File(temp_video_directory + fileManage.getFileName() + default_image_prxx);
			if(!FtpUtils.ftpUpload(FileUtil.getMessageChannel(), videoFile,fileManage.getFileName() + default_image_prxx, fileManage.getFilePath())){
				throw new  SystemException(ResultEnum.SERVER_FILE_UOLPAD_ERROR)
				.set("原本服务器路径:", fileManage2.getFilePath())
				.set("现上传服务路径：", fileManage2.getFilePath())
				.set("临时文件路径：", temp_video_directory+fileManage2.getFileName())
				.set("现上传文件路径", fileManage2.getFilePath())
				.set("待上传的文件是否存在:", videoFile.isFile());
			}else{
				logger.debug("文件："+fileManage2.getFileName()+"已经上传成功，上传至服务器："+fileManage2.getFilePath()+"下");
				logger.debug("高："+fileManage2.getFileHeight());
				logger.debug("压缩图宽："+fileManage2.getFileWidth());
				logger.debug("压缩临时路径："+temp_video_directory+fileManage2.getFileName());
			}
			fileManage2.setFilePath(fileManage.getFilePath() + fileManage.getFileName() + default_image_prxx);
		}else{
			logger.error("------------------视频截图文件还没生成成功~--------------------");
		}
		return fileManage2;
	}

     
}
