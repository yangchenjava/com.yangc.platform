package com.yangc.system.service;

import java.util.List;

import com.yangc.system.bean.oracle.TSysUser;

public interface UserService {

	public void addOrUpdateUser(Long userId, String username, Long personId, String roleIds);

	public void delUser(Long userId);

	public void updatePassword(Long userId, String password);

	public TSysUser getUserByUsername(String username);

	public TSysUser getUserByPersonId(Long personId);

	public List<TSysUser> getUserListByUsernameAndPassword(String username, String password);

}
