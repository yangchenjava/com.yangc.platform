package com.yangc.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yangc.dao.BaseDao;
import com.yangc.system.bean.TSysRole;
import com.yangc.system.service.AclService;
import com.yangc.system.service.RoleService;
import com.yangc.system.service.UsersrolesService;
import com.yangc.utils.BeanUtils;

@Service
@SuppressWarnings("unchecked")
public class RoleServiceImpl implements RoleService {

	@Autowired
	private BaseDao baseDao;
	@Autowired
	private UsersrolesService usersrolesService;
	@Autowired
	private AclService aclService;

	@Override
	public void addOrUpdateRole(Long roleId, String roleName) {
		TSysRole role = (TSysRole) this.baseDao.get(TSysRole.class, roleId);
		if (role == null) {
			role = new TSysRole();
		}
		role.setRoleName(roleName);
		this.baseDao.saveOrUpdate(role);
	}

	@Override
	public void delRole(Long roleId) {
		this.usersrolesService.delUsersrolesByMainId(roleId, 1);
		this.aclService.delAcl(roleId, 0);
		this.baseDao.updateOrDelete("delete TSysRole where id = ?", new Object[] { roleId });
	}

	@Override
	public List<TSysRole> getRoleList() {
		return this.baseDao.findAll("from TSysRole order by id", null);
	}

	@Override
	public List<TSysRole> getRoleList_page() {
		List<TSysRole> roles = this.baseDao.find("from TSysRole order by id", null);
		return BeanUtils.fillingTime(roles);
	}

}
