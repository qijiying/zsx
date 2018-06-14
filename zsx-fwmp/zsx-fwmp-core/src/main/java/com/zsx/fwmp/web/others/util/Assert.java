package com.zsx.fwmp.web.others.util;

import java.util.List;

import com.zsx.framework.exception.SystemException;
import com.zsx.framework.exception.enmus.ResultEnum;
import com.zsx.utils.string.StringUtils;

/**
  * 
  * @ClassName: Assert 
  * @Description: 校验类
  * @author xiayy 
  * @date 2018年3月29日 上午11:46:26 
  *
 */
public abstract class Assert{
	
	public static void isNull(Object object) {
		if (null == object) {
			throw new SystemException(ResultEnum.CHECK_PARAMETER_NULL_ERROR)
			.set("object is null ", object);
		}
	}
	
	public static void isNull(Object object,ResultEnum resultEnum) {
		if (null == object) {
			throw new SystemException(resultEnum);
		}
	}
	
	public static void isNotNull(Object object,ResultEnum resultEnum) {
		if (null != object) {
			throw new SystemException(resultEnum);
		}
	}
	
	public static void isNull(List<?> list) {
		if (null == list || list.size() == 0) {
			throw new SystemException(ResultEnum.CHECK_DATA_IS_NULL_ERROR);
		}
	}
	
	public static void isNull(Object ...objects){
		if(isNullByArrayLength(objects)){
			for (Object object : objects) {
				isNull(object);
			}
		}
	}
	
	
	public static void isNull(String object) {
		if (StringUtils.isBlank(object)) {
			throw new SystemException(ResultEnum.CHECK_PARAMETER_NULL_ERROR)
			.set("object is null ", object);
		}
	}
	
	public static boolean isNullByArrayLength(Object ... objects){
		if(null !=objects && objects.length > 0 )
			return true;
		return false;
	}
	
}
