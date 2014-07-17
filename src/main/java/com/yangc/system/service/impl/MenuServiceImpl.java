package com.yangc.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yangc.dao.BaseDao;
import com.yangc.dao.JdbcDao;
import com.yangc.system.bean.oracle.MenuTree;
import com.yangc.system.bean.oracle.TSysMenu;
import com.yangc.system.service.AclService;
import com.yangc.system.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private BaseDao baseDao;
	@Autowired
	private JdbcDao jdbcDao;
	@Autowired
	private AclService aclService;

	@Override
	public void addOrUpdateMenu(Long menuId, String menuName, String menuAlias, String menuUrl, Long parentMenuId, Long serialNum, Long isshow, String description) {
		TSysMenu menu = (TSysMenu) this.baseDao.get(TSysMenu.class, menuId);
		if (menu == null) {
			menu = new TSysMenu();
		}
		menu.setMenuName(menuName);
		menu.setMenuAlias(menuAlias);
		menu.setMenuUrl(menuUrl);
		menu.setParentMenuId(parentMenuId);
		menu.setSerialNum(serialNum);
		menu.setIsshow(isshow);
		menu.setDescription(description);
		this.baseDao.saveOrUpdate(menu);
	}

	@Override
	public void updateParentMenuId(Long menuId, Long parentMenuId) {
		this.baseDao.updateOrDelete("update TSysMenu set parentMenuId = ? where id = ?", new Object[] { parentMenuId, menuId });
	}

	@Override
	public void delMenu(Long menuId) throws IllegalStateException {
		int totalCount = this.baseDao.getCount("select count(m) from TSysMenu m where m.parentMenuId = ?", new Object[] { menuId });
		if (totalCount > 0) {
			throw new IllegalStateException("该节点下存在子节点");
		}
		this.aclService.delAcl(menuId, 1);
		this.baseDao.updateOrDelete("delete TSysMenu where id = ?", new Object[] { menuId });
	}

	@Override
	public int getNodePosition(Long menuId) {
		String sql = JdbcDao.SQL_MAPPING.get("system.menu.getNodePosition");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("menuId", menuId);
		return this.jdbcDao.getCount(sql, paramMap);
	}

	@Override
	public List<MenuTree> getMenuListByParentMenuId(Long parentMenuId) {
		String sql = JdbcDao.SQL_MAPPING.get("system.menu.getMenuListByParentMenuId");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("parentMenuId", parentMenuId);
		List<Map<String, Object>> mapList = this.jdbcDao.findAll(sql, paramMap);
		if (mapList == null || mapList.isEmpty()) return null;

		List<MenuTree> menuTreeList = new ArrayList<MenuTree>();
		for (Map<String, Object> map : mapList) {
			Long menuId = ((Number) map.get("ID")).longValue();
			String menuName = (String) map.get("MENU_NAME");
			String menuAlias = (String) map.get("MENU_ALIAS");
			String menuUrl = (String) map.get("MENU_URL");
			Long serialNum = ((Number) map.get("SERIAL_NUM")).longValue();
			Long isshow = ((Number) map.get("ISSHOW")).longValue();
			String description = (String) map.get("DESCRIPTION");
			long totalCount = ((Number) map.get("TOTALCOUNT")).longValue();

			MenuTree menuTree = new MenuTree();
			menuTree.setLeaf(totalCount == 0);
			menuTree.setMenuId(menuId);
			menuTree.setMenuName(menuName);
			menuTree.setMenuAlias(menuAlias);
			menuTree.setMenuUrl(menuUrl);
			menuTree.setParentMenuId(parentMenuId);
			menuTree.setSerialNum(serialNum);
			menuTree.setIsshow(isshow);
			menuTree.setDescription(description);
			menuTreeList.add(menuTree);
		}
		return menuTreeList;
	}

	@Override
	public List<TSysMenu> getTopFrame(Long parentMenuId, Long userId) {
		String sql = JdbcDao.SQL_MAPPING.get("system.menu.getTopFrame");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("parentMenuId", parentMenuId);
		paramMap.put("userId", userId);
		List<Map<String, Object>> mapList = this.jdbcDao.findAll(sql, paramMap);
		if (mapList == null || mapList.isEmpty()) return null;

		List<TSysMenu> menus = new ArrayList<TSysMenu>();
		for (Map<String, Object> map : mapList) {
			TSysMenu menu = new TSysMenu();
			menu.setId(((Number) map.get("ID")).longValue());
			menu.setMenuName((String) map.get("MENU_NAME"));
			menu.setMenuUrl((String) map.get("MENU_URL"));
			menus.add(menu);
		}
		return menus;
	}

	@Override
	public List<TSysMenu> getMainFrame(Long parentMenuId, Long userId) {
		String sql = JdbcDao.SQL_MAPPING.get("system.menu.getMainFrame");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("parentMenuId", parentMenuId);
		paramMap.put("userId", userId);
		List<Map<String, Object>> mapList = this.jdbcDao.findAll(sql, paramMap);
		if (mapList == null || mapList.isEmpty()) return null;

		Map<Long, Map<TSysMenu, List<TSysMenu>>> tempMap = new LinkedHashMap<Long, Map<TSysMenu, List<TSysMenu>>>();
		for (int i = 1, size = mapList.size(); i < size; i++) {
			Map<String, Object> map = mapList.get(i);
			Long id = ((Number) map.get("ID")).longValue();
			String menuName = (String) map.get("MENU_NAME");
			Long pid = ((Number) map.get("PARENT_MENU_ID")).longValue();

			if (pid == parentMenuId) {
				Map<TSysMenu, List<TSysMenu>> value = new HashMap<TSysMenu, List<TSysMenu>>();
				TSysMenu menu = new TSysMenu();
				menu.setId(id);
				menu.setMenuName(menuName);
				value.put(menu, new ArrayList<TSysMenu>());
				tempMap.put(id, value);
			} else {
				Map<TSysMenu, List<TSysMenu>> value = tempMap.get(pid);
				if (null == value || value.isEmpty()) continue;
				TSysMenu menu = new TSysMenu();
				menu.setId(id);
				menu.setMenuName(menuName);
				menu.setMenuUrl((String) map.get("MENU_URL"));
				menu.setParentMenuId(pid);
				value.entrySet().iterator().next().getValue().add(menu);
			}
		}

		List<TSysMenu> menus = new ArrayList<TSysMenu>();
		for (Entry<Long, Map<TSysMenu, List<TSysMenu>>> entry : tempMap.entrySet()) {
			Entry<TSysMenu, List<TSysMenu>> en = entry.getValue().entrySet().iterator().next();
			TSysMenu menu = en.getKey();
			menu.setChildRenMenu(en.getValue());
			menus.add(menu);
		}
		return menus;
	}

}
