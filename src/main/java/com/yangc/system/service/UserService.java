package com.yangc.system.service;

import java.util.List;

import com.yangc.system.bean.TSysPerson;
import com.yangc.system.bean.TSysUser;

public interface UserService {

	public void addOrUpdateUser(Long userId, String username, String password, TSysPerson person, String roleIds) throws IllegalStateException;

	public String delUser(Long userId);

	public void updatePassword(Long userId, String password);

	public TSysUser getUserByUsername(String username);

	public List<TSysUser> getUserListByUsernameAndPassword(String username, String password);

}
