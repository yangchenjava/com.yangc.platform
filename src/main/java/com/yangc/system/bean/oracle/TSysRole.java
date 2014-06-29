package com.yangc.system.bean.oracle;

import com.yangc.bean.BaseBean;

public class TSysRole extends BaseBean {

	private static final long serialVersionUID = -7674288924554035556L;

	private String roleName;

	@Override
	public String toString() {
		return "TSysRole [roleName=" + roleName + "]";
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}