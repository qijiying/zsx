package com.zsx.fwmp.web.others.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zsx.fwmp.web.others.listener.PropertiesListenerConfig;
import com.zsx.fwmp.web.service.file.impl.FileManageServiceImpl;
import com.zsx.model.dto.FileDto;
import com.zsx.model.pojo.FileManage;
import com.zsx.utils.bean.ObjectUtils;
import com.zsx.utils.file.FileUtils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {

	static final Logger logger=LoggerFactory.getLogger(ImageUtil.class);
	
	private static final float scala3=0.3f;
	
	
	public static final String temp_image_directory=System.getProperty("java.io.tmpdir")+"/image/";
	private static final String default_image_prxx=PropertiesListenerConfig.propertiesMap.get("file.imagePrxx");

	
	public void handleImage(byte[] bytes,FileManage fileManage,FileManageServiceImpl _fileManageServiceImpl) throws Exception{
		BufferedImage image = fileconvertImage(bytes);
		Executor exService = Executors.newFixedThreadPool(10);
		try {
			CompletableFuture.supplyAsync(() -> {
				return original(bytes, fileManage);
			},exService).handle((res, ex) -> {
			    if(ex != null) {
			        System.out.println("Oops! We have an exception - " + ex.getMessage());
			    }
			    return res;
			}).thenApply(f ->{
				try {
					if(FileUtil.isupload(f,f.getTemp_file(),f.getFileName())){
						FileUtils.deleteQuietly(f.getTemp_file()); //删除视频截图后生成的临时文件
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return f;
			}).thenApply(f->{
				_fileManageServiceImpl.saveUploadFile(f);
				return f;
			});
			
			CompletableFuture.supplyAsync(() -> {
				return imageCompress(image,Float.parseFloat(PropertiesListenerConfig.getProperty("file.scala.3")),fileManage);
			},exService).thenApply(f ->{
				try {
					if(FileUtil.isupload(f,f.getTemp_file(),f.getFileName())){
						FileUtils.deleteQuietly(f.getTemp_file()); //删除视频截图后生成的临时文件
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return f;
			}).thenApply(f->{
				_fileManageServiceImpl.saveUploadFile(f);
				return null;
			});;
			
			CompletableFuture.supplyAsync(() -> {
				return imageCompress(image,Float.parseFloat(PropertiesListenerConfig.getProperty("file.scala.1")),fileManage);
			},exService).thenApply(f ->{
				try {
					if(FileUtil.isupload(f,f.getTemp_file(),f.getFileName())){
						FileUtils.deleteQuietly(f.getTemp_file()); //删除视频截图后生成的临时文件
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return f;
			}).thenApply(f->{
				_fileManageServiceImpl.saveUploadFile(f);
				return f;
			});;
			
			CompletableFuture.supplyAsync(() -> {
				return imageTrim(image,Float.parseFloat("1.0"),fileManage);
			},exService).thenApply(f ->{
				try {
					if(FileUtil.isupload(f,f.getTemp_file(),f.getFileName())){
						FileUtils.deleteQuietly(f.getTemp_file()); //删除视频截图后生成的临时文件
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return f;
			}).thenApply(f->{
				_fileManageServiceImpl.saveUploadFile(f);
				return f;
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * @throws Exception 
	 * 
	  * @Title: iamgeCompress 
	  * @Description: 压缩图片 
	  * @param @param bytes
	  * @param @param scale
	  * @param @param outPath
	  * @param @throws IOException    设定文件 
	  * @return void    返回类型 
	  * @throws
	 */
	public FileDto imageCompress(BufferedImage iamge,float scale,FileManage fileManage) {
		String fileName=fileManage.getFileName()+"_"+scale+"_"+default_image_prxx;
		FileDto fileDto=null;
		try {
			fileDto = (FileDto) ObjectUtils.convertObject(new FileDto(), fileManage);
			fileDto.setFileName(fileName);
			File compressFile=new File(temp_image_directory+fileName);
			Thumbnails.of(iamge).scale(scale).toFile(compressFile);
			BufferedImage compress_iamge =fileconvertImage(FileUtil.FileConvertByte(compressFile));
			fileDto.setFileHeight(compress_iamge.getHeight());
			fileDto.setFileWidth(compress_iamge.getWidth());
			if(scale == scala3){
				fileDto.setFileStyle(2);
			}else{
				fileDto.setFileStyle(3);
			}
			fileDto.setTemp_file(compressFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileDto;
	}
	
	
	/**
	  * 
	  * @Title: original 
	  * @Description: 原图处理 
	  * @param @param file
	  * @param @param fileManage
	  * @param @return
	  * @param @throws Exception    设定文件 
	  * @return FileManage    返回类型 
	  * @throws
	 */
	public FileDto original(byte[] bytes,FileManage fileManage) {
		String fileName=fileManage.getFileName()+"_yt"+default_image_prxx;
		FileDto fileDto=(FileDto) ObjectUtils.convertObject(new FileDto(), fileManage);
		try {
			BufferedImage image=fileconvertImage(bytes);
			fileDto.setFileHeight(image.getHeight());
			fileDto.setFileName(fileName);
			fileDto.setFileWidth(image.getWidth());
			fileDto.setFileStyle(1);
			File originalpictureFile=new File(temp_image_directory+fileName);
			if(!originalpictureFile.exists()){
				FileUtils.writeByteArrayToFile(originalpictureFile, bytes);  //原图
			}
			fileDto.setTemp_file(originalpictureFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileDto;
	}
	
	/**
	  * 
	  * @Title: FileconvertImage 
	  * @Description: File 转BufferedImage
	  * @param @param bytes
	  * @param @return
	  * @param @throws IOException    设定文件 
	  * @return BufferedImage    返回类型 
	  * @throws
	 */
	public BufferedImage fileconvertImage(byte[] bytes) throws IOException{
		Image src = ImageIO.read(new ByteArrayInputStream(bytes)); 
		BufferedImage iamge = (BufferedImage) src; 
		return iamge;
	}
	
	
	/**
	 * @throws Exception 
	  * 
	  * @Title: imageTrim 
	  * @Description: 以中心为基点，裁剪图片
	  * @param @param bytes
	  * @param @param scale
	  * @param @param outPath
	  * @param @throws IOException    设定文件 
	  * @return void    返回类型 
	  * @throws
	 */
	public FileDto imageTrim(BufferedImage iamge, float scale,FileManage fileManage) {
		FileDto fileDto=null;
		try {
			fileDto = (FileDto) ObjectUtils.convertObject(new FileDto(),fileManage);
			String fileName=fileManage.getFileName()+"_cj"+default_image_prxx;
			int height = iamge.getHeight();
			int width = iamge.getWidth();
			int result;
			if (height > width) {
				result = width;
			} else {
				result = height;
			}
			File outPath = new File(temp_image_directory + fileName);
			Thumbnails.of(iamge).size(result, result).sourceRegion(Positions.CENTER, result, result).toFile(outPath);
			BufferedImage compress_iamge = fileconvertImage(FileUtil.FileConvertByte(outPath));
			fileDto.setFileHeight(compress_iamge.getHeight());
			fileDto.setFileWidth(compress_iamge.getWidth());
			fileDto.setFileStyle(4);
			fileDto.setFileName(fileName);
			fileDto.setTemp_file(outPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileDto;
	}
	
	

}
