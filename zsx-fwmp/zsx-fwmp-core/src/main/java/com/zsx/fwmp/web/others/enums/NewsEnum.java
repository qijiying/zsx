package com.zsx.fwmp.web.others.enums;

/**
  * 
  * @ClassName: NewsEnum 
  * @Description: 新闻枚举 
  * @author xiayy 
  * @date 2018年3月12日 下午5:55:47 
  *
 */
public enum NewsEnum {
	
	NOT_RELEASE(1,"未发布"),
	RELEASE(2,"已发布"), 
	;

	NewsEnum(int key, String value) {
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
		for (NewsEnum newsEnum : NewsEnum.values()) {
			if (newsEnum.getKey() == key) {
				return true;
			}
		}
		return false;
	}
	
	public static NewsEnum getByKey(int key) {
		for (NewsEnum newsEnum : values()) {
			if (newsEnum.getKey() ==key ) {
				return newsEnum;
			}
		}
		return null;
	}
}
