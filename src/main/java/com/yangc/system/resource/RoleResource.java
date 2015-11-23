package com.yangc.system.resource;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yangc.bean.DataGridBean;
import com.yangc.bean.ResultBean;
import com.yangc.exception.WebApplicationException;
import com.yangc.shiro.utils.ShiroUtils;
import com.yangc.system.bean.Permission;
import com.yangc.system.bean.TSysRole;
import com.yangc.system.service.RoleService;

@Controller
@RequestMapping("/role")
public class RoleResource {

	private static final Logger logger = LogManager.getLogger(RoleResource.class);

	@Autowired
	private RoleService roleService;

	@RequestMapping(value = "getRoleList", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("role:" + Permission.SEL)
	public List<TSysRole> getRoleList() {
		logger.info("getRoleList");
		return this.roleService.getRoleList();
	}

	/**
	 * @功能: 查询所有角色(分页)
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午7:16:59
	 * @return
	 */
	@RequestMapping(value = "getRoleList_page", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("role:" + Permission.SEL)
	public DataGridBean getRoleList_page() {
		logger.info("getRoleList_page");
		List<TSysRole> roleList = this.roleService.getRoleList_page();
		return new DataGridBean(roleList);
	}

	@RequestMapping(value = "addRole", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("role:" + Permission.ADD)
	public ResultBean addRole(String roleName) {
		logger.info("addRole - roleName=" + roleName);
		try {
			this.roleService.addOrUpdateRole(null, roleName);
			return new ResultBean(true, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	@RequestMapping(value = "updateRole", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("role:" + Permission.UPD)
	public ResultBean updateRole(Long id, String roleName) {
		logger.info("updateRole - id=" + id + ", roleName=" + roleName);
		try {
			this.roleService.addOrUpdateRole(id, roleName);
			return new ResultBean(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	@RequestMapping(value = "delRole", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("role:" + Permission.DEL)
	public ResultBean delRole(Long id) {
		logger.info("delRole - id=" + id);
		try {
			this.roleService.delRole(id);
			// 清除所有权限缓存信息
			ShiroUtils.clearAllCachedAuthorizationInfo();
			return new ResultBean(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

}
