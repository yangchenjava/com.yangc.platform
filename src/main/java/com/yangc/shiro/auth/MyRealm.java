package com.yangc.shiro.auth;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.CollectionUtils;

import com.yangc.shiro.utils.ShiroUtils;
import com.yangc.system.bean.oracle.Permission;
import com.yangc.system.bean.oracle.TSysAcl;
import com.yangc.system.bean.oracle.TSysUser;
import com.yangc.system.service.AclService;
import com.yangc.system.service.UserService;
import com.yangc.utils.Constants;
import com.yangc.utils.Message;

public class MyRealm extends AuthorizingRealm {

	private UserService userService;
	private AclService aclService;

	private SessionDAO sessionDAO;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// String username = (String) principals.getPrimaryPrincipal();
		List<TSysAcl> aclList = this.aclService.getAclListByUserId(ShiroUtils.getCurrentUser().getId());

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		if (aclList != null) {
			for (TSysAcl acl : aclList) {
				long operateStatus = acl.getOperateStatus();
				String menuAlias = acl.getMenuAlias();
				if (Permission.isPermission(operateStatus, Permission.SEL)) info.addStringPermission(menuAlias + ":" + Permission.SEL);
				if (Permission.isPermission(operateStatus, Permission.ADD)) info.addStringPermission(menuAlias + ":" + Permission.ADD);
				if (Permission.isPermission(operateStatus, Permission.UPD)) info.addStringPermission(menuAlias + ":" + Permission.UPD);
				if (Permission.isPermission(operateStatus, Permission.DEL)) info.addStringPermission(menuAlias + ":" + Permission.DEL);
			}
		}
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authToken;
		String username = token.getUsername();
		String password = String.valueOf(token.getPassword());
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			throw new AuthenticationException("用户名或密码不能为空");
		} else {
			List<TSysUser> users = this.userService.getUserListByUsernameAndPassword(username, password);
			if (users == null || users.isEmpty()) {
				throw new AuthenticationException("用户名或密码错误");
			} else if (users.size() > 1) {
				throw new AuthenticationException("用户重复");
			} else {
				Session currentSession = SecurityUtils.getSubject().getSession();
				// 判断同一账号, 只允许一处登录, 后登录的会踢掉前面登录的
				if (StringUtils.equals(Message.getMessage("shiro.kickout"), "1")) {
					Session existSession = null;
					Collection<Session> sessions = this.sessionDAO.getActiveSessions();
					for (Session session : sessions) {
						if (session != null) {
							SimplePrincipalCollection principals = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
							if (principals != null && StringUtils.equals((String) principals.getPrimaryPrincipal(), username)) {
								existSession = session;
								break;
							}
						}
					}
					if (existSession != null && !StringUtils.equals(existSession.getId().toString(), currentSession.getId().toString())) {
						existSession.stop();
						this.sessionDAO.delete(existSession);
						this.clearCachedAuthenticationInfo(username);
					}
				}

				currentSession.setAttribute(Constants.CURRENT_USER, users.get(0));
				return new SimpleAuthenticationInfo(username, password, this.getName());
			}
		}
	}

	/**
	 * @功能: 获取用户拥有的权限
	 * @作者: yangc
	 * @创建日期: 2014年6月13日 下午12:50:22
	 * @param principals
	 * @return
	 */
	public Collection<String> getUserPermission(PrincipalCollection principals) {
		if (!CollectionUtils.isEmpty(principals)) {
			return this.getAuthorizationInfo(principals).getStringPermissions();
		}
		return null;
	}

	/**
	 * @功能: 清除用户认证缓存信息
	 * @作者: yangc
	 * @创建日期: 2014年5月21日 上午10:24:18
	 * @param principal username
	 */
	public void clearCachedAuthenticationInfo(Object principal) {
		if (principal != null) {
			SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, this.getName());
			this.clearCachedAuthenticationInfo(principals);
		}
	}

	/**
	 * @功能: 清除用户权限缓存信息
	 * @作者: yangc
	 * @创建日期: 2014年5月21日 上午10:24:18
	 * @param principal username
	 */
	public void clearCachedAuthorizationInfo(Object principal) {
		if (principal != null) {
			SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, this.getName());
			this.clearCachedAuthorizationInfo(principals);
		}
	}

	/**
	 * @功能: 清除所有认证缓存信息
	 * @作者: yangc
	 * @创建日期: 2014年5月21日 下午7:54:41
	 */
	public void clearAllCachedAuthenticationInfo() {
		this.getAuthenticationCache().clear();
	}

	/**
	 * @功能: 清除所有权限缓存信息
	 * @作者: yangc
	 * @创建日期: 2014年5月21日 下午7:54:41
	 */
	public void clearAllCachedAuthorizationInfo() {
		this.getAuthorizationCache().clear();
	}

	/**
	 * @功能: 清除所有认证和权限缓存信息
	 * @作者: yangc
	 * @创建日期: 2014年5月21日 下午7:54:41
	 */
	public void clearAllCached() {
		this.clearAllCachedAuthenticationInfo();
		this.clearAllCachedAuthorizationInfo();
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setAclService(AclService aclService) {
		this.aclService = aclService;
	}

	public void setSessionDAO(SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}

}
