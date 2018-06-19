package com.zsx.fwmp.web.others.enums;

/** 
 * @ClassName: MessageEnum 
 * @Description: TODO
 * @author: xiayy
 * @date: 2018年5月4日 下午4:15:12  
 */
public enum MessageEnum {
	
	HAIL_FELLOW(1,"好友"),
	POST_THUMP(2,"帖子点赞"),
	POST_COMMENT(3,"帖子评论"),
	PRIVATE_LETTER(4,"私信"),
	GROUP_PUSH(5,"群通知"),
	
	UNREAD(0,"未读"),
	READ(1,"已读"),
	DELETE(2,"已删除"),
	
	
	UNSEND(0,"未发送"),
	SEND(1,"已发送"),
	
	REFUSE(0,"已拒绝"),
	AGREE(1,"已同意"),
	NUAGREE(2,"未同意")
	
	
	;

	MessageEnum(int key, String value) {
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
	
	public static MessageEnum getByKey(int key) {
		for (MessageEnum newsEnum : values()) {
			if (newsEnum.getKey() ==key ) {
				return newsEnum;
			}
		}
		return null;
	}
}
