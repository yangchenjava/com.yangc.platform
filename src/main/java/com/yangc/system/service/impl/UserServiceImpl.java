package com.yangc.system.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yangc.dao.BaseDao;
import com.yangc.system.bean.TSysPerson;
import com.yangc.system.bean.TSysUser;
import com.yangc.system.service.PersonService;
import com.yangc.system.service.UserService;
import com.yangc.system.service.UsersrolesService;
import com.yangc.utils.lang.NumberUtils;

@Service
@SuppressWarnings("unchecked")
public class UserServiceImpl implements UserService {

	@Autowired
	private BaseDao baseDao;
	@Autowired
	private PersonService personService;
	@Autowired
	private UsersrolesService usersrolesService;

	@Override
	public void addOrUpdateUser(Long userId, String username, String password, TSysPerson person, MultipartFile photo, String savePath, String urlPath, String roleIds) throws IllegalStateException,
			IOException {
		TSysUser user = this.getUserByUsername(username);
		if (user != null) {
			if (userId == null) {
				throw new IllegalStateException("用户名已存在");
			} else if (userId.longValue() != user.getId().longValue()) {
				throw new IllegalStateException("用户名已存在");
			}
		} else {
			user = new TSysUser();
			user.setUsername(username);
			user.setPassword(password);
			this.baseDao.saveOrUpdate(user);
		}

		// 保存person
		person.setUserId(user.getId());
		this.personService.addOrUpdatePerson(person, photo, savePath, urlPath);

		// 保存role
		this.usersrolesService.delUsersrolesByMainId(user.getId(), 0);
		if (StringUtils.isNotBlank(roleIds)) {
			for (String roleId : roleIds.split(",")) {
				this.usersrolesService.addUsersroles(user.getId(), NumberUtils.toLong(roleId));
			}
		}
	}

	@Override
	public String delUser(Long userId) {
		String username = ((TSysUser) this.baseDao.get(TSysUser.class, userId)).getUsername();
		this.personService.delPersonByUserId(userId);
		this.usersrolesService.delUsersrolesByMainId(userId, 0);
		this.baseDao.delete(TSysUser.class, userId);
		return username;
	}

	@Override
	public void updatePassword(Long userId, String password) {
		this.baseDao.updateOrDelete("update TSysUser set password = ? where id = ?", new Object[] { password, userId });
	}

	@Override
	public TSysUser getUserByUsername(String username) {
		return (TSysUser) this.baseDao.get("from TSysUser where username = ?", new Object[] { username });
	}

	@Override
	public List<TSysUser> getUserListByUsernameAndPassword(String username, String password) {
		String hql = "select new TSysUser(u.id, u.username, u.password, p.name as personName) from TSysUser u, TSysPerson p where u.id = p.userId and username = ? and password = ?";
		return this.baseDao.findAll(hql, new Object[] { username, password });
	}

}
