package com.yangc.system.bean;

import java.util.List;

import com.yangc.bean.BaseBean;

public class TSysMenu extends BaseBean {

	private static final long serialVersionUID = -6612592275613733140L;

	private String menuName;
	private String menuAlias;
	private String menuUrl;
	private Long parentMenuId;
	private Long serialNum;
	private Long isshow;
	private String description;

	private List<TSysMenu> childRenMenu;

	@Override
	public String toString() {
		return "TSysMenu [menuName=" + menuName + ", menuAlias=" + menuAlias + ", menuUrl=" + menuUrl + ", parentMenuId=" + parentMenuId + ", serialNum=" + serialNum + ", isshow=" + isshow
				+ ", description=" + description + ", childRenMenu=" + childRenMenu + "]";
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuAlias() {
		return menuAlias;
	}

	public void setMenuAlias(String menuAlias) {
		this.menuAlias = menuAlias;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public Long getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(Long parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public Long getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Long serialNum) {
		this.serialNum = serialNum;
	}

	public Long getIsshow() {
		return isshow;
	}

	public void setIsshow(Long isshow) {
		this.isshow = isshow;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<TSysMenu> getChildRenMenu() {
		return childRenMenu;
	}

	public void setChildRenMenu(List<TSysMenu> childRenMenu) {
		this.childRenMenu = childRenMenu;
	}

}