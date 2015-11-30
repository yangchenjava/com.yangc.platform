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
import com.yangc.system.bean.Permission;
import com.yangc.system.bean.TSysDepartment;
import com.yangc.system.service.DeptService;

@Controller
@RequestMapping("/dept")
public class DeptResource {

	private static final Logger logger = LogManager.getLogger(DeptResource.class);

	@Autowired
	private DeptService deptService;

	/**
	 * @功能: 查询所有部门(下拉列表)
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:13:04
	 * @return
	 */
	@RequestMapping(value = "getDeptList", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("dept:" + Permission.SEL)
	public List<TSysDepartment> getDeptList() {
		logger.info("getDeptList");
		return this.deptService.getDeptList();
	}

	/**
	 * @功能: 查询所有部门(分页)
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:13:04
	 * @return
	 */
	@RequestMapping(value = "getDeptList_page", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("dept:" + Permission.SEL)
	public DataGridBean getDeptList_page() {
		logger.info("getDeptList_page");
		List<TSysDepartment> deptList = this.deptService.getDeptList_page();
		return new DataGridBean(deptList);
	}

	/**
	 * @功能: 添加部门
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:59:26
	 * @return
	 */
	@RequestMapping(value = "addDept", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("dept:" + Permission.ADD)
	public ResultBean addDept(String deptName, Long serialNum) {
		logger.info("addDept - deptName={}, serialNum={}", deptName, serialNum);
		try {
			this.deptService.addOrUpdateDept(null, deptName, serialNum);
			return new ResultBean(true, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	/**
	 * @功能: 修改部门
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午2:59:26
	 * @return
	 */
	@RequestMapping(value = "updateDept", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("dept:" + Permission.UPD)
	public ResultBean updateDept(Long id, String deptName, Long serialNum) {
		logger.info("updateDept - id={}, deptName={}, serialNum={}", id, deptName, serialNum);
		try {
			this.deptService.addOrUpdateDept(id, deptName, serialNum);
			return new ResultBean(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WebApplicationException.build();
		}
	}

	/**
	 * @功能: 删除部门
	 * @作者: yangc
	 * @创建日期: 2013年12月23日 下午3:02:44
	 * @return
	 */
	@RequestMapping(value = "delDept", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("dept:" + Permission.DEL)
	public ResultBean delDept(Long id) {
		logger.info("delDept - id={}", id);
		ResultBean resultBean = new ResultBean();
		try {
			this.deptService.delDept(id);
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
