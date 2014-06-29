package com.yangc.system.bean.oracle;

import com.yangc.bean.AsyncTreeNode;

public class AuthTree extends AsyncTreeNode {

	private Long menuId;
	private String menuName;
	private boolean all;
	private boolean sel;
	private boolean add;
	private boolean upd;
	private boolean del;

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

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}

	public boolean isSel() {
		return sel;
	}

	public void setSel(boolean sel) {
		this.sel = sel;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public boolean isUpd() {
		return upd;
	}

	public void setUpd(boolean upd) {
		this.upd = upd;
	}

	public boolean isDel() {
		return del;
	}

	public void setDel(boolean del) {
		this.del = del;
	}

}
