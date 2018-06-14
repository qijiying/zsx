package com.zsx.fwmp.web.others.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zsx.fwmp.web.others.listener.PropertiesListenerConfig;
import com.zsx.fwmp.web.others.setting.YmlBean;

/**
  * 
  * @ClassName: FFmpegUtil 
  * @Description: 视频处理工具类 
  * @author xiayy 
  * @date 2018年2月7日 下午5:25:15 
  *
 */
@Component
public class FFmpegUtil{
	
	final static Logger logger=LoggerFactory.getLogger(FFmpegUtil.class);
	
	@Autowired
	YmlBean ymlBean;
	
	
	// 静态初使化当前类
	public static FFmpegUtil fFmpegUtil;

	
	@PostConstruct
	public void init() {
		fFmpegUtil = this;
	}


	/**
     * 
     * @Title: getprocessCommend 
     * @Description: 视频转换命令
     * @param @param toolPath
     * @param @param sourcePath
     * @param @param outPath
     * @param @return    设定文件 
     * @return List<String>    返回类型 
     * @throws
    */
	public static List<String> getprocessCommend(String sourcePath, String outPath) {
		
		// 创建一个List集合来保存转换视频文件为flv格式的命令
		List<String> command = new ArrayList<String>();
		command.add(fFmpegUtil.ymlBean.getFfmpeg().getFfmpegPath());
		command.add("-i");
		command.add(sourcePath);
		command.add("-vcodec");
		command.add("copy");
		command.add("-acodec");
		command.add("copy");
		command.add(outPath);
		String commands="";
		for (String string : command) {
			commands+=string;
		}
		logger.info("转换视频命令:"+commands);
		return command;
	}
	
	
	/**
	  * 
	  * @Title: getScalaCommend 
	  * @Description: 压缩命令 
	  * @param @param sourcePath
	  * @param @param outPath
	  * @param @return    设定文件 
	  * @return List<String>    返回类型 
	  * @throws
	 */
	public static List<String> getScalaCommend(String sourcePath, String outPath){
		List<String> command=new ArrayList<String>();
		command.add(fFmpegUtil.ymlBean.getFfmpeg().getFfmpegPath());
		command.add("-i");
		command.add(sourcePath);
		command.add("-c:a");
		command.add("copy");
		command.add("-c:v");
		command.add("libx264");
		command.add("-profile:v");
		command.add("high");
		command.add("-preset");
		command.add("slower");
		command.add("-crf:v");
		command.add("21");
		command.add(outPath);
		String commands="";
		for (String string : command) {
			commands+=string;
		}
		logger.info("压缩视频命令:"+commands);
		return command;
	}
   
  
   
   public static List<String> getImageCommend(String sourcePath,String imageOutPath){
	  	 List<String> cutpic = new ArrayList<String>();  
	       cutpic.add(fFmpegUtil.ymlBean.getFfmpeg().getFfmpegPath());  
	       cutpic.add("-i");  
	       cutpic.add(sourcePath); // 同上（指定的文件即可以是转换为flv格式之前的文件，也可以是转换的flv文件）  
	       cutpic.add("-y");  
	       cutpic.add("-f");  
	       cutpic.add("image2");  
	       cutpic.add("-ss"); // 添加参数＂-ss＂，该参数指定截取的起始时间  
	       cutpic.add(PropertiesListenerConfig.propertiesMap.get("file.imageTrimsSecond")); // 添加起始时间为第17秒  
	       cutpic.add("-t"); // 添加参数＂-t＂，该参数指定持续时间  
	       cutpic.add("0.001"); // 添加持续时间为1毫秒  
	      // cutpic.add("-s"); // 添加参数＂-s＂，该参数指定截取的图片大小  
	       //cutpic.add(PropertiesListenerConfig.propertiesMap.get("file.imageTrimsSize")); // 添加截取的图片大小为350*240  
	       cutpic.add(imageOutPath); // 添加截取的图片的保存路径  
			String commands="";
			for (String string : cutpic) {
				commands+=string;
			}
			logger.info("视频截图命令:"+commands);
	       return cutpic;
    }
	   
   /**
	  * 
	  * @Title: processAVI 
	  * @Description: 对于ffmpeg不能解析的格式，都先转成avi格式的。文件必须是avi后缀 
	  * @param @param videoPath
	  * @param @param outPath
	  * @param @return    设定文件 
	  * @return String    返回类型 
	  * @throws
	 */
	 public static String processAVI(String videoPath,String outPath) {  
		 
       List<String> commend = new ArrayList<String>();  
       commend.add(fFmpegUtil.ymlBean.getFfmpeg().getFfmpegPath());  
       commend.add(videoPath);  
       commend.add("-oac");  
       commend.add("lavc");  
       commend.add("-lavcopts");  
       commend.add("acodec=mp3:abitrate=64");  
       commend.add("-ovc");  
       commend.add("xvid");  
       commend.add("-xvidencopts");  
       commend.add("bitrate=600");  
       commend.add("-of");  
       commend.add("avi");  
       commend.add("-o");  
       commend.add(outPath);  
       try {  
           //调用线程命令启动转码
           ProcessBuilder builder = new ProcessBuilder();  
           builder.command(commend);  
           builder.start();  
           return outPath;  
       } catch (Exception e) {  
           e.printStackTrace();  
           return null;  
       }  
   } 
	 
	 /**
	  * 
	  * @Title: processMP4 
	  * @Description: 视频转码
	  * @param @param temp_video_path  源文件
	  * @param @param temp_video_mp4_path 转码文件
	  * @param @param temp_video_scale_path 压缩文件
	  * @param @param temp_video_image_path 截图图片
	  * @param @return 执行转码  asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等
	  * @param @throws InterruptedException    设定文件 
	  * @return boolean    返回类型 
	  * @throws
	 */
    public static boolean processMP4(String temp_video_path,String temp_video_mp4_path) {  
  
        try {
        	
       	 	 System.out.println("*************开始转换视频**********");
       	 	 System.out.println("*************视频源地址："+temp_video_path+"**********");
       	     System.out.println("*************转换后的地址以及视频格式："+temp_video_mp4_path+"**********");
       	     
       	     ProcessBuilder z_builder = new ProcessBuilder();  
       	 	 Process z_process = z_builder.command(FFmpegUtil.getprocessCommend(temp_video_path, temp_video_mp4_path)).redirectErrorStream(true).start();
       	 	 new StreamGobbler(z_process.getInputStream(), "STDOUT","转码").start();
       	 	 new StreamGobbler(z_process.getErrorStream(), "ERROR","转码").start();
       	 	 z_process.waitFor();
       	 	 
			return  true;
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return  false;
    } 
    
    /**
      * 
      * @Title: scalaMP4 
      * @Description: 压缩
      * @param @param fileName
      * @param @param temp_video_path
      * @param @param temp_video_mp4_path
      * @param @param temp_video_scale_path
      * @param @return
      * @param @throws InterruptedException    设定文件 
      * @return boolean    返回类型 
      * @throws
     */
    public static boolean scalaMP4(String temp_video_mp4_path,String temp_video_scale_path) {  
    	  
        try {
        	
       	 	 System.out.println("*************开始压缩视频**********");
       	     System.out.println("*************转换后的地址以及视频格式："+temp_video_mp4_path+"**********");
       	     ProcessBuilder z_builder = new ProcessBuilder(); 
       	 	 Process s_process = z_builder.command(FFmpegUtil.getScalaCommend(temp_video_mp4_path, temp_video_scale_path)).redirectErrorStream(true).start();
       	 	 new StreamGobbler(s_process.getInputStream(), "STDOUT","压缩").start();
      	 	 new StreamGobbler(s_process.getErrorStream(), "ERROR","压缩").start();
      	 	 s_process.waitFor();
       	 	
			return  true;
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return  false;
    } 
    
    

	/**
	  * 
	  * @Title: trimImage 
	  * @Description: 视频截图
	  * @param @param temp_video_path
	  * @param @param temp_video_image_path
	  * @param @return    设定文件 
	  * @return boolean    返回类型 
	  * @throws
	 */
	public static boolean trimImage(String fileName,String temp_video_path,String temp_video_image_path) {
		try {
			ProcessBuilder builder = new ProcessBuilder();
			Process i_process = builder.command(FFmpegUtil.getImageCommend(temp_video_path, temp_video_image_path)).redirectErrorStream(true).start();
    	 	//new FFmpegUtil().new PrintStream(i_process.getInputStream(),i_process.getErrorStream(),fileName+"截图").start();
			 new StreamGobbler(i_process.getInputStream(), "STDOUT","截图").start();
       	 	 new StreamGobbler(i_process.getErrorStream(), "ERROR","截图").start();
    	 	i_process.waitFor();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
	
	
	/**
	  * 
	  * @Title: checkContentType 
	  * @Description: 检查视频文件格式是否是ffmpeg能解析的
	  * @param @param fileName
	  * @param @return    设定文件 
	  * @return int    返回类型 
	  * @throws
	 */
	public static int checkContentType(String fileName) {
		String prixx = fileName.substring(fileName.lastIndexOf("."));
		String reg = ".+(.asx|.asf|.mpg|.wmv|.3gp|.mp4|.mov|.avi|.flv)$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(fileName.toLowerCase());
		if (matcher.find()) {
			return 0;
		} else {
			switch (prixx) {
				case "wmv9" :
					return 1;
				case "rm" :
					return 1;
				case "rmvb" :
					return 1;
				default :
					return 9;
			}
		}
	}
}
