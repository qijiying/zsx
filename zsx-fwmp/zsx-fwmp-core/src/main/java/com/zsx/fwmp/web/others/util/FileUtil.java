package com.zsx.fwmp.web.others.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.messaging.MessageChannel;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.google.common.collect.Maps;
import com.zsx.framework.base.BaseAppClass;
import com.zsx.framework.exception.SystemException;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.fwmp.web.others.listener.PropertiesListenerConfig;
import com.zsx.model.pojo.FileManage;
import com.zsx.model.webdto.FileDto;
import com.zsx.utils.date.DateUtils;
import com.zsx.utils.file.FtpUtils;


/**
  * 
  * @ClassName: FileUtil 
  * @Description: 本项目文件处理 
  * @author xiayy 
  * @date 2018年1月26日 下午5:18:22 
  *
 */
public class FileUtil {
	
	
	
	static final Logger logger=LoggerFactory.getLogger(FileUtil.class);
	
	private static MessageChannel ftpChannel=null;
	private static ConfigurableApplicationContext ctx;
	
	/**
	  * 
	  * @Title: getMessageChannel 
	  * @Description: 由于不知道spring boot 如何配置消息管道，故使用xml读取方式。后续改进 
	  * @param @return    设定文件 
	  * @return MessageChannel    返回类型 
	  * @throws
	 */
	public static MessageChannel getMessageChannel(){
		if(ftpChannel == null){
			 ctx = new ClassPathXmlApplicationContext("/ftp/spring-context-ftp.xml");
		     ftpChannel = ctx.getBean("ftpUploadChannel", MessageChannel.class);
		}
		return ftpChannel;
	}
	
	
	/**
	  * 
	  * @Title: deleteTempFile 
	  * @Description: 删除临时文件 
	  * @param @param map    设定文件 
	  * @return void    返回类型 
	  * @throws
	 */
	public static void deleteTempFile()throws Exception{
		logger.debug("删除图片临时文件，临时文件目录:"+ImageUtil.temp_image_directory);
		logger.debug("删除视频临时文件，临时文件目录:"+VideoUtil.temp_video_directory);
		org.apache.commons.io.FileUtils.cleanDirectory(new File(ImageUtil.temp_image_directory));
		org.apache.commons.io.FileUtils.cleanDirectory(new File(VideoUtil.temp_video_directory));
	}
	

	/**
	  * 
	  * @Title: checkFileType 
	  * @Description: 判断文件类型 
	  * @param @param fileName
	  * @param @return    设定文件 
	  * @return boolean    返回类型 
	  * @throws
	 
	public static boolean checkFileType(String fileName){
		String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(fileName.toLowerCase());
		if(matcher.find()){
			return true;
		}else{
			return false;
		}
	}*/
	
	/**
	  * 
	  * @Title: getFilePostfix 
	  * @Description: 获取文件后缀 
	  * @param @param fileName
	  * @param @return    设定文件 
	  * @return String    返回类型 
	  * @throws
	 
	public static String getFilePostfix(String fileName){
		return fileName.substring(0, fileName.indexOf(".") -1 );
	}*/
	
	/**
	  * 
	  * @Title: getReturnPath 
	  * @Description: 根据系统生成默认文件路径 
	  * @param @param systemType
	  * @param @param fileName
	  * @param @param p
	  * @param @return    设定文件 
	  * String fileName,Long sourceId,Long p,Integer fileSource,Integer usePostion
	  * @return Map<String,Object>    返回类型 
	  * @throws
	 */
	public  FileManage getReturnPath(FileManage manage){
		FileManage fileManage=new FileManage();
		fileManage.setFilePath(dynamicPath(manage));
		fileManage.setFileSource(manage.getFileSource());
		fileManage.setUsePostion(manage.getUsePostion());
		fileManage.setYears(DateUtils.getYear());
		fileManage.setMonths(DateUtils.getMonth());
		fileManage.setSourceId(manage.getSourceId());
		fileManage.setUserId(manage.getUserId());
		fileManage.setFileName(Long.toString(BaseAppClass.giveRandomId()));
		//TODO groupId可以进行排序使用，后期测试下看
		fileManage.setFileGroupId(fileManage.getFileName());
		return fileManage;
	}
	
	/**
	  * 
	  * @Title: dynamicPath 
	  * @Description: 动态组装文件路径 
	  * @param @param p
	  * @param @param fileSource
	  * @param @param usePostion
	  * @param @return    设定文件 
	  * @return String    返回类型 
	  * @throws
	 */
	public String dynamicPath(FileManage manage){
		
		String base_path=PropertiesListenerConfig.propertiesMap.get("file.upload.base");
		String user=PropertiesListenerConfig.propertiesMap.get("file.user");
		String icon=PropertiesListenerConfig.propertiesMap.get("file.icon");
		String post=PropertiesListenerConfig.propertiesMap.get("file.post");
		String reply=PropertiesListenerConfig.propertiesMap.get("file.reply");
		String group=PropertiesListenerConfig.propertiesMap.get("file.group");
		
		String dynamicPath="";
		String dateTime=DateUtils.getYear()+"/"+DateUtils.getMonth()+"/"+DateUtils.getDay()+"/"+manage.getUserId()+"/";
		
		Optional.of(manage.getUsePostion());
		switch (manage.getUsePostion()) {
			case 1 :
				dynamicPath=user+icon+manage.getSourceId()+"/";
				break;
			case 2 :
				dynamicPath=user+post+manage.getSourceId()+"/";
				break;
			case 3 :
				dynamicPath=user+reply+manage.getSourceId()+"/";
				break;
			case 4 :
				dynamicPath=group+icon+manage.getSourceId()+"/";
				break;
			default :
				dynamicPath=dateTime;
				break;
		}
		return base_path+dateTime+dynamicPath;
	}
	
	/**
	  * 
	  * @Title: FileConvertByte 
	  * @Description: File 转byte
	  * @param @param file
	  * @param @return    设定文件 
	  * @return byte[]    返回类型 
	  * @throws
	 */
	public static  byte[] FileConvertByte(File file){
		InputStream input = null;
		byte[] byt = null ;
		try {
			input = new FileInputStream(file);
			 byt = new byte[input.available()];
			 input.read(byt);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(input !=null){
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return byt;
	}
	
	
	 
	 /**
	   * 
	   * @Title: MultipartFileConvertFile 
	   * @Description: MultipartFile 转file
	   * @param @param file
	   * @param @return    设定文件 
	   * @return File    返回类型 
	   * @throws
	  */
	 public File multipartFileConvertFile(MultipartFile file){
	        CommonsMultipartFile cf= (CommonsMultipartFile)file; 
	        DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
	        File f = fi.getStoreLocation();
	        return f;
	 }
	 
	 
	 /**
	   * 
	   * @Title: FileConvertMultipart 
	   * @Description: file 转MultipartFile
	   * @param @param file
	   * @param @return
	   * @param @throws Exception    设定文件 
	   * @return MultipartFile    返回类型 
	   * @throws
	  */
	 public MultipartFile FileConvertMultipart(File file) throws Exception{
		 FileInputStream inputStream = new FileInputStream(file);
		 MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
		 return multipartFile;
	 }
	 
	 /**
	   * 
	   * @Title: checkfile 
	   * @Description: 检查文件是否存在 
	   * @param @param path
	   * @param @return    设定文件 
	   * @return boolean    返回类型 
	   * @throws
	  */
	 public static boolean checkfile(String path) {  
         File file = new File(path);  
         if (file.exists()) {  
             return true;  
         }  
         return false;  
     }  
	 
	 /**
	   * 
	   * @Title: isupload 
	   * @Description: 上传文件 
	   * @param @param file
	   * @param @return
	   * @param @throws Exception    设定文件 
	   * @return boolean    返回类型 
	   * @throws
	  */
	 public static boolean isupload(FileDto fileDto,File file,String fileName) {
		 boolean isupload=false;
		 logger.debug("正在上传接收到文件："+fileName);
			try {
				if(!FtpUtils.ftpUpload(FileUtil.getMessageChannel(),file,fileName,fileDto.getFilePath())){
					throw new  SystemException(ResultEnum.SERVER_FILE_UOLPAD_ERROR)
					.set("原本服务器路径:", fileDto.getFilePath())
					.set("现上传服务路径：", fileDto.getFilePath())
					.set("临时文件路径：", fileName)
					.set("待上传的文件是否存在:", file.isFile());
				}else{
					logger.debug("文件：["+fileName+"]已经上传成功，上传至服务器："+fileDto.getFilePath());
					isupload=true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return isupload;
	 }
	 
	 /**
	   * 
	   * @Title: getPrintSize 
	   * @Description: 字节转换 
	   * @param @param size
	   * @param @return    设定文件 
	   * @return String    返回类型 
	   * @throws
	  */
	 public static String getPrintSize(long size) {  
	        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义  
	        if (size < 1024) {  
	            return String.valueOf(size) + "B";  
	        } else {  
	            size = size / 1024;  
	        }  
	        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位  
	        //因为还没有到达要使用另一个单位的时候  
	        //接下去以此类推  
	        if (size < 1024) {  
	            return String.valueOf(size) + "KB";  
	        } else {  
	            size = size / 1024;  
	        }  
	        if (size < 1024) {  
	            //因为如果以MB为单位的话，要保留最后1位小数，  
	            //因此，把此数乘以100之后再取余  
	            size = size * 100;  
	            return String.valueOf((size / 100)) + "."  
	                    + String.valueOf((size % 100)) + "MB";  
	        } else {  
	            //否则如果要以GB为单位的，先除于1024再作同样的处理  
	            size = size * 100 / 1024;  
	            return String.valueOf((size / 100)) + "."  
	                    + String.valueOf((size % 100)) + "GB";  
	        }  
	    }  
	 
	 
	/**
	 * 
	 * @Title: postFileGroup 
	 * @Description:将上传的文件，按照文件组id进行切分，返回数据
	 * @param  fileDtos
	 *  @return
	 * Page<PostDto> 返回类型 @throws
	 */
	public static Map<Long, List<FileDto>> fileGroup(List<FileDto> fileDtos) {
		Map<Long, List<FileDto>> groupBy =Maps.newTreeMap();
		 
		//TODO 此处排序有问题，后续需要处理。目前是根据key排序的。
		if (fileDtos != null && fileDtos.size() > 0) {
			groupBy = fileDtos.stream().collect(Collectors.groupingBy(FileDto::getFileGroupIds,TreeMap::new,Collectors.toList()));
		}

		return groupBy;
	}
	
	
	
}
