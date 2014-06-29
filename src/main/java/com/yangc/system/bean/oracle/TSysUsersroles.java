package com.yangc.system.bean.oracle;

import com.yangc.bean.BaseBean;

public class TSysUsersroles extends BaseBean {

	private static final long serialVersionUID = -1360826651703861657L;

	private Long userId;
	private Long roleId;

	@Override
	public String toString() {
		return "TSysUsersroles [userId=" + userId + ", roleId=" + roleId + "]";
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}