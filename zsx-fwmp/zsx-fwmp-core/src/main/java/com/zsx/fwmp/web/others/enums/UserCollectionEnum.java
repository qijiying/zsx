/**   
 * Copyright © 2018 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.zsx.other.enums 
 * @author: xiayy   
 * @date: 2018年5月7日 上午10:00:07 
 */
package com.zsx.fwmp.web.others.enums;

/** 
 * @ClassName: ThumUpEnum 
 * @Description: 点赞类型
 * @author: xiayy
 * @date: 2018年5月7日 上午10:00:07  
 */
public enum UserCollectionEnum {
	
	NEWS(1,"新闻"),
	GOVERNMENT(2,"政务"),
	POLICY(3,"招商"),
	WORKMATTER(4,"办事"),
	POST(5,"帖子")
	;
	

	UserCollectionEnum(int key, String value) {
		this.key = key;
		this.value = value;
	}

	//枚举名称
	private int key;

	//枚举类型
	private String value;



	public int getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static boolean checkKey(int key) {
		for (UserCollectionEnum newsEnum : UserCollectionEnum.values()) {
			if (newsEnum.getKey() == key) {
				return true;
			}
		}
		return false;
	}
	
	public static UserCollectionEnum getByKey(int key) {
		for (UserCollectionEnum newsEnum : values()) {
			if (newsEnum.getKey() == key) {
				return newsEnum;
			}
		}
		return null;
	}
	

}
