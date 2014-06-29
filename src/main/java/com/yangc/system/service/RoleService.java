package com.yangc.system.service;

import java.util.List;

import com.yangc.system.bean.oracle.TSysRole;

public interface RoleService {

	public void addOrUpdateRole(Long roleId, String roleName);

	public void delRole(Long roleId);

	public List<TSysRole> getRoleList();

	public List<TSysRole> getRoleList_page();

}
