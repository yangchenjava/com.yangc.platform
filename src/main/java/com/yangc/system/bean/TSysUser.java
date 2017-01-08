package com.yangc.system.bean;

import com.yangc.bean.BaseBean;

public class TSysUser extends BaseBean {

	private static final long serialVersionUID = 2290383261386989572L;

	private String username;
	private String password;

	private String nickname;

	public TSysUser() {
	}

	public TSysUser(Long id, String username, String password, String nickname) {
		this.setId(id);
		this.username = username;
		this.password = password;
		this.nickname = nickname;
	}

	@Override
	public String toString() {
		return "TSysUser [username=" + username + ", password=" + password + "]";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
