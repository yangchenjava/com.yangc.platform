package com.yangc.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yangc.dao.BaseDao;
import com.yangc.dao.JdbcDao;
import com.yangc.system.bean.oracle.AuthTree;
import com.yangc.system.bean.oracle.Permission;
import com.yangc.system.bean.oracle.TSysAcl;
import com.yangc.system.service.AclService;

@Service
public class AclServiceImpl implements AclService {

	@Autowired
	private BaseDao baseDao;
	@Autowired
	private JdbcDao jdbcDao;

	@Override
	public void addOrUpdateAcl(Long roleId, Long menuId, int permission, int allow) {
		TSysAcl acl = this.getAclByRoleIdAndMenuId(roleId, menuId);
		if (acl == null) {
			acl = new TSysAcl();
			acl.setRoleId(roleId);
			acl.setMenuId(menuId);
			if (permission == Permission.ALL) {
				acl.setOperateStatus(Permission.VALUE_ALL);
			} else {
				acl.setPermission(permission, true);
			}
		} else {
			if (permission == Permission.ALL && allow == 0) {
				acl.setOperateStatus(Permission.VALUE_NONE);
			} else if (permission == Permission.ALL && allow == 1) {
				acl.setOperateStatus(Permission.VALUE_ALL);
			} else {
				acl.setPermission(permission, allow == 1);
			}
		}
		this.baseDao.saveOrUpdate(acl);
	}

	@Override
	public void delAcl(Long mainId, int roleOrMenu) {
		String hql = roleOrMenu == 0 ? "delete TSysAcl where roleId = ?" : "delete TSysAcl where menuId = ?";
		this.baseDao.updateOrDelete(hql, new Object[] { mainId });
	}

	@Override
	public TSysAcl getAclByRoleIdAndMenuId(Long roleId, Long menuId) {
		return (TSysAcl) this.baseDao.get("from TSysAcl where roleId = ? and menuId = ?", new Object[] { roleId, menuId });
	}

	@Override
	public List<AuthTree> getAclListByRoleIdAndParentMenuId(Long roleId, Long parentMenuId) {
		String sql = JdbcDao.SQL_MAPPING.get("system.acl.getAclAndMenu");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("roleId", roleId);
		paramMap.put("parentMenuId", parentMenuId);
		List<Map<String, Object>> mapList = this.jdbcDao.findAll(sql, paramMap);
		if (mapList == null || mapList.isEmpty()) return null;

		List<AuthTree> authTreeList = new ArrayList<AuthTree>();
		for (Map<String, Object> map : mapList) {
			Long menuId = ((Number) map.get("ID")).longValue();
			String menuName = (String) map.get("MENU_NAME");
			long totalCount = ((Number) map.get("TOTALCOUNT")).longValue();
			long operateStatus = ((Number) map.get("OPERATE_STATUS")).longValue();

			AuthTree authTree = new AuthTree();
			authTree.setLeaf(totalCount == 0);
			authTree.setMenuId(menuId);
			authTree.setMenuName(menuName);
			authTree.setAll(operateStatus == Permission.VALUE_ALL);
			authTree.setSel(Permission.isPermission(operateStatus, Permission.SEL));
			authTree.setAdd(Permission.isPermission(operateStatus, Permission.ADD));
			authTree.setUpd(Permission.isPermission(operateStatus, Permission.UPD));
			authTree.setDel(Permission.isPermission(operateStatus, Permission.DEL));
			authTreeList.add(authTree);
		}
		return authTreeList;
	}

	@Override
	public int getOperateStatus(Long userId, Long menuId, int permission) {
		String sql = JdbcDao.SQL_MAPPING.get("system.acl.getOperateStatus");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("menuId", menuId);
		List<Map<String, Object>> mapList = this.jdbcDao.findAll(sql, paramMap);
		if (mapList == null || mapList.isEmpty()) return Permission.ACL_NO;

		for (Map<String, Object> map : mapList) {
			long operateStatus = ((Number) map.get("OPERATE_STATUS")).longValue();
			if (Permission.isPermission(operateStatus, permission)) {
				return Permission.ACL_YES;
			}
		}
		return Permission.ACL_NO;
	}

	@Override
	public List<TSysAcl> getAclListByUserId(Long userId) {
		String sql = JdbcDao.SQL_MAPPING.get("system.acl.getAclListByUserId");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		List<Map<String, Object>> mapList = this.jdbcDao.findAll(sql, paramMap);
		if (mapList == null || mapList.isEmpty()) return null;

		Map<String, Long> aclMap = new HashMap<String, Long>();
		for (Map<String, Object> map : mapList) {
			String menuAlias = (String) map.get("MENU_ALIAS");
			Long operateStatus = ((Number) map.get("OPERATE_STATUS")).longValue();

			if (aclMap.containsKey(menuAlias)) {
				aclMap.put(menuAlias, operateStatus | aclMap.get(menuAlias));
			} else {
				aclMap.put(menuAlias, operateStatus);
			}
		}

		List<TSysAcl> aclList = new ArrayList<TSysAcl>();
		for (Entry<String, Long> entry : aclMap.entrySet()) {
			TSysAcl acl = new TSysAcl();
			acl.setOperateStatus(entry.getValue());
			acl.setMenuAlias(entry.getKey());
			aclList.add(acl);
		}
		return aclList;
	}

}
