package com.yangc.system.service;

import java.util.List;

import com.yangc.system.bean.oracle.TSysUsersroles;

public interface UsersrolesService {

	public void addUsersroles(Long userId, Long roleId);

	public void delUsersroles(Long userId, Long roleId);

	public void delUsersrolesByMainId(Long mainId, int userOrRole);

	public List<TSysUsersroles> getUsersrolesListByUserId(Long userId);

}
