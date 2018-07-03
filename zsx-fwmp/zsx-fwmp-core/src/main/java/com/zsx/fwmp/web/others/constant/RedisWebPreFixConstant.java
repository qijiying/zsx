package com.zsx.fwmp.web.others.constant;

/**
 * @ClassName: UpmsRedisPreFix 
 * @Description: UPMS系统Redis常量，命令格式如下：
 * 项目缩写：业务块：具体描述
 * upms:user:adduser
 * @author lz 
 * @date 2018年7月2日16:28:20
 *
*/
public class RedisWebPreFixConstant {

	public static final Long REDIS_TIME=12*12*60L;
	
	/**话语缓存KEY**/
	public static final String WEB_UTTERANCE_LIST = "web:utterance:list:";
	
	/**敏感词缓存KEY**/
	public static final String WEB_SENSITIVE_LIST = "web:SENSITIVE:list:";
	
}
