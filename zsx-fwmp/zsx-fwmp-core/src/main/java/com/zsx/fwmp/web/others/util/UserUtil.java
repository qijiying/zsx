package com.zsx.fwmp.web.others.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zsx.framework.exception.SystemException;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.framework.redis.RedisUtil;
import com.zsx.fwmp.web.others.base.Log;
import com.zsx.thirdparty.base.constant.ThirdpartyConstant;
import com.zsx.thirdparty.jpush.abs.AbstractJPush;
import com.zsx.utils.date.DateUtils;

/**
 * 
 * @ClassName: UserUtil
 * @Description: 用户工具类
 * @author xiayy
 * @date 2018年1月15日 下午4:30:27
 *
 */
@Component
public class UserUtil {
	
	@Autowired
	RedisUtil redisUtil;
	
	
	
	/**
	  * 
	  * @Title: isInteger 
	  * @Description: 判断参数是否为数字 
	  * @param @param str
	  * @param @return    设定文件 
	  * @return boolean    返回类型 
	  * @throws
	 */
	public static boolean isInteger(String str) {  
       Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
       return pattern.matcher(str).matches();  
	}
	

	/**
	 * 
	 * @Title: getAge @Description: 获取年龄 @param @param birthDay @param @return
	 * 设定文件 @return int 返回类型 @throws
	 */
	public static int getAge(String birthDay) {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(birthDay);
			cal.setTime(DateUtils.getDate(date));
		} catch (Exception e) {
			e.printStackTrace();
		}

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth)
					age--;
			} else {
				age--;
			}
		}
		return age;
	}

	
	
	/**
	  * 
	  * @Title: getProvincesCode 
	  * @Description: 获取省会code 
	  * @param @param areaId
	  * @param @return    设定文件 
	  * @return Integer    返回类型 
	  * @throws
	 */
	public static Integer getProvincesCode(Integer areaId) {
		return areaId / 10000 * 10000;
	}

	
	/**
	  * 
	  * @Title: getCityCode 
	  * @Description: 获取城市code 
	  * @param @param areaId
	  * @param @return    设定文件 
	  * @return Integer    返回类型 
	  * @throws
	 */
	public static Integer getCityCode(Integer areaId) {
		return areaId / 100 * 100;
	}
	
	/**
	  * 
	  * @Title: Md5Password 
	  * @Description: 密码加密
	  * @param @param password
	  * @param @param salt
	  * @param @return    设定文件 
	  * @return String    返回类型 
	  * @throws
	 */
	public static String Md5Password(String password,String salt){
		return new SimpleHash("MD5", password, salt, 1).toHex();
	}
	
	
	/**
	 * 平台选型别名推送
	 * @param abstractJPush
	 * @param ailge
	 * @param userSoucre
	 * @param title
	 * @param message
	 */
	public static void jpush(AbstractJPush abstractJPush,String ailge, String title,String message) {
		System.out.println("==============jpush=================");
		System.out.println(abstractJPush);
		System.out.println(ailge);
		System.out.println(title);
		System.out.println(message);
		Log.debug("abstractJpush:"+abstractJPush+"/n ailge "+ailge+"/n title:"+title+"/n message:"+message,UserUtil.class);
		abstractJPush.setTitle(title);
		abstractJPush.setMessage(message);
		if(ThirdpartyConstant.profilesVlaue.equals("prod")){
			abstractJPush.setEnvironment(true); 
		}else if(ThirdpartyConstant.profilesVlaue.equals("test")){
			abstractJPush.setEnvironment(false); 
		}else{
			throw new SystemException(ResultEnum.SYSTEM_LOAD_PROPERTIES_DATA_IS_NULL);
		}
		abstractJPush.pushMessageAllOfAilas(ailge);
	}
	
	
	/**
	 * @Title jpushTitle
	 * @param abstractJPush
	 * @param tag
	 * @param title
	 * @param message
	 * @description 根据tag推送
	 */
	public static void jpushTitle(AbstractJPush abstractJPush,String tag, String title,String message) {
		System.out.println("==============jpush=================");
		Log.debug("abstractJpush:"+abstractJPush+"---tag "+tag+"---title:"+title+"---message:"+message,UserUtil.class);
		abstractJPush.setTitle(title);
		abstractJPush.setMessage(message);
		if(ThirdpartyConstant.profilesVlaue.equals("prod")){
			abstractJPush.setEnvironment(true); 
		}else if(ThirdpartyConstant.profilesVlaue.equals("test")){
			abstractJPush.setEnvironment(false); 
		}else{
			throw new SystemException(ResultEnum.SYSTEM_LOAD_PROPERTIES_DATA_IS_NULL);
		}
		if(tag!="") abstractJPush.pushMessageAllOfTagAndTitle(tag);
		else abstractJPush.pushMessageAll();
		
	}


	/**
	 * @Title jpushAreas
	 * @description 推送到区域
	 * @param androidJpushService
	 * @param area
	 * @param title
	 * @param content
	 */
	public static void jpushAreas(AbstractJPush abstractJPush, String area, String title, String content) {
		System.out.println("==============jpush=================");
		Log.debug("abstractJpush:"+abstractJPush+"---area:"+area+"---title:"+title+"---content:"+content,UserUtil.class);
		abstractJPush.setTitle(title);
		abstractJPush.setMessage(content);
		if(ThirdpartyConstant.profilesVlaue.equals("prod")){
			abstractJPush.setEnvironment(true); 
		}else if(ThirdpartyConstant.profilesVlaue.equals("test")){
			abstractJPush.setEnvironment(false); 
		}else{
			throw new SystemException(ResultEnum.SYSTEM_LOAD_PROPERTIES_DATA_IS_NULL);
		}
		abstractJPush.pushMessageAllOfTag(area);
	}
	
}
