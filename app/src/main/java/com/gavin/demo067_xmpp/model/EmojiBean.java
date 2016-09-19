package com.gavin.demo067_xmpp.model;

import java.io.Serializable;

/**
 * 聊天表情bean
 * 
 * @ClassName: EmojiBean
 * @author yeliangliang
 * @date 2015-8-14 下午4:15:05
 */
public class EmojiBean implements Serializable {

	private static final long serialVersionUID = 1L;
	// 表情资源id
	private int id;
	// 表情资源名称
	private String name;
	// 表情所代表的内容
	private String content;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
