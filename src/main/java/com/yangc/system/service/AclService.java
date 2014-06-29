package com.yangc.system.service;

import java.util.List;

import com.yangc.system.bean.oracle.AuthTree;
import com.yangc.system.bean.oracle.TSysAcl;

public interface AclService {

	public void addOrUpdateAcl(Long roleId, Long menuId, int permission, int allow);

	public void delAcl(Long mainId, int roleOrMenu);

	public TSysAcl getAclByRoleIdAndMenuId(Long roleId, Long menuId);

	public List<AuthTree> getAclListByRoleIdAndParentMenuId(Long roleId, Long parentMenuId);

	public int getOperateStatus(Long userId, Long menuId, int permission);

	public List<TSysAcl> getAclListByUserId(Long userId);

}
