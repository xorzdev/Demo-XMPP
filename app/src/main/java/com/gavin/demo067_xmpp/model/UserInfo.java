package com.gavin.demo067_xmpp.model;

/**
 * 用户bean
 * 
 * @ClassName: UserInfo
 * @author yeliangliang
 * @date 2015-7-27 上午11:10:24
 */
public class UserInfo {
	/**
	 * 登录用户名
	 */
	private String userPassName;
	/**
	 * 登录用户密码
	 */
	private String userPassWord;
	/**
	 * 昵称
	 */
	private String userName;

	public UserInfo() {

	}

	public UserInfo(String userPassName, String userPassWord, String userName) {
		super();
		this.userPassName = userPassName;
		this.userPassWord = userPassWord;
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassName() {
		return userPassName;
	}

	public void setUserPassName(String userPassName) {
		this.userPassName = userPassName;
	}

	public String getUserPassWord() {
		return userPassWord;
	}

	public void setUserPassWord(String userPassWord) {
		this.userPassWord = userPassWord;
	}

}
