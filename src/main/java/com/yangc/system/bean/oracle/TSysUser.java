package com.yangc.system.bean.oracle;

import com.yangc.bean.BaseBean;

public class TSysUser extends BaseBean {

	private static final long serialVersionUID = 2290383261386989572L;

	private String username;
	private String password;
	private Long personId;

	private String personName;

	public TSysUser() {
	}

	public TSysUser(Long id, String username, String password, String personName) {
		this.setId(id);
		this.username = username;
		this.password = password;
		this.personName = personName;
	}

	@Override
	public String toString() {
		return "TSysUser [username=" + username + ", password=" + password + ", personId=" + personId + "]";
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

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

}