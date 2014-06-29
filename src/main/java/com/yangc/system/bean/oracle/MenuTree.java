package com.yangc.system.bean.oracle;

import com.yangc.bean.AsyncTreeNode;

public class MenuTree extends AsyncTreeNode {

	private Long menuId;
	private String menuName;
	private String menuUrl;
	private Long parentMenuId;
	private Long serialNum;
	private Long isshow;
	private String description;

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.setId("" + menuId);
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.setText(menuName);
		this.menuName = menuName;
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

}
