package com.yangc.system.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.yangc.system.bean.TSysPerson;
import com.yangc.system.bean.TSysUser;

public interface UserService {

	public void addOrUpdateUser(Long userId, String username, String password, TSysPerson person, MultipartFile photo, String savePath, String urlPath, String roleIds) throws IllegalStateException,
			IOException;

	public String delUser(Long userId);

	public void updatePassword(Long userId, String password);

	public TSysUser getUserByUsername(String username);

	public List<TSysUser> getUserListByUsernameAndPassword(String username, String password);

}
