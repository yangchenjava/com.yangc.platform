package com.yangc.system.bean.oracle;

import com.yangc.bean.BaseBean;

public class TSysDepartment extends BaseBean {

	private static final long serialVersionUID = -4588592810696141395L;

	private String deptName;
	private Long serialNum;

	@Override
	public String toString() {
		return "TSysDepartment [deptName=" + deptName + ", serialNum=" + serialNum + "]";
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Long getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Long serialNum) {
		this.serialNum = serialNum;
	}

}