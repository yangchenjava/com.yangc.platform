package com.yangc.shiro.utils;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.PrincipalCollection;

import com.yangc.shiro.auth.MyRealm;
import com.yangc.system.bean.oracle.TSysUser;
import com.yangc.utils.Constants;

public class ShiroUtils {

	/**
	 * @功能: 获取用户拥有的权限
	 * @作者: yangc
	 * @创建日期: 2014年6月13日 下午12:53:20
	 * @param principals
	 * @return
	 */
	public static Collection<String> getUserPermission(PrincipalCollection principals) {
		RealmSecurityManager realmSecurityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
		MyRealm myRealm = (MyRealm) realmSecurityManager.getRealms().iterator().next();
		return myRealm.getUserPermission(principals);
	}

	/**
	 * @功能: 清除用户认证缓存信息
	 * @作者: yangc
	 * @创建日期: 2014年5月21日 上午10:24:18
	 * @param username
	 */
	public static void clearCachedAuthenticationInfo(String username) {
		if (StringUtils.isNotBlank(username)) {
			RealmSecurityManager realmSecurityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
			MyRealm myRealm = (MyRealm) realmSecurityManager.getRealms().iterator().next();
			myRealm.clearCachedAuthenticationInfo(username);
		}
	}

	/**
	 * @功能: 清除用户权限缓存信息
	 * @作者: yangc
	 * @创建日期: 2014年5月21日 上午10:24:18
	 * @param username
	 */
	public static void clearCachedAuthorizationInfo(String username) {
		if (StringUtils.isNotBlank(username)) {
			RealmSecurityManager realmSecurityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
			MyRealm myRealm = (MyRealm) realmSecurityManager.getRealms().iterator().next();
			myRealm.clearCachedAuthorizationInfo(username);
		}
	}

	/**
	 * @功能: 清除所有权限缓存信息
	 * @作者: yangc
	 * @创建日期: 2014年5月21日 下午7:54:41
	 */
	public static void clearAllCachedAuthorizationInfo() {
		RealmSecurityManager realmSecurityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
		MyRealm myRealm = (MyRealm) realmSecurityManager.getRealms().iterator().next();
		myRealm.clearAllCachedAuthorizationInfo();
	}

	/**
	 * @功能: 获取当前登录用户
	 * @作者: yangc
	 * @创建日期: 2014年5月21日 下午7:54:41
	 */
	public static TSysUser getCurrentUser() {
		return (TSysUser) SecurityUtils.getSubject().getSession().getAttribute(Constants.CURRENT_USER);
	}

}
