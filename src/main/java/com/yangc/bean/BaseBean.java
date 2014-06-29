package com.yangc.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @描述: 所有java Bean的父类
 * @创建日期: 2013-7-17 23:43:07
 * @author yangc
 */
public class BaseBean implements Serializable {

	private static final long serialVersionUID = 7999110583613155677L;

	private Long id;
	private Long createUser;
	private Date createTime;
	private String createTimeStr;
	private Long updateUser;
	private Date updateTime;
	private String updateTimeStr;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public Long getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateTimeStr() {
		return updateTimeStr;
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}

}
