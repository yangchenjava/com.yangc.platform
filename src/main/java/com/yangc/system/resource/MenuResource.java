package com.yangc.system.resource;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yangc.bean.ResultBean;
import com.yangc.exception.WebApplicationException;
import com.yangc.shiro.utils.ShiroUtils;
import com.yangc.system.bean.oracle.MenuTree;
import com.yangc.system.bean.oracle.Permission;
import com.yangc.system.bean.oracle.TSysMenu;
import com.yangc.system.service.MenuService;

@Controller
@RequestMapping("/menu")
public class MenuResource {

	private static final Logger logger = Logger.getLogger(MenuResource.class);

	@Autowired
	private MenuService menuService;

	/**
	 * @功能: 显示顶层tab
	 * @作者: yangc
	 * @创建日期: 2012-9-10 上午12:00:10
	 * @return
	 */
	@RequestMapping(value = "showTopFrame", method = RequestMethod.POST)
	@ResponseBody
	public List<TSysMenu> showTopFrame() {
		Long userId = ShiroUtils.getCurrentUser().getId();
		logger.info("showTopFrame - userId=" + userId);
		return this.menuService.getTopFrame(0L, userId);
	}

	/**
	 * @功能: 显示主页左侧和主页内容
	 * @作者: yangc
	 * @创建日期: 2012-9-10 上午12:00:10
	 * @return
	 */
	@RequestMapping(value = "showMainFrame", method = RequestMethod.POST)
	@ResponseBody
	public List<TSysMenu> showMainFrame(Long parentMenuId) {
		Long userId = ShiroUtils.getCurrentUser().getId();
		logger.info("showMainFrame - parentMenuId=" + parentMenuId + ", userId=" + userId);
		return this.menuService.getMainFrame(parentMenuId, userId);
	}

	/**
	 * @功能: 根据parentMenuId获取菜单树
	 * @作者: yangc
	 * @创建日期: 2014年1月2日 下午4:04:09
	 * @return
	 */
	@RequestMapping(value = "getMenuTreeList", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("menu:" + Permission.SEL)
	public List<MenuTree> getMenuTreeList(Long parentMenuId) {
		logger.info("getMenuTreeList - parentMenuId=" + parentMenuId);
		return this.menuService.getMenuListByParentMenuId(parentMenuId);
	}

	/**
	 * @功能: 添加菜单
	 * @作者: yangc
	 * @创建日期: 2014年1月2日 下午2:06:05
	 * @return
	 */
	@RequestMapping(value = "addMenu", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("menu:" + Permission.ADD)
	public ResultBean addMenu(String menuName, String menuUrl, Long parentMenuId, Long serialNum, Long isshow, String description) {
		logger.info("addMenu - menuName=" + menuName + ", menuUrl=" + menuUrl + ", parentMenuId=" + parentMenuId + ", serialNum=" + serialNum + ", isshow=" + isshow + ", description=" + description);
		try {
			this.menuService.addOrUpdateMenu(null, menuName, menuUrl, parentMenuId, serialNum, isshow, description);
			return new ResultBean(true, "添加成功，请授权后进行查看");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	/**
	 * @功能: 修改菜单
	 * @作者: yangc
	 * @创建日期: 2014年1月2日 下午2:06:05
	 * @return
	 */
	@RequestMapping(value = "updateMenu", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("menu:" + Permission.UPD)
	public ResultBean updateMenu(Long id, String menuName, String menuUrl, Long parentMenuId, Long serialNum, Long isshow, String description) {
		logger.info("updateMenu - id=" + id + ", menuName=" + menuName + ", menuUrl=" + menuUrl + ", parentMenuId=" + parentMenuId + ", serialNum=" + serialNum + ", isshow=" + isshow
				+ ", description=" + description);
		try {
			this.menuService.addOrUpdateMenu(id, menuName, menuUrl, parentMenuId, serialNum, isshow, description);
			return new ResultBean(true, "修改成功，请刷新页面进行查看");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	/**
	 * @功能: 修改所属父节点
	 * @作者: yangc
	 * @创建日期: 2014年1月2日 下午2:09:41
	 * @return
	 */
	@RequestMapping(value = "updateParentMenuId", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("menu:" + Permission.UPD)
	public ResultBean updateParentMenuId(Long id, Long parentMenuId) {
		logger.info("updateParentMenuId - id=" + id + ", parentMenuId=" + parentMenuId);
		try {
			this.menuService.updateParentMenuId(id, parentMenuId);
			return new ResultBean(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	/**
	 * @功能: 删除菜单
	 * @作者: yangc
	 * @创建日期: 2014年1月2日 下午2:06:17
	 * @return
	 */
	@RequestMapping(value = "delMenu", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("menu:" + Permission.DEL)
	public ResultBean delMenu(Long id) {
		logger.info("delMenu - id=" + id);
		ResultBean resultBean = new ResultBean();
		try {
			this.menuService.delMenu(id);
			resultBean.setSuccess(true);
			resultBean.setMessage("删除成功");
			return resultBean;
		} catch (IllegalStateException e) {
			resultBean.setSuccess(false);
			resultBean.setMessage(e.getMessage());
			return resultBean;
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

}
