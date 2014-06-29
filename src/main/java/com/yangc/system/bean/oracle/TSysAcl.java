package com.yangc.system.bean.oracle;

import com.yangc.bean.BaseBean;

public class TSysAcl extends BaseBean {

	private static final long serialVersionUID = -8774166936234817298L;

	private Long roleId;
	private Long menuId;
	private Long operateStatus = 0L;

	private String menuAlias;

	@Override
	public String toString() {
		return "TSysAcl [roleId=" + roleId + ", menuId=" + menuId + ", operateStatus=" + operateStatus + ", menuAlias=" + menuAlias + "]";
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getMenuId() {
		return this.menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Long getOperateStatus() {
		return this.operateStatus;
	}

	public void setOperateStatus(Long operateStatus) {
		this.operateStatus = operateStatus;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public int getPermission(int permission) {
		int temp = 1;
		temp = temp << permission;
		temp &= this.operateStatus;
		if (temp == 0) {
			return Permission.ACL_NO;
		}
		return Permission.ACL_YES;
	}

	public void setPermission(int permission, boolean allow) {
		int temp = 1;
		temp = temp << permission;
		if (allow) {
			this.operateStatus |= temp;
		} else {
			this.operateStatus &= ~temp;
		}
	}

}