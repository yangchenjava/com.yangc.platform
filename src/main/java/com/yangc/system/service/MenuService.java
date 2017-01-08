package com.yangc.system.service;

import java.util.List;

import com.yangc.system.bean.MenuTree;
import com.yangc.system.bean.TSysMenu;

public interface MenuService {

	public void addOrUpdateMenu(Long menuId, String menuName, String menuAlias, String menuUrl, Long parentMenuId, Long serialNum, Long isshow, String description);

	public void updateParentMenuId(Long menuId, Long parentMenuId);

	public void delMenu(Long menuId) throws IllegalStateException;

	public int getNodePosition(Long menuId);

	public List<MenuTree> getMenuTreeListByParentMenuId(Long parentMenuId);

	public List<TSysMenu> getTopFrame(Long parentMenuId, Long userId);

	public List<TSysMenu> getMainFrame(Long parentMenuId, Long userId);

}
