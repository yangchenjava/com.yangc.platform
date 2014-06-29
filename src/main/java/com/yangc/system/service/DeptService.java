package com.yangc.system.service;

import java.util.List;

import com.yangc.system.bean.oracle.TSysDepartment;

public interface DeptService {

	public void addOrUpdateDept(Long deptId, String deptName, Long serialNum);

	public void delDept(Long deptId) throws IllegalStateException;

	public List<TSysDepartment> getDeptList();

	public List<TSysDepartment> getDeptList_page();

}
